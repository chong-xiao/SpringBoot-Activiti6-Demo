package com.cazj.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 菜单表
 * @author UID
 *
 */
@Data
public class Menu implements Serializable {
	private static final long serialVersionUID = 882986549095371622L;
	
	private Integer id;/**菜单id*/
	private String name;/**菜单名称*/
	private String url;/**菜单url*/
	private Integer type;/**菜单类型 1：菜单2：按钮*/
	private Integer sort;/**菜单排序*/
	private Date createdTime;/**创建时间*/
	private String createdName;/**创建员工*/
	private Date modifiedTime;/**修改时间*/
	private String modifiedName;/**修改员工*/
	private Integer parentId;/** 父菜单ID，一级菜单为0 */
	private String permission;/** 授权(如：emp:create) */
}
