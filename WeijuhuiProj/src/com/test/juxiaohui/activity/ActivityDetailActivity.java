package com.test.juxiaohui.activity;

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
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.activity.CreateActivityActivity2.IntentBuilder;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.DianpingDataHelper;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.DianpingDao.ComplexBusiness;
import com.test.juxiaohui.domain.MyServerManager;
import com.test.juxiaohui.domain.activity.ActivityManager;
import com.test.juxiaohui.R;
import com.test.juxiaohui.R.id;
import com.test.juxiaohui.R.layout;

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
	private Button mBtnConfirm;
	private Button mBtnCancel;
	private TextView mTvContent;
	private CheckBox mCBPayMe;
	private CheckBox mCBPayAA;
	private CheckBox mCBPayOther;	
	//Handler
	private Handler mUIHandler;
	private ArrayList<MyUser> mFriends = new ArrayList<MyUser>();
	private ActivityData mData;
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
	}
	
	private void initData()
	{
		/*test code begin*/
			Intent intent =  getIntent();
			IntentBuilder ib = new IntentBuilder(intent);
			mData = MyServerManager.getInstance().getActivity(ib.getActivityID());
			if(mData == null)
			{
				Toast.makeText(this, "找不到活动内容", Toast.LENGTH_SHORT).show();
				
			}
		/*test code end*/
		mUIHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				updateUI();
			}
		};
		mActivityIndex = intent.getIntExtra("activityIndex", -1);
		
	}
	
	private void initUI()
	{
		RelativeLayout layout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.fragment_activity_detail, null);
		mView = layout;
		setContentView(layout);
		
		mTvName = (TextView) layout.findViewById(R.id.textView_title_sub);
		mTvName.setText(mData.mTitle);
		
		mTvContent = (TextView)layout.findViewById(R.id.editText_content_sub);
		mTvContent.setText(mData.mContent);
		
		mTvFriends = (TextView) layout.findViewById(R.id.button_select_friends);
		
		String userNames = "活动参与者: ";
		for(MyUser user:mData.mUsers)
		{
			userNames += user.mName;
		}
		mTvFriends.setText(userNames);
		
		mCBPayMe = (CheckBox)findViewById(R.id.checkBox_pay_me);
		mCBPayAA = (CheckBox)findViewById(R.id.checkBox_pay_aa);
		mCBPayOther = (CheckBox)findViewById(R.id.checkBox_pay_other);
		
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
		
		mBtnConfirm = (Button)findViewById(R.id.button_OK);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				com.test.juxiaohui.domain.activity.Activity activity = ActivityManager.getInstance().createActivity(mData);
				activity.finishActivity();
				ActivityDetailActivity.this.finish();
			}
		});
		
		mBtnCancel = (Button)findViewById(R.id.button_Cancel);
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				com.test.juxiaohui.domain.activity.Activity activity = ActivityManager.getInstance().createActivity(mData);
				activity.dismissActivity();
				ActivityDetailActivity.this.finish();				
			}
		});
//		mImgContent = (ImageView) layout.findViewById(R.id.imageView_img);
//		
//		mLayoutAddress = (RelativeLayout) layout.findViewById(R.id.relativeLayout_address);
//		
//		mTvAddress = (TextView) layout.findViewById(R.id.textView_address);
//		
//		mLayoutPhone = (RelativeLayout) layout.findViewById(R.id.relativeLayout_phone);
//		
//		mTvPhone = (TextView) layout.findViewById(R.id.textView_phone);
//		
//		mBtnAddFriends = (Button) layout.findViewById(R.id.button_addFriends);
//		mBtnAddFriends.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(ActivityDetailActivity.this, ActivityMembersActivity.class);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("members", mFriends);
//				intent.putExtra("members", bundle);
//				startActivityForResult(intent, 0);
//			}
//		});
//		
//		mTvFriends = (TextView) layout.findViewById(R.id.textView_friends);
//		
//		mBtnConfirm = (Button) layout.findViewById(R.id.button_confirm);
//		mBtnConfirm.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				MyUser currentUser = new MyUser();
//				currentUser.mName = DemoApplication.getInstance().getUserName();
//				ActivityData data = new ActivityData.ActivityBuilder().setComplexBusiness(mCBData).setUsers(mFriends).setCreator(currentUser).create();
//				if(mFriends.size() == 1)
//				{
//					sendActivityToSingle(data);
//				}
//				else if(mFriends.size() > 1)
//				{
//					sendActivityToGroup(data);
//				}
//				//ActivityManager.getInstance().addActivity(data);
//				ActivityDetailActivity.this.setResult(Activity.RESULT_OK);
//				ActivityDetailActivity.this.finish();
//			}
//			
//		});
//		
//		mBtnCancel = (Button)layout.findViewById(R.id.button_cancel);
//		mBtnCancel.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				ActivityDetailActivity.this.finish();
//			}
//		});
		
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
