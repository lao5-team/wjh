package com.test.weijuhui.activity;

import java.util.ArrayList;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.squareup.picasso.Picasso;
import com.test.weijuhui.R;
import com.test.weijuhui.R.id;
import com.test.weijuhui.R.layout;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.data.DianpingDataHelper;
import com.test.weijuhui.data.User;
import com.test.weijuhui.domain.ActivityManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*
 * 1 显示活动的详细内容和地点
 * 2 抽取活动内容和地点（包括经纬度）
 * 3 添加好友
 * 4 发起聚会，向好友发送邀请单
 * */
public class ActivityDetailActivity extends FragmentActivity {

	//data
	private ComplexBusiness mCBData;
	private String mBusinessID;
	private ArrayList<User> mFriends = new ArrayList<User>( );
	//UI
	private RelativeLayout mView;
	private TextView mTvName;
	private ImageView mImgContent;
	private RelativeLayout mLayoutAddress;
	private TextView mTvAddress;
	private RelativeLayout mLayoutPhone;
	private TextView mTvPhone;
	private Button mBtnAddFriends;
	private TextView mTvFriends;
	private Button mBtnConfirm;
	private Button mBtnCancel;
	
	//Handler
	private Handler mUIHandler;
	
	public void addFriend(com.test.weijuhui.domain.User user)
	{
		User user1 = new User();
		user1.mName = user.getNick();
		mFriends.add(user1);
		updateFriendsUI();
	}
	
	private void updateFriendsUI()
	{
		String result = "";
		for(int i=0; i<mFriends.size(); i++)
		{
			if(i==mFriends.size()-1)
			{
				result += mFriends.get(i).mName;
			}
			else
			{
				result += mFriends.get(i).mName + " , ";
			}
		}
		mTvFriends.setText(result);
	}
	
	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		initData();
		initUI();
		
		mUIHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				updateUI();
			}
		};
	}
	
	private void initData()
	{
		Intent intent = getIntent();
		mBusinessID = intent.getStringExtra("businessID");
		if(null != mBusinessID)
		{
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					mCBData = DianpingDataHelper.getInstance().getBusinessByID(mBusinessID);
					if(null != mCBData)
					{
						Message msg = mUIHandler.obtainMessage();
						mUIHandler.sendMessage(msg);
					}
				}
			});
			t.start();
		}
		else
		{
			finish();
		}
	}
	
	private void initUI()
	{
		RelativeLayout layout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.fragment_activity_detail, null);
		mView = layout;
		setContentView(layout);
		
		mTvName = (TextView) layout.findViewById(R.id.textView_name);
		
		mImgContent = (ImageView) layout.findViewById(R.id.imageView_img);
		
		mLayoutAddress = (RelativeLayout) layout.findViewById(R.id.relativeLayout_address);
		
		mTvAddress = (TextView) layout.findViewById(R.id.textView_address);
		
		mLayoutPhone = (RelativeLayout) layout.findViewById(R.id.relativeLayout_phone);
		
		mTvPhone = (TextView) layout.findViewById(R.id.textView_phone);
		
		mBtnAddFriends = (Button) layout.findViewById(R.id.button_addFriends);
		mBtnAddFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ftx = getSupportFragmentManager().beginTransaction();
				ContactlistFragment fragment = new ContactlistFragment();
				ftx.add(R.id.fragment_container, fragment);
				ftx.commit();
			}
		});
		
		mTvFriends = (TextView) layout.findViewById(R.id.textView_friends);
		
		mBtnConfirm = (Button) layout.findViewById(R.id.button_confirm);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityData data = new ActivityData.ActivityBuilder().setComplexBusiness(mCBData).setUsers(mFriends).create();
				if(mFriends.size() == 1)
				{
					sendActivityToSingle(data);
				}
				else if(mFriends.size() > 1)
				{
					sendActivityToGroup(data);
				}
			}
		});
		
		mBtnCancel = (Button)layout.findViewById(R.id.button_cancel);
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityDetailActivity.this.finish();
			}
		});
	}
	
	private void updateUI()
	{
		mTvName.setText(mCBData.mName + mCBData.mBranchName);
		Picasso.with(this).load(mCBData.mImgUrl).into(mImgContent);
		if(null!=mCBData.mAddress)
		{
			mTvAddress.setText(mCBData.mAddress);
		}
		else
		{
			mLayoutAddress.setVisibility(View.INVISIBLE);
		}
		if(null!=mCBData.mPhoneNumber)
		{
			mTvPhone.setText(mCBData.mPhoneNumber);
		}
		else
		{
			mLayoutPhone.setVisibility(View.INVISIBLE);
		}
		String buffer = "";
		if(mFriends.size() > 1)
		{
			for(int i=0; i<mFriends.size() - 1; i++)
			{
				buffer += mFriends.get(i) + " , ";
			}			
		}

		if(mFriends.size() > 0)
		{
			buffer += mFriends.get(mFriends.size()-1);
		}
		
		mTvFriends.setText(buffer);
		mView.invalidate();
		
	}
	
	private void sendActivityToSingle(ActivityData data)
	{
		ActivityManager.getInstance().addActivity(data);
		EMConversation conversation = EMChatManager.getInstance().getConversation(data.mUsers.get(0).mName);
		
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// 如果是群聊，设置chattype,默认是单聊
		TextMessageBody txtBody = new TextMessageBody(data.mCB.mName);
		// 设置消息body
		message.addBody(txtBody);
		// 设置要发给谁,用户username或者群聊groupid
		message.setReceipt(mFriends.get(0).mName);
		conversation.addMessage(message);
//		try {
//			//EMChatManager.getInstance().sendMessage(message);
//		} catch (EaseMobException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
	
	private void sendActivityToGroup(ActivityData data)
	{
		String[] userNames = new String[data.mUsers.size()];
		EMGroup group;
		try {
			group = EMGroupManager.getInstance().createPrivateGroup("", "", (String[]) data.mUsers.toArray(), false);
			EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());										
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			message.setChatType(ChatType.GroupChat);
		    message.setReceipt(group.getGroupId());
		    // 把messgage加到conversation中
		    conversation.addMessage(message);
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		


	}
	
	
	
}
