package com.cazj.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.cazj.common.vo.EmpDeptVo;
import com.cazj.pojo.Emp;

@Mapper
public interface EmpDao {
	
	/**
	 * 基于员工id查找员工档案
	 * @param id
	 * @return
	 */
	EmpDeptVo findEmpById(Integer id);
	
	/**
	   * 修改员工档案
	 * @param emp
	 * @return
	 */
	int updateEmp(Emp emp);
	/**
	 * 添加员工档案
	 * @param emp
	 * @return
	 */
	int addEmp(Emp emp);
	
	/**
	 * 基于员工名查询总记录数
	 * @param empName
	 * @return
	 */
	int getRowCount(@Param("empName")String empName);
	
	/**
	 * 查询员工分页展示
	 * @param empName
	 * @param startIndex
	 * @param pageSize
	 * @return
	 */
	List<EmpDeptVo> findPageEmp(@Param("empName")String empName, @Param("startIndex")Integer startIndex, @Param("pageSize")Integer pageSize);
	
	/**
	 * 员工登陆
	 * @param username 员工账号
	 * @return 返回一个员工对象
	 */
	@Select("select * from cazj_emp where empName = #{empName}")
	Emp findEmpByUserName(@Param("empName")String empName);
	
	/**
	 * 判断员工账号是否存在
	 * @param empName
	 * @return
	 */
	@Select("select count(*) from cazj_emp where empName = #{empName}")
	boolean isExists(@Param("empName")String empName);
	/**
	 * 修改密码
	 * @param password
	 * @param salt
	 * @param id
	 * @return
	 */
	int updatePassword(@Param("password")String password, @Param("salt")String salt, @Param("id")Integer id);
	/**
	 * 禁用功能实现
	 * @param id
	 * @param valid
	 * @param modifiedName
	 * @return
	 */
	int validById(
			@Param("id")Integer id,
			@Param("valid")Integer valid,
			@Param("modifiedName")String modifiedName);
	Emp findEmpManagerByEmpName(@Param("empName")String empName);
}
