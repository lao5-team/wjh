package com.test.art.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.test.art.DemoApplication;
import com.test.art.R;
import com.test.art.activity.ActivityDetailActivity;
import com.test.art.activity.CreateActivityActivity2;
import com.test.art.data.ActivityData;
import com.test.art.domain.activity.ActivityManager;
import com.test.art.domain.activity.IActivityLoader;

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
	private IActivityLoader mLoader = null;
	private ArrayList<ActivityData> mActivityList;
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
	
	public ActivityAdapter(Fragment context, IActivityLoader loader)
	{
		mFragment = context;
		mLoader = loader;
		if(null != mLoader)
		{
			mActivityList = mLoader.getActivityList();
		}
		
	}
	
	public void refresh()
	{
		if(null != mLoader)
		{
			mActivityList = mLoader.getActivityList();
		}
		this.notifyDataSetChanged();
	}
	
	
	@Override
	public int getCount() {
		//return ActivityManager.getInstance().getActivitySize();
		if(null == mActivityList)
		{
			return 0;
		}
		else
		{
			return mActivityList.size();
		}
		
	}

	@Override
	public Object getItem(int position) {
		//return ActivityManager.getInstance().getActivity(position);
		if(null == mActivityList)
		{
			return null;
		}
		else
		{
			return mActivityList.get(position);
		}
		
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
					ActivityDetailActivity.IntentBuilder ib = new ActivityDetailActivity.IntentBuilder(intent);
					ib.setUseType(ActivityDetailActivity.USE_EDIT);
					ib.setActivityID(mActivityList.get(pos).mID);
					intent.setClass(mFragment.getActivity(), ActivityDetailActivity.class);
					mFragment.startActivityForResult(intent, 0);
				}
			});
		}
		else
		{
			holder = (SBItemViewHolder)convertView.getTag();
		}
		ActivityData data = (ActivityData)getItem(position);
		holder.mTvName.setText(data.mTitle);
		try
		{
			holder.mFromUser.setText("组织者: " + data.mCreator.mName);
//			if(data.mState == ActivityData.BEGIN)
//			{
//				holder.mTvState.setText("待确定");
//			}
//			else if(data.mState == ActivityData.PROCESSING)
//			{
//				holder.mTvState.setText("进行中");
//			}
//			else if(data.mState == ActivityData.END)
//			{
//				holder.mTvState.setText("完成");
//			}
			
		}
		catch(Exception e)
		{
			Log.v(DemoApplication.TAG, e.toString());
		}
		//Picasso.with(mFragment.getActivity()).load(mActivities.get(position).mCB.mImgUrl).into(holder.mImgPic);
		return convertView;
	}

}
