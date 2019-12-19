package com.cazj.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cazj.common.util.ShiroUtils;
import com.cazj.dao.CazjFileDao;
import com.cazj.dao.NoticeDao;
import com.cazj.pojo.Notice;
import com.cazj.service.EmpService;

@Controller
@RequestMapping("/")
public class PageController {
	
	@Autowired
	EmpService empService;
	@Autowired
	NoticeDao noticeDao;
	@Autowired
	CazjFileDao cazjFileDao;
	@RequestMapping("doIndexUI")
	public String doIndexUI(Model model) {
		String empName = ShiroUtils.getEmpName();
		model.addAttribute("empName", empName);
		Notice notice = noticeDao.findMaxIdNotice();
		model.addAttribute("notice", notice);
		int allCount = noticeDao.getAllCount();
		model.addAttribute("allCount", allCount);
		int FileSum = cazjFileDao.getRowCount(null);
		model.addAttribute("FileSum",FileSum);
		return "starter";
	}
	@RequestMapping("doPageUI")
	public String doPageUI() {
		return "common/page";
	}
	@RequestMapping("doLoginUI")
	public String doLoginUI() {
		return "login";
	}
	
	@RequestMapping("{module}/{moduleUI}")
	public String doPageUI(@PathVariable String moduleUI) {
		return "cazj/"+moduleUI;
	}
}
