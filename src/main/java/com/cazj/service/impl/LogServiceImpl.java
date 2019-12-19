package com.cazj.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cazj.common.annotation.RequiredLog;
import com.cazj.common.exception.ServiceException;
import com.cazj.common.vo.PageObject;
import com.cazj.dao.LogDao;
import com.cazj.pojo.CazjLog;
import com.cazj.service.LogService;


@Service
public class LogServiceImpl implements LogService{
	@Autowired
	private LogDao logDao;
	/**
	 * @Async 描述的方法表示这个方法要在独立于
	 * web服务器的外部线程中进行工作.
	 */
	@Async
	@Transactional(propagation = 
	               Propagation.REQUIRES_NEW)
	@Override
	public void saveObject(CazjLog entity) {
		System.out.println("log.thread.name:"+Thread.currentThread().getName());
		logDao.insertObject(entity);
		//try {
		//Thread.sleep(60000);
		//}catch(Exception e) {e.printStackTrace();}
	}
	//@RequiresPermisssion("sys:log:delete")
	
	@RequiredLog("删除日志信息")//哲
	@Override
	public int deleteObjects(Integer... ids) {
		//1.验证参数的有效性
		if(ids==null || ids.length==0)
			throw new IllegalArgumentException("请选择删除的记录");
		//2.执行删除操作
		int rows=logDao.deleteObjects(ids);
		//3.验证并返回结果
		if(rows==0)
		throw new ServiceException("记录可能已经不存在");
		return 0;
	}
	
	@RequiredLog("日志分页查询")
	@Override
	public PageObject<CazjLog> findPageObjects(
	String empName, Integer pageCurrent) {
		//1.对参数校验
		if(pageCurrent==null||pageCurrent<1)
			throw new IllegalArgumentException("页码值不正确");
		//2.基于条件查询总记录数并进行校验
		int rowCount=logDao.getRowCount(empName);
		if(rowCount==0)
			throw new ServiceException("记录不存在");
		//3.基于条件查询当前页记录
		int pageSize=3;
		int startIndex=(pageCurrent-1)*pageSize;
		List<CazjLog> records=
		logDao.findPageObjects(empName, startIndex, pageSize);
		//4.对查询结果进行封装并返回
		return new PageObject<>(pageCurrent, pageSize, rowCount,records);
	}
}





