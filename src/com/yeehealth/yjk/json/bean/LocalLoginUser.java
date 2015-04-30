package com.yeehealth.yjk.json.bean;

import java.io.Serializable;

/**
 * @ClassName LocalLoginUser
 * @Description 保存在本地用户名和头像
 * @author 陈昌燕
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-20,上午11:24:16
 */
public class LocalLoginUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	/**上一次用户的头像*/
	private String url;
	/**上一次登陆用户名*/
	private String userName;
	/**access_token*/
	private String access_token;
	/**access_token*/
	private String openid;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}

}
