package com.cazj.service;

import java.util.List;

import com.cazj.common.vo.Node;
import com.cazj.pojo.Menu;

public interface MenuService {
	/**修改菜单信息*/
	int updateObject(Menu menu);

	/**保存菜单信息*/
	int saveObject(Menu menu);

	/**根据id删除菜单*/
	int deleteObject(Integer id);

	List<Menu> findAllMenu();
	/**查询菜单树状图*/
	List<Node> findZTreeNodes();
}
