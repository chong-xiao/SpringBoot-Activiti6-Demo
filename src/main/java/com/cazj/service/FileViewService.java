package com.cazj.service;

import java.util.List;

import com.cazj.pojo.TransferFileInfo;


public interface FileViewService {
	public List<TransferFileInfo> fileView(String path);
}
