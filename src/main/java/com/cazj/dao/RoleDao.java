package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cazj.common.vo.RoleMenuVo;
import com.cazj.pojo.Role;

@Mapper
public interface RoleDao {
	/**
	 * 基于id查询数据
	 * @param id
	 * @return
	 */
	RoleMenuVo findObjectById(Integer id);
	/**
	/**
	 * 删除角色
	 * @param id
	 * @return
	 */
	@Delete("delete from cazj_role where id =#{id}")
	int deleteRoleById(Integer id);
	/**
	   * 添加角色数据
	 * @param role
	 * @return
	 */
	int insertObject(Role role);
	/**
	 * 基于角色名查询总记录数
	 * @param name
	 * @return
	 */
	int getRowCount(String name);
	/**
	   * 分页展示角色
	 * @return
	 */
	List<Role> findPageRole(@Param("name")String  name, @Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);
	
	/**
	 * 查询角色信息展示在复选框
	 * @return
	 */
	@Select("select id, name from cazj_role")
	List<Role> findRoles();
	/**
	   * 修改角色信息
	 * @param role
	 * @return
	 */
	int updateObject(Role role);
}
