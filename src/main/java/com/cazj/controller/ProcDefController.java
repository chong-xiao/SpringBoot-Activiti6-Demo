package com.cazj.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.cazj.common.vo.JsonResult;
import com.cazj.common.vo.WorkflowBean;
import com.cazj.service.ProcDefService;


/**
 * 这是一个关于流程管理相关的 controller类。
 * 处理： 流程定义 ，发布，查看相关， 属性系统管理员级别
 * @author Administrator
 *
 */

@Controller
@RequestMapping("procDef")
@ResponseBody
public class ProcDefController {

	@Autowired
	private ProcDefService procDefService;
	
	@RequestMapping("newDeploy")
	public JsonResult newdeploy(
			@RequestParam("processFile") MultipartFile processFile,
			@RequestParam("processFilename")String processFilename) {

		//完成部署
		procDefService.saveNewDeploye(processFile,processFilename);
		return new JsonResult("deploy ok");
	}
	@RequestMapping("deploymenList")
	public JsonResult deploymenList() {
		List<Map<String,Object>> depList=procDefService.findDeploymenList();
		List<Map<String,Object>> pdList=procDefService.findProcessDefineList();
		Map<String,Object> pmap=new HashMap<>();
		pmap.put("depList", depList);
		pmap.put("pdList", pdList);
		
		return new JsonResult(pmap);
	}
	
	//删除流程布署
	@RequestMapping("deleteDeploymentById")
	public JsonResult deleteDeploymentById(@RequestParam("id")String id) {
		procDefService.deleteDeploymentById(id);
		return new JsonResult("delete ok");
	}
	
	
	//查看流程定义中的流程图
	@RequestMapping(value="findProcessImage",produces = MediaType.IMAGE_JPEG_VALUE) //此注解解释等下返回的是图片流,
	//还有@ResponseBody注解,是表示方法返回byte[]
	public byte[] findProcessImage(WorkflowBean workflowBean) throws Exception {
		String deploymentId = workflowBean.getDeploymentId();
		String imageName = workflowBean.getImageName();
		InputStream inputStream=procDefService.findImageInputStream(deploymentId,imageName);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
	}
	
}
