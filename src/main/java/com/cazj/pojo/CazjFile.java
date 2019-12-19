package com.cazj.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;

@Data
public class CazjFile implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -5120408086724710871L;
	private Integer id;
	private String name;
	private Date releaseTime;
	private Date updateTime;
	private String createdUser;
	private String reviewUser;
	private Integer statusId;
	private String content;
	@Override
	public String toString() {
		return "CazjFile [id=" + id + ", name=" + name + ", releaseTime=" + releaseTime + ", updateTime=" + updateTime
				+ ", createdUser=" + createdUser + ", statusId=" + statusId + "]";
	}
	
}
