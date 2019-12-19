package com.cazj;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cazj.dao.EmpDao;
import com.cazj.pojo.Emp;

@SpringBootTest
public class EmpTest {
	@Autowired
	EmpDao empDao;
	
	@Test
	public void empTest() {
		Emp emp = new Emp();
		emp.setAddr("123");
		emp.setPhone("123546");
		emp.setEmail("1231223132");
		emp.setEmpName("1213213");
		emp.setAddr("123456");
		emp.setId(15);
		int row = empDao.updateEmp(emp);
		System.out.println(row);
	}
}
