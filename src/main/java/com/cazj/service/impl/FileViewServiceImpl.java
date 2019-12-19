package com.cazj.service.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;

import com.cazj.common.util.PathUtil;
import com.cazj.pojo.TransferFileInfo;
import com.cazj.service.FileViewService;

@Controller
public class FileViewServiceImpl implements FileViewService {

	@Override
	public List<TransferFileInfo> fileView(String path) {
		List<TransferFileInfo> fileList = new ArrayList<TransferFileInfo>();
		String dir = PathUtil.getFilePath();
		dir = dir + path;
		File rootFile = new File(dir);
		System.out.println(rootFile);
		File[] files = rootFile.listFiles();
		for (File file : files) {
			TransferFileInfo info = new TransferFileInfo();
			if (file.isFile()?true:false) {
				info = setFileInfo(file,info);
			} else {
				info = setFolderInfo(file,info);
			}
			fileList.add(info);
		}
		return fileList;
	}
	
	public TransferFileInfo setFileInfo(File file,TransferFileInfo info) {
		info.setFileName(file.getName().toString());
		info.setIsFile(true);
		return info;
	}
	
	
	public TransferFileInfo setFolderInfo(File file,TransferFileInfo info) {
		info.setFileName(file.getName().toString());
		info.setIsFile(false);
		return info;
	}

}
