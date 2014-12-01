package com.test.juxiaohui.adapter;

import java.util.ArrayList;

import com.test.juxiaohui.R;
import com.test.juxiaohui.data.comment.ActivityComment;
import com.test.juxiaohui.data.comment.IActivityCommentLoader;

import android.app.Activity;
import android.view.View;
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
		// TODO Auto-generated method stub
		return mCommentList.size();
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
		View view = mContext.getLayoutInflater().inflate(R.layout.item_comment, null);
		TextView tvUserName = (TextView)view.findViewById(R.id.textView_username);
		tvUserName.setText(mCommentList.get(position).mUserName);
		
		TextView tvContent = (TextView)view.findViewById(R.id.textView_content);
		tvContent.setText(mCommentList.get(position).mContent);
		return view;
	}

}
