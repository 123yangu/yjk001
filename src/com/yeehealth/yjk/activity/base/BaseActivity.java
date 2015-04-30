package com.yeehealth.yjk.activity.base;



import com.yeehealth.yjk.utils.Res;

import android.app.Activity;
import android.os.Bundle;

/**
 * 
* @Title: BaseActivity.java 
* @Package com.onehealth.activity.ui.base 
* @Description: activity必须继承该类 
* @author ccy  
* @date 2014-11-5 下午1:07:23 
* @version V1.0
 */
public abstract class BaseActivity extends Activity{
	protected Res res = Res.getInstance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	protected abstract void findViewById();
	
	protected abstract void initView();
	
	protected abstract void initListener();
}
