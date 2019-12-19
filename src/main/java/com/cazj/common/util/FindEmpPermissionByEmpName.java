package com.cazj.common.util;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.cazj.dao.EmpRoleDao;
import com.cazj.dao.MenuDao;
import com.cazj.dao.RoleMenuDao;
import com.cazj.pojo.Emp;

public class FindEmpPermissionByEmpName {
	@Autowired
	private EmpRoleDao empRoleDao;
	@Autowired
	private RoleMenuDao roleMenuDao;
	@Autowired
	private MenuDao menuDao;
	public int doFindEmpPermissionByEmpName() {
		int pre=1;
		//1.获取登陆用户信息
//				Emp emp = (Emp)principals.getPrimaryPrincipal();
//				Integer empId = emp.getId();
		Emp emp = ShiroUtils.getEmp();
		Integer empId = emp.getId();
		List<Integer> roleIds = empRoleDao.findRoleIdsByEmpId(empId);
		if(roleIds==null||roleIds.size()==0) {
			pre=0;
		}else {
		//3.基于角色id获取菜单id
		Integer[] array= {};
		List<Integer> perIds = roleMenuDao.findPerIdsByRoleIds(roleIds.toArray(array));
		if(perIds==null||perIds.size()==0) {
			pre=0;
		}else {
			//4.基于菜单id对比
				for (Integer integer : perIds) {
					if(integer!=3) {
						pre=0;
					}else {
						pre=1;
					}
				}
			}
		}
		
	return pre;
	}
}
