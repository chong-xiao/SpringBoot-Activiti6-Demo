package com.cazj.common.vo;

import java.io.Serializable;

import com.cazj.pojo.CazjFile;

import lombok.Data;

/**
   * 封装控制层要响应到客户端的数据
 * @author UID
 * 
 */
@Data
public class JsonResult implements Serializable {
	private static final long serialVersionUID = 342897534668560369L;
	private int state = 1;/** 状态码(1表示OK, 0表示错误) */
	private String message = "ok";/** 状态信息 */
	private Object data;/** 正确数据 */
	private CazjFile file;
	public JsonResult() {}
	/** 一般返回时调用，传递的数据信息 */
	public JsonResult(String message) {
		this.message = message;
	}
	/** 一般查询时调用，封装查询结果 */
	public JsonResult(Object data) {
		this.data = data;
	}
	/** 出现异常时调用 */
	public JsonResult(Throwable t) {
		this.state = 0;
		this.message = t.getMessage();
	}
	public JsonResult(CazjFile file) {
		this.file = file;
	}
}
