package com.cazj.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cazj.common.util.PathUtil;
import com.cazj.common.vo.JsonResult;
import com.cazj.common.vo.PageObject;
import com.cazj.pojo.CazjFile;
import com.cazj.service.CazjFileService;

@RestController
@RequestMapping("/files/")
public class CazjFileController {
	@Autowired
	private CazjFileService cazjFileService;

	@RequestMapping("doFindObjectByPass")
	public JsonResult doFindObjectByPass(String name,Integer pageCurrent) {
		PageObject<CazjFile> pageObject 
		= cazjFileService.findObjectByPass(name, pageCurrent);
		return new JsonResult(pageObject);
	}

	@RequestMapping("doFindObjectByUnsub")
	public JsonResult doFindObjectByUnsub() {
		return new JsonResult(cazjFileService.findObjectByUnsub());
	}

	@RequestMapping("doFindObjectByReview")
	public JsonResult doFindObjectByReview() {
		return new JsonResult(cazjFileService.findObjectByReview());
	}

	@RequestMapping("doDeleteObjects")
	public JsonResult doDeleteObjects(Integer... ids) {
		cazjFileService.deleteObjects(ids);
		return new JsonResult("删除成功!");
	}

	@RequestMapping("doCommitObject")
	public JsonResult doCommitObject(CazjFile entity) {
		JsonResult json = new JsonResult(entity);
		String name = json.getFile().getName();
		try {
			File file = new File(PathUtil.getFilesPath()+name+".doc");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(json.getFile().getContent());
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cazjFileService.commitObject(entity);
		return new JsonResult("提交成功!");
	}

	@RequestMapping("doSaveObject")
	public JsonResult doSaveObject(CazjFile entity) {
		JsonResult json = new JsonResult(entity);
		String name = json.getFile().getName();
		try {
			File file = new File(PathUtil.getFilesPath()+name+".doc");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(json.getFile().getContent());
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cazjFileService.saveObject(entity);
		return new JsonResult("保存成功!");
	}

	@RequestMapping("doUpdateCommit")
	public JsonResult doUpdateCommit(Integer id) {
		return new JsonResult(cazjFileService.updateCommit(id));
	}
	
	@RequestMapping("doFindObjectById")
	public JsonResult doFindObjectById(Integer id) {
		CazjFile cazjFile = cazjFileService.findObjectById(id);
		String line,lineTxt = " ";	
		try {
			File file = new File(PathUtil.getFilesPath()+cazjFile.getName()+".doc");
			FileInputStream fis = new FileInputStream(file);//创建文件输入流(输入文件)
			InputStreamReader isr = new InputStreamReader(fis,"utf-8");//创建文件读取流(读取输入流)
			BufferedReader br = new BufferedReader(isr);//创建文件读取缓冲区(读取流缓冲)		
			while((line = br.readLine())!=null){
				lineTxt+=("\n"+line);
			}
			cazjFile.setContent(lineTxt);
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
		return new JsonResult(cazjFile);	
	}
	
	@RequestMapping("doUpdateFile")
	public JsonResult doUpdateFile(CazjFile entity) {
		JsonResult json = new JsonResult(entity);
		String name = json.getFile().getName();
		try {
			File file = new File(PathUtil.getFilesPath()+name+".doc");
			if (!file.exists()) {
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream fos = new FileOutputStream(file);
			OutputStreamWriter osw = new OutputStreamWriter(fos,"utf-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(json.getFile().getContent());
			bw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		cazjFileService.updateFile(entity);
		return new JsonResult("更新成功!");
	}
	
	@RequestMapping("doUpdatePass")
	public JsonResult doUpdatePass(Integer id,String reviewUser) {
		return new JsonResult(cazjFileService.updatePass(id, reviewUser));
	}
	
	@RequestMapping("doUpdateUnpass")
	public JsonResult doUpdateUnpass(Integer id,String reviewUser) {
		return new JsonResult(cazjFileService.updateUnpass(id, reviewUser));
	}
}
