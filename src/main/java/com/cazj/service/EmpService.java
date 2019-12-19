package com.cazj.service;

import java.util.Map;

import com.cazj.common.vo.EmpDeptVo;
import com.cazj.common.vo.PageObject;
import com.cazj.pojo.Emp;

public interface EmpService {
	boolean isExists(String username);
	PageObject<EmpDeptVo> findPageEmp(String empName, Integer pageCurrent);
	int addEmp(Emp emp, Integer[] roleIds);
	int updateEmp(Emp emp, Integer[] roleIds);
	Map<String, Object> findEmpById(Integer empId);
	int updatePassword(String pwd, String newPwd, String cfgPwd);	
	int validById(Integer id,Integer valid,String modifiedName);//禁用功能
	Emp findEmpManagerByEmpName(String empName);
} 
