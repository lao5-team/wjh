package com.test.juxiaohui.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.activity.ActivityDetailActivity;
import com.test.juxiaohui.activity.CreateActivityActivity2;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.domain.ActivityManager;
import com.test.juxiaohui.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityAdapter extends BaseAdapter {

	private Fragment mFragment = null;
	
	private static class SBItemViewHolder
	{
		public TextView mTvName;
		public TextView mFromUser;
		public TextView mTvState;
		//public ImageView mImgPic;
		
		public void createFromView(View view)
		{
			mTvName = (TextView)view.findViewById(R.id.textView_activity);
			mFromUser = (TextView)view.findViewById(R.id.textView_from);
			mTvState = (TextView)view.findViewById(R.id.textView_state);
			//mImgPic = (ImageView)view.findViewById(R.id.imageView_pic);
		}
		
	}
	
	public ActivityAdapter(Fragment context)
	{
		mFragment = context;
	}
	
	
	@Override
	public int getCount() {
		return ActivityManager.getInstance().getActivitySize();
	}

	@Override
	public Object getItem(int position) {
		return ActivityManager.getInstance().getActivity(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		SBItemViewHolder holder;
		if(null == convertView)
		{
			RelativeLayout layout = (RelativeLayout)mFragment.getActivity().getLayoutInflater().inflate(R.layout.listitem_activity, null);
			holder = new SBItemViewHolder();
			holder.createFromView(layout);
			convertView = layout;
			convertView.setTag(holder);
			final int pos = position;
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					//intent.putExtra("business", mActivities.get(pos).mCB);
					intent.putExtra("activityIndex", pos);
					intent.putExtra("use", CreateActivityActivity2.INTENT_EDIT);
					intent.setClass(mFragment.getActivity(), CreateActivityActivity2.class);
					mFragment.startActivityForResult(intent, 0);
				}
			});
		}
		else
		{
			holder = (SBItemViewHolder)convertView.getTag();
		}
		ActivityData data = ActivityManager.getInstance().getActivity(position).getData();
		holder.mTvName.setText(data.mTitle);
		try
		{
			holder.mFromUser.setText("组织者: " + ActivityManager.getInstance().getActivity(position).getData().mCreator.mName);
			if(data.mState == ActivityData.BEGIN)
			{
				holder.mTvState.setText("待确定");
			}
			else if(data.mState == ActivityData.PROCESSING)
			{
				holder.mTvState.setText("进行中");
			}
			else if(data.mState == ActivityData.END)
			{
				holder.mTvState.setText("完成");
			}
			
		}
		catch(Exception e)
		{
			Log.v(DemoApplication.TAG, e.toString());
		}
		//Picasso.with(mFragment.getActivity()).load(mActivities.get(position).mCB.mImgUrl).into(holder.mImgPic);
		return convertView;
	}

}
