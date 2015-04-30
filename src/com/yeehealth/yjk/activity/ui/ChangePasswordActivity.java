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
 * @ClassName ChangePasswordActivity
 * @Description 修改密码
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-17,下午3:09:48
 */
public class ChangePasswordActivity extends BaseActivity implements
		OnClickListener {
	/** 旧密码输入框 */
	private EditText et_oldpwd;
	/** 新密码输入框 */
	private EditText et_newpwd;
	/** 确认新密码输入框 */
	private EditText et_newpwd_again;
	/** 修改完成按钮 */
	private Button btn_finish_changepwd;
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 标题 */
	private TextView tv_title;
	private String oldpwd;
	private String newpwd;
	private String newpwd_again;
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED:
				JsonBean<Object> jsonBean=(JsonBean<Object>) msg.obj;
				ToastUtils.showMessage(jsonBean.getMsg(), ChangePasswordActivity.this);
				if("200".equals(jsonBean.getStatus())){
					Constant.isRef=true;//回账户页面要刷新
					finish();
				}
				break;
			case Constant.GET_REQUEST_FAIL:
				ToastUtils.showMessage(msg.obj.toString(), ChangePasswordActivity.this);
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
		setContentView(R.layout.layout_change_password);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		et_oldpwd = (EditText) findViewById(R.id.et_oldpwd);
		et_newpwd = (EditText) findViewById(R.id.et_newpwd);
		et_newpwd_again = (EditText) findViewById(R.id.et_newpwd_again);
		btn_finish_changepwd = (Button) findViewById(R.id.btn_finish_changepwd);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
	}

	@Override
	protected void initView() {
		tv_title.setText("修改密码");
		loginBean = (QQLoginReslutJsonBean) SPUtil.readObjectExt(
				ChangePasswordActivity.this, "");
	}

	@Override
	protected void initListener() {
		btn_finish_changepwd.setOnClickListener(this);
		ib_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_finish_changepwd:
			if(checkPwd()){
				changePwd();
			}
			break;
		case R.id.ib_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void changePwd() {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
//			SingletonHttpConnetManger httpConnetManger = SingletonHttpConnetManger
//					.getInstance();
//			HashMap<String, String> params = new HashMap<String, String>();
//			 params.put("token", loginBean.getToken());
//			 params.put("oldpwd", oldpwd);
//			 params.put("newpwd", newpwd);
//			httpConnetManger.userLoginType(mHandler, params,
//					new JsonBean<Object>(),Constant.CHANGE_PWD_URL);
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> params = new HashMap<String, String>();
//						 params.put("token", loginBean.getToken());
						 params.put("token", "abcd");
						 params.put("oldpwd", oldpwd);
						 params.put("newpwd", newpwd);
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, params,
								Constant.CHANGE_PWD_URL);
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
			ToastUtils.showMessage("未连接网络", ChangePasswordActivity.this);
			return;
		}
	}

	private boolean checkPwd() {
		oldpwd = et_oldpwd.getText().toString().trim();
		newpwd = et_newpwd.getText().toString().trim();
		newpwd_again = et_newpwd_again.getText().toString().trim();
		if(StringUtils.isEmpty(oldpwd)||StringUtils.isEmpty(newpwd)||StringUtils.isEmpty(newpwd_again)){
			ToastUtils.showMessage("密码不能为空", ChangePasswordActivity.this);
			return false;
		}else if(!newpwd.equals(newpwd_again)){
			ToastUtils.showMessage("两次输入的新密码不一致", ChangePasswordActivity.this);
			return false;
		}else if(oldpwd.equals(newpwd_again)){
			ToastUtils.showMessage("新密码和旧密码不能相同", ChangePasswordActivity.this);
			return false;
		}else{
			return true;
		}
	}

}
