package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface RoleMenuDao {

	/**
	 * 基于roleId删除角色菜单关系数据
	 * @param roleId
	 * @return
	 */
	int deleteObjectsByRoleId(Integer roleId);
	/**
	 * 添加关系数据
	 * @param roleId
	 * @param menuIds
	 * @return
	 */
	int insertObjects(@Param("roleId")Integer roleId, @Param("menuIds")Integer[] menuIds);
	/**
	 * 	基于角色id查找菜单id的方法
	 * @param roleIds
	 * @return
	 */
	List<Integer> findMenuIdsByRoleIds(@Param("roleIds")Integer[] roleIds);
	//基于角色id查找权限id的方法（哲）
	List<Integer> findPerIdsByRoleIds(@Param("roleIds")Integer[] roleIds);
}
