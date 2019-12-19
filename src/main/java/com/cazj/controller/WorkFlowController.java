package com.cazj.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cazj.common.util.ShiroUtils;
import com.cazj.common.vo.JsonResult;
import com.cazj.common.vo.WorkflowBean;
import com.cazj.pojo.LeaveBill;
import com.cazj.service.WorkflowService;

@Controller
@RequestMapping("workflow")
public class WorkFlowController {

	@Autowired
	private WorkflowService workflowService;

	//用户新建请假申请时, 跳转至新增编辑页
	@RequestMapping("workflow_leave_edit")
	public String workflow_leave_edit(Model model) {
		String username = ShiroUtils.getEmpName();
		model.addAttribute("username", username);
		return "cazj/workflow_leave_edit";
	}



	//用户修改请假单时，跳转的请假单修改页

	@RequestMapping("workflow_leave_modify")
	@ResponseBody
	public JsonResult workflow_leave_modify(String workflowType,Integer workflowId) {
		if("LeaveBill".equals(workflowType)) {
			LeaveBill leaveBill=workflowService.findLeaveBillById(workflowId);
			return new JsonResult(leaveBill);
		}
		return new JsonResult("没有查询到相应记录可修改");
	}



	@RequestMapping("workflow_leave_save")
	@ResponseBody
	public JsonResult workflow_leave_save(LeaveBill leaveBill) {
		workflowService.saveLeaveBill(leaveBill);
		return new JsonResult("save ok");
	}

	@RequestMapping("workflow_leave_update")
	@ResponseBody
	public JsonResult workflow_leave_update(LeaveBill leaveBill) {
		workflowService.updateLeaveBill(leaveBill);
		return new JsonResult("update ok");
	}


	//用户发起流程的页面
	@RequestMapping("workflow_prepared_start")
	//@ResponseBody
	public String workflow_prepared_start(Model model,String workflowType) {
		Map<String,Object> map=new LinkedHashMap<>();
		//查询请假单列表
		String empName = ShiroUtils.getEmpName();
		List<LeaveBill> leaveBillList=workflowService.findLeaveBillListByUsername(empName);
		map.put("leaveBillList", leaveBillList);

		//return new JsonResult(map);
		model.addAttribute("workflowList", map);
		return "cazj/workflow_prepared_start";
	}

	//用户发起流程的页面中, 点到删除按妞
	@RequestMapping("workflow_leave_delete")
	@ResponseBody
	public JsonResult workflow_leave_delete(
			String workflowType,
			Integer workflowId) {

		switch (workflowType) {
		case "LeaveBill":
			workflowService.deleteLeaveBillById(workflowId);
			break;
		default:
			break;
		}

		return new JsonResult("delete ok");
	}

	//用户发起流程的页面中, 点到发起流程按妞
	@RequestMapping("workflow_leave_start")
	@ResponseBody
	public JsonResult workflow_leave_start(
			String workflowType,
			Integer workflowId) {

		switch (workflowType) {
		case "LeaveBill":
			workflowService.startLeaveBillProcess(workflowId);
			break;
		default:
			break;
		}

		return new JsonResult("start ok");
	}

	//<需我处理>的页面呈现, 需要查询的是acvitivi中,runtimeservice任务列表.
	@RequestMapping("workflow_handle_list")
	public String workflow_handle_list(Model model) {
		String Assignee=ShiroUtils.getEmpName();
		List<Map<String, Object>> TaskList = workflowService.findTaskListByName(Assignee);
		//System.out.println(TaskList);
		model.addAttribute("listTask", TaskList);
		return "cazj/workflow_handle_list";

	}

	//<需我处理>页面， 点击处理流程时，此URL处理此事件, 此事件需要支持302跳转
	@RequestMapping("workflow_handler_jump")
	public void workflow_handler_jump(@RequestParam("taskId")String taskId,
			HttpServletResponse response) throws IOException {
		//从流程图中，获取formKey值， 间接，将这个流程处理， 对应到相对应的业务视图去
		String url = workflowService.findTaskFormKeyByTaskId(taskId);
		url += "?taskId="+taskId;
		//拼接获得类似以下这种url地址。这样处理，可以前端就统一url跳转， 后端通过流程图中的formkey进行url跳转识别。
		/**
		 * workflow/workflow_handler_LeaveBill
		 */
		//System.out.println(url);
		response.sendRedirect(url);
	}

