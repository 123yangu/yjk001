package com.yeehealth.yjk.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

/**
 * 获取资源<br>
 * @ 注：必须初始化activiy<br>
 * 
 * @author ccy
 * 
 */
public class Res {
	private Activity activity = null;

	public final String res_id = "id";
	public final String res_string = "string";
	public final String res_array = "array";
	public final String res_drawable = "drawable";
	public final String res_color = "color";
	public final String res_style = "style";
	public final String res_layout = "layout";
	public final String res_menu = "menu";
	public final String res_anim = "anim";
	public final String res_attr = "attr";
	public final String res_xml = "xml";
	public final String res_dimen = "dimen";
	public final String res_styleable = "styleable";
	public final String res_raw = "raw";

	private final static Res instance = new Res();

	private Res() {
	}

	public static Res getInstance() {
		return instance;
	}

	public void init(Activity activity) {
		this.activity = activity;
	}

	public Activity getActivity() {
		return activity;
	}

	public int getStyle(String style) {
		return getResIdentifier(style, res_style);

	}

	public int getID(String resID) {
		return getResIdentifier(resID, res_id);
	}

	public int getInt(String id, String typeName) {
		return getRes().getInteger(getResIdentifier(id, typeName));
	}

	public String getString(String stringID) {
		int id_res = getResIdentifier(stringID, res_string);
		return getRes().getString(id_res);
	}

	public int getStringID(String stringID) {
		return getResIdentifier(stringID, res_string);
	}

	// 鼓励使用此方法。
	public String getString(int xmlStringId) {
		return getRes().getString(xmlStringId);
	}

	public int[] getIntArray(String arrayID) {
		int id_res = getResIdentifier(arrayID, res_array);
		return getRes().getIntArray(id_res);
	}

	// 鼓励使用此方法。
	public int[] getIntArray(int xmlIntArrayId) {
		return getRes().getIntArray(xmlIntArrayId);
	}

	public String[] getStringArray(String arrayID) {
		int id_res = getResIdentifier(arrayID, res_array);
		return getRes().getStringArray(id_res);
	}

	// 鼓励使用此方法。
	public String[] getStringArray(int xmlStrArrayId) {
		return getRes().getStringArray(xmlStrArrayId);
	}

	public int getDrawableID(String drawableID) {
		return getResIdentifier(drawableID, res_drawable);
	}

	public Drawable getDrawable(String drawableID) {
		int id_res = getDrawableID(drawableID);
		return getRes().getDrawable(id_res);
	}

	// 鼓励使用此方法。
	public Drawable getDrawable(int xmlDrawableId) {
		return getRes().getDrawable(xmlDrawableId);
	}

	public int getAnimID(String animID) {
		return getResIdentifier(animID, res_anim);
	}

	public AnimationDrawable getAnimDrawable(String animID) {
		int id_res = getAnimID(animID);
		return (AnimationDrawable) getRes().getDrawable(id_res);
	}

	public int getLayoutID(String layoutID) {
		return getResIdentifier(layoutID, res_layout);
	}

	public int getDimen(String dimenID) {
		int id_res = getResIdentifier(dimenID, res_dimen);
		return getRes().getInteger(id_res);
	}

	// 鼓励使用此方法。
	public int getDimen(int xmlDimenId) {
		return (int) getRes().getDimension(xmlDimenId);
	}

	public int getColor(String colorID) {
		int id_res = getResIdentifier(colorID, res_color);
		return getRes().getColor(id_res);
	}

	// 鼓励使用此方法。
	public int getColor(int xmlColorId) {
		return getRes().getColor(xmlColorId);
	}

	public int getRaw(String rawID) {
		return getResIdentifier(rawID, res_raw);
	}

	public int getXml(String xmlID) {
		return getResIdentifier(xmlID, res_xml);
	}

	public int getAttr(String attrID) {
		return getResIdentifier(attrID, res_attr);
	}

	public int getStyleable(String styleableID) {
		return getResIdentifier(styleableID, res_styleable);
	}

	public Resources getRes() {
		return this.activity.getApplication().getResources();
	}

	public int getResIdentifier(String idName, String typeName) {
		String packageName = this.activity.getApplication().getPackageName();
		String name = String.format("%s:%s/%s", packageName, typeName, idName);
		int identifier = getRes().getIdentifier(name, typeName, null);
		if (identifier == 0) {
			name = String.format("%s:%s/%s", "android", typeName, idName);
			identifier = getRes().getIdentifier(name, typeName, "android");
		}
		return identifier;
	}

}