package com.test.weijuhui.activity;

import java.util.ArrayList;

import com.test.weijuhui.R;
import com.test.weijuhui.data.User;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityMembersActivity extends FragmentActivity {

	private ArrayList<User> mFriends = new ArrayList<User>( );
	private Button mBtnOK;
	private Button mBtnCancel;
	private TextView mTvMembers;
	
	@Override
	public void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		this.setContentView(R.layout.activity_members_activity);
		mTvMembers = (TextView)this.findViewById(R.id.textView_members);
		mBtnOK = (Button) this.findViewById(R.id.button_ok);
		mBtnCancel = (Button) this.findViewById(R.id.button_cancel);
		FragmentTransaction ftx = getSupportFragmentManager().beginTransaction();
		ContactlistFragment fragment = new ContactlistFragment(ContactlistFragment.ACTIVITY);
		ftx.add(R.id.fragment_container, fragment);
		ftx.commit();
		
		mBtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();
				bundle.putSerializable("members", mFriends);
				intent.putExtra("members", bundle);
				setResult(0, intent);
				finish();
			}
		});
		
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}
	
	public void addFriend(com.test.weijuhui.domain.User user)
	{
		for(int i=0; i<mFriends.size(); i++)
		{
			if(mFriends.get(i).mName.equals(user.getNick()))
			{
				return;
			}
		}
		User user1 = new User();
		user1.mName = user.getNick();
		mFriends.add(user1);
		String buffer = "";
		for(int i=0; i<mFriends.size(); i++)
		{
			if(i==mFriends.size() - 1)
			{
				buffer += mFriends.get(i).mName;
			}
			else
			{
				buffer += mFriends.get(i).mName + " , ";
			}
		}
		mTvMembers.setText(getString(R.string.select_activity_members) + " : " + buffer);
		
	}
	
	
}
