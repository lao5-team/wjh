package com.test.art.activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.squareup.picasso.Picasso;
import com.test.art.DemoApplication;
import com.test.art.R;
import com.test.art.R.id;
import com.test.art.R.layout;
import com.test.art.activity.CreateActivityActivity2.IntentBuilder;
import com.test.art.data.ActivityData;
import com.test.art.data.DianpingDataHelper;
import com.test.art.data.MyUser;
import com.test.art.data.DianpingDao.ComplexBusiness;
import com.test.art.domain.MyServerManager;
import com.test.art.domain.activity.ActivityManager;
import com.test.art.mediator.IActivityDetailMediator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
/**
 * @author yh
 * 1 显示活动的详细内容和地点
 * 2 抽取活动内容和地点（包括经纬度）
 * 3 添加好友
 * 4 发起聚会，向好友发送邀请单
 */
public class ActivityDetailActivity extends FragmentActivity {

	public static class IntentBuilder
	{
		Intent mIntent;
		/** 决定是用来创建一个活动还是用来处理一个活动
		 *  
		 * @param useType 可以是USE_CREATE,USE_EDIT
		 */
		public IntentBuilder(Intent intent)
		{
			if(null == intent)
				throw new IllegalArgumentException("IntentBuilder intent cannot be null!");
			mIntent = intent;
		}
		
		public void setUseType(int useType)
		{
			mIntent.putExtra("use", useType);
		}
		
		public void setActivityID(String activityID)
		{
			mIntent.putExtra("activity_id", activityID);
		}
		
		public int getUseType()
		{
			return mIntent.getIntExtra("use", USE_CREATE);
		}
		
		public String getActivityID()
		{
			return mIntent.getStringExtra("activity_id");
		}
	};
	
	public static final int USE_CREATE = 4;
	public static final int USE_EDIT = 5;
	
