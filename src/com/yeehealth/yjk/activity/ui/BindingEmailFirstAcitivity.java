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
 * @ClassName BindingEmailFirstAcitivity
 * @Description 绑定邮箱的第一步
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-19,上午10:11:49
 */
public class BindingEmailFirstAcitivity extends BaseActivity implements
		OnClickListener {
	/** 邮箱输入框 */
	private EditText et_email;
	/** 下一步按钮 */
	private Button btn_next;
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 标题 */
	private TextView tv_title;
	/** 邮箱 */
	private String email;
	private QQLoginReslutJsonBean loginBean;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED:
				JsonBean<Object> jsonBean = (JsonBean<Object>) msg.obj;
				ToastUtils.showMessage(jsonBean.getMsg(),
						BindingEmailFirstAcitivity.this);
				if ("200".equals(jsonBean.getStatus())) {
					Intent intent = new Intent(BindingEmailFirstAcitivity.this,
							BindingEmailSecondAcitivity.class);
					intent.putExtra("email", email);
					startActivity(intent);
					finish();
				}
				break;
			case Constant.GET_REQUEST_FAIL:
				ToastUtils.showMessage(msg.obj.toString(),
						BindingEmailFirstAcitivity.this);
				break;

			default:
				break;
			}
		};
	};
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_bindingemail_first);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		et_email = (EditText) findViewById(R.id.et_email);
		btn_next = (Button) findViewById(R.id.btn_next);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	protected void initView() {
		tv_title.setText("绑定邮箱");
		loginBean = (QQLoginReslutJsonBean) SPUtil.readObjectExt(
				BindingEmailFirstAcitivity.this, "user");
	}

	@Override
	protected void initListener() {
		btn_next.setOnClickListener(this);
		ib_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			if (checkEmail()) {
				bindEmail(email);
			}
			break;
		case R.id.ib_back:
			finish();
			break;
		default:
			break;
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
			ToastUtils.showMessage("未连接网络", BindingEmailFirstAcitivity.this);
			return;
		}
	}

	private boolean checkEmail() {
		email = et_email.getText().toString().trim();
		if (StringUtils.isEmpty(email)) {
			ToastUtils.showMessage("邮件不能为空", BindingEmailFirstAcitivity.this);
			return false;
		} else if (StringUtils.isEmail(email)) {
			return true;
		} else {
			ToastUtils.showMessage("您输入的邮件格式不合法", this);
			return false;
		}
	}

}