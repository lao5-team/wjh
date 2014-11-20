package com.test.juxiaohui.activity;

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
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.DianpingDao.ComplexBusiness;
import com.test.juxiaohui.data.message.MyMessage;
import com.test.juxiaohui.domain.MessageManager;
import com.test.juxiaohui.domain.MyServerManager;
import com.test.juxiaohui.domain.activity.ActivityManager;
import com.test.juxiaohui.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
	//private float mLocation_Long
	private ArrayList<MyUser> mUsers;
	private final int INTENT_MEMBERS = 0;
	private final int INTENT_BUSINESS = 1;
	private final int INTENT_DATE = 2;
	private final int INTENT_LOCATION = 3;
	
	public static final int USE_CREATE = 4;
	public static final int USE_EDIT = 5;
	
	
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
				
				intent.putExtra("members", mUsers);
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
		
		//mBtnSelectLocation = (Button)findViewById(R.id.button_select_location);
		
		mBtnOK = (Button)findViewById(R.id.button_confirm);
		mBtnCancel = (Button)findViewById(R.id.button_cancel);	
//		if(mActivityData == null||mActivityData.mState == ActivityData.UNBEGIN)
//		{

//		}
//		if(mActivityData.mStatus == ActivityData.BEGIN)
//		{
//			if(mActivity.getMyRoleType().equals(com.test.juxiaohui.domain.activity.Activity.CREATOR))
//			{
//				mBtnOK.setText("确认开始");
//				mBtnOK.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						mActivity.confirmActivity();
//						CreateActivityActivity2.this.finish();
//					}
//				});	
//				
//				mBtnCancel.setText("活动取消");
//				mBtnCancel.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						mActivity.cancelActivity();
//						CreateActivityActivity2.this.finish();						
//					}
//				});
//			}
//			else if(mActivity.getMyRoleType().equals(com.test.juxiaohui.domain.activity.Activity.JOINER))
//			{
//				mBtnOK.setText("同意加入");
//				mBtnOK.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						mActivity.acceptActivity();
//						CreateActivityActivity2.this.finish();
//					}
//				});	
//				
//				mBtnCancel.setText("拒绝加入");
//				mBtnCancel.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						mActivity.cancelActivity();
//						CreateActivityActivity2.this.finish();						
//					}
//				});				
//			}
//		}
//		else if(mActivityData.mStatus == ActivityData.PROCESSING)
//		{
//			if(mActivity.getMyRoleType().equals(com.test.juxiaohui.domain.activity.Activity.CREATOR))
//			{
//				mBtnOK.setText("完成");
//				mBtnOK.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						mActivity.finishActivity();
//						CreateActivityActivity2.this.finish();
//					}
//				});	
//				
//				mBtnCancel.setText("活动取消");
//				mBtnCancel.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						mActivity.cancelActivity();
//						CreateActivityActivity2.this.finish();						
//					}
//				});
//			}
//			else if(mActivity.getMyRoleType().equals(com.test.juxiaohui.domain.activity.Activity.JOINER))
//			{
//				mBtnOK.setVisibility(View.INVISIBLE);
//				mBtnOK.setText("同意加入");
//				mBtnOK.setOnClickListener(new OnClickListener() {
//
//					@Override
//					public void onClick(View v) {
//						mActivity.acceptActivity();
//						CreateActivityActivity2.this.finish();
//					}
//				});	
//				
//				mBtnCancel.setText("退出");
//				mBtnCancel.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						mActivity.quitActivity();
//						CreateActivityActivity2.this.finish();						
//					}
//				});				
//			}			
//		}
		
		
		if(mUseType == USE_EDIT)
		{
			mEtxTitle.setText(mActivityData.mTitle);
			mEtxContent.setText(mActivityData.mContent);
			if(null != mActivityData.mCB)
			{
				mBtnSelectBusiness.setText(mActivityData.mCB.mName);
			}
			if(null != mActivityData.mBeginDate)
			{
				mBtnSelectDate.setText(DateFormat.format(ActivityData.dataPattern, mActivityData.mBeginDate));
			}
			
			if(DemoApplication.getInstance().getUser().mID.equals(mActivityData.mCreator.mID))
			{
				mBtnOK.setText("完成");
				mBtnOK.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						DemoApplication.getInstance().getUser().finishActivity(mActivityData);
						CreateActivityActivity2.this.finish();		
					}
				});	
				mBtnCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CreateActivityActivity2.this.finish();						
					}
				});
			}

		}
		
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
					if(createActivityData())
					{
						mActivityData.mCreator = DemoApplication.getInstance().getUser();
						DemoApplication.getInstance().getUser().startActivity(mActivityData);
						CreateActivityActivity2.this.finish();					
					}
				}
			});
			
			mBtnCancel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CreateActivityActivity2.this.finish();						
				}
			});
		}

			
	}
	
	public void initData()
	{
		mUsers = new ArrayList<MyUser>();
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
				mUsers = (ArrayList<MyUser>) data.getSerializableExtra("members");
				String users = "";
				for(MyUser user:mUsers)
				{
					users += user.mName + " ";
				}
				mBtnSelectFriends.setText(users);
				
				break;
				
			case INTENT_DATE:
				mBeginDate = (Date)data.getSerializableExtra("date");
				mBtnSelectDate.setText(DateFormat.format(ActivityData.dataPattern, mBeginDate));
				break;
			}
		}
		
	}
	
	/**
	 * @return
	 */
	/**
	 * @return
	 */
	private boolean createActivityData()
	{
		String title;
		if(mEtxTitle.getEditableText().toString().length() != 0)
		{
			title = mEtxTitle.getEditableText().toString();
		}
		else
		{
			Toast.makeText(this, "请填写聚会标题", Toast.LENGTH_SHORT).show();
			return false;
		}
		
		String content;
		if(mEtxContent.getEditableText().toString().length() != 0)
		{
			content = mEtxContent.getEditableText().toString();
		}
		else
		{
			Toast.makeText(this, "请填写聚内容", Toast.LENGTH_SHORT).show();
			return false;
		}
		if(null == mBeginDate)
		{
			Toast.makeText(this, "请选择聚会时间", Toast.LENGTH_SHORT).show();
			return false;			
		}
		if(null == mUsers||mUsers.size()==0)
		{
			Toast.makeText(this, "请选择聚会人员", Toast.LENGTH_SHORT).show();
			return false;			
		}		
		MyUser creator = new MyUser();
		creator.mName = DemoApplication.getInstance().getUserName();
		//creator.mActivityState = MyUser.CONFIRMED;
		if(mUsers.size()==1)
		{
			mActivityData = new ActivityData.ActivityBuilder().setTitle(title).setContent(content).
					setComplexBusiness(mComplexBusiness).setCreator(creator).setBeginTime(mBeginDate).
					setUsers(mUsers).create();			
		}
		/*3人或以上属于群聊，需要groupID*/
		else if(mUsers.size()>1)
		{
			String[] usernames = new String[mUsers.size()];
			for(int i=0; i<mUsers.size(); i++)
			{
				usernames[i] = mUsers.get(i).mName;
			}
			//usernames[mUsers.size()] = creator.mName;
			EMGroup group;
			try {
				group = EMGroupManager.getInstance().createPrivateGroup("", "", usernames, false);
				mActivityData = new ActivityData.ActivityBuilder().setTitle(title).setContent(content).
						setComplexBusiness(mComplexBusiness).setCreator(creator).setBeginTime(mBeginDate).
						setUsers(mUsers).setGroupID(group.getGroupId()).create();
			} catch (EaseMobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		return true;
		
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
			mUsers = mActivityData.mUsers;
			String users = "";
			for(MyUser user:mUsers)
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
