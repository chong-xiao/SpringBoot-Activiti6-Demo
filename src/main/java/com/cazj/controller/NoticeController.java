package com.cazj.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cazj.common.vo.JsonResult;
import com.cazj.dao.NoticeDao;
import com.cazj.pojo.Notice;
import com.cazj.pojo.PageObject;
import com.cazj.service.NoticeService;

@RestController
@RequestMapping("/notice/")
public class NoticeController {
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private NoticeDao noticeDao;
	@RequestMapping("doFindPageObjects")
	public JsonResult doFindPageObjects(String title,Integer pageCurrent) {
		PageObject<Notice> pageObject = noticeService.findPageObjects(title, pageCurrent);
		return new JsonResult(pageObject);
	}
	@RequestMapping("doFindNoticeById")
	public JsonResult doFindNoticeById(Integer id) {
		return new JsonResult(noticeDao.findNoticeById(id));
	}
	@RequestMapping("doDeleteNoticeById")
	public JsonResult doDeleteNoticeById(Integer id) {
		noticeService.deleteNoticeById(id);
		return new JsonResult("删除成功");
	}
	@RequestMapping("doInsertNotice")
	public JsonResult doInsertNotice(Notice notice) {
		noticeService.insertNotice(notice);
		return new JsonResult("保存成功");
	}
	 @RequestMapping("doFindObjectById")
		public JsonResult doFindObjectById(Integer id) {
			Map<String,Object> map=
					noticeService.findObjectById(id);	
			return new JsonResult(map);				
		}

		@RequestMapping("doUpdateObject")
		public JsonResult doUpdateObject(Notice entity) {
			noticeService.updateObject(entity);
			return new JsonResult("修改成功!");
		}

	
}
