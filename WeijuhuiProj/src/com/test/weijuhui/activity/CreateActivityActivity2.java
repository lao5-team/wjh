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
import com.test.weijuhui.data.User;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.ActivityManager;

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
	private ComplexBusiness mComplexBusiness;
	private Date mBeginDate;
	private int mUse;
	//private float mLocation_Long
	private ArrayList<User> mUsers;
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
		
		mBtnSelectLocation = (Button)findViewById(R.id.button_select_location);
		
		mBtnOK = (Button)findViewById(R.id.button_confirm);
		mBtnOK.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(createActivityData())
				{
					if(mUsers.size() == 1)
					{
						sendActivityToSingle(mActivityData);
					}
					else if(mUsers.size() > 1)
					{
						sendActivityToGroup(mActivityData);
					}
					ActivityManager.getInstance().addActivity(mActivityData);
					CreateActivityActivity2.this.finish();					
				}

			}
		});
		
		mBtnCancel = (Button)findViewById(R.id.button_cancel);	
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
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
		mUsers = new ArrayList<User>();
		Assert.assertTrue(getIntent().hasExtra("use"));
		mUse = getIntent().getIntExtra("use", INTENT_CREATE);
		if(INTENT_CREATE == mUse)
		{
			mActivityData = null;
		}
		else if(INTENT_EDIT == mUse)
		{
			Assert.assertTrue(getIntent().hasExtra("activityIndex"));
			mActivityData = ActivityManager.getInstance().getActivities().get(getIntent().getIntExtra("activityIndex", 0));
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
				mUsers = (ArrayList<User>) data.getSerializableExtra("members");
				String users = "";
				for(User user:mUsers)
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
		
		User currentUser = new User();
		currentUser.mName = DemoApplication.getInstance().getUserName();
		
		mActivityData = new ActivityData.ActivityBuilder().setTitle(title).setContent(content).
				setComplexBusiness(mComplexBusiness).setCreator(currentUser).setBeginTime(mBeginDate).
				setUsers(mUsers).create();
		return true;
		
	}
	
	private void sendActivityToSingle(ActivityData data)
	{
		EMConversation conversation = EMChatManager.getInstance().getConversation(data.mUsers.get(0).mName);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		Log.v("weijuhui", ActivityData.toJSON(data).toString());
		TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
		message.addBody(txtBody);
		message.setReceipt(data.mUsers.get(0).mName);
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
}
