package com.yeehealth.yjk.http.utils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import android.os.Handler;
import android.os.Message;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yeehealth.yjk.json.bean.JsonBean;
import com.yeehealth.yjk.json.bean.QQLoginReslutJsonBean;
import com.yeehealth.yjk.utils.Constant;
import com.yeehealth.yjk.utils.LogUtil;

public class SingletonHttpConnetManger {
	private static SingletonHttpConnetManger instance;  
	private SingletonHttpConnetManger (){}  
	/***
	 * @Title: getInstance 
	 * @Description: 线程安全的单列模式
	 * @return  SingletonHttpConnetManger 
	 * @author 	陈昌燕
	 * @date2015-3-19,下午1:59:10
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @throws
	 */
	public static synchronized SingletonHttpConnetManger getInstance() {  
		if (instance == null) {  
			instance = new SingletonHttpConnetManger();  
		}  
		return instance;  
	}

	/**
	 * @param <T>
	 * @param mHandler 
	 * @Title: userLoginType 
	 * @Description: qq第三方登陆请求服务端
	 * @param qqUserBean  void 
	 * @author 	陈昌燕
	 * @date2015-3-19,下午1:59:30
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @throws
	 */
	public <T> void userLoginType(final Handler mHandler,final HashMap<String, String> params,final JsonBean<T> bean,final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					LogUtil.e("ccy", url);
					byte[] bt = HttpUtils.useHttpClientGetData(HttpUtils.POST,
							null, params, url);
					if(bt != null && bt.length > 0){
						String str = new String(bt, "UTF-8");
						Gson gson = new Gson();
						JsonBean<T> bean = gson.fromJson(str, new TypeToken<JsonBean<T>>(){}.getType());
						if(bean != null && bean.getStatus().equals("200")){
//							msg.obj = bean.getData();
							msg.obj = bean;
							msg.what = Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED;
						}else{
							msg.obj = bean.getMsg();
							msg.what = Constant.GET_REQUEST_FAIL;
						}
					}else{
						msg.obj = "连接服务端异常,请检查您的网络";
						msg.what = Constant.GET_REQUEST_FAIL;
					}
				} catch (UnsupportedEncodingException e) {
					msg.obj = "网络异常";
					msg.what = Constant.GET_REQUEST_FAIL;
				}
				mHandler.sendMessage(msg);
			}
		}).start();
	} 
	/**
	 * @Title: userSetAvatar 
	 * @Description: 修改用户头像
	 * @author 	lyl
	 * @date2015-3-19,下午3:48:37
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @throws
	 */
	public static void uploadUserIcon(final String token,final String path,final Handler mHandler){
		new Thread(new Runnable() {
			@Override
			public void run() {
				Message msg = new Message();
				try {
					HashMap<String, String> params = new HashMap<String, String>();
					params.put("token", token);
					File file = new File(path);
					String str = HttpUtils.uploadFile(Constant.SET_AVATAR_URL
							, "avatar", params,file);
					Gson gson = new Gson();
					QQLoginReslutJsonBean jsonBean = gson.fromJson(str, QQLoginReslutJsonBean.class);
					if(jsonBean != null && jsonBean.getStatus().equals(Constant.REQUEST_SUCCESS)){
						msg.obj = jsonBean;
						msg.what = Integer.parseInt(Constant.REQUEST_SUCCESS);
					}else{
						msg.obj = jsonBean;
						msg.what = Integer.parseInt(Constant.REQUEST_FAIL);
					}
				} catch (Exception e) {
					msg.obj = "上传失败";
					msg.what = Integer.parseInt(Constant.REQUEST_FAIL);
				}
				mHandler.sendMessage(msg);
			}
		}).start();
	}
	
}
