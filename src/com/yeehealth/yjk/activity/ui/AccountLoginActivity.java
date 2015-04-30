package com.yeehealth.yjk.activity.ui;

import java.io.File;
import java.util.HashMap;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yeehealth.yjk.MainActivity;
import com.yeehealth.yjk.R;
import com.yeehealth.yjk.activity.base.BaseActivity;
import com.yeehealth.yjk.http.utils.SingletonHttpConnetManger;
import com.yeehealth.yjk.json.bean.JsonBean;
import com.yeehealth.yjk.json.bean.LocalLoginUser;
import com.yeehealth.yjk.json.bean.QQLoginReslutJsonBean;
import com.yeehealth.yjk.utils.Constant;
import com.yeehealth.yjk.utils.IconUtil;
import com.yeehealth.yjk.utils.LogUtil;
import com.yeehealth.yjk.utils.SPUtil;
import com.yeehealth.yjk.utils.StringUtils;
import com.yeehealth.yjk.widget.cache.volley.ImageCacheManager;
import com.yeehealth.yjk.widget.circleimg.CircleImageView;
import com.yeehealth.yjk.widget.dialog.ActionSheetDialog;
import com.yeehealth.yjk.widget.dialog.ActionSheetDialog.OnSheetItemClickListener;
import com.yeehealth.yjk.widget.dialog.ActionSheetDialog.SheetItemColor;

/**
 * @ClassName AccountLoginActivity
 * @Description 账户中心
 * @author 陈昌燕
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-20,下午2:10:44
 */
