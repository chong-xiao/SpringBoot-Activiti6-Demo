package com.cazj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.cazj.common.annotation.RequiredLog;
import com.cazj.common.exception.ServiceException;
import com.cazj.common.util.ShiroUtils;
import com.cazj.dao.NoticeDao;
import com.cazj.pojo.Notice;
import com.cazj.pojo.PageObject;
import com.cazj.service.NoticeService;
/**
 * 通知
 * @author 李东起
 */
@Service
public class NoticeServiceImpl implements NoticeService {
	@Autowired
	private NoticeDao noticeDao;
	
	@RequiredLog("查看通告")//哲
	@Override
	public PageObject<Notice> findPageObjects(String title, Integer pageCurrent) {
		//1.验证参数合法性
		if(pageCurrent==null||pageCurrent<1) {
			throw new IllegalArgumentException("当前页码值不正确");
		}
		//2.查询总记录数,并验证
		int rowCount = noticeDao.getRowCount(title);
		if(rowCount==0) {
			throw new ServiceException("无记录");
		}
		//3.查询当前页记录(pageSize=4)
		int pageSize = 7;//页面显示记录数
		int startIndex = (pageCurrent-1)*pageSize;//startIndex
		List<Notice> records = noticeDao.findPageObjects(title, startIndex, pageSize);
		//4.封装分页信息和当前页记录信息
		PageObject<Notice> p = new PageObject<>();
		p.setPageCurrent(pageCurrent);
		p.setPageSize(pageSize);
		p.setRowCount(rowCount);
		p.setRecords(records);
		p.setPageCount((rowCount-1)/pageSize+1);
		return p;
	}
	
	@RequiredLog("删除通告")//哲
	@Override
	public int deleteNoticeById(Integer id) {
		//1.参数合法性校验
		if(id==null||id<1) {
			throw new IllegalArgumentException("请选择一个");
		}
		//2.执行删除操作
		int row;
		try {
			row = noticeDao.deleteNoticeById(id);
		} catch (Throwable e) {
			e.printStackTrace();
			throw new ServiceException("系统维护中");
		}
		
		//3.对结果进行校验
		if(row==0) {
			throw new ServiceException("记录不存在");
		}
		//4.返回结果
		
		return row;
	}
	@Override
	public int insertNotice(Notice notice) {
		if(notice==null)
			throw new ServiceException("保存对象不能为空");
		if(StringUtils.isEmpty(notice.getTitle()))
			throw new ServiceException("标题不能为空");
		if(StringUtils.isEmpty(notice.getType()))
			throw new ServiceException("类型不能为空");
		if(StringUtils.isEmpty(notice.getContent()))
			throw new ServiceException("内容不能为空");
		int rows;
		try {
			notice.setEmpId(noticeDao.findEmpIdByName(ShiroUtils.getEmpName()));
			rows = noticeDao.insertNotice(notice);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("保存失败");
		}
		return rows;
	}
	@Override
	public Map<String,Object> findObjectById(Integer id) {
		//1.合法性验证
		if(id==null || id<=0)
			throw new ServiceException("id的值为不合法");
		//2.执行查询
		Notice result = noticeDao.findObjectById(id);
		//3.验证结果并返回
		if(result==null)throw new ServiceException("此记录已经不存在!");
		Map<String,Object> map=new HashMap<>();
		map.put("result", result);
		return map;
	}
	@Override
	public int updateObject(Notice entity) {
		//1.参数有效性验证
		if(entity==null)
			throw new IllegalArgumentException("保存对象不能为空");
		
		if(StringUtils.isEmpty(entity.getTitle()))
			throw new IllegalArgumentException("公告标题不能为空");
		
		if(StringUtils.isEmpty(entity.getContent()))
			throw new IllegalArgumentException("通告内容不能为空");
		
		//2.更新用户自身信息
		int rows = noticeDao.updateObject(entity);
		return rows;
	}
	
}














