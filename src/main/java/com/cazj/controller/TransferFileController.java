package com.cazj.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cazj.common.exception.ServiceException;
import com.cazj.common.util.PathUtil;
import com.cazj.common.vo.JsonResult;
import com.cazj.pojo.TransferFileInfo;
import com.cazj.service.FileViewService;

@RestController
@RequestMapping("common/")
public class TransferFileController {
	
	@RequestMapping("deletefile")
	public JsonResult deletefile(String filename) {
			filename = filename.replace("/", "\\");
			//System.out.println("filename="+filename);
		String path =PathUtil.getFilePath();
			//System.out.println("path="+path);
		String filePath = path + filename;
			System.out.println("filePath="+filePath);
		File file=new File(filePath);
        if(file.exists()&&file.isFile()) {
        	file.delete();
        	return new JsonResult("删除成功");
        }
            return new JsonResult("删除失败,请重试");
	}
	 
	@RequestMapping("deleteFolder")
	public JsonResult deleteFolder(String foldername) {
		foldername=foldername.replace("/", "\\");
		String path =PathUtil.getFilePath();
		String folderPath = path + foldername;
		System.out.println("folderPath="+folderPath);
		File folder=new File(folderPath);
		File[] list = folder.listFiles();
		//System.out.println(list.length);
		if (list.length > 0) {
			return new JsonResult("请先删除文件夹内的文件");
		} else {
			folder.delete();
			return new JsonResult("删除成功");
		}
	}
	
	@RequestMapping("newFolder")
	public JsonResult createDir(String path){
		System.out.println("path="+path);
		String folderPath = PathUtil.getFilePath() + path;
		System.out.println("Folder="+folderPath);
        File dir=new File(folderPath);
        if(!dir.exists()) {
        	dir.mkdir();
        	return new JsonResult("创建成功");
        } else {
        	throw new ServiceException("创建失败");
        }
    }
	
	@RequestMapping("upload")
	public  JsonResult upload(@RequestParam("file") MultipartFile file,String filePath) {
		//对上传的文件进行有效性判断
		if (file.isEmpty()) {
			throw new ServiceException("上传文件为空");
		}
		//获取文件名
		String fileName = file.getOriginalFilename();
		System.out.println("上传的文件名为：" + fileName);
		//获取文件的后缀名
		String suffixName = fileName.substring(fileName.lastIndexOf("."));
		System.out.println("上传的后缀名为：" + suffixName);
		//文件存储空间所在路径
		String uploadPath = PathUtil.getFilePath();
		String path = uploadPath + filePath + fileName;
		System.out.println("最终上传全路径为"+path);
		//根据文件上传的路径创建接收文件的容器
		File dest = new File(path);
		//检测是否存在目录
		if (!dest.getParentFile().exists()) {
			dest.getParentFile().mkdirs();//如果不在就新建一个目录
		}
		try {
			file.transferTo(dest);
			return new JsonResult("上传成功");
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("上传失败");
		}
	}

	@RequestMapping("download/{fileName}")
	public void download(@PathVariable String fileName,String filePath,
			HttpServletResponse response) {
			System.out.println("downloadFileName="+fileName);
		//假设下载的文件名,后期可从客户端传入
		//fileName = "微信图片_20191122163839.png";
		//文件存储空间所在路径
		String downloadPath = PathUtil.getFilePath();
		//文件下载的相对路径
		String path = downloadPath + filePath + fileName;
		System.out.println("最终下载全路径为"+path);
		File file = new File(path);
		System.out.println(path);
		if (file.exists()) {
			response.setContentType("application/force-download");// 设置强制下载不打开
			try {
				response.setHeader("Content-Disposition",
						"attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
			response.setHeader("Content-Type", "application/octet-stream");
			byte[] buffer = new byte[1024];
			FileInputStream fis = null;
			BufferedInputStream bis = null;
			try {
				fis = new FileInputStream(file);
				bis = new BufferedInputStream(fis);
				OutputStream os = response.getOutputStream();
				int i = bis.read(buffer);
				while (i != -1) {
					os.write(buffer, 0, i);
					i = bis.read(buffer);
				}
				//return new JsonResult("下载成功");
			} catch (Exception e) {
				e.printStackTrace();
				throw new ServiceException("下载过程出现异常,请重新下载");
			} finally {
				if (bis != null) {
					try {
						bis.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new ServiceException("下载过程出现异常,请重新下载");
					}
				}
				if (fis != null) {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
						throw new ServiceException("下载过程出现异常,请重新下载");
					}
				}
			}
		}
	}
	
	@Autowired
	private FileViewService fileViewService;
	
	@RequestMapping("view")
	public JsonResult view(String path) {
		List<TransferFileInfo> fileList = fileViewService.fileView(path);
		return new JsonResult(fileList);
	}
}
