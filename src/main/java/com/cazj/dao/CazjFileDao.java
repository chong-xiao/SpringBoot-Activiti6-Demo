package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.cazj.pojo.CazjFile;

@Mapper
public interface CazjFileDao {
	/**查询指定文件的总数*/
	public int getRowCount(String name); 
	
	/**查询指定文件并按发布时间降序分页显示*/
	public List<CazjFile> findObjectByPass(@Param("name")String name,
											@Param("startIndex")Integer startIndex,
											@Param("pageSize")Integer pageSize);
	
	/**查询未提交和审核未过的文件*/
	public List<CazjFile> findObjectByUnsub();
	
	/**查询审核中的文件*/
	public List<CazjFile> findObjectByReview();
	
	/**基于id删除文件*/
	int deleteObjects(@Param("ids")Integer... ids); 
	
	/**新增文件*/
	int insertFile(CazjFile entity);
	
	/**提交文件*/
	@Update("update cazj_files set status_id=1,update_time=now() where id=#{id}")
	int updateCommit(Integer id);
	
	/**基于id查询文件信息*/
	CazjFile findObjectById(Integer id);
	
	/**基于id查询文件名*/
	@Select("select name from cazj_files where id=#{id}")
	String findNameById(Integer id);
	
	/**保存/提交文件*/
	int updateFile(CazjFile entity);
	
	/**审核通过*/
	int updatePass(Integer id,String reviewUser);
	
	/**审核不通过*/
	int updateUnpass(Integer id,String reviewUser);
}	
