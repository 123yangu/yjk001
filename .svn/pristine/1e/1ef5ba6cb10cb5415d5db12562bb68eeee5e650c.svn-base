package com.yeehealth.yjk.widget.cache.volley;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.yeehealth.yjk.utils.LogUtil;

/**
 * Basic LRU Memory cache.
 * 
 * @author ccy
 *
 */
public class BitmapLruImageCache extends LruCache<String, Bitmap> implements ImageCache{
	
	private final String TAG = this.getClass().getSimpleName();
	
	public BitmapLruImageCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		LogUtil.e(TAG, "Retrieved item from Mem Cache");
		return get(url);
	}
 
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		LogUtil.e(TAG, "Added item to Mem Cache");
		put(url, bitmap);
	}
}
