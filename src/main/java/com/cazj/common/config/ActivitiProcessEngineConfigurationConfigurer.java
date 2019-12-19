package com.cazj.common.config;


import org.activiti.spring.SpringProcessEngineConfiguration;
import org.activiti.spring.boot.ProcessEngineConfigurationConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import lombok.extern.slf4j.Slf4j;
/**
 *1个用于 初时activiti流程引擎的配置项， 用以将来以任何方式导入流程图时， 可以识别流程图上的中文宋体，它跟查看流程图中的字体不是共享相同变量
 *@author 肖冲
 */

@Slf4j
public class ActivitiProcessEngineConfigurationConfigurer implements ProcessEngineConfigurationConfigurer{

	@Override
	public void configure(SpringProcessEngineConfiguration processEngineConfiguration) {
		processEngineConfiguration.setActivityFontName("宋体"); //流程图上活动名称字体显示
        processEngineConfiguration.setAnnotationFontName("宋体"); //流程图上注解字体显示
        processEngineConfiguration.setLabelFontName("宋体"); //流程图上标签

        log.info("Activiti--EngineConfigurationConfigurer配置的字体#############"+processEngineConfiguration.getActivityFontName());


		
	}

}
