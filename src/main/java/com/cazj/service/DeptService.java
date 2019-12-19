package com.cazj.service;

import java.util.List;
import java.util.Map;

import com.cazj.common.vo.Node;
import com.cazj.pojo.Dept;

public interface DeptService {
	List<Node> findZtreeDeptNodes();
	List<Map<String, Object>> findObjects();
	int deleteObject(Integer id);
	
	int saveObject(Dept dept);
	
	int updateObject(Dept dept);
}
