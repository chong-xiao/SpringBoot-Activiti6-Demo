package com.cazj.service.impl;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cazj.common.annotation.RequiredLog;
import com.cazj.common.config.PageProperties;
import com.cazj.common.exception.ServiceException;
import com.cazj.common.util.PathUtil;
import com.cazj.common.vo.PageObject;
import com.cazj.dao.CazjFileDao;
import com.cazj.pojo.CazjFile;
import com.cazj.service.CazjFileService;

@Service
public class CazjFileServiceImpl implements CazjFileService {
	@Autowired
	private CazjFileDao cazjFileDao;
	@Autowired
	private PageProperties pageProperties;
	
	@Override
	public PageObject<CazjFile> findObjectByPass(String name, Integer pageCurrent) {
		//1.验证参数的合法性
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码不正确");
		int rowCount = cazjFileDao.getRowCount(name);
		if(rowCount==0)
			throw new IllegalArgumentException("系统没有查询到指定文件");
		//3.基于条件查询当前页记录(pageSize定义为10)
		//3.1)定义pageSize
		int pageSize=pageProperties.getPageSize();
		//3.2计算startIndex	
		int startIndex = (pageCurrent-1)*pageSize;
		//3.3.执行当前页面数据查询操作
		List<CazjFile> records = cazjFileDao.findObjectByPass(name, startIndex, pageSize);
		//4.对分页信息以及当前页记录进行封装
		PageObject<CazjFile> pageObject = 
				new PageObject<>(pageCurrent,pageSize,rowCount,records );
		//5.返回封装数据
		return pageObject;
	}
	@RequiredLog("用户查询")//哲
	@Override
	public List<CazjFile> findObjectByUnsub() {
		List<CazjFile> list = cazjFileDao.findObjectByUnsub();
		return list;
	}
	@RequiredLog("审核查询")//哲
	@Override
	public List<CazjFile> findObjectByReview() {
		List<CazjFile> list = cazjFileDao.findObjectByReview();
		return list;
	}
	@RequiredLog("删除文件")//哲
	@Override
	public int deleteObjects(Integer... ids) {
		//1.验证参数的合法性
		if(ids==null||ids.length<1) 
			throw new IllegalArgumentException("请选择对象");
		for(int id:ids) {
			String name = cazjFileDao.findNameById(id);
			File file =new File(PathUtil.getFilesPath()+name+".doc");
			file.delete();
		}
		int rows = cazjFileDao.deleteObjects(ids);
		if(rows==0) 
			throw new ServiceException("记录不存在!");
		return rows;
	}

	@RequiredLog("提交文件")//哲
	@Override
	public int commitObject(CazjFile entity) {
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空!");
		int rows=cazjFileDao.insertFile(entity);
		if(rows==0)
			throw new ServiceException("保存失败");
		return rows;
	}
	@RequiredLog("保存文件")//哲
	@Override
	public int saveObject(CazjFile entity) {
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空!");
		int rows=cazjFileDao.insertFile(entity);
		if(rows==0)
			throw new ServiceException("保存失败");
		return rows;
	}
	@RequiredLog("更新文件")//哲
	@Override
	public int updateCommit(Integer id) {
		if(id==null||id<1) 
			throw new IllegalArgumentException("请选择一个对象！");
		int rows = cazjFileDao.updateCommit(id);
		return rows;
	}
	@RequiredLog("查询文件")
	@Override
	public CazjFile findObjectById(Integer id) {
		if(id==null||id<1) 
			throw new IllegalArgumentException("请选择一个对象！");
//		if(statusId!=1 && statusId!=2 && statusId!=3 && statusId!=4)
//			throw new ServiceException("参数不正确！");
		CazjFile cazjFile = cazjFileDao.findObjectById(id);
		return cazjFile;
	}

	@Override
	public int updatePass(Integer id, String reviewUser) {
		return cazjFileDao.updatePass(id, reviewUser);
	}

	@Override
	public int updateUnpass(Integer id, String reviewUser) {
		return cazjFileDao.updateUnpass(id, reviewUser);
	}

	@Override
	public int updateFile(CazjFile entity) {
		if(entity==null) 
			throw new IllegalArgumentException("请选择一个对象！");
		if(entity.getStatusId()!=1 && entity.getStatusId()!=2 
				&& entity.getStatusId()!=3 && entity.getStatusId()!=4)
			throw new ServiceException("参数不正确！");
		int rows=cazjFileDao.updateFile(entity);
		return rows;
	}


}
