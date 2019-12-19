package com.cazj.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricVariableInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.repository.Deployment;

import org.activiti.engine.repository.ProcessDefinition;

import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cazj.common.config.ActivitiProcessEngineConfigurationConfigurer;
import com.cazj.common.exception.ServiceException;
import com.cazj.common.util.ShiroUtils;
import com.cazj.common.util.SpringUtil;
import com.cazj.dao.LeaveBillDao;
import com.cazj.pojo.LeaveBill;
import com.cazj.service.EmpService;
import com.cazj.service.WorkflowService;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * 这是一个关于《工作流》处理相关的的实现接口类
 * @author 肖冲
 *
 */

@Service
public class WorkflowServiceImpl implements WorkflowService {

	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired 
	private TaskService taskService;
	@Autowired
	private FormService formService;
	@Autowired
	private HistoryService historyService;
	//activiti引擎初始配置实现类
	@Autowired
	private ProcessEngineConfigurationImpl processEngineConfiguration;
	
	
	SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");

	@Autowired
	private LeaveBillDao leaveBillDao;

	//保存新建的请假单
	@Override
	public void saveLeaveBill(LeaveBill leaveBill) {
		if(StringUtils.isEmpty(leaveBill.getUsername()))throw new IllegalArgumentException("请假人 不能为空");
		if(leaveBill.getLeaveDays()==null||leaveBill.getLeaveDays()<1)throw new IllegalArgumentException("请假天数 不能为空");
		if(leaveBill.getTypeId()==null||leaveBill.getTypeId()<1)throw new IllegalArgumentException("请假类型 不能为空");
		int rows = leaveBillDao.saveLeaveBill(leaveBill);
		if(rows<1)throw new ServiceException("保存请假单失败");

	}

	@Override
	public void updateLeaveBill(LeaveBill leaveBill) {
		if(StringUtils.isEmpty(leaveBill.getUsername()))throw new IllegalArgumentException("请假人 不能为空");
		if(leaveBill.getLeaveDays()==null||leaveBill.getLeaveDays()<1)throw new IllegalArgumentException("请假天数 不能为空");
		if(leaveBill.getTypeId()==null||leaveBill.getTypeId()<1)throw new IllegalArgumentException("请假类型 不能为空");
		int rows = leaveBillDao.updateLeaveBill(leaveBill);
		if(rows<1)throw new ServiceException("保存请假单失败");

	}

	//基于用户名查找请假列表
	@Override
	public List<LeaveBill> findLeaveBillListByUsername(String username) {
		return leaveBillDao.findLeaveBillListByUsername(username);
	}
	//查找所有请假列表
	@Override
	public List<LeaveBill> findLeaveBillList() {
		return leaveBillDao.findLeaveBillList();
	}

	//基于请假单id，删除请假单
	@Override
	public void deleteLeaveBillById(Integer workflowId) {
		if(workflowId==null||workflowId<0)throw new IllegalArgumentException("请假id参数 不能为空");
		int rows=leaveBillDao.deleteLeaveBillById(workflowId);
		if(rows<1)throw new ServiceException("删除请假单失败");
	}
	//基于请假单id，请求请假单对象
	@Override
	public LeaveBill findLeaveBillById(Integer workflowId) {
		if(workflowId==null||workflowId<0)throw new IllegalArgumentException("请假id参数 不能为空");
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(workflowId);
		return leaveBill;
	}

