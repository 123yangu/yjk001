package com.yeehealth.yjk.activity.ui;

import java.util.HashMap;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yeehealth.yjk.R;
import com.yeehealth.yjk.R.layout;
import com.yeehealth.yjk.activity.base.BaseActivity;
import com.yeehealth.yjk.http.utils.HttpUtils;
import com.yeehealth.yjk.http.utils.SingletonHttpConnetManger;
import com.yeehealth.yjk.json.bean.JsonBean;
import com.yeehealth.yjk.json.bean.QQLoginReslutJsonBean;
import com.yeehealth.yjk.utils.Constant;
import com.yeehealth.yjk.utils.LogUtil;
import com.yeehealth.yjk.utils.SPUtil;
import com.yeehealth.yjk.utils.StringUtils;
import com.yeehealth.yjk.utils.TUtils;
import com.yeehealth.yjk.utils.ToastUtils;

/**
 * @ClassName ChangeUserNameActivity
 * @Description 修改用户名
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-17,下午3:20:11
 */
public class ChangeUserNameActivity extends BaseActivity implements
		OnClickListener {
	/** 请求数据设置昵称成功 */
	protected static final int SETNICK_SUCESS = 0;
	/** 请求数据设置昵称失败 */
	protected static final int SETNICK_FAIL = 1;
	/** 用户名输入框 */
	private EditText et_username;
	/** 完成按钮 */
	private Button btn_finish;
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 标题 */
	private TextView tv_title;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED:
				JsonBean<Object> jsonBean=(JsonBean<Object>) msg.obj;
				ToastUtils.showMessage(jsonBean.getMsg(), ChangeUserNameActivity.this);
				if("200".equals(jsonBean.getStatus())){
					Constant.isRef=true;//回账户页面要刷新
					finish();
				}
				break;
			case Constant.GET_REQUEST_FAIL:
				
				break;

			default:
				break;
			}
		};
	};
	private QQLoginReslutJsonBean loginBean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_change_username);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		et_username = (EditText) findViewById(R.id.et_username);
		btn_finish = (Button) findViewById(R.id.btn_finish);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	protected void initView() {
		tv_title.setText("用户名");
		loginBean = (QQLoginReslutJsonBean) SPUtil.readObjectExt(
				ChangeUserNameActivity.this, "");
	}

	@Override
	protected void initListener() {
		btn_finish.setOnClickListener(this);
		ib_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish:
			String username = et_username.getText().toString().trim();
			if (StringUtils.isEmpty(username)) {
				ToastUtils.showMessage("用户名不能为空", this);
			} else {
				checkUserName(username);
			}
			break;
		case R.id.ib_back:
			finish();
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: check
	 * @Description: 判断用户名是否已经存在
	 * @param username
	 *            void
	 * @author 谭元元
	 * @date2015-3-17,下午4:11:45
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @throws
	 */
	private void checkUserName(final String username) {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
//			SingletonHttpConnetManger httpConnetManger = SingletonHttpConnetManger
//					.getInstance();
//			HashMap<String, String> params = new HashMap<String, String>();
//			 params.put("token", loginBean.getToken());
//			 params.put("username", username);
//			httpConnetManger.userLoginType(mHandler, params,
//					new JsonBean<Object>(),Constant.SET_USERNAME_URL);
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("token", loginBean.getToken());
//						params.put("token", "abcd");
						 params.put("username", username);
						 LogUtil.i("TYY", username);
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, params,
								Constant.SET_USERNAME_URL);
						String str = new String(bt, "UTF-8");
						LogUtil.i("tyy", str);
						Gson gson = new Gson();
						JsonBean<Object> jsonBean = gson.fromJson(str,
								JsonBean.class);
						LogUtil.i("tyy", jsonBean.getStatus());
						if (jsonBean != null) {
							msg.obj = jsonBean;
							msg.what = Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED;
						} else {
							msg.obj = "网络异常";
							msg.what = Constant.GET_REQUEST_FAIL;
						}
					} catch (Exception e) {
						msg.obj = "网络异常";
						msg.what = Constant.GET_REQUEST_FAIL;
						e.printStackTrace();
					}
					mHandler.sendMessage(msg);
				}
			}).start();
		} else {
			ToastUtils.showMessage("未连接网络", ChangeUserNameActivity.this);
			return;
		}
	}

}
