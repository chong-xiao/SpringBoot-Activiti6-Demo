package com.cazj.pojo;

import java.util.Date;

import lombok.Data;

@Data
public class TransferFileInfo {
	/**文件id*/
	private Integer id;
	/**文件名*/
	private String fileName;
	/**文件存储路径*/
	private String filePath;
	/**最后修改时间*/
	private Date updateTime;
	/**上传者*/
	private String uploader;
	/**是文件还是文件夹*/
	private Boolean isFile;
}
