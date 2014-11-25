package com.test.art.adapter;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.test.art.R;
import com.test.art.data.MyArtUser;
import com.test.art.data.PictureInfo;
import com.test.art.domain.activity.IPictureInfoLoader;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class PictureInfoAdapter extends BaseAdapter {

	ArrayList<PictureInfo> mPictureList;
	ArrayList<MyArtUser> mUserList;
	Context mContext;
	IPictureInfoLoader mLoader;
	public PictureInfoAdapter(Context context, IPictureInfoLoader loader)
	{
		mContext = context;
		mLoader = loader;
	}
	
	public void setListData(ArrayList<PictureInfo> pictureList, ArrayList<MyArtUser> userList)
	{
		if(null == pictureList || null == userList)
		{
			throw new IllegalArgumentException("setListData list can not be null!");
		}
		if(pictureList.size()!=userList.size())
		{
			throw new IllegalArgumentException("setListData lists' size not equal");
		}
		mPictureList = pictureList;
		mUserList = userList;
			
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(null != mPictureList)
		{
			return mPictureList.size();
		}
		return 0;
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
		View view = ((Activity)mContext).getLayoutInflater().inflate(R.layout.item_picture_info, null);
		TextView tvUserName = (TextView)view.findViewById(R.id.textView_username);
		tvUserName.setText(mUserList.get(position).mName);
		
		TextView tvPicTitle = (TextView)view.findViewById(R.id.textView_pic_title);
		tvPicTitle.setText(mPictureList.get(position).mTitle);
		
		ImageView imgPic = (ImageView)view.findViewById(R.id.imageView_picture);
		Picasso.with(mContext).load(mPictureList.get(position).mUrl).into(imgPic);
		return view;
	}

	public void refresh() {
		// TODO Auto-generated method stub
		mPictureList = mLoader.getPictureList();
		mUserList = mLoader.getUserList(mPictureList);
		notifyDataSetChanged();
	}

}
