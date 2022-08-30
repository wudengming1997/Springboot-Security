package com.security.common;

import lombok.Data;

public enum ResultEnum {

	/**
	 * 未知异常
	 */
	UNKNOWN_EXCEPTION(-1, "未知异常"),
	/**
	 * 请求成功
	 */
	SUCCESS(1000, "请求成功"),
	/**
	 * 请求失败
	 */
	FAILED(0, "请求失败"),

	/**
	 * 验证码有误
	 */
	CHECKCODE_ERROR(1001, " 验证码有误"),
	/**
	 * 用户名或密码错误
	 */
	PASSWORD_ERROR(1002, "用户名或密码错误"),
	/**
	 * 参数异常
	 */
	PARAMETER_ERROR(1003, "参数异常"),
	/**
	 * 自定义参数异常
	 */
	METHOD_ARGUMENT_EXCEPTION(1003, "参数格式错误"),
	
	/**
	 * 网络异常
	 */
	WEAK_NET_WORK(1004, "网络异常"),
	
	/**
	 * JSOIN解析异常
	 */
	JSON_PARSE_ERROR(1005, "JSOIN解析异常"),
	/**
	 * 查询数据异常
	 */
	QUERY_DATA_ERROR(1006, "查询数据异常"),
	/**
	 * 添加数据异常
	 */
	INSERT_DATA_ERROR(1006, "添加数据异常"),
	/**
	 * 删除数据异常
	 */
	DELETE_DATA_ERROR(1006, "删除数据异常"),
	/**
	 * 更新数据异常
	 */
	UPDATE_DATA_ERROR(1006, "更新数据异常"),
	/**
	 * 分页数据异常
	 */
	QUERY_PAGE_PARAMTER_ERROR(1007, "分页参数异常"),

	/**
	 * 文件上传失败
	 */
	FILE_UPLOAD_ERROR(1008, "文件上传失败"),
	/**
	 * EXCEL解析异常
	 */
	EXCEL_ERROR(1009, "EXCEL文件解析异常"),
	/**
	 * 跳转至初始化页面
	 */
	LOCATION_TO_INITIALISE(1100, "跳转至初始化页面"),

	/**
	 * 未查询到数据
	 */
	QUERY_DATA_NULL(1010, "未查询到数据"),

	/**
	 * 登出
	 */
	LOG_OUT(1111, "登出"),
	
	/**
	 * 跳转至登录页面
	 */
	LOCATION_TO_LOGIN(1112, "跳转至登录页面");

	/** 响应码 */
	private Integer code;

	/** 响应信息 */
	private String msg;

	ResultEnum(Integer respCode, String respMsg) {
		this.code = respCode;
		this.msg = respMsg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}