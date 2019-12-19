package com.cazj;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.cazj.common.vo.EmpDeptVo;
import com.cazj.common.vo.PageObject;
import com.cazj.service.EmpService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LogTest {
	@Autowired
	 private EmpService empService;
	 @Test
	 public void testSysempService() {
	
		 PageObject<EmpDeptVo> po = empService.findPageEmp("马云", 1);
		 System.out.println("rowCount:"+po.getRowCount());
	 }
}
