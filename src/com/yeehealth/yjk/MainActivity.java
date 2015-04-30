package com.yeehealth.yjk;

import java.util.HashMap;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.yeehealth.yjk.activity.base.BaseActivity;
import com.yeehealth.yjk.activity.ui.AccountLoginActivity;
import com.yeehealth.yjk.activity.ui.ForgetPasswordFirstActivity;
import com.yeehealth.yjk.activity.ui.RegisterFirstActivity;
import com.yeehealth.yjk.http.utils.SingletonHttpConnetManger;
import com.yeehealth.yjk.json.bean.JsonBean;
import com.yeehealth.yjk.json.bean.LocalLoginUser;
import com.yeehealth.yjk.json.bean.QQLoginReslutJsonBean;
import com.yeehealth.yjk.json.bean.QQResultJsonBean;
import com.yeehealth.yjk.json.bean.QQUserBean;
import com.yeehealth.yjk.utils.Constant;
import com.yeehealth.yjk.utils.LogUtil;
import com.yeehealth.yjk.utils.SPUtil;
import com.yeehealth.yjk.utils.StringUtils;
import com.yeehealth.yjk.utils.TUtils;
import com.yeehealth.yjk.utils.ToastUtils;
import com.yeehealth.yjk.widget.cache.volley.ImageCacheManager;
import com.yeehealth.yjk.widget.circleimg.CircleImageView;

/**
 * @ClassName MainActivity
 * @Description 应用入口
 * @author 陈昌燕
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-18,下午1:47:31
 */
public class MainActivity extends BaseActivity implements OnClickListener {

	/** qq第三方登陆 */
	private ImageView qq_img;
	/** 用户头像 */
	private CircleImageView user_image_header;
	/** 账号 */
	private EditText et_account;
	/** 密码 */
	private EditText et_password;
	/** 登陆按钮 */
	private Button bt_login;
	/** 注册账号 */
	private TextView tv_register;
	/** 忘记密码 */
	private TextView tv_forget_pwd;
	/**腾讯解析qq用户的实体*/
	private UserInfo mInfo;
	public static Tencent mTencent;
	/**qq头像url*/
	private String mUserHeaderUrl;
	/**服务端返回实体*/
	private QQLoginReslutJsonBean loginReslutJsonBean;
	/**
	 * 腾讯appid
	 */
	public static String mAppid = "222222";
	private String access_token = "";
	private String openid = "";
	private String userNmae = "";
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.GET_REQUEST_FAIL:
				ToastUtils.showMessage(msg.obj.toString(), getApplicationContext());
				break;
			case Constant.GET_QQUSER_SUCCEE:
				//获得qq用户的信息成功，请求服务端
				QQUserBean qqUserBean = (QQUserBean) msg.obj;
				LocalLoginUser qq_localLoginUser = new LocalLoginUser();
				qq_localLoginUser.setAccess_token(access_token);
				qq_localLoginUser.setOpenid(openid);
				qq_localLoginUser.setUserName(userNmae);
				qq_localLoginUser.setUrl(qqUserBean.getFigureurl_qq_2());
				/*把qq头像保存下来*/
				SPUtil.saveObjectExt(getApplicationContext(), "account", qq_localLoginUser);
				Intent loginIntent = new Intent(getApplicationContext(),AccountLoginActivity.class);
				//保存登陆用户
//				if(qqUserBean != null){
//					loginReslutJsonBean.setUsername(qqUserBean.getNickname());
//					loginReslutJsonBean.setAvatar(qqUserBean.getFigureurl_qq_2());
					SPUtil.saveObjectExt(getApplicationContext(), "user", loginReslutJsonBean);
//				}
				startActivity(loginIntent);
				finish();
				break;
			case Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED:
				//				if (mTencent != null && mTencent.isSessionValid()) {
				JsonBean<QQLoginReslutJsonBean> jsonBean = (JsonBean<QQLoginReslutJsonBean>) msg.obj;
				LogUtil.e("ccy", jsonBean.toString());
				Gson gson = new Gson();
				String s_reult = gson.toJson(jsonBean.getData());
				loginReslutJsonBean = gson.fromJson(s_reult, QQLoginReslutJsonBean.class);
				if(!StringUtils.isEmpty(loginReslutJsonBean.getUsername())){
					userNmae = loginReslutJsonBean.getNickname();
				}
				if(Constant.type == 1){
					//					StringMap<QQLoginReslutJsonBean> stringMap = new StringMap<QQLoginReslutJsonBean>();
					//					stringMap = (StringMap<QQLoginReslutJsonBean>) jsonBean.get.getData();
					Intent login = new Intent(getApplicationContext(),AccountLoginActivity.class);
//					login.putExtra("userBean", loginReslutJsonBean);
					//保存登陆用户
					LocalLoginUser localLoginUser = new LocalLoginUser();
					localLoginUser.setUserName(et_account.getText().toString()+"".trim());
					localLoginUser.setUrl(loginReslutJsonBean.getAvatar());
					SPUtil.saveObjectExt(getApplicationContext(), "account", localLoginUser);
					SPUtil.saveObjectExt(getApplicationContext(), "user", loginReslutJsonBean);
					//只保存账号
					startActivity(login);
					finish();
				}
				//				}
				break;

