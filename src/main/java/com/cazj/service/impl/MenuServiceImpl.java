package com.cazj.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cazj.common.exception.ServiceException;
import com.cazj.common.vo.Node;
import com.cazj.dao.MenuDao;
import com.cazj.dao.RoleMenuDao;
import com.cazj.pojo.Menu;
import com.cazj.service.MenuService;

@Service
public class MenuServiceImpl implements MenuService {

	@Autowired
	MenuDao menuDao; 
	@Autowired
	RoleMenuDao roleMenuDao;
	@Override
	public int updateObject(Menu menu) {
		//1.验证是否合法
		if(menu==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(menu.getName()))
			throw new ServiceException("菜单不能为空");
		if(StringUtils.isEmpty(menu.getPermission()))
			throw new ServiceException("授权标识不能为空");
		//2.修改对象保存到数据库
		int rows = menuDao.updateObject(menu);
		if(rows==0)
			throw new ServiceException("记录可能不存在");
		//3.返回结果
		return rows;
	}

	@Override
	public int saveObject(Menu menu) {
		//1.验证参数是否合法
		if(menu==null)
			throw new IllegalArgumentException("保存对象不能为空");
		if(StringUtils.isEmpty(menu.getName()))
			throw new ServiceException("保存菜单名不能为空");
		if(StringUtils.isEmpty(menu.getPermission()))
			throw new ServiceException("授权标识不能为空");
		//2.保存信息
		int rows = menuDao.insertObject(menu);
		//3.返回数据
		return rows;
	}

	@Override
	public int deleteObject(Integer id) {
		//1.判断是否合法
		if(id==null || id<1)
			throw new IllegalArgumentException("请先选择");
		int childCount = menuDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("请先删除子菜单");
		//2.删除关系数据
		int row = roleMenuDao.deleteObjectsByRoleId(id);
		System.out.println(row);
		//3.删除菜单
		int rows = menuDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("此菜单可能不存在");
		//4.返回数据
		return rows;
	}

	@Override
	public List<Node> findZTreeNodes() {
		return menuDao.findZTreeNodes();
	}

	@Override
	public List<Menu> findAllMenu() {
		List<Menu> list = menuDao.findAllMenu();
		return list;
	}

}
