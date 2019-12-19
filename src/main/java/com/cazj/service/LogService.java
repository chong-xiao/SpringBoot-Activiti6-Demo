package com.cazj.service;

import org.springframework.stereotype.Service;

import com.cazj.common.vo.PageObject;
import com.cazj.pojo.CazjLog;

/**
 * 日志业务处理接口对象
 */
@Service
public interface LogService {
	
	/**
	 * 保存用户行为的日志
	 * @param entity
	 */
	void saveObject(CazjLog entity);
	
	/**
	  *  执行删除业务
	 * @param ids 多个日志id
	 * @return 删除的行数
	 */
	int deleteObjects(Integer...ids);
	
     /**
            * 分页查询当前页记录
      * @param empName 查询条件
      * @param pageCurrent 当前页码值
      * @return 封装了分页信息和当前页记录的值对象
      */
	 PageObject<CazjLog> findPageObjects(
			 String empName,
			 Integer pageCurrent);
}
