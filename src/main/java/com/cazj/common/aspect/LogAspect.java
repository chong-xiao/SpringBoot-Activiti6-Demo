package com.cazj.common.aspect;

import java.lang.reflect.Method;
import java.util.Date;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cazj.common.annotation.RequiredLog;
import com.cazj.common.util.IPUtils;
import com.cazj.common.util.ShiroUtils;
import com.cazj.pojo.CazjLog;
import com.cazj.service.LogService;

@Aspect
@Component
public class LogAspect {
	@Pointcut("@annotation(com.cazj.common.annotation.RequiredLog)")
	public void doPointCut() {}
	@Around("doPointCut()")
	public Object aroundMethod(ProceedingJoinPoint jp) throws Throwable {
		long t1 = System.currentTimeMillis();
		Object result = jp.proceed();
		long t2 = System.currentTimeMillis();
		saveUserLog(jp,(t2-t1));
		return result;
	}
	@Autowired
	private LogService logService;
	public void saveUserLog(ProceedingJoinPoint jp,long time) throws Exception {
	//1.获取用户行为日志信息
			//1.1获取目标方法对象(class,jp)
			//1.1.1获取方法签名(通过连接点获取)
			MethodSignature ms = (MethodSignature)jp.getSignature();
			//1.1.2获取目标类类型
			Class<?> targetCls = jp.getTarget().getClass();
			//1.1.3获取目标方法对象
			Method targetMethod = targetCls.getDeclaredMethod(ms.getName(), ms.getParameterTypes());
			//1.2获取目标方法对象上RequiredLog注解中定义的操作名
			String operation="";
			RequiredLog requiredLog = targetMethod.getAnnotation(RequiredLog.class);
			if(requiredLog!=null)
			operation= requiredLog.value();
			//1.3获取目标方法对象对应的类名,方法名
			String tagetClasMethod = targetMethod.getDeclaringClass()+","+targetMethod.getName();
					
//			//1.4获取方法执行的实际参数
//			String params = new ObjectMapper().writeValueAsString(jp.getArgs());
			
			//1.5获取员工姓名
			String empName = ShiroUtils.getEmpName();
			//2.封装日志
			CazjLog obj = new CazjLog();
			obj.setIp(IPUtils.getIpAddr());
			obj.setTime(time);
			obj.setEmpName(empName);
			obj.setCreatedTime(new Date());
			obj.setMethod(tagetClasMethod);
			obj.setOperation(operation);
			//3.将日志存储到数据库
//			new Thread() {
//				 public void run() {
//				 };
//			 }.start();
			logService.saveObject(obj);
		}
}
