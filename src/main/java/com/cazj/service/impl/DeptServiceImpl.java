package com.cazj.service.impl;

import java.util.List;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cazj.common.annotation.RequiredLog;
import com.cazj.common.exception.ServiceException;
import com.cazj.common.vo.Node;
import com.cazj.dao.DeptDao;
import com.cazj.pojo.Dept;
import com.cazj.service.DeptService;
@Service
public class DeptServiceImpl implements DeptService {

	@Autowired
	private DeptDao deptDao;
	@RequiredLog("修改部门信息")//
	@Override
	public int updateObject(Dept dept) {
		if(dept==null)
			throw new ServiceException("修改对象不能为空");
		if(StringUtils.isEmpty(dept.getName()))
			throw new ServiceException("修改名称不能为空");
		int rows = deptDao.updateObject(dept);
		if(rows==0)
			throw new ServiceException("记录可能不存在");
		return rows;
	}
	
	@RequiredLog("保存部门信息")//哲
	@Override
	public int saveObject(Dept dept) {
		if(dept==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(dept.getName()))
			throw new ServiceException("部门名字不能为空");
		int rows = deptDao.insertObject(dept);
		if(rows==0)
			throw new ServiceException("保存失败");
		return rows;
	}
	
	@RequiredLog("部门节点查询")//哲
	@Override
	public List<Node> findZtreeDeptNodes() {
		return deptDao.findZtreeDeptNodes();
	}
	
	@RequiredLog("查询部门信息")//哲
	@Override
	public List<Map<String, Object>> findObjects() {
		return deptDao.findObject();
	}
	
	@RequiredLog("删除部门信息")//哲
	@Override
	public int deleteObject(Integer id) {
		if(id==null || id<1)
			throw new IllegalArgumentException("参数不正确");
		int childCount = deptDao.getChildCount(id);
		if(childCount>0)
			throw new ServiceException("此元素有子元素,不允许删除");
		int rows = deptDao.deleteObject(id);
		if(rows==0)
			throw new ServiceException("此信息可能不存在");
		return rows;
	}

}
