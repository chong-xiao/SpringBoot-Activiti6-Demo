package com.cazj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cazj.common.vo.JsonResult;
import com.cazj.pojo.Dept;
import com.cazj.service.DeptService;

@RestController
@RequestMapping("/dept/")
public class DeptController {
	
	@Autowired
	DeptService deptService;
	
	@RequestMapping("doFindZTreeNodes")
	public JsonResult doFindZTreeNodes() {
		return new JsonResult(deptService.findZtreeDeptNodes());
	}
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(Dept dept) {
		deptService.updateObject(dept);
		return new JsonResult("update ok");
	}
	
	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(Dept dept) {
		deptService.saveObject(dept);
		return new JsonResult("save ok");
	}
		
	@RequestMapping("doFindObjects")
	public JsonResult doFindObjects() {
		return new JsonResult(deptService.findObjects());
	}
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		deptService.deleteObject(id);
		return new JsonResult("delete ok");
	}
	
}
