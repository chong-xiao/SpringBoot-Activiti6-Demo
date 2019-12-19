package com.cazj.pojo;

import java.io.Serializable;
import java.util.Date;

/**
 * 这个是请假单的pojo类
 * @author 肖冲
 *
 */
public class LeaveBill implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6284954427786457219L;
	
	private Integer id; //主键ID
	private Integer typeId; //请假类型,  37">年假 38">事假 39">病假 40">婚假 41">产假及哺乳假 42">陪产假 43">丧假
	private Integer deeply;  //紧急程度   22">正常  23">重要 24">紧急
	private String username;  // 请假人
	private Integer leaveDays;// 请假天数
	private String content;// 请假原因
	private Date creatTime = new Date();// 创建时间

	private String remark;// 备注

	private Integer state=0;// 请假单状态 0初始录入,1.开始审批,2为审批完成

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getTypeId() {
		return typeId;
	}

	public void setTypeId(Integer typeId) {
		this.typeId = typeId;
	}

	public Integer getDeeply() {
		return deeply;
	}

	public void setDeeply(Integer deeply) {
		this.deeply = deeply;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(Integer leaveDays) {
		this.leaveDays = leaveDays;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreatTime() {
		return creatTime;
	}

	public void setCreatTime(Date creatTime) {
		this.creatTime = creatTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "LeaveBill [id=" + id + ", typeId=" + typeId + ", deeply=" + deeply + ", username=" + username
				+ ", leaveDays=" + leaveDays + ", content=" + content + ", creatTime=" + creatTime + ", remark="
				+ remark + ", state=" + state + "]";
	}
	
	
	 
	
}
