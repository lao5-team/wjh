package com.test.juxiaohui.adapter;

import com.test.juxiaohui.R;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ActivityCategoryAdapter extends BaseAdapter {

	String[] mCategories = {};
	Context mContext;
	public ActivityCategoryAdapter(Context context)
	{
		mContext = context;
	}
	
	public void setData(String []categories)
	{
		mCategories = categories;
		this.notifyDataSetChanged();
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCategories.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		TextView tv = new TextView(mContext);
		//tv.setBackgroundResource(R.drawable.bg_activity_categories);
		tv.setText(mCategories[position]);
		tv.setGravity(Gravity.CENTER);
		tv.setTextSize(20);
		return tv;
	}

}
