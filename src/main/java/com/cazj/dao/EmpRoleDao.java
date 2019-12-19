package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmpRoleDao {
	/**
	 * 添加用户角色关系数据
	 * @param empId
	 * @param roleIds
	 * @return
	 */
	int addEmpRole(Integer empId, Integer roleIds[]);
	/**
	 * 基于用户id删除角色用户数据
	 * @param empId
	 * @return
	 */
	int deleteEmpRoleById(Integer empId);
	/**
	 * 基于empid查询角色数据
	 * @param empId
	 * @return
	 */
	List<Integer> findRoleIdsByEmpId(Integer empId);
	/**
	 * 基于角色id删除用户角色数据
	 * @param roleId
	 * @return
	 */
	@Delete("delete from cazj_emp_role where roleId=#{roleId}")
	int deleteObjectsByRoleId(Integer roleId);
	
}
