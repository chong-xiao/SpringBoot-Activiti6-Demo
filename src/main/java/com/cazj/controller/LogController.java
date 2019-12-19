package com.cazj.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cazj.common.vo.JsonResult;
import com.cazj.common.vo.PageObject;
import com.cazj.pojo.CazjLog;
import com.cazj.service.LogService;


//@Controller
//@ResponseBody
@RestController
@RequestMapping("/log/")
public class LogController {//BaseController
	 @Autowired
	 private LogService logService;
	 @RequestMapping("doDeleteObjects")
	 public JsonResult doDeleteObjects(Integer...ids) {
		 logService.deleteObjects(ids);
		 return new JsonResult("delete ok");
	 }
	 /***
	  * 分页查询请求处理方法(该方法由Spring MVC框架通过反射技术调用)
	  * @param empName 接收客户端请求中的empName值
	  * @param pageCurrent 接收客户端请求中的pageCurrent值
	  * @return 封装了业务数据以及状态信息的一个对象(JsonResult)
	  */
	 @RequestMapping("doFindPageObjects")
	 //@ResponseBody
	 public JsonResult doFindPageObjects(
			 String empName,
			 Integer pageCurrent) {
		 PageObject<CazjLog> pageObject=
		 logService.findPageObjects(empName, pageCurrent);
		 return new JsonResult(pageObject);
	 }
	 //局部异常处理
//	 @ExceptionHandler(RuntimeException.class)
//	 @ResponseBody
//	 public JsonResult doHandleRuntimeException(
//			 RuntimeException e) {
//		 return null;
//	 }
}