	@Transactional
	@Override
	public void startLeaveBillProcess(Integer workflowId) {
		if(workflowId==null||workflowId<0)throw new IllegalArgumentException("请假id参数 不能为空");
		//获取请假单id,查询请假单对象.
		LeaveBill leaveBill=leaveBillDao.findLeaveBillById(workflowId);
		//更新请假单状态state 从0-->1
		leaveBill.setState(1);
		int rows=leaveBillDao.changeLeaveBillStateById(leaveBill);
		//使用当前对象的类名,作为流程图中的key,间接启动流程
		String key=leaveBill.getClass().getSimpleName();
		//使用key启动流程实例

		/**
		 * 4：从Session中获取当前任务的办理人，使用流程变量设置下一个任务的办理人
		 * inputUser是流程变量的名称，
		 * 获取的办理人是流程变量的值
		 */
		Map<String, Object> variables = new HashMap<String,Object>();
		variables.put("inputUser", ShiroUtils.getEmpName());//表示惟一用户


		/**
		 * 5：	(1)使用流程变量设置字符串（格式：LeaveBill.id的形式），通过设置，让启动的流程（流程实例）关联业务, 这个LeaveBill表中就可以对应得上这条记录
   				(2)使用正在执行对象表中的一个字段BUSINESS_KEY（Activiti提供的一个字段），让启动的流程（流程实例）关联业务
		 */
		String businessKey = key+"."+workflowId;
		variables.put("objId", businessKey);
		runtimeService.startProcessInstanceByKey(key, businessKey, variables);
		//更新
	}
	//<需我处理>的页面呈现, 需要查询的是acvitivi中,runtimeservice任务列表.
	@Override
	public List<Map<String,Object>> findTaskListByName(String assignee) {

		List<Task> list = taskService.createTaskQuery()//
				.taskAssignee(assignee)//指定个人任务查询
				.orderByTaskCreateTime().asc()//
				.list();

		List<Map<String,Object>> listMap = new ArrayList<>();

		//activiti使用很多懒加载的概念, 导致list集合回传到controller的前端时, 无法封状成json数据, 需要在这里就要把list对象转成map对象. 再回传
		for (Task task : list) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", task.getId());
			map.put("name", task.getName());
			map.put("createTime", sdf.format(task.getCreateTime()));
			map.put("assignee", task.getAssignee());
			//通过任务,拿到流程定义ID,
			String processDefinitionId = task.getProcessDefinitionId();
			//通过流程定义ID,,创建流程定义查询, 获得布署对象
			ProcessDefinition defProc = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
			//布署对象, 拿到部署ID
			String deploymentId = defProc.getDeploymentId();
			//部署ID,拿 到部署名称
			Deployment dep = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			//System.out.println("deploy_name"+dep.getName());
			//将部署名称也返回到前端
			map.put("deployName", dep.getName());

			listMap.add(map);
		}

