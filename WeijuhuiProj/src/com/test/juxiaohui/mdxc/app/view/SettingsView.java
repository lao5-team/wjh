package com.test.juxiaohui.mdxc.app.view;

import com.test.juxiaohui.R;

import android.content.Context;

import android.os.Handler;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class SettingsView extends LinearLayout {
	
	private LinearLayout mContentLayout;
	private Context mContext;
	
	private LayoutInflater mInflater;
	
	public SettingsView(Context context) {
		super(context);
		mContext = context;
		initView();
		// TODO Auto-generated constructor stub
	}
	
	private void initView()
	{
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentLayout = (LinearLayout) mInflater.inflate(R.layout.view_settings, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mContentLayout.setLayoutParams(params);
		this.addView(mContentLayout);
	}

		
}