public class AccountLoginActivity extends BaseActivity implements
		OnClickListener {
	/** 返回按钮 */
	private ImageButton ib_back;
	/** 用户头像 */
	private CircleImageView account_image_header;
	/** 昵称 */
	private TextView tv_nickname;
	/** 用户名 */
	private TextView tv_account_name;
	/**账号*/
	private TextView tv_account;
	/** 用户邮箱 */
	private TextView tv_account_email;

	/** 退出按钮 */
	private Button bt_logout;

	private Bitmap bitmap;
	/** 账户顶部个人信息 */
	private RelativeLayout rl_account_top;
	/** 修改密码 */
	private RelativeLayout rl_change_pwd;
	/** 用户名 */
	private RelativeLayout rl_change_username;
	/** 绑定邮箱 */
	private RelativeLayout rl_bind_email;
	private ImageView account_arrow_user;
	private ImageView account_arrow_email;
	private QQLoginReslutJsonBean loginReslutJsonBean;

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Constant.GET_USER_INFO_SUCCESS:
				JsonBean<QQLoginReslutJsonBean> jsonBean = (JsonBean<QQLoginReslutJsonBean>) msg.obj;
				LogUtil.e("ccy", jsonBean.toString());
				Gson gson = new Gson();
				String s_reult = gson.toJson(jsonBean.getData());
				loginReslutJsonBean = gson.fromJson(s_reult,
						QQLoginReslutJsonBean.class);
				// 保存登陆用户
				LocalLoginUser localLoginUser = new LocalLoginUser();
				localLoginUser.setUserName(loginReslutJsonBean.getMobile());
				localLoginUser.setUrl(loginReslutJsonBean.getAvatar());
				SPUtil.saveObjectExt(getApplicationContext(), "account",
						localLoginUser);
				SPUtil.saveObjectExt(getApplicationContext(), "user",
						loginReslutJsonBean);
				Constant.isRef=false;
				initUserInfo();
				break;

			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_login_succeed);
		findViewById();
		initView();
		initListener();
	}

	@Override
	protected void findViewById() {
		ib_back = (ImageButton) findViewById(R.id.ib_back);
		account_image_header = (CircleImageView) findViewById(R.id.account_image_header);
		tv_nickname = (TextView) findViewById(R.id.tv_nickname);
		tv_account = (TextView) findViewById(R.id.tv_account);
		rl_account_top = (RelativeLayout) findViewById(R.id.rl_account_top);
		rl_change_pwd = (RelativeLayout) findViewById(R.id.rl_change_pwd);
		rl_change_username = (RelativeLayout) findViewById(R.id.rl_change_username);
		rl_bind_email = (RelativeLayout) findViewById(R.id.rl_bind_email);
		tv_account_name = (TextView) findViewById(R.id.tv_account_name);
		tv_account_email = (TextView) findViewById(R.id.tv_account_email);
		bt_logout = (Button) findViewById(R.id.bt_logout);
		account_arrow_user = (ImageView) findViewById(R.id.account_arrow_user);
		account_arrow_email = (ImageView) findViewById(R.id.account_arrow_email);
	}

	@Override
	protected void initView() {
		ib_back.setVisibility(View.GONE);
		initUserInfo();
	}

	private void initUserInfo() {
		loginReslutJsonBean = (QQLoginReslutJsonBean) SPUtil.readObjectExt(
				getApplicationContext(), "user");
		if (loginReslutJsonBean != null) {
			if (!StringUtils.isEmpty(loginReslutJsonBean.getAvatar())) {
				LocalLoginUser localLoginUser = (LocalLoginUser) SPUtil
						.readObjectExt(getApplicationContext(), "account");
				if (!StringUtils.isEmpty(localLoginUser.getUrl())) {
					LogUtil.e("ccy", localLoginUser.getUrl());
					account_image_header.setImageUrl(localLoginUser.getUrl().trim(),
							ImageCacheManager.getInstance().getImageLoader());
				}
			} else {
				account_image_header.setImageUrl(loginReslutJsonBean
						.getAvatar(), ImageCacheManager.getInstance()
						.getImageLoader());
			}
			account_image_header.setBorderColor(res
					.getColor(R.color.color_header_border));
			account_image_header.setBorderWidth(2);
			if (!StringUtils.isEmpty(loginReslutJsonBean.getNickname())) {
				tv_nickname.setText(loginReslutJsonBean.getNickname());
			}
			
			if(!StringUtils.isEmpty(loginReslutJsonBean.getMobile())){
				tv_account.setText(res.getString(R.string.str_account)+":"+loginReslutJsonBean.getMobile());
			}else{
				tv_account.setText(res.getString(R.string.str_account)+":未设置");
			}
			
			if (!StringUtils.isEmpty(loginReslutJsonBean.getUsername())) {
				tv_account_name.setText(loginReslutJsonBean.getUsername());
				account_arrow_user.setVisibility(View.GONE);
				rl_change_username.setEnabled(false);

			}
			if (!StringUtils.isEmpty(loginReslutJsonBean.getEmail())) {
				tv_account_email.setText(loginReslutJsonBean.getEmail());
				account_arrow_email.setVisibility(View.GONE);
				rl_bind_email.setEnabled(false);
			}
		}
	}

	@Override
	protected void initListener() {
		account_image_header.setOnClickListener(this);
		rl_account_top.setOnClickListener(this);
		rl_change_pwd.setOnClickListener(this);
		rl_change_username.setOnClickListener(this);
		rl_bind_email.setOnClickListener(this);
		bt_logout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_account_top:
			Intent setnicknameIntent = new Intent(this,
					SetNicknameActivity.class);
			startActivity(setnicknameIntent);
			break;
		case R.id.rl_change_pwd:
			Intent changepwdIntent = new Intent(this,
					ChangePasswordActivity.class);
			startActivity(changepwdIntent);
			break;
		case R.id.rl_change_username:
			Intent changeusernameIntent = new Intent(this,
					ChangeUserNameActivity.class);
			startActivity(changeusernameIntent);
			break;
		case R.id.account_image_header:
			new ActionSheetDialog(AccountLoginActivity.this)
					.builder()
					.setCancelable(false)
					.setCanceledOnTouchOutside(false)
					.addSheetItem("拍照", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									try {
										openCamera();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							})
					.addSheetItem("从相册选择", SheetItemColor.Blue,
							new OnSheetItemClickListener() {
								@Override
								public void onClick(int which) {
									openPhotos();
								}
							}).show();
			break;
		case R.id.rl_bind_email:
			Intent bindemailIntent = new Intent(this,
					BindingEmailFirstAcitivity.class);
			startActivity(bindemailIntent);

			break;
		case R.id.bt_logout:
			Intent logoutIntent = new Intent(this, MainActivity.class);
			// 清空登陆数据
			SPUtil.saveObjectExt(getApplicationContext(), "user", null);
			startActivity(logoutIntent);
			finish();
			break;

		default:
			break;
		}
	}

	/**
	 * 打开照相机拍照
	 */
	protected void openCamera() {
		try {
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			// 下面这句指定调用相机拍照后的照片存储的路径
			intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
					IconUtil.getStoragePath(AccountLoginActivity.this),
					Constant.USER_ICON_NAME)));
			startActivityForResult(intent, 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 打开相册选择照片
	 */
	protected void openPhotos() {
		try {
			Intent intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			startActivityForResult(intent, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Title: deleteTempIconPath
	 * @Description: 删除头像临时文件
	 * @author lyl
	 * @date2015-3-19,上午9:27:20
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @return void 返回类型
	 * @throws
	 */
	private void deleteTempIconPath() {
		File userIconPathTemp = new File(
				IconUtil.getStoragePath(AccountLoginActivity.this)
						+ Constant.USER_ICON_NAME);
		if (userIconPathTemp != null && userIconPathTemp.exists()) {
			userIconPathTemp.delete();
			LogUtil.i("lyl", "删除成功");
		} else {
			LogUtil.i("lyl", "文件不存在");
		}
	}

	/**
	 * @Title: setImageView
	 * @Description: 裁剪完成后对图片做处理后再设置图片到CircleImageView
	 * @author lyl
	 * @date2015-3-19,上午10:01:20
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @param @param bundle 设定文件
	 * @return void 返回类型
	 * @throws
	 */
	private void setImageView(Bundle bundle) {
		if (bundle != null) {
			bitmap = bundle.getParcelable("data");
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						BitmapFactory.Options newOpts = new BitmapFactory.Options();
						newOpts.inJustDecodeBounds = true;// 只读边,不读内容
						newOpts.inJustDecodeBounds = false;
						float hh = 300f;//
						float ww = 150f;//
						int be = 1;
						if (((int) (newOpts.outWidth / ww)) > ((int) (newOpts.outHeight / hh))) {
							be = (int) (newOpts.outWidth / ww);
						} else {
							be = (int) (newOpts.outHeight / hh);
						}
						if (be <= 0)
							be = 1;
						newOpts.inSampleSize = be;// 设置采样率
						newOpts.inPreferredConfig = android.graphics.Bitmap.Config.ARGB_8888;// 该模式是默认的,可不设
						newOpts.inPurgeable = true;// 同时设置才会有效
						newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收
						// BitmapFactory.decodeStream(is,
						// null, newOpts);
						Message message = new Message();
						handler.sendMessage(message);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				Handler handler = new Handler() {
					@Override
					public void handleMessage(Message msg) {
						super.handleMessage(msg);
						if (bitmap != null) {
							account_image_header.setImageBitmap(bitmap);
						}
					}
				};
			}).start();
			deleteTempIconPath();
		}
	}

	/**
	 * @Title: startPhotoZoom
	 * @Description: 裁剪图片方法
	 * @author lyl
	 * @date2015-3-19,上午9:27:20
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @return void 返回类型
	 * @throws
	 */
	public void startPhotoZoom(Uri uri) {
		try {
			Intent intent = new Intent("com.android.camera.action.CROP");
			intent.setDataAndType(uri, "image/*");
			// 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
			intent.putExtra("crop", "true");
			// aspectX aspectY 是宽高的比例
			intent.putExtra("aspectX", 2);
			intent.putExtra("aspectY", 1);
			// outputX outputY 是裁剪图片宽高
			intent.putExtra("outputX", 300);
			intent.putExtra("outputY", 150);
			intent.putExtra("scale", true);
			intent.putExtra("noFaceDetection", false);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		switch (requestCode) {
		case 1:
			LogUtil.i("lyl", "图片来自相机拍照");
			File temp = new File(
					IconUtil.getStoragePath(AccountLoginActivity.this)
							+ Constant.USER_ICON_NAME);
			if (temp != null && temp.exists()) {
				startPhotoZoom(Uri.fromFile(temp));
			}
			break;
		case 2:
			LogUtil.i("lyl", "图片来自相册选择");
			if (data != null) {
				startPhotoZoom(data.getData());
			}
			break;
		case 3:
			LogUtil.i("lyl", "取得裁剪后的图片");
			if (data != null) {
				Bundle extras = data.getExtras();
				setImageView(extras);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		finish();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(Constant.isRef){
			getUserInfo();
			
		}
	}

	private void getUserInfo() {
		
		LocalLoginUser loginUser = (LocalLoginUser) SPUtil.readObjectExt(
				getApplicationContext(), "account");
		if (loginUser != null) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", loginUser.getAccess_token());
			JsonBean<QQLoginReslutJsonBean> bean = null;
			String url = Constant.GET_USER_INFO;
			SingletonHttpConnetManger.getInstance().userLoginType(mHandler,
					params, bean, url);
		}
	}

}