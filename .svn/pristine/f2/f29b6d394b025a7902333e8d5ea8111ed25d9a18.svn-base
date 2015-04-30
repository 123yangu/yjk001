package com.yeehealth.yjk.utils;

import java.io.File;
import java.lang.reflect.Method;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.storage.StorageManager;

/**   
 * @Title: IconUtil.java 
 * @Package com.yeehealth.yjk.utils 
 * @Description: TODO(用一句话描述该文件做什么) 
 * @author lyl   
 * @date 2015-3-19 上午9:34:20 
 * @Copyright: 2015 深圳市壹键康科技有限公司. All rights reserved.
 * @version V1.0   
 */
@SuppressLint({ "SimpleDateFormat", "NewApi" })
public class IconUtil {
	/**
	 * 获取存储路径
	 * 
	 * @return
	 */
	public static String getStoragePath(Context context) {
		if (Environment.MEDIA_MOUNTED.equals(Environment
				.getExternalStorageState())) {
			return Environment.getExternalStorageDirectory().getAbsolutePath();
		}
		StorageManager storageManager = (StorageManager) context.getApplicationContext()
				.getSystemService(Context.STORAGE_SERVICE);
		try {
			Class<?>[] paramClasses = {};
			Method getVolumePathsMethod = StorageManager.class.getMethod(
					"getVolumePaths", paramClasses);
			getVolumePathsMethod.setAccessible(true);
			Object[] params = {};
			Object invoke = getVolumePathsMethod.invoke(storageManager, params);
			for (int i = 0; i < ((String[]) invoke).length; i++) {
				File file = new File(((String[]) invoke)[i]);
				if (file != null && file.listFiles() != null) {
					return file.getAbsolutePath();
				}
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

}
