package com.cazj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cazj.common.annotation.RequiredLog;
import com.cazj.common.exception.ServiceException;
import com.cazj.common.util.ShiroUtils;
import com.cazj.common.vo.EmpDeptVo;
import com.cazj.common.vo.PageObject;
import com.cazj.dao.EmpDao;
import com.cazj.dao.EmpRoleDao;
import com.cazj.pojo.Emp;
import com.cazj.service.EmpService;

@Service
public class EmpServiceImpl implements EmpService {

	@Autowired
	EmpDao empDao;
	@Autowired
	EmpRoleDao empRoleDao;
	
	@RequiredLog("员工信息查询")//zhe
	public int updatePassword(String pwd, String newPwd, String cfgPwd) {
		//1.判定新密码与密码确认是否相同
		if(StringUtils.isEmpty(newPwd))
			throw new IllegalArgumentException("新密码不能为空");
		if(StringUtils.isEmpty(cfgPwd))
			throw new IllegalArgumentException("确认密码不能为空");
		if(!newPwd.equals(cfgPwd))
			throw new IllegalArgumentException("两次输入的密码不相等");
		//2.判定原密码是否正确
		if(StringUtils.isEmpty(pwd))
			throw new IllegalArgumentException("原密码不能为空");
		//获取登陆用户
		Emp user=(Emp)SecurityUtils.getSubject().getPrincipal();
		SimpleHash sh=new SimpleHash("MD5",
				pwd, user.getSalt(), 1);
		if(!user.getPassword().equals(sh.toHex()))
			throw new IllegalArgumentException("原密码不正确");
		//3.对新密码进行加密
		String salt=UUID.randomUUID().toString();
		sh=new SimpleHash("MD5",newPwd,salt, 1);
		//4.将新密码加密以后的结果更新到数据库
		int rows=empDao.updatePassword(sh.toHex(), salt,user.getId());
		if(rows==0)
			throw new ServiceException("修改失败");
		return rows;
	}

	@Override
	public Map<String, Object> findEmpById(Integer empId) {
		//1.合法性验证
		if(empId==null||empId<=0)
			throw new ServiceException("参数数据不合法,userId="+empId);
		//2.业务查询
		EmpDeptVo emp = empDao.findEmpById(empId);
		if(emp==null)
			throw new ServiceException("此用户已经不存在");
		List<Integer> roleIds=empRoleDao.findRoleIdsByEmpId(empId);
		//3.数据封装
		Map<String,Object> map=new HashMap<>();
		map.put("emp", emp);
		map.put("roleIds", roleIds);
		return map;
	}
	@RequiredLog("更新员工信息")//哲
	@Override
	public int updateEmp(Emp emp, Integer[] roleIds) {
		//1.参数有效性校验
		if(emp == null) throw new ServiceException("修改数据不能为空");
		if(roleIds == null || roleIds.length == 0) throw new ServiceException("请给该员工添加一个角色");
		emp.setModifiedName(ShiroUtils.getEmpName());
		//2.更新员工档案信息
		int row = empDao.updateEmp(emp);
		if(row == 0) throw new ServiceException("更新数据失败");
		//3.保存关系数据(先删除原有的数据，然后再添加)
		empRoleDao.deleteEmpRoleById(emp.getId());
		empRoleDao.addEmpRole(emp.getId(), roleIds);
		//4.返回结果
		return row;
	}
	@RequiredLog("添加员工信息")//哲
	@Override
	public int addEmp(Emp emp, Integer[] roleIds) {
		//1.参数有效性校验
		if (emp == null)  throw new ServiceException("员工档案信息不能为空");
		if (roleIds == null || roleIds.length == 0) throw new ServiceException("员工角色不能为空");
		//2.保存员工信息
		//2.1对密码进行加密
		String pwd = emp.getPassword();
		String salt = UUID.randomUUID().toString();
		SimpleHash sh = new SimpleHash("MD5",pwd,salt);
		emp.setSalt(salt);
		emp.setPassword(sh.toHex());
		emp.setCreatedName(ShiroUtils.getEmpName());
		int row = empDao.addEmp(emp);
		if(row == 0) new ServiceException("保存数据失败"); 
		//3.保存关系数据
		empRoleDao.addEmpRole(emp.getId(), roleIds);
		//4.返回结果
		return row;
	}

	@Override
	public boolean isExists(String username) {
		return empDao.isExists(username);
	}
	@RequiredLog("员工信息分页查询")//哲
	@Override
	public PageObject<EmpDeptVo> findPageEmp(String empName, Integer pageCurrent) {
		//1.参数有效性校验
		if (pageCurrent == null || pageCurrent == 0) throw new IllegalArgumentException("页面值不正确");
		//2.查询总记录行数
		int rowCount = empDao.getRowCount(empName);
		if(rowCount == 0) throw new ServiceException("没有记录");
		//3.查询员工并分页展示
		int pageSize = 6;
		int startIndex = (pageCurrent - 1)*pageSize;
		List<EmpDeptVo> empList = empDao.findPageEmp(empName, startIndex, pageSize);
		//4.对结果进行封装
		return new PageObject<>(pageCurrent, pageSize, rowCount, empList);
	}

	@Override
	public int validById(Integer id, Integer valid, String modifiedName) {
		//1.合法性验证
		if(id==null||id<=0)
			throw new ServiceException("参数不合法,id="+id);
		if(valid!=1&&valid!=0)
			throw new ServiceException("参数不合法,valie="+valid);
		if(StringUtils.isEmpty(modifiedName))
			throw new ServiceException("修改用户不能为空");
		//2.执行禁用或启用操作
		int rows=empDao.validById(id, valid, modifiedName);
		//3.判定结果,并返回
		if(rows==0)
			throw new ServiceException("此记录可能已经不存在");
		return rows;
	}

	//肖冲添加， 作用，activiti的中间件需要通过注入中间Service层的实现类，获取一个用户的上级领导名字
	@Override
	public Emp findEmpManagerByEmpName(String empName) {
		Emp manager = empDao.findEmpManagerByEmpName(empName);
		return manager;
	}

}
