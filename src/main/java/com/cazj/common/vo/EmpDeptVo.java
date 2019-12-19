package com.cazj.common.vo;

import java.io.Serializable;
import java.util.Date;

import com.cazj.pojo.Dept;

import lombok.Data;

@Data
public class EmpDeptVo implements Serializable {
	private static final long serialVersionUID = -7595989989820237562L;
	private Integer id;
	private String empName;
	private String password;//md5
	private String salt;
	private String email;
	private String phone;
	private Integer valid=1;
	private Dept dept; //private Integer deptId;
	private Date createdTime;
	private Date modifiedTime;
	private String createdEmp;
	private String modifiedEmp; 
	private String addr;
	private String cardId;
}
