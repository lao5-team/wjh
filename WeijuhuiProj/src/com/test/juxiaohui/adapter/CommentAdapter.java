package com.test.juxiaohui.adapter;

import java.util.ArrayList;

import com.test.juxiaohui.R;
import com.test.juxiaohui.data.comment.ActivityComment;
import com.test.juxiaohui.data.comment.IActivityCommentLoader;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CommentAdapter extends BaseAdapter {
	Activity mContext;
	IActivityCommentLoader mLoader;
	ArrayList<ActivityComment> mCommentList;
	public CommentAdapter(Activity context, IActivityCommentLoader loader)
	{
		if(null == context)
		{
			throw new IllegalArgumentException("Activity can not be null!");
		}
		mContext = context;
		mLoader = loader;
		mCommentList = loader.getCommentList();
	}
	
	public void refresh()
	{
		mCommentList = mLoader.getCommentList();
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		if(null!=mCommentList)
		{
			return mCommentList.size();
		}
		else
		{
			return 0;
		}
		
	}

	@Override
	public Object getItem(int position) {
		if(null!=mCommentList)
		{
			return mCommentList.get(position);
		}
		else
		{
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(null == convertView)
		{
			convertView = mContext.getLayoutInflater().inflate(R.layout.item_comment, null);
		}
		TextView tvUserName = (TextView)convertView.findViewById(R.id.textView_username);
		if(null==mCommentList.get(position).mReplyTo)
		{
			tvUserName.setText(mCommentList.get(position).mUserName+":");
		}
		else
		{
			tvUserName.setText(mCommentList.get(position).mUserName + " 回复 " + mCommentList.get(position).mReplyTo+":");
		}
		TextView tvContent = (TextView)convertView.findViewById(R.id.textView_content);
		tvContent.setText(mCommentList.get(position).mContent);
		return convertView;
	}

}
