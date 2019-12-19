package com.cazj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cazj.common.vo.JsonResult;
import com.cazj.pojo.Menu;
import com.cazj.service.MenuService;

@RestController
@RequestMapping("/menu/")
public class MenuController {

	@Autowired
	MenuService menuService;
	/**修改菜单信息*/
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(Menu menu) {
		menuService.updateObject(menu);
		return new JsonResult("update ok");
	}
	/**新增菜单信息*/
	@RequestMapping("doSaveObject")	
	public JsonResult doSaveObject(Menu menu) {
		menuService.saveObject(menu);
		return new JsonResult("save ok");
	}

	/**删除菜单信息*/
	@RequestMapping("doDeleteObject")
	public JsonResult doDeleteObject(Integer id) {
		menuService.deleteObject(id);
		return new JsonResult("delete ok");
	}
	/**查询菜单树状图*/
	@RequestMapping("doFindZtreeMenuNodes")
	public JsonResult doFindZTreeMenuNodes() {
		return new JsonResult(menuService.findZTreeNodes());
	}
	/**查看菜单信息*/
	@RequestMapping("doFindObjects")
	public JsonResult doFindObjects() {
		return new JsonResult(menuService.findAllMenu());
	}
}
