package com.cazj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cazj.common.exception.ServiceException;
import com.cazj.common.util.ShiroUtils;
import com.cazj.common.vo.RoleMenuVo;
import com.cazj.dao.EmpRoleDao;
import com.cazj.dao.RoleDao;
import com.cazj.dao.RoleMenuDao;
import com.cazj.pojo.PageObject;
import com.cazj.pojo.Role;
import com.cazj.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {
	@Autowired
	RoleDao roleDao;
	@Autowired
	RoleMenuDao roleMenuDao;
	@Autowired
	EmpRoleDao empRoleDao;

	@Override
	public RoleMenuVo findObjectById(Integer id) {
		//1.校验参数有效性
		if (id==null || id == 0) {
			throw new IllegalArgumentException("请选中要修改的对象");
		}
		//2.基于id查询
		RoleMenuVo roleMenuVo = roleDao.findObjectById(id);
		if (roleMenuVo == null) {
			throw new ServiceException("该对象不存在");
		}
		return roleMenuVo;
	}

	@Override
	public int updateObject(Role role, Integer[] menuIds) {
		//1.校验参数有效性
		if (role == null) {
			throw new IllegalArgumentException("请选中你要修改的对象");
		}
		if (menuIds == null || menuIds.length == 0) {
			throw new IllegalArgumentException("必须为角色赋予权限");
		}
		if (StringUtils.isEmpty(role.getName())) {
			throw new IllegalArgumentException("角色名不能为空");
		}
		role.setModifiedName(ShiroUtils.getEmpName());
		//2.更新数据
		int rows = roleDao.updateObject(role);
		if (rows == 0) {
			throw new ServiceException("更新数据失败，数据可能已经不存在");
		}
		//3.先删除后新增关系数据
		roleMenuDao.deleteObjectsByRoleId(role.getId());
		roleMenuDao.insertObjects(role.getId(), menuIds);
		/**
		 * 1.问：用户及角色的关系数据需要删除吗？怎么添加
		 * sysUserRoleDao.deleteObjectsByRoleId(entity.getId()); //新增用户及用户角色的关系数据
		 */		
		return rows;
	}

	@Override
	public int deleteObject(Integer id) {
		//1.参数有效性验证
		if (id == null) {
			throw new IllegalArgumentException("请选中要删除的对象");
		} 
		//2.基于id删除关系数据
		roleMenuDao.deleteObjectsByRoleId(id);
		empRoleDao.deleteObjectsByRoleId(id);
		//3.基于id删除自身数据
		int rows = roleDao.deleteRoleById(id);
		if (rows == 0) {
			throw new ServiceException("该角色已经不存在了");
		}
		//4.返回结果
		return rows;
	}

	@Override
	public int saveObject(Role role, Integer[] menuIds) {
		//1.参数有效性校验
		if (role == null) {
			throw new IllegalArgumentException("保存对象不能为空");
		}
		if (menuIds == null || menuIds.length == 0) {
			throw new IllegalArgumentException("关联菜单id不能为空");
		}
		if (StringUtils.isEmpty(role.getName())) {
			throw new IllegalArgumentException("保存角色名不能为空");
		}
		role.setCreatedName(ShiroUtils.getEmpName());
		//2.保存角色自身信息
		int rows = roleDao.insertObject(role);
		//3.保存角色关系数据
		roleMenuDao.insertObjects(role.getId(), menuIds);
		//4.返回执行结果
		return rows;
	}

	@Override
	public PageObject<Role> findPageObjects(String name, Integer pageCurrent) {
		//1.参数有效性校验
		if(pageCurrent==null || pageCurrent == 0) throw new ServiceException("乜有数据");
		//2.查询所有角色分页展示
		int rowCount = roleDao.getRowCount(name);
		if(rowCount==0)
			throw new ServiceException("没有对应的记录");
		int pageSize=7;
		int startIndex=(pageCurrent-1)*pageSize;
		List<Role> records=
				roleDao.findPageRole(name, startIndex, pageSize);
		//3.返回执行结果
		return new PageObject<>(pageCurrent, pageSize, rowCount, records);
	}

	@Override
	public List<Role> findRoles() {
		return roleDao.findRoles();
	}

}
