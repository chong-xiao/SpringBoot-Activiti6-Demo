package com.cazj.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class Role implements Serializable { 
	private static final long serialVersionUID = 8961390501504302490L;
	private Integer id;/**角色id*/
	private String name;/**角色名称*/
	private String note;/**备注*/
	private String createdTime;/**角色创建时间*/
	private String createdName;/**创建员工*/
	private String modifiedTime;/**修改时间*/
	private String modifiedName;/**修改员工*/
}
