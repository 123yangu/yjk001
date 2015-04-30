package com.yeehealth.yjk.activity.manager;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;

import com.yeehealth.yjk.MainActivity;
import com.yeehealth.yjk.R;
import com.yeehealth.yjk.utils.LogUtil;
import com.yeehealth.yjk.utils.Res;
import com.yeehealth.yjk.utils.SPUtil;
import com.yeehealth.yjk.utils.TUtils;
import com.yeehealth.yjk.widget.cache.volley.ImageCacheManager;
import com.yeehealth.yjk.widget.cache.volley.ImageCacheManager.CacheType;
import com.yeehealth.yjk.widget.cache.volley.RequestManager;


/**
 * 
* @Title: AppStart.java 
* @Package com.onehealth.activity.manager 
* @Description: 启动界面
* @author ccy  
* @date 2014-11-6 上午8:53:57 
* @version V1.0
 */
public class AppStart extends Activity {

    /** 是否退出系统  */
    private boolean isExitSystem;
    /**欢迎显示图片显示时长*/
    private final static int DISPLAY_TIME = 500; 
    /** 主界面*/
    private View rootView;
    
    private static int DISK_IMAGECACHE_SIZE = 1024*1024*10;
   	private static CompressFormat DISK_IMAGECACHE_COMPRESS_FORMAT = CompressFormat.PNG;
   	private static int DISK_IMAGECACHE_QUALITY = 100;  //PNG is lossless so quality is ignored but must be provided
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		rootView = View.inflate(this,R.layout.layout_appstart,null);
		setContentView(rootView);
		LogUtil.e("json", "-------------AppStart onCreate()---------------");
		Res.getInstance().init(this);
		into();
		
		init();
	}
	
	/**
	 * Intialize the request manager and the image cache 
	 */
	private void init() {
		RequestManager.init(this);
		createImageCache();
	}
	
	/**
	 * Create the image cache. Uses Memory Cache by default. Change to Disk for a Disk based LRU implementation.  
	 */
	private void createImageCache(){
		ImageCacheManager.getInstance().init(this,
				this.getPackageCodePath()
				, DISK_IMAGECACHE_SIZE
				, DISK_IMAGECACHE_COMPRESS_FORMAT
				, DISK_IMAGECACHE_QUALITY
				, CacheType.MEMORY);
	}
	
	
	/**
	 * @Title: into 
	 * @Description: 进入到主程序
	 * @author 	陈昌燕
	 * @date2015-2-15,下午1:40:15
	 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
	 * @throws
	 */
	public void into(){
		final int old_versionCode = SPUtil.getInt(this, "pre_appVersionCode", 0);
		Animation startAnimation = AnimationUtils.loadAnimation(this,R.anim.appstart_alpha);
		// 给view设置动画效果
		rootView.startAnimation(startAnimation);
		startAnimation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {}
			@Override
			public void onAnimationRepeat(Animation animation) {}
			@Override
			public void onAnimationEnd(Animation animation) {
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						Intent intent;
						//如果版本编号不同，则进入引导页SplashActivity
						if (TUtils.differentVersionCode(TUtils.getAppVersionCode(AppStart.this),old_versionCode)) {
							//这里是进入到引导页，到时候替换成引导页
							intent = new Intent(AppStart.this,MainActivity.class);
						} else {
							intent = new Intent(AppStart.this,MainActivity.class);
						}
						startActivity(intent);
						finish();
					}
				}, DISPLAY_TIME);
			}
		});
	
	}
	
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
			this.isExitSystem = true;
			this.finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	

}
