package com.cazj.common.util;

import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Component;


import com.cazj.common.vo.EmpDeptVo;
import com.cazj.dao.EmpDao;
import com.cazj.pojo.Emp;
import com.cazj.service.EmpService;
import com.cazj.service.impl.EmpServiceImpl;


/**
 * 这个工具类是为了查看当前登录用户对象的上级领导username
 * @author 肖冲
 *
 */
@Component
public class ManagerTaskHandler implements TaskListener {
	
	private static final long serialVersionUID = 3058009608512581864L;

	@Override
	public void notify(DelegateTask delegateTask) {
		String empName = ShiroUtils.getEmpName();
		System.out.println("Activiti中间方法，获法登录用户为："+empName);
		/**从新查询当前用户对象，再获取当前用户对应的领导*/
		//这里需要从spring容器获取Service层的类。从Service层的类实例类，反查经理名称
		EmpService empService = SpringUtil.getObject(EmpService.class);
		Emp manager =empService.findEmpManagerByEmpName(empName);
		String managerName= manager.getEmpName();
		//调用dao层方法,拿 到此用户的上级领导名字
		System.out.println("当前登录对象的领导是："+managerName);
		delegateTask.setAssignee(managerName);
	}

}
