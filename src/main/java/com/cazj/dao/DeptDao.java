package com.cazj.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.cazj.common.vo.Node;
import com.cazj.pojo.Dept;

@Mapper
public interface DeptDao {
	Dept findDeptById(Integer id);
	/**查询部门信息*/
	List<Map<String, Object>>findObject();
	/**基于id删除部门信息*/
	int deleteObject(Integer id);
	/**基于id查询是否有子元素*/
	int getChildCount(Integer id);
	/**部门节点查询部门信息*/
	List<Node> findZtreeDeptNodes();
	/**添加部门信息*/
	int insertObject(Dept dept);
	/**修改部门信息*/
	int updateObject(Dept dept);
}