		return listMap;

	}

	//基于任务id查找FormKey
	@Override
	public String findTaskFormKeyByTaskId(String taskId) {

		TaskFormData formData = formService.getTaskFormData(taskId);
		//获取Form key的值
		String url = formData.getFormKey();
		return url;
	}


	//activi审核业务核心关联查找模块
	/**一：使用任务ID，查找请假单ID，从而获取请假单信息*/
	@Override
	public LeaveBill findLeaveBillByTaskId(String taskId) {
		//1：使用任务ID，查询任务对象Task
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//2：使用任务对象Task获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		//3：使用流程实例ID，查询正在执行的执行对象表，返回流程实例对象
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
				.processInstanceId(processInstanceId)//使用流程实例ID查询
				.singleResult();
		//4：使用流程实例对象获取BUSINESS_KEY
		String buniness_key = pi.getBusinessKey();
		//5：获取BUSINESS_KEY对应的主键ID，使用主键ID，查询请假单对象（LeaveBill.1）
		String id = "";
		if(StringUtils.isNotBlank(buniness_key)){
			//截取字符串，取buniness_key小数点的第2个值
			id = buniness_key.split("\\.")[1];
		}else {
			throw new ServiceException("此流程没有关联buniness_key，无法反查业务类型");
		}
		//查询请假单对象
		//使用hql语句：from LeaveBill o where o.id=1
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(Integer.parseInt(id));
		return leaveBill;
	}

	/**二：已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中*/
	//查询act_ru_task表
	@Override
	public List<String> findOutComeListByTaskId(String taskId) {

		//1:使用任务ID，查询任务对象
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//2：获取当前任务节点对象的名字“usertask1”。
		String activityId = task.getTaskDefinitionKey();

		System.out.println("当前活动节点Id:"+activityId);
		
		//顺便获取流程定义Id
		String processDefinitionId=task.getProcessDefinitionId();
		//通过流程定义ID,,创建流程定义查询, 获得布署对象
		ProcessDefinition defProc = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		/**第2个参数需求 deploymentId*/
		//布署对象, 拿到部署ID，  部署ID是查找流程图的关键
		String deploymentId = defProc.getId();
		
		//返回存放连线的名称集合
		List<String> outgoingFlowslist = new ArrayList<String>();
        //获取要生成的流程图的BpmnModel

        BpmnModel bpmnModel = repositoryService.getBpmnModel(deploymentId);
        //通过BpmnModel，获取当前活动节点的对象
        FlowNode currentFlowNode  = (FlowNode) bpmnModel.getMainProcess().getFlowElement(activityId, true);
        //获得当前节点的出线对象
        List<SequenceFlow> outgoingFlows = currentFlowNode.getOutgoingFlows();
        if(outgoingFlows!=null && outgoingFlows.size()>0) {
	        for (SequenceFlow sequenceFlow : outgoingFlows) {
	        	//System.out.println("连接指向的下一个节点的名称:"+sequenceFlow.getTargetRef());
	        	//System.out.println("箭头的名称（同意、驳回）:"+sequenceFlow.getName());
	        	if(!StringUtils.isBlank(sequenceFlow.getName())) {
	        		outgoingFlowslist.add(sequenceFlow.getName());
	        	}else{
	        		outgoingFlowslist.add("默认提交");
				}
			}
        }
        System.out.println(outgoingFlowslist);
		return outgoingFlowslist;
	}

	/**三：查询所有历史审核人的审核信息，帮助当前人完成审核，返回List<Comment>*/
	//用以填充历史处理人的审核意见
	@Override
	public List<Comment> findCommentByTaskId(String taskId) {
		List<Comment> list = new ArrayList<Comment>();
		//使用当前的任务ID，查询当前流程对应的历史任务ID
		//使用当前任务ID，获取当前任务对象
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		
		
		//		//使用流程实例ID，查询历史任务，获取历史任务对应的每个任务ID
		//		List<HistoricTaskInstance> htiList = historyService.createHistoricTaskInstanceQuery()//历史任务表查询
		//						.processInstanceId(processInstanceId)//使用流程实例ID查询
		//						.list();
		//		//遍历集合，获取每个任务ID
		//		if(htiList!=null && htiList.size()>0){
		//			for(HistoricTaskInstance hti:htiList){
		//				//任务ID
		//				String htaskId = hti.getId();
		//				//获取批注信息
		//				List<Comment> taskList = taskService.getTaskComments(htaskId);//对用历史完成后的任务ID
		//				list.addAll(taskList);
		//			}
		//		}
		

		list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
	}



	//需我处理---处理流程时，提交的动作业务

	@Transactional
	@Override
	public void saveSubmitTask(String taskId, String outcome, String message, Integer leaveBillId) {

		//获取任务ID
		//String taskId = taskId;
		//获取连线的名称
		//String outcome = outcome;
		//批注信息
		//String message = message;
		//获取请假单ID
		Integer id = leaveBillId;
		if(outcome==null)throw new ServiceException("outcome指排动作，不能为空");
		//if(!"同意".equals(outcome)&&!"驳回".equals(outcome))throw new ServiceException("outcome指排动作，只有同意和驳回2个选项");
		/**
		 * 1：在完成之前，添加一个批注信息，向act_hi_comment表中添加数据，用于记录对当前申请人的一些审核信息
		 */
		//使用任务ID，查询任务对象，获取流程流程实例ID
		Task task = taskService.createTaskQuery()//
				.taskId(taskId)//使用任务ID查询
				.singleResult();
		//获取流程实例ID
		String processInstanceId = task.getProcessInstanceId();
		/**
		 * 注意：添加批注的时候，由于Activiti底层代码是使用：
		 * 		String userId = Authentication.getAuthenticatedUserId();
			    CommentEntity comment = new CommentEntity();
			    comment.setUserId(userId);
			  所有需要从Session中获取当前登录人，作为该任务的办理人（审核人），对应act_hi_comment表中的User_ID的字段，不过不添加审核人，该字段为null
			 所以要求，添加配置执行使用Authentication.setAuthenticatedUserId();添加当前任务的审核人
		 * */
		Authentication.setAuthenticatedUserId(ShiroUtils.getEmpName());
		taskService.addComment(taskId, processInstanceId, message);

		/**
		 * 2：如果连线的名称是“默认提交”，那么就不需要设置，如果不是，就需要设置流程变量
		 * 在完成任务之前，设置流程变量，按照连线的名称，去完成任务
				 流程变量的名称：outcome
				 流程变量的值：连线的名称
		 */
		Map<String, Object> variables = new HashMap<String,Object>();
		if(outcome!=null && !outcome.equals("默认提交")){
			variables.put("outcome", outcome);
		}
		//3：使用任务ID，完成当前人的个人任务，同时流程变量
		taskService.complete(taskId, variables);
		//4：当任务完成之后，需要指定下一个任务的办理人（使用类）-----已经开发完成

		/**
		 * 5：在完成任务之后，判断流程是否结束
   			如果流程结束了，更新请假单表的状态从1变成2（审核中-->审核完成）
		 */
		ProcessInstance pi = runtimeService.createProcessInstanceQuery()//
				.processInstanceId(processInstanceId)//使用流程实例ID查询
				.singleResult();
		//流程结束了
		if(pi==null){
			//更新请假单表的状态从1变成2（审核中-->审核完成） ,如果状态包含“终止”，则表示审核终止
			//System.out.println("流程结束了，需要把请假单改为2的状态。");
			LeaveBill bill = leaveBillDao.findLeaveBillById(id);
			if("终止".equals(variables.get("outcome"))) {
				bill.setState(3);
			}else {
				bill.setState(2);
			}
			leaveBillDao.changeLeaveBillStateById(bill);
		}

	}

	/**使用请假单ID，查询历史批注信息*/
	@Override
	public List<Comment> findCommentByLeaveBillId(Integer workflowId) {
		//使用请假单ID，查询请假单对象
		LeaveBill leaveBill = leaveBillDao.findLeaveBillById(workflowId);
		//获取对象的名称
		String objectName = leaveBill.getClass().getSimpleName();
		//组织流程表中的字段中的值
		String objId = objectName+"."+workflowId;

		/**1:使用历史的流程实例查询，返回历史的流程实例对象，获取流程实例ID*/
		//		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//对应历史的流程实例表
		//						.processInstanceBusinessKey(objId)//使用BusinessKey字段查询
		//						.singleResult();
		//		//流程实例ID
		//		String processInstanceId = hpi.getId();
		/**2:使用历史的流程变量查询，返回历史的流程变量的对象，获取流程实例ID*/
		HistoricVariableInstance hvi = historyService.createHistoricVariableInstanceQuery()//对应历史的流程变量表
				.variableValueEquals("objId", objId)//使用流程变量的名称和流程变量的值查询
				.singleResult();
		//流程实例ID
		String processInstanceId = hvi.getProcessInstanceId();
		List<Comment> list = taskService.getProcessInstanceComments(processInstanceId);
		return list;
	}


	@Override
	public InputStream findImageInputStreamByTaskId(String taskId) {
		//方式一：通过 taskId查询流程实例Id+流程定义Id
		if(StringUtils.isEmpty(taskId))throw new ServiceException("taskId参数不能为空");

		//由于通过任务id查询，历史表中任务对象， 唯一结果集
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		
		/**第1个参数需求，processInstanceId*/
		//获取流程实例Id
		String processInstanceId = task.getProcessInstanceId();
		
		//通过任务tash结果对象,拿到流程定义ID,
		String processDefinitionId = task.getProcessDefinitionId();
		//通过流程定义ID,,创建流程定义查询, 获得布署对象
		ProcessDefinition defProc = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		
		/**第2个参数需求 deploymentId*/
		//布署对象, 拿到部署ID，  部署ID是查找流程图的关键
		//deploymentId = defProc.getDeploymentId();
		String deploymentId = defProc.getId();
		
		return findImageInputStream(processInstanceId,deploymentId);
		
	}

	@Override
	public InputStream findImageInputStreamByWorkflowId(String workflowId) {
		
		//方式二：通过 业务id查询流程实例Id+流程定义Id
		if(StringUtils.isEmpty(workflowId))throw new ServiceException("workflowId参数不能为空");

		LeaveBill leaveBill=leaveBillDao.findLeaveBillById(Integer.parseInt(workflowId));
		String key=leaveBill.getClass().getSimpleName();
		String businessKey = key+"."+workflowId;
		//通过businessKey查act_hi_procinst表
		HistoricProcessInstance hpi = historyService.createHistoricProcessInstanceQuery()//对应历史的流程实例表
		.processInstanceBusinessKey(businessKey)//使用BusinessKey字段查询
		.singleResult();
		//流程实例获取流程定义Id
		String processDefinitionId=hpi.getProcessDefinitionId();
		//获取流程实例Id
		String processInstanceId = hpi.getId();
		//流程实例历史流程实例对象
		//HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();

		//通过流程定义ID,,创建流程定义查询, 获得布署对象
		ProcessDefinition defProc = repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
		//布署对象, 拿到部署ID，  部署ID是查找流程图的关键
		//deploymentId = defProc.getDeploymentId();
		String deploymentId = defProc.getId();
		
		return findImageInputStream(processInstanceId,deploymentId);
		
	}

	/**
	 * 定认一个公共方法，接收《流程实例Id,和流程布署Id》,查询到当前“未结束”的高亮任务节点的流程图
	 */
	@Override
	public InputStream findImageInputStream(String processInstanceId, String deploymentId) {
		List<String> hightLightElements = new ArrayList<>();
		
		/**第3个参数需求 高亮节点*/
		//通过流程实例ID获取最新的流程节点名称：( 查询act_hi_actinst表中，未完成的节点tashId,关键参数流程实例Id )
		
		//hightLightElements.clear();//清空List集合
		List<HistoricActivityInstance> actList = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId)
				.unfinished()
				.list();
		
		for (HistoricActivityInstance historicActivityInstance : actList) {
			String activityId = historicActivityInstance.getActivityId();
			System.out.println("activityId:"+activityId);
			hightLightElements.add(activityId);
		}
		
		try
		{
			/**
			 * 构造参数1
			 * ProcessDiagramGenerator.generateDiagram
			 * (
			 * BpmnModel bpmnModel,   //获取要生成的流程图的BpmnModel,需要传入流程定义ID
			 * String imageType,    // 默认拿取 png 图片
			 * List<String> highLightedActivities,  //需要高亮的活动名称
			 * List<String> highLightedFlows //需要高亮的箭头
			 * String activityFontName, 流程图，流动节点字体
			 * String labelFontName,  , 流程图，箭头节点字体
			 * String annotationFontName,   , 流程图，注解字体
			 * ClassLoader customClassLoader, 
			 * double scaleFactor
			 * 
			 */
			
//			ProcessEngine defaultProcessEngine = ProcessEngines.getDefaultProcessEngine();
//	        ProcessEngineConfigurationImpl processEngineConfiguration = (ProcessEngineConfigurationImpl) defaultProcessEngine.getProcessEngineConfiguration();
/**这里直接用注入方式获得processEngineConfiguration*/
	        //获取默认图片生成器
	        ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
	        //获取要生成的流程图的BpmnModel
	        BpmnModel bpmnModel = repositoryService.getBpmnModel(deploymentId);
	        //设置需要标注高亮的节点,要求传入List集合，我们只传入1个最新的节点就行了
	        
	        //判断是否仍是空值。
	        if (hightLightElements==null||hightLightElements.size()==0) {
	        	hightLightElements=Collections.<String>emptyList();
	        	System.out.println("当前查得活动节点为空，图片不需要高亮显示处理节点");
	        };

	        
	        InputStream inputStream = processDiagramGenerator.generateDiagram(bpmnModel,"PNG",hightLightElements,Collections.<String>emptyList(),"宋体", "微软雅黑", "黑体", null, 0);
			//看了generateDiagram方法return时，调到的字体，无法从配置类中获取，只能在这里直接强制写入。
	        //Collections.<String>emptyList(),作用是为了传一个空List，且不会报空指针
	        
			return inputStream;

			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ServiceException("无法找到对应的流程图");
		}
	}
	
}
