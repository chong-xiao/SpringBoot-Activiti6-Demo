package com.cazj.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

/**
 * 部门表
 * @author UID
 */
@Data
public class Dept implements Serializable {

	private static final long serialVersionUID = -1913372653984818863L;
	private Integer id;/**部门编号*/
	private String name;/**部门名称*/
	private String sort;/**部门排序*/
	private String note;/**部门备注*/
	private String createdName;/**部门创建员工*/
	private Date createdTime;/**部门创建时间*/
	private Integer parentId;/**上级部门*/
	private Date modifiedTime;/**部门修改时间*/
	private String modifiedName;/**部门修改员工*/
}
