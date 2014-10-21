package com.test.weijuhui.activity;

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
import com.test.weijuhui.DemoApplication;
import com.test.weijuhui.R;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.Message;
import com.test.weijuhui.data.MyUser;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.ActivityManager;
import com.test.weijuhui.domain.MessageManager;
import com.test.weijuhui.domain.MyServerManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivityActivity2 extends Activity {

	//UI Controls
	private EditText mEtxTitle;
	private EditText mEtxContent;
	private Button mBtnSelectBusiness;
	private Button mBtnSelectFriends;
	private Button mBtnSelectDate;
	private Button mBtnSelectLocation;
	private Button mBtnOK;
	private Button mBtnCancel;
	
	//Data
	private ActivityData mActivityData;
	private String mActivityID;
	private com.test.weijuhui.domain.Activity mActivity;
	private ComplexBusiness mComplexBusiness;
	private Date mBeginDate;
	private int mUse;
	//private float mLocation_Long
	private ArrayList<MyUser> mUsers;
	private final int INTENT_MEMBERS = 0;
	private final int INTENT_BUSINESS = 1;
	private final int INTENT_DATE = 2;
	private final int INTENT_LOCATION = 3;
	
	public static final int INTENT_CREATE = 4;
	public static final int INTENT_EDIT = 5;
	
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
				if(mActivity == null)
				{
					intent.putExtra("state", ActivityData.UNBEGIN);
				}
				else if(mActivity.getData().mState == ActivityData.BEGIN)
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
		if(mActivityData == null||mActivityData.mState == ActivityData.UNBEGIN)
		{
			mBtnOK.setText("发起聚会");
			mBtnOK.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(createActivityData())
					{
						com.test.weijuhui.domain.Activity activity = ActivityManager.getInstance().createActivity(mActivityData);
						activity.startActivity();
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
		else if(mActivityData.mState == ActivityData.BEGIN)
		{
			if(mActivity.getMyRoleType().equals(com.test.weijuhui.domain.Activity.CREATOR))
			{
				mBtnOK.setText("确认开始");
				mBtnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mActivity.confirmActivity();
						CreateActivityActivity2.this.finish();
					}
				});	
				
				mBtnCancel.setText("活动取消");
				mBtnCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mActivity.cancelActivity();
						CreateActivityActivity2.this.finish();						
					}
				});
			}
			else if(mActivity.getMyRoleType().equals(com.test.weijuhui.domain.Activity.JOINER))
			{
				mBtnOK.setText("同意加入");
				mBtnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mActivity.acceptActivity();
						CreateActivityActivity2.this.finish();
					}
				});	
				
				mBtnCancel.setText("拒绝加入");
				mBtnCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mActivity.cancelActivity();
						CreateActivityActivity2.this.finish();						
					}
				});				
			}
		}
		else if(mActivityData.mState == ActivityData.PROCESSING)
		{
			if(mActivity.getMyRoleType().equals(com.test.weijuhui.domain.Activity.CREATOR))
			{
				mBtnOK.setText("完成");
				mBtnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mActivity.finishActivity();
						CreateActivityActivity2.this.finish();
					}
				});	
				
				mBtnCancel.setText("活动取消");
				mBtnCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mActivity.cancelActivity();
						CreateActivityActivity2.this.finish();						
					}
				});
			}
			else if(mActivity.getMyRoleType().equals(com.test.weijuhui.domain.Activity.JOINER))
			{
				mBtnOK.setVisibility(View.INVISIBLE);
				mBtnOK.setText("同意加入");
				mBtnOK.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mActivity.acceptActivity();
						CreateActivityActivity2.this.finish();
					}
				});	
				
				mBtnCancel.setText("退出");
				mBtnCancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mActivity.quitActivity();
						CreateActivityActivity2.this.finish();						
					}
				});				
			}			
		}
		
		
		if(mUse == INTENT_EDIT)
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
		}
			
	}
	
	public void initData()
	{
		mUsers = new ArrayList<MyUser>();
		Assert.assertTrue(getIntent().hasExtra("use"));
		mUse = getIntent().getIntExtra("use", INTENT_CREATE);
		if(INTENT_CREATE == mUse)
		{
			mActivityData = null;
		}
		else if(INTENT_EDIT == mUse)
		{
			Assert.assertTrue(getIntent().hasExtra("activityIndex"));
			mActivityData = ActivityManager.getInstance().getActivity(getIntent().getIntExtra("activityIndex", 0)).getData();
			mActivity = ActivityManager.getInstance().getActivity(getIntent().getIntExtra("activityIndex", 0));
			mUsers = mActivityData.mUsers;
					
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
		mActivityID = MyServerManager.getInstance().getNewActivityID(DemoApplication.getInstance().getUserName());
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
		creator.mActivityState = MyUser.CONFIRMED;
		if(mUsers.size()==1)
		{
			mActivityData = new ActivityData.ActivityBuilder().setTitle(title).setContent(content).
					setComplexBusiness(mComplexBusiness).setCreator(creator).setBeginTime(mBeginDate).
					setUsers(mUsers).setID(mActivityID).create();			
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
						setUsers(mUsers).setID(mActivityID).setGroupID(group.getGroupId()).create();
			} catch (EaseMobException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		

		return true;
		
	}
	

}
