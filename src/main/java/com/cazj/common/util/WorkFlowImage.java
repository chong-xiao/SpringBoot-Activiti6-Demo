package com.cazj.common.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.image.ProcessDiagramGenerator;
import org.springframework.beans.factory.annotation.Autowired;

import com.cazj.common.exception.ServiceException;

public class WorkFlowImage {
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
	@Autowired ProcessDiagramGenerator processDiagramGenerator;
	
	/** 放在我们的业务代码中，穿个流程实例id进来，返回一个字符数组，然后该怎么处理你们应该知道怎么做了吧
	 */
	public InputStream getImageByProcessInstanceId() throws Exception
	{
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
			 * 
			 * generateDiagram(
			 * BpmnModel bpmnModel, 
			 * String imageType, 
			 * List<String> highLightedActivities  //需要高亮的活动名称
			 * );
			 * 
			 */

			//获取要生成的流程图的BpmnModel,需要传入流程定义ID
			BpmnModel bpmnModel = repositoryService.getBpmnModel("delegateTest:1:4");
			//设置需要标注高亮的节点
			List<String> hightLightElements = new ArrayList<>();
			hightLightElements.add("usertask1");

			// 获取流程图图像字符流
			InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", hightLightElements);

			return imageStream;

			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			throw new ServiceException("无法找到对应的流程图");
		}
	}
}
