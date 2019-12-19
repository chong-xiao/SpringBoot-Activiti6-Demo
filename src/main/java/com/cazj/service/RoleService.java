package com.cazj.service;

import java.util.List;

import com.cazj.common.vo.RoleMenuVo;
import com.cazj.pojo.PageObject;
import com.cazj.pojo.Role;

public interface RoleService {
	List<Role> findRoles();
	
	PageObject<Role> findPageObjects(String name,Integer pageCurrent);

	int saveObject(Role role, Integer[] menuIds);

	int deleteObject(Integer id);

	int updateObject(Role role, Integer[] menuIds);

	RoleMenuVo findObjectById(Integer id);
}
