package com.cazj.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;
/**
 * 基于此类封装分页参数信息,此类的设计和实现可参考
 * SpringBoot中的ThymeleafProperties配置类等.
 * @ConfigurationProperties 注解描述的类可以交给
 * spring容器管理,并且可以从配置文件中读取配置信息
 * 然后赋值给对象属性(但属性需要提供set方法)
 * @author Administrator
 */
@Data
@Configuration	//springboot 2.2.1版本需要加上
@ConfigurationProperties(prefix = "db.page")
public class PageProperties {
	private Integer pageSize=3;
}
