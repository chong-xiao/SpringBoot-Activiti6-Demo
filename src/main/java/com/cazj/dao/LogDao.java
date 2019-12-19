package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.cazj.pojo.CazjLog;


@Mapper
public interface LogDao {
	  /**
     * 用于持久化用户行为的日志信息
  * @param entity
  * @return
  */
 int insertObject(CazjLog entity);
 /**
      * 基于多个日志记录id,进行日志删除.
  * @param ids 用于存储多个记录id
  * @return 删除的行数
  */
 int deleteObjects(@Param("ids")Integer...ids);
 /**
     *  基于条件统计记录总数
  * @return 具体行数
  * @Param 注解可以描述数据层方法参数,尤其是
     *   当方法有多个参数或者参数可能会用在动态sql中
  */
 int getRowCount(@Param("empName")String empName);
 /**
     *  查询当前页记录
  * @param empName 查询条件,允许为空
  * @param startIndex 表示起始位置
  * @param pageSize 页面大小
  * @return 当前页记录
  */
 List<CazjLog> findPageObjects(
		  @Param("empName")String empName,
		  @Param("startIndex")Integer startIndex,
		  @Param("pageSize")Integer pageSize);//param1,param2,param3
}
