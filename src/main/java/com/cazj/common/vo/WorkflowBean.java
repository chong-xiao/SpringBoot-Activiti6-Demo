package com.cazj.common.vo;

import java.io.File;
import java.io.Serializable;

/**
 * 这是一个通用流程处理类， 用于在Controlelr层，接收前端关于流程相关api请求时，把它若干请求参数，封装成这个类对象。以方便对象传递
 * @author 肖冲
 *
 */
public class WorkflowBean implements Serializable{

	private static final long serialVersionUID = -2144447508608155691L;
	private File processFile; //流程定义部署文件
	private String processFilename; //流程定义名称
	
	private Integer id; //申请单ID
	
	private String deploymentId; //部署对象ID
	private String imageName; //资源文件名称
	private String taskId; //任务ID
	private String outcome; //出方向 连线名称
	private String comment ; //备注
	public File getProcessFile() {
		return processFile;
	}
	public void setProcessFile(File processFile) {
		this.processFile = processFile;
	}
	public String getProcessFilename() {
		return processFilename;
	}
	public void setProcessFilename(String processFilename) {
		this.processFilename = processFilename;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDeploymentId() {
		return deploymentId;
	}
	public void setDeploymentId(String deploymentId) {
		this.deploymentId = deploymentId;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getTaskId() {
		return taskId;
	}
	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	public String getOutcome() {
		return outcome;
	}
	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "WorkflowBean [processFile=" + processFile + ", processFilename=" + processFilename + ", id=" + id
				+ ", deploymentId=" + deploymentId + ", imageName=" + imageName + ", taskId=" + taskId + ", outcome="
				+ outcome + ", comment=" + comment + "]";
	}
	

}
