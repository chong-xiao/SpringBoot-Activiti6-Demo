package com.cazj.service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.activiti.engine.repository.Deployment;
import org.springframework.web.multipart.MultipartFile;
/**
 * 这是一个关于流程定义发布的实现接口 层
 * @author Administrator
 *
 */
public interface ProcDefService {

	void saveNewDeploye(MultipartFile processFile, String filename);

	List<Map<String,Object>> findDeploymenList();

	void deleteDeploymentById(String id);

	List<Map<String, Object>> findProcessDefineList();

	InputStream findImageInputStream(String deploymentId, String imageName);

}
