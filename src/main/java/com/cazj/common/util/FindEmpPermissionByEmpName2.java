package com.cazj.common.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.cazj.dao.EmpRoleDao;
import com.cazj.dao.MenuDao;
import com.cazj.dao.RoleMenuDao;
import com.cazj.pojo.Emp;

public class FindEmpPermissionByEmpName2 {
	@Autowired
	private EmpRoleDao empRoleDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private MenuDao menuDao;
	public int doFindEmpPermissionByEmpName() {
		int pass=1;
		//1.获取登陆用户信息
//				Emp emp = (Emp)principals.getPrimaryPrincipal();
//				Integer empId = emp.getId();
		Emp emp = ShiroUtils.getEmp();
		Integer empId = emp.getId();
		List<Integer> roleIds = empRoleDao.findRoleIdsByEmpId(empId);
		if(roleIds==null||roleIds.size()==0) {
			pass=0;
		}else {
		//3.基于角色id获取菜单id
		Integer[] array= {};
		List<Integer> menuIds = roleMenuDao.findMenuIdsByRoleIds(roleIds.toArray(array));
		if(menuIds==null||menuIds.size()==0) {
			pass=0;
		}else {
			//4.基于菜单id对比
			List<String> permissions = menuDao.findPermissions(menuIds.toArray(array));
			for (String per : permissions) {
				if(!StringUtils.isEmpty(per)) {
					pass=0;
				}else {
					
				}
			}
		}
			
		}
		
	return pass;
	}
}
