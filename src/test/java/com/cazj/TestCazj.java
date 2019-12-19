package com.cazj;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cazj.common.vo.Node;
import com.cazj.dao.DeptDao;

@SpringBootTest
public class TestCazj {
	@Autowired
	private DeptDao deptDao;
	
	@Test
	public void testNode() {
		List<Node> rows = deptDao.findZtreeDeptNodes();
		System.out.println(rows);
	}
}
