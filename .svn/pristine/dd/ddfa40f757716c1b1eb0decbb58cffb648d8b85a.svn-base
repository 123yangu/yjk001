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

import com.google.gson.Gson;
import com.yeehealth.yjk.R;
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
import com.yeehealth.yjk.utils.TimeCountUtil;
import com.yeehealth.yjk.utils.ToastUtils;
/**
 * @ClassName BindingEmailSecondAcitivity
 * @Description 绑定邮箱第二步
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-19,上午10:12:42
 */
public class BindingEmailSecondAcitivity extends BaseActivity implements OnClickListener {
	protected static final int GETCODE_SUCESS = 0;
	/** 验证码输入框 */
	private EditText et_code;
	/** 发送验证码按钮 */
	private Button btn_send_code;
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 标题 */
	private TextView tv_title;
	private QQLoginReslutJsonBean loginBean;
	private String email;
	private TimeCountUtil time;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED:
				JsonBean<Object> jsonBean = (JsonBean<Object>) msg.obj;
				ToastUtils.showMessage(jsonBean.getMsg(),
						BindingEmailSecondAcitivity.this);
				if ("200".equals(jsonBean.getStatus())) {
//					Intent intent=new Intent(BindingEmailSecondAcitivity.this, AccountLoginActivity.class);
//					startActivity(intent);
					Constant.isRef=true;//回账户页面要刷新
					finish();
				}
				break;
			case Constant.GET_REQUEST_FAIL:
				ToastUtils.showMessage(msg.obj.toString(),
						BindingEmailSecondAcitivity.this);
				break;
			case GETCODE_SUCESS:
				JsonBean<Object> jsBean = (JsonBean<Object>) msg.obj;
				ToastUtils.showMessage(jsBean.getMsg(),
						BindingEmailSecondAcitivity.this);
				if ("200".equals(jsBean.getStatus())) {
					time.start();
				}
				break;

			default:
				break;
			}
		};
	};
	private Button btn_finish;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bindingemail_second);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		et_code = (EditText) findViewById(R.id.et_code);
		btn_send_code = (Button) findViewById(R.id.btn_send_code);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_finish = (Button) findViewById(R.id.btn_finish);
	}

	@Override
	protected void initView() {
		tv_title.setText("绑定邮箱");
		loginBean = (QQLoginReslutJsonBean) SPUtil.readObjectExt(
				BindingEmailSecondAcitivity.this, "user");
		email = getIntent().getStringExtra("email");
	}

	@Override
	protected void initListener() {
		ib_back.setOnClickListener(this);
		btn_send_code.setOnClickListener(this);
		btn_finish.setOnClickListener(this);
		time = new TimeCountUtil(80000, 1000, btn_send_code);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.btn_finish:
			String code = et_code.getText().toString().trim();
			if(StringUtils.isEmpty(code)){
				ToastUtils.showMessage("验证码不能为空", BindingEmailSecondAcitivity.this);
			}else{
				checkCode(code);
			}
			break;
		case R.id.btn_send_code:
			bindEmail(email);
			break;
		default:
			break;
		}
	}

	private void checkCode(final String code) {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
//			SingletonHttpConnetManger httpConnetManger = SingletonHttpConnetManger
//					.getInstance();
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("token", loginBean.getToken());
//			params.put("email", email);
//			params.put("code", code);
//			httpConnetManger.userLoginType(mHandler, params,
//					new JsonBean<Object>(), Constant.VERFIY_EMAIL_URL);
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("token", loginBean.getToken());
//						params.put("token", "abcd");
						params.put("email", email);
						params.put("code", code);
						LogUtil.i("tyy", email+".."+code);
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, params,
								Constant.VERFIY_EMAIL_URL);
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
			ToastUtils.showMessage("未连接网络", BindingEmailSecondAcitivity.this);
			return;
		}
	}
	private void bindEmail(final String email) {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
//			SingletonHttpConnetManger httpConnetManger = SingletonHttpConnetManger
//					.getInstance();
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("token", loginBean.getToken());
//			params.put("email", email);
//			httpConnetManger.userLoginType(mHandler, params,
//					new JsonBean<Object>(), Constant.BIND_EMAIL_URL);
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> forms = new HashMap<String, String>();
						forms.put("token", loginBean.getToken());
//						forms.put("token", "abcd");
						forms.put("email",email);
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, forms,
								Constant.BIND_EMAIL_URL);
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
			ToastUtils.showMessage("未连接网络", BindingEmailSecondAcitivity.this);
			return;
		}
	}
}
