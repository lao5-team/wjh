package com.test.weijuhui.adapter;

import java.util.ArrayList;

import junit.framework.Assert;

import com.test.weijuhui.Constant;
import com.test.weijuhui.R;
import com.test.weijuhui.activity.ActivityMembersActivity;
import com.test.weijuhui.data.User;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class ActivityMemberStateAdapter extends ArrayAdapter<User>  {

	private LayoutInflater layoutInflater;
	ArrayList<User> mUsers;
	int mRes;
	public ActivityMemberStateAdapter(Context context, int resource, ArrayList<User> users)
	{
		super(context, resource, users);
		Assert.assertNotNull(users);
		mUsers = users;
		mRes = resource;
		layoutInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mUsers.size();
	}


	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = layoutInflater.inflate(mRes, null);
		}

		ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
		TextView unreadMsgView = (TextView) convertView
				.findViewById(R.id.unread_msg_number);
		TextView nameTextview = (TextView) convertView.findViewById(R.id.name);
		TextView tvHeader = (TextView) convertView.findViewById(R.id.header);
		CheckBox cbSelect = (CheckBox) convertView
				.findViewById(R.id.checkBox_selected);
		TextView tvState = (TextView)convertView.findViewById(R.id.user_state);
		tvState.setVisibility(View.VISIBLE);
		
		User user = getItem(position);
		// 设置nick，demo里不涉及到完整user，用username代替nick显示
		nameTextview.setText(user.mName);
		if(user.mActivityState == User.UNCONFIRMED)
		{
			tvState.setText("待确定");
		}
		else if(user.mActivityState == User.CONFIRMED)
		{
			tvState.setText("已确定");
		}
		if (unreadMsgView != null)
			unreadMsgView.setVisibility(View.INVISIBLE);
		avatar.setImageResource(R.drawable.default_avatar);
		final int fpos = position;
		cbSelect.setVisibility(View.INVISIBLE);
		return convertView;
	}

}
