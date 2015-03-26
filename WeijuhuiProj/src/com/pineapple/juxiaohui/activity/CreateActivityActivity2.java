package com.pineapple.juxiaohui.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.Assert;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.pineapple.juxiaohui.DemoApplication;
import com.pineapple.juxiaohui.data.ActivityData;
import com.pineapple.juxiaohui.data.ActivityData.ActivityBuilder;
import com.pineapple.juxiaohui.data.MyUser;
import com.pineapple.juxiaohui.data.DianpingDao.ComplexBusiness;
import com.pineapple.juxiaohui.data.message.MyMessage;
import com.pineapple.juxiaohui.domain.MessageManager;
import com.pineapple.juxiaohui.domain.MyServerManager;
import com.pineapple.juxiaohui.domain.UserManager;
import com.pineapple.juxiaohui.domain.activity.ActivityManager;
import com.pineapple.juxiaohui.mediator.IActivityCreateMediator;
import com.pineapple.juxiaohui.mediator.IActivityDetailMediator;
import com.pineapple.juxiaohui.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.Reminders;
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
	boolean mIsTimeSet = false;
	
	private IActivityCreateMediator mMediator = new IActivityCreateMediator() {
		private ActivityBuilder mActivityBuilder = new ActivityBuilder();
	    
		
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
			mActivityBuilder.setInviteUsers(users);
			
		}
		
		@Override
		public void setContent(String content) {
			mActivityBuilder.setContent(content);
		}
		
		@Override
		public void onOKClicked() {
			if(checkDataComplete())
			{
				final ProgressDialog pd = new ProgressDialog(CreateActivityActivity2.this);
				pd.setMessage("正在创建活动...");
				pd.show();
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						
						if(DemoApplication.isDebug)
						{
							UserManager.getInstance().getCurrentUser().startActivity(mActivityData);
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									if (!CreateActivityActivity2.this.isFinishing())
										pd.dismiss();
									addActivityToCalendar(mActivityData);
									Toast.makeText(getApplicationContext(), "创建活动成功", 0).show();
									CreateActivityActivity2.this.finish();		
								}
							});
										
						}
						else
						{
							mMediator.setCreator(UserManager.getInstance().getCurrentUser());
							if(null != (mActivityData = mMediator.createActivityData()))
							{
								UserManager.getInstance().getCurrentUser().startActivity(mActivityData);
								runOnUiThread(new Runnable() {
									
									@Override
									public void run() {
										if (!CreateActivityActivity2.this.isFinishing())
											pd.dismiss();
										addActivityToCalendar(mActivityData);
										Toast.makeText(getApplicationContext(), "创建活动成功", 0).show();
										CreateActivityActivity2.this.finish();		
									}
								});
							}				
						}
						
					}
				});
				thread.start();
			}
		}
		
		@Override
		public void onCancelClicked() {
			CreateActivityActivity2.this.finish();	
		}

		@Override
		public ActivityData createActivityData() {
			return mActivityBuilder.create();
		}

		@Override
		public void setCreator(MyUser user) {
			mActivityBuilder.setCreator(user);
		}
		
		private void addActivityToCalendar(ActivityData data)
		{
			try
			{
				String[] projection = new String[] { "_id", "name" };
				Uri calendars = Uri.parse("content://com.android.calendar/calendars");
				Cursor managedCursor = getContentResolver().query(calendars, projection,
						null, null, null);
				if (managedCursor.moveToFirst()) {
					String calName;
					String calId;
					int nameColumn = managedCursor.getColumnIndex("name");
					int idColumn = managedCursor.getColumnIndex("_id");
					do {
						calName = managedCursor.getString(nameColumn);
						calId = managedCursor.getString(idColumn);
					} while (managedCursor.moveToNext());
					
					long startMillis = 0; 
					long endMillis = 0;     
					Calendar beginTime = Calendar.getInstance();
					beginTime.set(2014, 11, 31, 10, 30);
					startMillis = beginTime.getTimeInMillis();
					Calendar endTime = Calendar.getInstance();
					endTime.set(2014, 11, 31, 11, 0);
					endMillis = endTime.getTimeInMillis();	
				ContentValues event = new ContentValues(); 
				event.put(Events.DTSTART, data.mBeginDate.getTime());
				event.put(Events.DTEND, data.mBeginDate.getTime() + 3600000);
				event.put(Events.CALENDAR_ID, calId); 	 
				event.put(Events.TITLE, data.mTitle); 
				event.put(Events.DESCRIPTION, data.mContent); 
				event.put(Events.EVENT_TIMEZONE, TimeZone.getDefault().getID().toString());
				Uri uri = getContentResolver().insert(Events.CONTENT_URI, event);
				long eventID = Long.parseLong(uri.getLastPathSegment());
				event = new ContentValues(); 
				event.put(Reminders.MINUTES, 15);
				event.put(Reminders.EVENT_ID, eventID);
				event.put(Reminders.METHOD, Reminders.METHOD_ALERT);
				uri = getContentResolver().insert(Reminders.CONTENT_URI, event);
				} 
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
			
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
//				if(mActivityData.mStatus == ActivityData.BEGIN)
//				{
//					intent.putExtra("state", ActivityData.BEGIN);
//				}
				if(null!=mActivityData && null!=mActivityData.mUsers)
				{
					intent.putExtra("members", mActivityData.mUsers);
				}
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
			mMediator.setTime(mBeginDate);
			mBtnSelectDate.setText(DateFormat.format(ActivityData.dataPattern, mBeginDate));
			String users = "";
			for(MyUser user:mActivityData.mInvitingUsers)
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
	
	private boolean checkDataComplete()
	{
		String title;
		if(mEtxTitle.getEditableText().toString().length() != 0)
		{
			title = mEtxTitle.getEditableText().toString();
			mMediator.setTitle(title);
		}
		else
		{
			Toast.makeText(CreateActivityActivity2.this, "请填写聚会标题", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		String content;
		if(mEtxContent.getEditableText().toString().length() != 0)
		{
			content = mEtxContent.getEditableText().toString();
			mMediator.setContent(content);
		}
		else
		{
			Toast.makeText(CreateActivityActivity2.this, "请填写聚内容", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if(!mIsTimeSet)
		{
			Toast.makeText(CreateActivityActivity2.this, "请选择聚会时间", Toast.LENGTH_SHORT).show();
			return false;			
		}
		return true;
	}

}
