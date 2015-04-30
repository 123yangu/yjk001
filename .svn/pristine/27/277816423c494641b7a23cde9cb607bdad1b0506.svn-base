package com.yeehealth.yjk.activity.ui;

import java.util.HashMap;

import android.content.Intent;
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
import com.yeehealth.yjk.json.bean.JsonBean;
import com.yeehealth.yjk.json.bean.QQLoginReslutJsonBean;
import com.yeehealth.yjk.utils.Constant;
import com.yeehealth.yjk.utils.LogUtil;
import com.yeehealth.yjk.utils.SPUtil;
import com.yeehealth.yjk.utils.StringUtils;
import com.yeehealth.yjk.utils.TUtils;
import com.yeehealth.yjk.utils.ToastUtils;

/**
 * @ClassName SendRegisterCodeActivity
 * @Description 发送注册验证码
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-17,上午9:41:23
 */
public class ForgetPasswordSecondActivity extends BaseActivity implements
		OnClickListener {
	/** 注册请求服务器成功 */
	protected static final int SETPWD_SUCESS = 0;
	/** 注册请求服务器失败 */
	protected static final int SETPWD_FAIL = 1;
	/** 密码输入框 */
	private EditText et_pwd;
	/** 再次输入密码框 */
	private EditText et_pwd_again;
	/** 完成按钮 */
	private Button btn_finish;
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 标题 */
	private TextView tv_title;
	/** 用户token */
	private String token;
	/** 手机唯一标识 */
	private String device_id;
	/** 密码 */
	private String pwd;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SETPWD_SUCESS:
				QQLoginReslutJsonBean loginBean = (QQLoginReslutJsonBean) msg.obj;
				ToastUtils.showMessage(loginBean.getMsg() + "",
						ForgetPasswordSecondActivity.this);
				String status = loginBean.getStatus();
				LogUtil.i("tyy", loginBean.getData().getToken());
				LogUtil.i("tyy", status);
				if ("200".equals(status)) {
//					Intent intent = new Intent(ForgetPasswordSecondActivity.this,
//							AccountLoginActivity.class);
//					intent.putExtra("userBean", loginBean.getData());
//					//保存登陆用户
//					SPUtil.saveObjectExt(getApplicationContext(), "user", loginBean.getData());
//					startActivity(intent);
					finish();
				}
				break;
			case SETPWD_FAIL:
				ToastUtils.showMessage(msg.obj.toString() + "",
						ForgetPasswordSecondActivity.this);
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_register_second);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		et_pwd = (EditText) findViewById(R.id.et_pwd);
		et_pwd_again = (EditText) findViewById(R.id.et_pwd_again);
		btn_finish = (Button) findViewById(R.id.btn_finish);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	protected void initView() {
		tv_title.setText("注册");
		token = getIntent().getStringExtra("token");
		device_id = TUtils.getPhoneIMEI(ForgetPasswordSecondActivity.this);
		LogUtil.i("tyy", token + "  " + device_id);
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
			if (checkPwd()) {
				register();
			}
			break;
		case R.id.ib_back:
			finish();
			break;
		default:
			break;
		}
	}

	private boolean checkPwd() {
		pwd = et_pwd.getText().toString().trim();
		String pwd_again = et_pwd_again.getText().toString().trim();
		if (StringUtils.isEmpty(pwd) || StringUtils.isEmpty(pwd_again)) {
			ToastUtils.showMessage("密码不能为空", this);
			return false;
		} else if (!pwd.equals(pwd_again)) {
			ToastUtils.showMessage("两次输入的密码不一致", this);
			return false;
		} else {
			return true;
		}

	}

	private void register() {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> forms = new HashMap<String, String>();
						forms.put("token", token);
						forms.put("pwd", pwd);
						forms.put("device_id", device_id);
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, forms,
								Constant.SET_PWD_URL);
						String str = new String(bt, "UTF-8");
						LogUtil.i("tyy", str);
						Gson gson = new Gson();
						QQLoginReslutJsonBean loginBean = gson.fromJson(str,
								QQLoginReslutJsonBean.class);
						if (loginBean != null) {
							msg.obj = loginBean;
							msg.what = SETPWD_SUCESS;
						} else {
							msg.obj = "网络异常";
							msg.what = SETPWD_FAIL;
						}
					} catch (Exception e) {
						msg.obj = "网络异常";
						msg.what = SETPWD_FAIL;
						e.printStackTrace();
					}
					mHandler.sendMessage(msg);
				}
			}).start();
		} else {
			ToastUtils.showMessage("未连接网络", ForgetPasswordSecondActivity.this);
			return;
		}
	}
}
