package com.test.art.activity;

import java.util.ArrayList;
import java.util.Date;

import junit.framework.Assert;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.test.art.DemoApplication;
import com.test.art.R;
import com.test.art.data.ActivityData;
import com.test.art.data.ActivityData.ActivityBuilder;
import com.test.art.data.MyUser;
import com.test.art.data.DianpingDao.ComplexBusiness;
import com.test.art.data.message.MyMessage;
import com.test.art.domain.MessageManager;
import com.test.art.domain.MyServerManager;
import com.test.art.domain.activity.ActivityManager;
import com.test.art.mediator.IActivityCreateMediator;
import com.test.art.mediator.IActivityDetailMediator;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivityActivity2 extends Activity {
	
	
	
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

	//UI Controls
	private EditText mEtxTitle;
	private EditText mEtxContent;
	private Button mBtnSelectBusiness;
	private Button mBtnSelectFriends;
	private Button mBtnSelectDate;
	private Button mBtnSelectLocation;
	private Button mBtnOK;
	private Button mBtnCancel;
	private CheckBox mCBPayMe;
	private CheckBox mCBPayAA;
	private CheckBox mCBPayOther;
	//Data
	private ActivityData mActivityData;
	private ComplexBusiness mComplexBusiness;
	private Date mBeginDate;
	private int mUseType;
	private final int INTENT_MEMBERS = 0;
	private final int INTENT_BUSINESS = 1;
	private final int INTENT_DATE = 2;
	private final int INTENT_LOCATION = 3;
	
	public static final int USE_CREATE = 4;
	public static final int USE_EDIT = 5;
	
	private IActivityCreateMediator mMediator = new IActivityCreateMediator() {
		private ActivityBuilder mActivityBuilder = new ActivityBuilder();
	    boolean mIsTimeSet = false;
		
		@Override
		public void setTitle(String title) {
			mActivityBuilder.setTitle(title);
			
		}
		
		@Override
		public void setTime(Date date) {
			if(null != date)
			{
				mIsTimeSet = true;
				mActivityBuilder.setBeginTime(date);
			}
			
		}
		
		@Override
		public void setPayType(int type) {
			mActivityBuilder.setSpentType(type);
			if(type == ActivityData.PAY_ME)
			{
				mCBPayAA.setChecked(false);
				mCBPayOther.setChecked(false);
			}
			else if(type == ActivityData.PAY_AA)
			{
				mCBPayMe.setChecked(false);
				mCBPayOther.setChecked(false);
			}
			else if(type == ActivityData.PAY_OTHER)
			{
				mCBPayMe.setChecked(false);
				mCBPayAA.setChecked(false);
			}			
		}
		
		@Override
		public void setMembers(ArrayList<MyUser> users) {
			String usersString = "";
			for(MyUser user:users)
			{
				usersString += user.mName + " ";
			}
			mBtnSelectFriends.setText(usersString);
			mActivityBuilder.setUsers(users);
			
		}
		
		@Override
		public void setContent(String content) {
			mActivityBuilder.setContent(content);
		}
		
		@Override
		public void onOKClicked() {
			mMediator.setCreator(DemoApplication.getInstance().getUser());
			if(null != (mActivityData = mMediator.createActivityData()))
			{
				DemoApplication.getInstance().getUser().startActivity(mActivityData);
				CreateActivityActivity2.this.finish();	
			}
	
			
		}
		
		@Override
		public void onCancelClicked() {
			CreateActivityActivity2.this.finish();	
		}

		@Override
		public ActivityData createActivityData() {
			String title;
			if(mEtxTitle.getEditableText().toString().length() != 0)
			{
				title = mEtxTitle.getEditableText().toString();
				setTitle(title);
			}
			else
			{
				Toast.makeText(CreateActivityActivity2.this, "请填写聚会标题", Toast.LENGTH_SHORT).show();
				return null;
			}
			
			String content;
			if(mEtxContent.getEditableText().toString().length() != 0)
			{
				content = mEtxContent.getEditableText().toString();
				setContent(content);
			}
			else
			{
				Toast.makeText(CreateActivityActivity2.this, "请填写聚内容", Toast.LENGTH_SHORT).show();
				return null;
			}
			
			if(!mIsTimeSet)
			{
				Toast.makeText(CreateActivityActivity2.this, "请选择聚会时间", Toast.LENGTH_SHORT).show();
				return null;			
			}
			return mActivityBuilder.create();
		}

		@Override
		public void setCreator(MyUser user) {
			mActivityBuilder.setCreator(user);
		}
	};
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		initData();
		
		initUI();
		
	}
	
	public void initUI()
	{
		setContentView(R.layout.activity_newactivity2);
		mEtxTitle = (EditText)findViewById(R.id.editText_title);
		mEtxContent = (EditText)findViewById(R.id.editText_content);
		mCBPayMe = (CheckBox)findViewById(R.id.checkBox_pay_me);
		mCBPayAA = (CheckBox)findViewById(R.id.checkBox_pay_aa);
		mCBPayOther = (CheckBox)findViewById(R.id.checkBox_pay_other);
		mBtnSelectBusiness = (Button)findViewById(R.id.button_select_business);
		mBtnSelectBusiness.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CreateActivityActivity2.this, CreateActivityActivity.class);
				startActivityForResult(intent, INTENT_BUSINESS);
			}
		});
		
		
		mBtnSelectFriends = (Button)findViewById(R.id.button_select_friends);
		mBtnSelectFriends.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CreateActivityActivity2.this, ActivityMembersActivity.class);
				if(mActivityData.mStatus == ActivityData.BEGIN)
				{
					intent.putExtra("state", ActivityData.BEGIN);
				}
				
				intent.putExtra("members", mActivityData.mUsers);
				startActivityForResult(intent, INTENT_MEMBERS);				
			}
		});
		
		mBtnSelectDate = (Button)findViewById(R.id.button_select_date);
		mBtnSelectDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(CreateActivityActivity2.this, DateActivity.class);
				startActivityForResult(intent, INTENT_DATE);				
			}
		});
		
		
		mBtnOK = (Button)findViewById(R.id.button_confirm);
		mBtnCancel = (Button)findViewById(R.id.button_cancel);	
		
		
		if(mUseType == USE_CREATE)
		{
			if(DemoApplication.isDebug)
			{
				mActivityData = ActivityData.createTestData();
				updateView();
			}
			
			mBtnOK.setText("发起聚会");
			mBtnOK.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mMediator.onOKClicked();
				}
			});
			
			mBtnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					mMediator.onCancelClicked();
				}
			});
		}

			
	}
	
	public void initData()
	{
		mActivityData = null;
		Intent intent =  getIntent();
		IntentBuilder ib = new IntentBuilder(intent);
		mUseType = ib.getUseType();
		if(mUseType == USE_EDIT)
		{
			mActivityData = MyServerManager.getInstance().getActivity(ib.getActivityID());
		}
		
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode == RESULT_OK)
		{
			switch(requestCode)
			{
			case INTENT_BUSINESS:
				mComplexBusiness = (ComplexBusiness) data.getSerializableExtra("business");
				mBtnSelectBusiness.setText(mComplexBusiness.mName);
				break;
				
			case INTENT_MEMBERS:
				ArrayList<MyUser> users = (ArrayList<MyUser>) data.getSerializableExtra("members");
				mMediator.setMembers(users);
				break;
				
			case INTENT_DATE:
				Date date = (Date)data.getSerializableExtra("date");
				mBtnSelectDate.setText(DateFormat.format(ActivityData.dataPattern, date));
				mMediator.setTime(date);
				break;
			}
		}
		
	}
	
	
	/**
	 * ActivityData 更新 View
	 * 
	 */
	private void updateView( )
	{
		if(null != mActivityData)
		{
			mEtxTitle.setText(mActivityData.mTitle);
			mEtxContent.setText(mActivityData.mContent);
			mBeginDate = mActivityData.mBeginDate;
			mBtnSelectDate.setText(DateFormat.format(ActivityData.dataPattern, mBeginDate));
			String users = "";
			for(MyUser user:mActivityData.mUsers)
			{
				users += user.mName + " ";
			}
			mBtnSelectFriends.setText(users);		
			
			if(mActivityData.mSpentType == 0)
			{
				mCBPayMe.setChecked(true);
			}
			if(mActivityData.mSpentType == 1)
			{
				mCBPayAA.setChecked(true);
			}
			if(mActivityData.mSpentType == 2)
			{
				mCBPayOther.setChecked(true);
			}
		}
		
	}

}
