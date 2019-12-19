package com.cazj.pojo;

import java.io.Serializable;
import java.util.Date;

import lombok.Data;
/**
 * 通告POJO
 * @author 李东起
 */
@Data
public class Notice implements Serializable{
	private static final long serialVersionUID = -5434293746399251334L;
	
	private Integer id;//通告id
	
	private String type;//通告类型
	
	private String content;//通告内容
	
	private String title;//通告标题
	
	private Date modifyTime;//修改时间
	
	private Date noticeTime;//发布时间
	
	private Integer empId;//发布人id
	
	private String empname;//发布人
	
	private String name;//部门名称
	
}