	@RequestMapping("workflow_view_jump")
	public String workflow_view_jump(@RequestParam("workflowType")String workflowType,Integer workflowId,Model model) {
		System.out.println("进入跳转方法");
		/**一：使用任务ID，查找请假单ID，从而获取请假单信息*/
		LeaveBill leaveBill = workflowService.findLeaveBillById(workflowId);

		model.addAttribute("leaveBill", leaveBill);

		//2：使用请假单ID，查询历史的批注信息
		List<Comment> commentList = workflowService.findCommentByLeaveBillId(workflowId);
		model.addAttribute("commentList", commentList);

		return "cazj/workflow_leave_view";
	}

	@RequestMapping("workflow_handler_LeaveBill")
	public String workflow_handler_LeaveBill(@RequestParam("taskId")String taskId,Model model) {
		System.out.println("进入跳转方法");
		/**一：使用任务ID，查找请假单ID，从而获取请假单信息*/
		LeaveBill leaveBill = workflowService.findLeaveBillByTaskId(taskId);
		model.addAttribute("taskId", taskId);
		model.addAttribute("leaveBill", leaveBill);

		/**二：已知任务ID，查询ProcessDefinitionEntiy对象，从而获取当前任务完成之后的连线名称，并放置到List<String>集合中*/
		//用于生成，前端页面，有一个“同意”+“驳回的按钮”
		List<String> outcomeList = workflowService.findOutComeListByTaskId(taskId);
		model.addAttribute("outcomeList", outcomeList);

		/**三：查询所有历史审核人的审核信息，帮助当前人完成审核，返回List<Comment>*/
		//用以填充历史处理人的审核意见
		List<Comment> commentList = workflowService.findCommentByTaskId(taskId);
		model.addAttribute("commentList", commentList);

		return "cazj/workflow_leave_handler";
	}

	//此方法是专门处理 同意+驳回 请求的
	@RequestMapping("workflow_handler_LeaveBill_submit")
	@ResponseBody
	public JsonResult workflow_handler_LeaveBill_submit(String taskId,String outcome,String message,Integer LeaveBillId) {
		workflowService.saveSubmitTask(taskId,outcome,message,LeaveBillId);
		return new JsonResult("action ok");
	}

	//这是记录审查的连接，查看所有人的历史记录。
	@RequestMapping("workflow_handle_record")
	public String workflow_handle_record(Model model,String workflowType) {
		Map<String,Object> map=new LinkedHashMap<>();
		//查询请假单列表
		List<LeaveBill> leaveBillList=workflowService.findLeaveBillList();
		map.put("leaveBillList", leaveBillList);

		//return new JsonResult(map);
		model.addAttribute("workflowList", map);
		return "cazj/workflow_handle_record";
	}

	//查看<正在审核>的流程中的流程图，方式一，基于任务Id
	@RequestMapping(value="findCurrentProcessImageByTaskId",produces = MediaType.IMAGE_JPEG_VALUE) //此注解解释等下返回的是图片流,
	//还有@ResponseBody注解,是表示方法返回byte[]
	@ResponseBody
	public byte[] findCurrentProcessImageByTaskId(String taskId) throws Exception {

		InputStream inputStream=workflowService.findImageInputStreamByTaskId(taskId);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
        
	}
	
	//查看<正在审核>的流程中的流程图，方式二，基于业务流程Id
	@RequestMapping(value="findCurrentProcessImageByWorkflowId",produces = MediaType.IMAGE_JPEG_VALUE) //此注解解释等下返回的是图片流,
	//还有@ResponseBody注解,是表示方法返回byte[]
	@ResponseBody
	public byte[] findCurrentProcessImageByWorkflowId(String workflowId) throws Exception {

		InputStream inputStream=workflowService.findImageInputStreamByWorkflowId(workflowId);
        byte[] bytes = new byte[inputStream.available()];
        inputStream.read(bytes, 0, inputStream.available());
        return bytes;
        
	}
	
}
