package com.cazj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cazj.common.vo.JsonResult;
import com.cazj.pojo.Role;
import com.cazj.service.RoleService;

@RestController
@RequestMapping("/role/")
public class RoleController {
	@Autowired
	RoleService roleService;
	
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		return new JsonResult(roleService.findObjectById(id));
	}
	
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(Role role, Integer[] menuIds) {
		roleService.updateObject(role, menuIds);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		roleService.deleteObject(id);
		return new JsonResult("delete ok");
	}
	
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(Role role, Integer[] menuIds) {
		roleService.saveObject(role, menuIds);
		return new JsonResult("save ok");
	}
	
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String name,Integer pageCurrent) {
		return new JsonResult(roleService.findPageObjects(name, pageCurrent));
	}
	
	@RequestMapping("doFindRoles")
	public JsonResult doFindRoles() {
		return new JsonResult(roleService.findRoles());
	}
}
