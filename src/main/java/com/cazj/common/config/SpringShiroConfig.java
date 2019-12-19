package com.cazj.common.config;

import java.util.LinkedHashMap;

import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class SpringShiroConfig {

	@Bean
	public RememberMeManager rememberMeManager() {
		CookieRememberMeManager cManager = new CookieRememberMeManager();
		SimpleCookie cookie = new SimpleCookie("rememberMe");
		cookie.setMaxAge(10*60);
		cManager.setCookie(cookie);
		return cManager;
	}

	/**
	 * 配置SecurityManager对象(Spring框架的核心管理器对象)
	 * @param realm
	 * @return
	 */
	@Bean
	public SecurityManager newSerurityManager(@Autowired Realm realm, RememberMeManager rememberMeManager) {
		DefaultWebSecurityManager sManager = new DefaultWebSecurityManager();
		sManager.setRealm(realm);
		sManager.setRememberMeManager(rememberMeManager);
		return sManager;
	}

	/**
	 * 配置ShiroFilterFactoryBean对象(通过此对象创建shiro中的过滤器对象)
	 * @param manager
	 * @return
	 */
	@Bean("shiroFilterFaction")
	public ShiroFilterFactoryBean newShiroFilterFactoryBean(@Autowired SecurityManager manager) {
		ShiroFilterFactoryBean sfBean = new ShiroFilterFactoryBean();
		sfBean.setSecurityManager(manager);
		//假如乜有认证请求先访问此认证的url
		sfBean.setLoginUrl("/doLoginUI");
		//定义map指定请求过滤规则(哪些允许匿名访问、哪些不能匿名访问)
		LinkedHashMap<String, String> map = new LinkedHashMap<>();
		//静态资源允许匿名访问："anon"
		map.put("/bower_components/**", "anon");
		map.put("/build/**", "anon");
		map.put("/dist/**", "anon");
		map.put("/plugins/**", "anon");
		map.put("/emp/doLogin", "anon");
		map.put("/doLogout", "logout");
		//除了匿名访问的资源，其他都要认证("authc")后访问
		map.put("/**", "user");//(authc)
		sfBean.setFilterChainDefinitionMap(map);
		return sfBean;
	}

	/**
	 * 配置bean对象的生命周期管理
	 * @return
	 */
	@Bean
	public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
		return new LifecycleBeanPostProcessor();
	}

	/**
	 * 通过如下配置要为业务目标对象创建代理对象(SpringBoot中可以省略)
	 * @return
	 */
	@DependsOn("lifecycleBeanPostProcessor")
	@Bean
	public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
		return new DefaultAdvisorAutoProxyCreator();
	}

	@Bean
	public AuthorizationAttributeSourceAdvisor newAuthorizationAttributeSourceAdvisor(@Autowired SecurityManager securityManager) {
		AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
		advisor.setSecurityManager(securityManager);
		return advisor;
	}
}
