package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cazj.common.vo.Node;
import com.cazj.pojo.Menu;

@Mapper
public interface MenuDao {
	
	/**修改菜单*/
	int updateObject(Menu menu);
	

	/**保存菜单信息*/
	int insertObject(Menu menu);
	
	/**
	 * 查询菜单id是否有记录
	 * @param id
	 * @return
	 */
	int getChildCount(Integer id);
	/**
	 * 删除菜单id记录
	 * @param id
	 * @return
	 */
	int deleteObject(Integer id);
	
	
	//菜单id查找权限标识的方法(哲)
	List<String> findPermissions(
			@Param("menuIds")
			Integer[] menuIds);
	/**
	 * 查询所有菜单
	 * @return
	 */
	List<Menu> findAllMenu();
	/**
	 * 查询菜单树状图
	 * @return
	 */
	List<Node> findZTreeNodes();
}
