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
import com.yeehealth.yjk.json.bean.TokenBean;
import com.yeehealth.yjk.utils.Constant;
import com.yeehealth.yjk.utils.LogUtil;
import com.yeehealth.yjk.utils.StringUtils;
import com.yeehealth.yjk.utils.TUtils;
import com.yeehealth.yjk.utils.TimeCountUtil;
import com.yeehealth.yjk.utils.ToastUtils;

/**
 * @ClassName ForgetPasswordFirstActivity
 * @Description 忘记密码第一步
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-17,下午3:20:32
 */
public class ForgetPasswordFirstActivity extends BaseActivity implements
		OnClickListener {
	/** 请求后台发送验证码成功 */
	protected static final int GETCODE_SUCESS = 0;
	/** 请求后台发送验证码失败 */
	protected static final int GETCODE_FAIL = 1;
	/** 请求校验验证码成功 */
	protected static final int VERFIY_CODE_SUCESS = 2;
	/** 手机号输入框 */
	private EditText et_phone;
	/** 验证码输入框 */
	private EditText et_code;
	/** 发送验证码按钮 */
	private Button btn_send_code;
	/** 下一步按钮 */
	private Button btn_next;
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 标题 */
	private TextView tv_title;
	/** 手机号 */
	private String phone;
	/** 注册第一次交互返回的状态码 */
	private String status;
	/** 验证码 */
	private String code;
	private TimeCountUtil time;

	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GETCODE_SUCESS:
				JsonBean<Object> jsonBean = (JsonBean<Object>) msg.obj;
				ToastUtils.showMessage(jsonBean.getMsg() + "",
						ForgetPasswordFirstActivity.this);
				status = jsonBean.getStatus();
				if ("200".equals(status)) {
					time.start();
				}
				LogUtil.i("tyy", status);
				break;
			case GETCODE_FAIL:
				ToastUtils.showMessage(msg.obj.toString() + "",
						ForgetPasswordFirstActivity.this);
				break;
			case VERFIY_CODE_SUCESS:
				TokenBean tokenBean = (TokenBean) msg.obj;
				ToastUtils.showMessage(tokenBean.getMsg() + "",
						ForgetPasswordFirstActivity.this);
				if ("200".equals(tokenBean.getStatus())) {
					Intent intent = new Intent(
							ForgetPasswordFirstActivity.this,
							ForgetPasswordSecondActivity.class);
					intent.putExtra("token", tokenBean.getData().getToken());
					startActivity(intent);
					finish();
				}
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
		setContentView(R.layout.layout_register_first);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_code = (EditText) findViewById(R.id.et_code);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		btn_next = (Button) findViewById(R.id.btn_next);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	protected void initView() {
		tv_title.setText("忘记密码");
	}

	@Override
	protected void initListener() {
		btn_send_code.setOnClickListener(this);
		btn_next.setOnClickListener(this);
		ib_back.setOnClickListener(this);
		time = new TimeCountUtil(80000, 1000, btn_send_code);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_send_code:
			if (checkPhone()) {
				getCode();
			}

			break;
		case R.id.btn_next:
			code = et_code.getText().toString().trim();
			if (StringUtils.isEmpty(code)) {
				ToastUtils.showMessage("验证码不能为空",
						ForgetPasswordFirstActivity.this);
			} else {
				if (!StringUtils.isEmpty(status) && "200".equals(status)) {
					checkCode();
				} else {
					ToastUtils.showMessage("您还没有成功获取验证码,请先获取验证码", this);
				}
			}
			break;
		case R.id.ib_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void getCode() {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> forms = new HashMap<String, String>();
						forms.put("mobile", phone);
						forms.put("type", "1");
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, forms,
								Constant.GET_CODE_URL);
						String str = new String(bt, "UTF-8");
						LogUtil.i("tyy", str);
						Gson gson = new Gson();
						JsonBean<Object> jsonBean = gson.fromJson(str,
								JsonBean.class);
						LogUtil.i("tyy", jsonBean.getStatus());
						if (jsonBean != null) {
							msg.obj = jsonBean;
							msg.what = GETCODE_SUCESS;
						} else {
							msg.obj = "网络异常";
							msg.what = GETCODE_FAIL;
						}
					} catch (Exception e) {
						msg.obj = "网络异常";
						msg.what = GETCODE_FAIL;
						e.printStackTrace();
					}
					mHandler.sendMessage(msg);
				}
			}).start();
		} else {
			ToastUtils.showMessage("未连接网络", ForgetPasswordFirstActivity.this);
			return;
		}
	}

	private boolean checkPhone() {
		phone = et_phone.getText().toString().trim();
		if (!StringUtils.isEmpty(phone) && StringUtils.isHandset(phone)) {
			return true;
		} else if (StringUtils.isEmpty(phone)) {
			ToastUtils.showMessage("手机号码不能为空", this);
			return false;
		} else {
			ToastUtils.showMessage("您输入的手机号不合法", this);
			return false;
		}
	}

	private void checkCode() {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> forms = new HashMap<String, String>();
						forms.put("mobile", phone);
						forms.put("code", code);
						forms.put("type", "1");
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, forms,
								Constant.VERFIY_CODE_URL);
						String str = new String(bt, "UTF-8");
						LogUtil.i("tyy", phone+"  "+code);
						LogUtil.i("tyy", str); 
						Gson gson = new Gson();
						TokenBean tokenBean = gson.fromJson(str,
								TokenBean.class);
						LogUtil.i("tyy", tokenBean.getMsg());
						if (tokenBean != null) {
							msg.obj = tokenBean;
							msg.what = VERFIY_CODE_SUCESS;
						} else {
							msg.obj = "网络异常";
							msg.what = GETCODE_FAIL;
						}
					} catch (Exception e) {
						msg.obj = "网络异常";
						msg.what = GETCODE_FAIL;
						e.printStackTrace();
					}
					mHandler.sendMessage(msg);
				}
			}).start();
		} else {
			ToastUtils.showMessage("未连接网络", ForgetPasswordFirstActivity.this);
			return;
		}
	}
}
