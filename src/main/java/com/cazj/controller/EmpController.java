package com.cazj.controller;

import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cazj.common.util.FindEmpPermissionByEmpName;
import com.cazj.common.vo.JsonResult;
import com.cazj.dao.EmpRoleDao;
import com.cazj.dao.MenuDao;
import com.cazj.dao.RoleMenuDao;
import com.cazj.pojo.Emp;
import com.cazj.service.EmpService;

@RestController
@RequestMapping("/emp/")
public class EmpController {

	@Autowired
	EmpService empService;

	@RequestMapping("doUpdatePassword")
	public JsonResult doUpdatePassword(String pwd, String newPwd, String cfgPwd) {
		empService.updatePassword(pwd, newPwd, cfgPwd);
		return new JsonResult("update ok");
	}


	@RequestMapping("doFindEmpPermissionByEmpName")
	public JsonResult doFindEmpPermission(){//哲
		FindEmpPermissionByEmpName per = new FindEmpPermissionByEmpName();
		return new JsonResult(per);
	}
	@RequestMapping("doUpdateObject")
	public JsonResult doUpdateObject(Emp emp,Integer[] roleIds){
		empService.updateEmp(emp, roleIds);
		return new JsonResult("update ok");
	}

	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id){
		Map<String,Object> map = empService.findEmpById(id);
		return new JsonResult(map);
	}

	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(Emp emp,Integer[] roleIds) {
		empService.addEmp(emp, roleIds);
		return new JsonResult("add ok");
	}

	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String empName, Integer pageCurrent) {
		return new JsonResult(empService.findPageEmp(empName, pageCurrent));
	}

	@RequestMapping("doLogin")
	public JsonResult doLogin(boolean isRememberMe, String username, String password) {
		//1.封装用户信息
		UsernamePasswordToken token = new UsernamePasswordToken(username, password);
		if (isRememberMe) {
			token.setRememberMe(true);
		}
		//2.提交用户信息 
		Subject subject = SecurityUtils.getSubject();
		subject.login(token);
		return new JsonResult("login ok");
	}

	@RequestMapping("isExists")
	public JsonResult isExists(String username) {
		boolean flag = empService.isExists(username);
		return new JsonResult(flag);
	}
	@RequestMapping("doValidById")
	public JsonResult doValidById(Integer id,Integer valid){
		empService.validById(
				id,valid, "admin"); //"admin"用户将来是登陆用户
		return new JsonResult("update ok");
	}


}
