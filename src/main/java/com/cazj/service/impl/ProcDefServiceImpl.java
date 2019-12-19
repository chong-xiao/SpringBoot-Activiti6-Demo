package com.cazj.service.impl;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipInputStream;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cazj.common.exception.ServiceException;
import com.cazj.service.ProcDefService;

import io.micrometer.core.instrument.util.StringUtils;

/**
 * 这是一个关于流程定义发布的实现类
 * @author 肖冲
 *
 */

@Service
public class ProcDefServiceImpl implements ProcDefService {

	/***
	 * 注入 activiti 五大Service 方便调用
	 */
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
	
	SimpleDateFormat sdf = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
	
	/**
	 * 流程定义部署方法
	 */
	//@RequiresPermissions("sys:proc:define")
	@Override
	public void saveNewDeploye(MultipartFile processFile, String filename) {
		if(StringUtils.isBlank(filename))throw new ServiceException("定义的流程名称不能为空");
		
        // 获取文件名
        String fileName = processFile.getOriginalFilename();
        System.out.println(fileName);
        // 获取文件后缀
        String prefix="";
        try {
        	prefix=fileName.substring(fileName.lastIndexOf("."));
        	System.out.println(prefix);
		} catch (Exception e) {
			throw new ServiceException("流程定义文件不能为空");
		}
        if (!prefix.equals(".zip")) throw new ServiceException("流程定义文件的上传, 目前 只支持zip格式");
        //1：获取页面上传递的zip格式的文件，格式需要转成File类型

        try {
			//将File类型的文件转化成ZipInputStream流
			//ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(file));
        	ZipInputStream zipInputStream = new ZipInputStream(processFile.getInputStream());
			//创建部署对象
			repositoryService
				.createDeployment()
				.name(filename)
				.addZipInputStream(zipInputStream)
				.deploy();
			

        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	
	
	//查询部署对象信息, 对应表(act_re_deployment)
	@Override
	public List<Map<String,Object>> findDeploymenList() {
		List<Deployment> list = repositoryService.createDeploymentQuery().orderByDeploymenTime().asc().list();
		
		List<Map<String,Object>> listMap = new ArrayList<>();
		
		//activiti使用很多懒加载的概念, 导致list集合回传到controller的前端时, 无法封状成json数据, 需要在这里就要把list对象转成map对象. 再回传
		for (Deployment deployment : list) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", deployment.getId());
			map.put("name", deployment.getName());
			map.put("deploymentTime", sdf.format(deployment.getDeploymentTime()));
			listMap.add(map);
		}
		
		return listMap;
	}
	//查询流程定义对象信息, 对应表(act_re_procdef)
	@Override
	public List<Map<String, Object>> findProcessDefineList() {
		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().orderByProcessDefinitionId().asc().list();
		List<Map<String,Object>> listMap = new ArrayList<>();
		
		//activiti使用很多懒加载的概念, 导致list集合回传到controller的前端时, 无法封状成json数据, 需要在这里就要把list对象转成map对象. 再回传
		for (ProcessDefinition processDefinition : list) {
			Map<String, Object> map=new HashMap<>();
			map.put("id", processDefinition.getId());
			map.put("name", processDefinition.getName());
			map.put("key",processDefinition.getKey());
			map.put("version",processDefinition.getVersion());
			map.put("resourceName",processDefinition.getResourceName());
			map.put("diagramResourceName",processDefinition.getDiagramResourceName());
			map.put("deploymentId",processDefinition.getDeploymentId());
			listMap.add(map);
		}
		return listMap;
	}
	
	//通过页面传递过来的布署id, 删除
	@Override
	public void deleteDeploymentById(String id) {
		
		try {
			repositoryService.deleteDeployment(id, true);  //带有参数true,代表强制删除, 无论是否有人引用此流程
			//repositoryService.deleteDeployment(id);  
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("该流程当前有在使用,不允许删除");
		}
	}
	
	//从数据库的流程资源文件表中,获取流程图片的, 转成InputSteam流
	@Override
	public InputStream findImageInputStream(String deploymentId, String imageName) {
		if(StringUtils.isBlank(imageName))throw new ServiceException("传入的参数不正确,无法找到流程图片");
		if(StringUtils.isBlank(deploymentId))throw new ServiceException("传入的参数不正确,无法找到流程图片");
		
		return repositoryService.getResourceAsStream(deploymentId, imageName);
	}
}