	private IActivityDetailMediator mMediator = new IActivityDetailMediator() {
		
		@Override
		public void showTitle(String title) {
			mTvName.setText(mData.mTitle);
		}
		
		@Override
		public void showTime(Date date) {
			mTvTime.setText( new SimpleDateFormat(ActivityData.dataPattern).format(date));
		}
		
		@Override
		public void showMembers(ArrayList<MyUser> users) {
			String result = "聚会参与人员: ";
			for(int i=0; i<users.size(); i++)
			{
				if(i==users.size()-1)
				{
					result += users.get(i).mName;
				}
				else
				{
					result += users.get(i).mName + " , ";
				}
			}
			mTvFriends.setText(result);
		}
		
		@Override
		public void showContent(String content) {
			mTvContent.setText(mData.mContent);
		}
		
		@Override
		public void setActivityData(ActivityData data) {
			mData = data;
			showTitle(data.mTitle);
			showContent(data.mContent);
			showTime(data.mBeginDate);
			showMembers(data.mUsers);
			showPayType(data.mSpentType);
			if(DemoApplication.getInstance().getUser().getMyRoleType(mData).endsWith(MyUser.CREATOR))
			{
				mBtnConfirm.setText("完成");
				mBtnCancel.setText("解散");
			}
			else if(DemoApplication.getInstance().getUser().getMyRoleType(mData).endsWith(MyUser.JOINER))
			{
				mBtnConfirm.setVisibility(View.GONE);
				mBtnCancel.setText("退出");				
			}
		}
		
		@Override
		public void changeMembers(ArrayList<MyUser> users) {
			mData.mUsers = users;
			showMembers(users);
		}

		@Override
		public void showPayType(int type) {
			if(mData.mSpentType == 0)
			{
				mCBPayMe.setChecked(true);
			}
			if(mData.mSpentType == 1)
			{
				mCBPayAA.setChecked(true);
			}
			if(mData.mSpentType == 2)
			{
				mCBPayOther.setChecked(true);
			}
		}

		@Override
		public void onOKClicked() {
			if(DemoApplication.getInstance().getUser().getMyRoleType(mData).endsWith(MyUser.CREATOR))
			{
				DemoApplication.getInstance().getUser().finishActivity(mData);
			}
		}

		@Override
		public void onCancelClicked() {
			if(DemoApplication.getInstance().getUser().getMyRoleType(mData).endsWith(MyUser.CREATOR))
			{
				DemoApplication.getInstance().getUser().dismissActivity(mData);
			}
			else if(DemoApplication.getInstance().getUser().getMyRoleType(mData).endsWith(MyUser.JOINER))
			{
				DemoApplication.getInstance().getUser().quitActivity(mData);
			}
		}


	};
	//data
	private ComplexBusiness mCBData;
	private int mActivityIndex;
	
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
	private TextView mTvTime;
	private Button mBtnConfirm;
	private Button mBtnCancel;
	private TextView mTvContent;
	private CheckBox mCBPayMe;
	private CheckBox mCBPayAA;
	private CheckBox mCBPayOther;	
	//Handler
	private ArrayList<MyUser> mFriends = new ArrayList<MyUser>();
	private ActivityData mData;
	private void updateFriendsUI()
	{

	}
	
	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		
		initUI();
		initData();
	}
	
	private void initData()
	{
		/*test code begin*/
		Intent intent = getIntent();
		IntentBuilder ib = new IntentBuilder(intent);
		mData = MyServerManager.getInstance().getActivity(ib.getActivityID());
		if (mData == null) {
			Toast.makeText(this, "找不到活动内容", Toast.LENGTH_SHORT).show();

		}
		mMediator.setActivityData(mData);
		
	}
	
	private void initUI()
	{
		RelativeLayout layout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.fragment_activity_detail, null);
		mView = layout;
		setContentView(layout);
		
		mTvName = (TextView) layout.findViewById(R.id.textView_title_sub);

		mTvTime = (TextView)layout.findViewById(R.id.button_select_date);
		
		mTvContent = (TextView)layout.findViewById(R.id.editText_content_sub);
		
		mTvFriends = (TextView) layout.findViewById(R.id.button_select_friends);
		
		mCBPayMe = (CheckBox)findViewById(R.id.checkBox_pay_me);
		mCBPayAA = (CheckBox)findViewById(R.id.checkBox_pay_aa);
		mCBPayOther = (CheckBox)findViewById(R.id.checkBox_pay_other);
		
		mBtnConfirm = (Button)findViewById(R.id.button_OK);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMediator.onOKClicked();
				ActivityDetailActivity.this.finish();
			}
		});
		
		mBtnCancel = (Button)findViewById(R.id.button_Cancel);
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMediator.onCancelClicked();
				ActivityDetailActivity.this.finish();				
			}
		});
		
	}
	
	
	private void sendActivityToSingle(ActivityData data)
	{
		EMConversation conversation = EMChatManager.getInstance().getConversation(data.mUsers.get(0).mName);
		
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		// 如果是群聊，设置chattype,默认是单聊
		
		Log.v(DemoApplication.TAG, ActivityData.toJSON(data).toString());
		TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
		message.addBody(txtBody);
		message.setReceipt(mFriends.get(0).mName);
		conversation.addMessage(message);
		try {
			EMChatManager.getInstance().sendMessage(message);
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
	}
	
	private void sendActivityToGroup(ActivityData data)
	{
		EMGroup group;
		try {
			String[] names = new String[data.mUsers.size()];
			for(int i=0; i<data.mUsers.size(); i++)
			{
				names[i] = data.mUsers.get(i).mName;
			}
			group = EMGroupManager.getInstance().createPrivateGroup("", "", names, false);
			EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());										
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			message.setChatType(ChatType.GroupChat);
			TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
			message.addBody(txtBody);
		    message.setReceipt(group.getGroupId());
		    conversation.addMessage(message);
			try {
				EMChatManager.getInstance().sendMessage(message);
			} catch (EaseMobException e) {
				e.printStackTrace();
			}
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(data!=null)
		{
			Bundle bundle = data.getBundleExtra("members");
			mFriends = (ArrayList<MyUser>)bundle.getSerializable("members");
			updateFriendsUI();			
		}

	}
}
