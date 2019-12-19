package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cazj.pojo.Notice;

/**
 * 通告Dao
 * @author 李东起
 */
@Mapper
public interface NoticeDao {
	/**
	 * 通过通告标题查询表记录数
	 * @param title 通告标题
	 * @return
	 */
	int getRowCount(@Param("title")String title);
	/**
	 * 通过条件查询分页通告信息
	 * @param title 标题
	 * @param startIndex 当前页的起始位置
	 * @param pageSize 页面的显示记录数
	 * @return
	 */
	List<Notice> findPageObjects(
			@Param("title")String title,
			@Param("startIndex")Integer startIndex,
			@Param("pageSize")Integer pageSize);
	
 	/**
 	 *  查询主菜单通知面板的最新信息
 	 * @return
 	 */
 	Notice findMaxIdNotice();
 	/**
 	 *  查询所有通知记录
 	 * @return
 	 */
 	int getAllCount();
 	
 	/**
 	 * 通过id查询公告信息
 	 * @param id
 	 * @return
 	 */
 	Notice findNoticeById(@Param("id")Integer id); 
 	
 	
 	
 	/**
 	 * 通过ids批量删除通告
 	 * @param ids
 	 * @return
 	 */
 	int deleteNoticeById(@Param("id")Integer id);
 	
 	
 	
 	/**添加*/
 	@Select("select id from cazj_emp where empname=#{empname}")
 	int findEmpIdByName(@Param("empname")String empname);
 	
 	int insertNotice(Notice notice);
 	
 	/**修改*/
 	Notice findObjectById(@Param("id")Integer id);
 	
  	/*添加数据更新方法*/
  	int updateObject(Notice entity);

 	
}

