			default:
				break;
			}
			ToastUtils.stopProgressDialog();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewById();
		initView();
		initListener();
	}

	protected void userLoginType(QQResultJsonBean qqUserBean) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", qqUserBean.getOpenid());
		params.put("provider", "qq");
		params.put("device_id", TUtils.getPhoneIMEI(getApplicationContext()).toString());
		params.put("access_token", qqUserBean.getAccess_token());
		String url = Constant.OPPENQQ_LOGIN_URL;
		JsonBean<QQLoginReslutJsonBean> jsonBean = null;
		SingletonHttpConnetManger.getInstance().userLoginType(mHandler,params,jsonBean,url);
	}

	@Override
	protected void findViewById() {
		qq_img = (ImageView) findViewById(R.id.qq_img);
		user_image_header = (CircleImageView) findViewById(R.id.user_image_header);
		et_account = (EditText) findViewById(R.id.et_account);
		et_password = (EditText) findViewById(R.id.et_password);
		bt_login = (Button) findViewById(R.id.bt_login);
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_forget_pwd = (TextView) findViewById(R.id.tv_forget_pwd);

	}

	@Override
	protected void initView() {
		if (mTencent == null) {//创建一个腾讯实例
			mTencent = Tencent.createInstance(mAppid, this);
		}

		//是否有登陆
		QQLoginReslutJsonBean loginedUser = (QQLoginReslutJsonBean) SPUtil.readObjectExt(getApplicationContext(), "user");
		if(loginedUser != null){
			Intent login = new Intent(getApplicationContext(),AccountLoginActivity.class);
			login.putExtra("userBean", loginedUser);
			startActivity(login);
			finish();
		}else{
			LocalLoginUser lastTimeLoginUser =  (LocalLoginUser) SPUtil.readObjectExt(getApplicationContext(), "account");
			if(lastTimeLoginUser != null){
				if(!StringUtils.isEmpty(lastTimeLoginUser.getUserName())){
					et_account.setText(lastTimeLoginUser.getUserName());
				}
				
				if(!StringUtils.isEmpty(lastTimeLoginUser.getUrl())){
					user_image_header.setImageUrl(lastTimeLoginUser.getUrl(), ImageCacheManager.getInstance().getImageLoader());
					user_image_header.setBorderColor(res.getColor(R.color.white));
					user_image_header.setBorderWidth(2);
				}
			}
			//判断上一次的登陆用户
		}
	}

	@Override
	protected void initListener() {
		qq_img.setOnClickListener(this);
		tv_register.setOnClickListener(this);
		tv_forget_pwd.setOnClickListener(this);
		bt_login.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.qq_img:
			onClickLogin();
			break;
		case R.id.tv_register:
			Intent registerIntent = new Intent(MainActivity.this,
					RegisterFirstActivity.class);
			startActivity(registerIntent);
			break;
		case R.id.tv_forget_pwd:
			Intent forgetIntent = new Intent(MainActivity.this,
					ForgetPasswordFirstActivity.class);
			startActivity(forgetIntent);
			break;
		case R.id.bt_login://首页用户登录
			bt_login.setEnabled(false);
			ToastUtils.startProgressDialog(this, res.getString(R.string.str_loading));
			//验证
			validationUser();
			break;
		default:
			break;
		}

	}

	private void validationUser() {
		String account = et_account.getText().toString()+"".trim();
		String password = et_password.getText().toString()+"".trim();
		if(StringUtils.isEmpty(account)){
			ToastUtils.showMessage("账号不能为空", getApplicationContext());
			return ;
		}
		if(StringUtils.isEmpty(password)){
			ToastUtils.showMessage("密码不能为空", getApplicationContext());
			return ;
		}
		Constant.type = 1;
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("account", account);
		params.put("pwd", password);
		params.put("device_id", TUtils.getPhoneIMEI(getApplicationContext()));
		userLongin(params);

	}

	private void userLongin(HashMap<String, String> params) {
		JsonBean<QQLoginReslutJsonBean> jsonBean = null;
		String url = Constant.USER_LOGIN_URL;
		SingletonHttpConnetManger.getInstance().userLoginType(mHandler, params, jsonBean, url);
	}

	/**
	 * @ClassName BaseUiListener
	 * @Description 监听qq登陆是否成功
	 * @author 陈昌燕
	 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
	 * @date 2015-3-18,下午1:51:12
	 */
	private class BaseUiListener implements IUiListener {

		@Override
		public void onComplete(Object response) {
			if (null == response) {
				ToastUtils.showMessage("返回为空,登录失败",getApplicationContext());
				return;
			}
			JSONObject jsonResponse = (JSONObject) response;
			if (null != jsonResponse && jsonResponse.length() == 0) {
				ToastUtils.showMessage("返回为空,登录失败",getApplicationContext());
				return;
			}
			String loginResult = response.toString();
			Gson gson = new Gson();
			QQResultJsonBean qqResultJsonBean = gson.fromJson(loginResult, QQResultJsonBean.class);
			if(qqResultJsonBean != null){
			   access_token = qqResultJsonBean.getAccess_token();
			   openid = qqResultJsonBean.getOpenid();
			}
			
			userLoginType(qqResultJsonBean);
			//token openid
			LogUtil.e("ccy", loginResult);
			// 有奖分享处理
			// handlePrizeShare();
			doComplete((JSONObject)response);
		}

		protected void doComplete(JSONObject values) {
			Log.e("ccy", values.toString());
		}

		@Override
		public void onError(UiError e) {
			ToastUtils.showMessage("qq登陆异常", getApplicationContext());
		}

		@Override
		public void onCancel() {
			ToastUtils.showMessage("您已经退出qq登陆", getApplicationContext());
		}
	}

	/**
	 * @Title: initOpenidAndToken 
	 * @Description: 验证用户
	 * @param jsonObject  void 
	 * @author 	陈昌燕
	 * @date2015-3-18,下午1:51:38
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @throws
	 */
	public static void initOpenidAndToken(JSONObject jsonObject) {
		try {
			String token = jsonObject.getString(Constants.PARAM_ACCESS_TOKEN);
			String expires = jsonObject.getString(Constants.PARAM_EXPIRES_IN);
			String openId = jsonObject.getString(Constants.PARAM_OPEN_ID);
			if (!TextUtils.isEmpty(token) && !TextUtils.isEmpty(expires)
					&& !TextUtils.isEmpty(openId)) {
				mTencent.setAccessToken(token, expires);
				mTencent.setOpenId(openId);
			}
		} catch(Exception e) {
		}
	}



	IUiListener loginListener = new BaseUiListener() {
		@Override
		protected void doComplete(JSONObject values) {
			Log.d("SDKQQAgentPref", "AuthorSwitch_SDK:" + SystemClock.elapsedRealtime());
			initOpenidAndToken(values);
			updateUserInfo();
		}

	};

	private void onClickLogin() {
		if (!mTencent.isSessionValid()) {
			mTencent.login(this, "all", loginListener);
			Log.d("SDKQQAgentPref", "FirstLaunch_SDK:" + SystemClock.elapsedRealtime());
		} else {
			mTencent.logout(this);
			updateUserInfo();
		}
	}

	/**
	 * @Title: updateUserInfo 
	 * @Description: 修改用户信息
	 * @author 	陈昌燕
	 * @date2015-3-18,下午1:53:47
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @throws
	 */
	private void updateUserInfo() {
		if (mTencent != null && mTencent.isSessionValid()) {
			IUiListener listener = new IUiListener() {
				@Override
				public void onError(UiError e) {
				}
				@Override
				public void onComplete(final Object response) {
					new Thread(){
						@Override
						public void run() {
							JSONObject json = (JSONObject)response;
							Message msg = new Message();
							Gson gson = new Gson();
							String strJson = json.toString();
							if(!StringUtils.isEmpty(strJson)){
								QQUserBean qqUserBean = null;
								try {
									qqUserBean = gson.fromJson(strJson, QQUserBean.class);
									msg.obj = qqUserBean;
									msg.what = Constant.GET_QQUSER_SUCCEE;
								} catch (JsonSyntaxException e) {
									msg.what = Constant.GET_REQUEST_FAIL;
									msg.obj = qqUserBean.getMsg();
								}
							}
							mHandler.sendMessage(msg);
						}

					}.start();
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(this, mTencent.getQQToken());
			mInfo.getUserInfo(listener);
		} 
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			String resulData = data.getStringExtra(Constants.LOGIN_INFO);//qq登陆失败后返回的错误信息
			ToastUtils.showMessage(resulData, getApplicationContext());
		}

	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}
	

}