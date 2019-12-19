package com.cazj.service;

import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.apache.ibatis.annotations.Param;


import com.cazj.pojo.LeaveBill;

/**
 * 这是一个关于《工作流》处理相关的的实现接口
 * @author 肖冲
 *
 */
public interface WorkflowService {

	void saveLeaveBill(LeaveBill leaveBill);

	List<LeaveBill> findLeaveBillListByUsername(String username);
	List<LeaveBill> findLeaveBillList();
	
	void deleteLeaveBillById(@Param("workflowId")Integer workflowId);

	void startLeaveBillProcess(Integer workflowId);

	List<Map<String, Object>> findTaskListByName(String assignee);

	LeaveBill findLeaveBillById(Integer workflowId);

	void updateLeaveBill(LeaveBill leaveBill);

	String findTaskFormKeyByTaskId(String taskId);

	/**一：使用任务ID，查找请假单ID，从而获取请假单信息*/
	LeaveBill findLeaveBillByTaskId(String taskId);
	/**二：已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中*/
	List<String> findOutComeListByTaskId(String taskId);
	/**三：查询所有历史审核人的审核信息，帮助当前人完成审核，返回List<Comment>*/
	List<Comment> findCommentByTaskId(String taskId);

	//提交任务，需要的参数
	void saveSubmitTask(String taskId, String outcome, String message, Integer leaveBillId);

	List<Comment> findCommentByLeaveBillId(Integer workflowId);

	//查看<正在审核>的流程中的流程图
	InputStream findImageInputStreamByTaskId(String taskId);
	//查看<正在审核>的流程中的流程图
	InputStream findImageInputStreamByWorkflowId(String workflowId);
	/**
	 * 定认一个公共方法，接收《流程实例Id,和流程布署Id》,查询到当前“未结束”的高亮任务节点的流程图，为上面2个方法而生
	 */
	InputStream findImageInputStream(String processInstanceId, String deploymentId);

}
