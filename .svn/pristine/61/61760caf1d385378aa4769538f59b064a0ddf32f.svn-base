package com.yeehealth.yjk.utils;

import com.yeehealth.yjk.R;

import android.os.CountDownTimer;
import android.widget.Button;
/**
 * @ClassName TimeCountUtil
 * @Description 验证码倒计时
 * @author 谭元元
 * @Copyright 2015, 深圳壹键康科技有限公司 All Rights Reserved.
 * @date 2015-3-19,下午12:45:08
 */
public class TimeCountUtil extends CountDownTimer {
	private Button button;

	public TimeCountUtil(long millisInFuture, long countDownInterval,
			Button button) {
		super(millisInFuture, countDownInterval);
		this.button = button;
	}

	@Override
	public void onFinish() {
		button.setBackgroundResource(R.drawable.btn);
		button.setText("重新获取验证码");
		button.setClickable(true);
	}

	@Override
	public void onTick(long millisUntilFinished) {
		button.setBackgroundResource(R.drawable.btn_gray);
		button.setClickable(false);
		button.setText(millisUntilFinished / 1000 +"秒后重发");
	}

}
