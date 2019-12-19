package com.cazj.pojo;

import java.io.Serializable;

import lombok.Data;

/**
 * 员工表
 * @author UID
 *
 */
@Data
public class Emp implements Serializable {
	
	private static final long serialVersionUID = -4922104016968112062L;
	private Integer id;/**员工编号*/
	private String empName;/**员工名称*/
	private String password;/**密码*/
	private String salt;/**盐值*/
	private String gender;/**性别 1：男 0：女*/
	private String phone;/**手机号码*/
	private String deptId;/**所属部门*/
	private String cardId;/**身份证号码*/
	private String addr;/**地址*/
	private String email;/**邮箱*/
	private String entryTime;/**员工入职日期*/
	private String createdName;/**创建员工*/
	private String createdTime;/**创建时间*/
	private String modifiedName;/**修改员工*/
	private String modifiedTime;/**修改时间*/
	private Integer valid;/**状态  0：禁用   1：正常  默认值 ：1*/
	private Integer parentId;/**上级领导*/
}
