package com.yeehealth.yjk.json.bean;

/**
 * @ClassName jsonBean
 * @Description json解析基类
 * @author 陈昌燕
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-19,上午10:09:29
 * @param <T>
 */
public class JsonBean<T> {
	private String status;// 状态
	private String msg;// 提示
	private T data;// 泛型实体bean对象
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
