package com.cazj.common.util;

public class PathUtil {
	public static String getFilePath() {
		//获取项目的路径
		String path = System.getProperty("user.dir");
		String uploadPath = path + "\\src\\main\\resources\\static\\file\\";
		return uploadPath;
	} 
	public static String getFilesPath() {
		//获取项目的路径
		String path = System.getProperty("user.dir");
		String uploadPath = path + "\\src\\main\\resources\\static\\file2\\";
		return uploadPath;
	} 
}
