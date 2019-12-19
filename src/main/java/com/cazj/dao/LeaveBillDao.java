package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cazj.pojo.LeaveBill;

@Mapper
public interface LeaveBillDao {

	
	int saveLeaveBill(LeaveBill leaveBill);

	List<LeaveBill> findLeaveBillListByUsername(@Param("empName")String empName);
	List<LeaveBill> findLeaveBillList();	
	int deleteLeaveBillById(Integer workflowId);

	LeaveBill findLeaveBillById(@Param("workflowId")Integer workflowId);

	int changeLeaveBillStateById(LeaveBill leaveBill);

	int updateLeaveBill(LeaveBill leaveBill);

}
