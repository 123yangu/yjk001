package com.yeehealth.yjk.utils;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yeehealth.yjk.R;
import com.yeehealth.yjk.widget.dialog.DefineCustomProgressDialog;





public class ToastUtils {

	private static DefineCustomProgressDialog myprogressDialog = null;
	
	public static void showMessage(String toastContent,Context context){
		LayoutInflater inflater =  LayoutInflater.from(context) ;
		// 根据指定的布局文件创建一个具有层级关系的View对象
		View layout = inflater.inflate(R.layout.layout_toast, null);
		LinearLayout root = (LinearLayout) layout
				.findViewById(R.id.toast_layout_root);
		root.getBackground().setAlpha(200);// 0~255透明度值

		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(toastContent);

		Toast toast = new Toast(context);
		// 设置Toast的位置
		//		 toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		// toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		// 让Toast显示为我们自定义的样子
		toast.setView(layout);
		toast.show();
	}
	
	/**
	 * 显示loading
	 */
	public static void startProgressDialog(Activity context ,String message){
			myprogressDialog = DefineCustomProgressDialog.createDialog(context);
			myprogressDialog.setMessage(message);
		    myprogressDialog.show();
	}
	
	/**
	 * 关闭loading
	 */
	public static void stopProgressDialog(){
		if (myprogressDialog != null&&myprogressDialog.isShowing()){
			myprogressDialog.dismiss();
			myprogressDialog = null;
		}
	}
}
