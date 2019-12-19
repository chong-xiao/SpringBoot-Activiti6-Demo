package com.cazj.service;

import java.util.Map;

import com.cazj.pojo.Notice;
import com.cazj.pojo.PageObject;

/**
 * 通知
 * @author 李东起
 */
public interface NoticeService {
	/**
	 * 通过此方法是是实现分页查询操作
	 * @param title 通告标题
	 * @param pageCurrent 当前页码值
	 * @return 当前页记录+页码信息
	 */
	PageObject<Notice> findPageObjects(String title,Integer pageCurrent);
	/**
	 * 通过ids批量删除通告
	 * @param ids
	 * @return
	 */
	int deleteNoticeById(Integer id);
	/**
	 * 通过此方法添加通知
	 * @param notice
	 * @return
	 */
	 int insertNotice(Notice notice);
	 
	 Map<String,Object> findObjectById(Integer id) ;
		/*添加用于更新通知对象的方法*/
	 int updateObject(Notice entity);

}
