package com.cazj.common.util;

import org.apache.shiro.SecurityUtils;

import com.cazj.pojo.Emp;

public class ShiroUtils {

	public static String getEmpName() {
		return getEmp().getEmpName();
	}

	public static Emp getEmp() {
		return (Emp) SecurityUtils.getSubject().getPrincipal();
	}
}
