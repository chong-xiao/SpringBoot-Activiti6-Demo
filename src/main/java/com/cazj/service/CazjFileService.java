package com.cazj.service;

import java.util.List;

import com.cazj.common.vo.PageObject;
import com.cazj.pojo.CazjFile;

public interface CazjFileService {
	public PageObject<CazjFile> findObjectByPass(String name,Integer pageCurrent);

	public List<CazjFile> findObjectByUnsub();
	
	public List<CazjFile> findObjectByReview();
	
	int deleteObjects(Integer... ids);
	
	int saveObject(CazjFile entity);
	
	int commitObject(CazjFile entity);
	
	int updateCommit(Integer id);
	
	CazjFile findObjectById(Integer id);
	
	int updateFile(CazjFile entity);
	
	int updatePass(Integer id,String reviewUser);
	
	int updateUnpass(Integer id,String reviewUser);
}
