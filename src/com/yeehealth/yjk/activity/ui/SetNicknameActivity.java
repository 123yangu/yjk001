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
 * @ClassName SetNicknameActivity
 * @Description 设置昵称
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-19,上午10:15:01
 */
public class SetNicknameActivity extends BaseActivity implements
		OnClickListener {
	/** 昵称输入框 */
	private EditText et_nickname;
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 标题 */
	private TextView tv_title;
	private Button btn_save;
	private QQLoginReslutJsonBean loginBean;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.SERVER_VALIDATION_QQLOGIN_SUCCEED:
				JsonBean<Object> jsonBean = (JsonBean<Object>) msg.obj;
				ToastUtils.showMessage(jsonBean.getMsg(),
						SetNicknameActivity.this);
				if ("200".equals(jsonBean.getStatus())) {
					Constant.isRef=true;//回账户页面要刷新
					finish();
				}
				break;
			case Constant.GET_REQUEST_FAIL:
				ToastUtils.showMessage(msg.obj.toString(),
						SetNicknameActivity.this);
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_setnickname);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		et_nickname = (EditText) findViewById(R.id.et_nickname);
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		tv_title = (TextView) findViewById(R.id.tv_title);
		btn_save = (Button) findViewById(R.id.btn_save);
	}

	@Override
	protected void initView() {
		tv_title.setText("设置昵称");
		loginBean = (QQLoginReslutJsonBean) SPUtil.readObjectExt(
				SetNicknameActivity.this, "");
	}

	@Override
	protected void initListener() {
		ib_back.setOnClickListener(this);
		btn_save.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_back:
			finish();
			break;
		case R.id.btn_save:
			String nickname = et_nickname.getText().toString().trim();
			if (StringUtils.isEmpty(nickname)) {
				ToastUtils.showMessage("昵称不能为空", SetNicknameActivity.this);
			} else {
				setProfile(nickname);
			}
			break;
		default:
			break;
		}
	}

	private void setProfile(final String nickname) {
		if (TUtils.getNetType(this) != 0) {// 判定是否有网络
//			SingletonHttpConnetManger httpConnetManger = SingletonHttpConnetManger
//					.getInstance();
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("token", loginBean.getToken());
//			params.put("nickname", nickname);
//			httpConnetManger.userLoginType(mHandler, params,
//					new JsonBean<Object>(), Constant.SET_PROFILE_URL);
			new Thread(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					try {
						HashMap<String, String> params = new HashMap<String, String>();
						params.put("token", loginBean.getToken());
						params.put("nickname", nickname);
						byte[] bt = HttpUtils.useHttpClientGetData(
								HttpUtils.POST, null, params,
								Constant.SET_PROFILE_URL);
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
			ToastUtils.showMessage("未连接网络", SetNicknameActivity.this);
			return;
		}
	}

}
