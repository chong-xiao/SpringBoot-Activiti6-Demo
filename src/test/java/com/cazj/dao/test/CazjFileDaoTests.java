package com.cazj.dao.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.cazj.common.util.PathUtil;
import com.cazj.dao.CazjFileDao;
import com.cazj.pojo.CazjFile;

@SpringBootTest
public class CazjFileDaoTests {
	@Autowired
	private CazjFileDao cazjFileDao;
//	@Autowired
//	private CazjFileService cazjFileService;
	@Test
	public void testCazjFileDao() throws Exception{
		int rows = cazjFileDao.getRowCount("文件");
		System.out.println(rows);
		List<CazjFile> list = cazjFileDao.findObjectByPass("文件",0,12);
		for(CazjFile c:list) {
			System.out.println(c.toString());
		}
		CazjFile a=cazjFileDao.findObjectById(41);
		System.out.println(a);
		String path=PathUtil.getFilesPath();
		System.out.println(path);
//		File file = new File(PathUtil.getFilePath()+"的是根深蒂固.txt");
//		FileInputStream fis = new FileInputStream(file);//创建文件输入流(输入文件)
//		InputStreamReader isr = new InputStreamReader(fis);//创建文件读取流(读取输入流)
//		BufferedReader br = new BufferedReader(isr);//创建文件读取缓冲区(读取流缓冲)
//		String line;
//		String lineTxt="";
//		while((line = br.readLine())!=null){
//			//System.out.println(line);
//			lineTxt+=line;
//		}
//		System.out.println(lineTxt);
//		br.close();
//		try {
//			File file = new File(PathUtil.getFilePath()+123+".doc");
//			if (!file.exists())
//				file.getParentFile().mkdirs();
//				file.createNewFile();
//			FileOutputStream fos = new FileOutputStream(file);
//			OutputStreamWriter osw = new OutputStreamWriter(fos,"utf-8");
//			BufferedWriter bw = new BufferedWriter(osw);
//			bw.write("如果文件不存在就创建!");
//			bw.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}
}
