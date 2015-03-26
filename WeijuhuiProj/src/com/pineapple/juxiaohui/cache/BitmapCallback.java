package com.pineapple.juxiaohui.cache;

import android.graphics.Bitmap;

public interface BitmapCallback extends CustomCallback{
	public void onReady(String key, Bitmap bitmap);
}
