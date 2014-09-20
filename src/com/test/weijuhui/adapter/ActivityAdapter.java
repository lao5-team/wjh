package com.test.weijuhui.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.test.weijuhui.R;
import com.test.weijuhui.activity.ActivityDetailActivity;
import com.test.weijuhui.data.ActivityData;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityAdapter extends BaseAdapter {

	private ArrayList<ActivityData> mActivities = null;
	private Activity mActivity = null;
	
	private static class SBItemViewHolder
	{
		public TextView mTvName;
		public TextView mFromUser;
		public ImageView mImgPic;
		
		public void createFromView(View view)
		{
			mTvName = (TextView)view.findViewById(R.id.textView_activity);
			mFromUser = (TextView)view.findViewById(R.id.textView_from);
			mImgPic = (ImageView)view.findViewById(R.id.imageView_pic);
		}
		
	}
	
	public ActivityAdapter(Activity activity)
	{
		mActivity = activity;
	}
	
	public void setData(ArrayList<ActivityData> activities)
	{
		mActivities = activities;
	}
	
	@Override
	public int getCount() {
		if(mActivities!=null)
		{
			return mActivities.size();
		}
		else
		{
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		if(mActivities!=null)
		{
			return mActivities.get(position);
		}
		else
		{
			return 0;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null == mActivities)
		{
			return null;
		}
		
		SBItemViewHolder holder;
		if(null == convertView)
		{
			RelativeLayout layout = (RelativeLayout)mActivity.getLayoutInflater().inflate(R.layout.listitem_activity, null);
			holder = new SBItemViewHolder();
			holder.createFromView(layout);
			convertView = layout;
			convertView.setTag(holder);
			final int pos = position;
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.putExtra("business", mActivities.get(pos).mCB);
					intent.setClass(mActivity, ActivityDetailActivity.class);
					mActivity.startActivityForResult(intent, 0);
				}
			});
		}
		else
		{
			holder = (SBItemViewHolder)convertView.getTag();
		}
		holder.mTvName.setText(mActivities.get(position).mCB.mName);
		holder.mFromUser.setText("组织者: " + mActivities.get(position).mCreator.mName);
		Picasso.with(mActivity).load(mActivities.get(position).mCB.mImgUrl).into(holder.mImgPic);
		return convertView;
	}

}
