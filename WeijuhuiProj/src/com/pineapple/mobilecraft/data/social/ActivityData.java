package com.pineapple.mobilecraft.data.social;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import junit.framework.Assert;
import android.text.format.DateFormat;
import android.view.inputmethod.CompletionInfo;

import com.pineapple.mobilecraft.DemoApplication;
import com.pineapple.mobilecraft.data.social.MyUser;
import com.pineapple.mobilecraft.data.social.DianpingDao.ComplexBusiness;
import com.pineapple.mobilecraft.data.social.DianpingDao.SimpleBusiness;
import com.pineapple.mobilecraft.manager.social.MyServerManager;
import com.pineapple.mobilecraft.manager.social.UserManager;

public class ActivityData implements Serializable {

	private static final long serialVersionUID = 3368450357726232660L;
	public static int PAY_ME = 0;
	public static int PAY_AA = 1;
	public static int PAY_OTHER = 2;
	
	/*活动状态可选值*/
	//public final static int UNBEGIN = 0; //活动未开始，创建者还在填写活动信息，未发起的状态
	public final static int BEGIN = 1;   //活动开始，创建者已经发起，等待报名的状态
	//public final static int PROCESSING = 2;  //活动进行中，指参与者已经报名和支付完成，正式开始的状态
	public final static int FINISH = 2;   //完成，活动发起人点击完成
	public final static int CANCELED = 3;   //活动取消，发起人中途终止活动
	
	public final static String dataPattern = "yyyy年MM月dd日hh时";
	public final static String dataPattern_test = "MM月dd日hh时mm分";
	public String mID;
	public ComplexBusiness mCB;
	public Date mBeginDate;
	final public ArrayList<MyUser> mInvitingUsers;   //被邀请的活动参与者
	final public ArrayList<MyUser> mUsers;  //活动参与者，注意不包括创建者
	public float mSpent;    //活动所需金额
	public String mGroupID;  //多人活动的groupID，供环信使用
	public int mStatus;       //活动状态
	public MyUser mCreator;  //活动创建者
	public String mTitle;    //活动标题
	public String mContent;  //活动内容介绍
	public int mSpentType; //0 me, 1 aa, 2 other
	private ActivityData()
	{
		mUsers = new ArrayList<MyUser>();
		mInvitingUsers = new ArrayList<MyUser>();
		mBeginDate = null;
		mTitle = null;
		mContent = null;
		mCreator = null;
		mStatus = BEGIN;
		mCB = null;
		mGroupID = null;
		mID = null;
	}
	
	/** 添加活动参与者，如果参与者已经在活动中，则不会重复添加
	 * @param user
	 * 
	 */
	public void addUser(MyUser user)
	{
		if(null == user)
		{
			throw new IllegalArgumentException("addUser user can not be null");
		}
		for(MyUser itemUser:mUsers)
		{
			if(itemUser.mID.equals(user.mID))
			{
				return;
			}
		}
		mUsers.add(user);
	}
	
	/** 移除活动参与者
	 * @param user
	 * 
	 */
	public void removeUser(MyUser user)
	{
		if(null == user)
		{
			throw new IllegalArgumentException("removeUser user can not be null");
		}
		if(null != mUsers)
		{
			for(MyUser itemUser:mUsers)
			{
				if(itemUser.mID.equals(user.mID))
				{
					mUsers.remove(itemUser);
				}
			}
		}
		
	}	
	public static JSONObject toJSON(ActivityData cb)
	{
		JSONObject obj = new JSONObject();
		try {
			if(null != cb.mCB)
			{
				obj.put("business", ComplexBusiness.toJSON(cb.mCB));
			}
			if(null != cb.mBeginDate)
			{
				obj.put("date", DateFormat.format(dataPattern, cb.mBeginDate));				
			}

			if(null!=cb.mUsers)
			{
				JSONArray array = new JSONArray();
				for(int i = 0; i < cb.mUsers.size(); i++)
				{
					array.put(MyUser.toJSON(cb.mUsers.get(i)));
				}	
				obj.put("users", array);
			}

			if(null != cb.mID)
			{
				obj.put("id", cb.mID);
			}
			
			obj.put("spent", cb.mSpent);
			obj.put("state", cb.mStatus);
			obj.put("creator", MyUser.toJSON(cb.mCreator));
			obj.put("title", cb.mTitle);
			obj.put("content", cb.mContent);
			obj.put("spent_type", cb.mSpentType);
			if(cb.mGroupID != null)
			{
				obj.put("groupID", cb.mGroupID);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			obj = null;
		}
		return obj;
	}
	
	public static ActivityData fromJSON(JSONObject obj)
	{
		ActivityData data = new ActivityData();
		try {
			if(obj.has("business"))
			{
				data.mCB = ComplexBusiness.fromJSON(obj.getJSONObject("business"));
			}
			data.mSpent = obj.getInt("spent");
			data.mStatus = obj.getInt("state");
			data.mCreator = MyUser.fromJSON(obj.getJSONObject("creator"));
			data.mContent = obj.getString("content");
			data.mTitle = obj.getString("title");
			data.mSpentType = obj.getInt("spent_type");
			if(obj.has("id"))
			{
				data.mID = obj.getString("id");
			}
			if(obj.has("users"))
			{
				JSONArray array = obj.getJSONArray("users");
				for(int i=0; i<array.length(); i++)
				{
					MyUser user = MyUser.fromJSON(array.getJSONObject(i));
					data.mUsers.add(user);
				}				
			}

			data.mBeginDate = new SimpleDateFormat(dataPattern).parse(obj.getString("date"));
			
			if(obj.has("groupID"))
			{
				data.mGroupID = obj.getString("groupID");
			}
		} catch (ParseException e) {
			e.printStackTrace();
			data = null;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			data = null;
		}
		return data;
	}
	
	public static ActivityData createTestData()
	{
		String test_title = "讨论聚会app";//"测试 war3 2v2 "+ new SimpleDateFormat(ActivityData.dataPattern_test).format(new Date());
		ComplexBusiness testCB = new ComplexBusiness();
		testCB.mName = "东来顺";
		testCB.mImgUrl = "http://i2.dpfile.com//pc//e6801a8a0b89fa2dd93e582c69d2e7cd(700x700)//thumb.jpg";
		testCB.mPhoneNumber = "12345678";
		
		MyUser testUser = UserManager.getInstance().getCurrentUser();
		ArrayList<MyUser> users = new ArrayList<MyUser>();
		MyUser testGuest = MyServerManager.getInstance().getUserInfo("rlk");
		users.add(testGuest);
			try {
				ActivityData data = new ActivityData.ActivityBuilder().setBeginTime(new SimpleDateFormat(ActivityData.dataPattern).parse("2014年12月31日16时")
						).setComplexBusiness(testCB).setCreator(testUser).
						setInviteUsers(users).setTitle(test_title).setContent("中午一起吃个吃饭。\n，讨论一下聚会app的事儿。").
						setGroupID("testGroup0").setSpentType(0).
						create();
				return data;
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	}

	public static class ActivityBuilder
	{
		private ActivityData mData;
		public ActivityBuilder()
		{
			mData = new ActivityData();
		}
		
		public ActivityBuilder setComplexBusiness(ComplexBusiness cb)
		{
			mData.mCB = cb;
			return this;
		}
		
		public ActivityBuilder setUsers(ArrayList<MyUser> users)
		{
			mData.mUsers.addAll(users);
			return this;
		}
		public ActivityBuilder setInviteUsers(ArrayList<MyUser> users)
		{
			mData.mInvitingUsers.addAll(users);
			return this;
		}		
		public ActivityBuilder setCreator(MyUser user)
		{
			mData.mCreator = user;
			return this;
		}
		
		public ActivityBuilder setSpentMoney(int money)
		{
			mData.mSpent = money;
			return this;
		}
		
		public ActivityBuilder setState(int state)
		{
			Assert.assertTrue(state<=CANCELED&&state>=BEGIN);
			mData.mStatus = state;
			return this;
		}
		
		public ActivityBuilder setBeginTime(Date beginTime)
		{
			Assert.assertNotNull(beginTime);
			mData.mBeginDate = beginTime;
			return this;
		}
		
		public ActivityBuilder setContent(String content)
		{
			Assert.assertNotNull(content);
			mData.mContent = content;
			return this;
		}

		public ActivityBuilder setTitle(String title)
		{
			Assert.assertNotNull(title);
			mData.mTitle = title;
			return this;
		}	
		
		public ActivityBuilder setID(String ID)
		{
			Assert.assertNotNull(ID);
			mData.mID = ID;
			return this;
		}
		
		public ActivityBuilder setGroupID(String groupID)
		{
			Assert.assertNotNull(groupID);
			mData.mGroupID = groupID;
			return this;			
		}
		
		public ActivityBuilder setSpentType(int value)
		{
			Assert.assertTrue(0<=value && 2>=value);
			mData.mSpentType = value;
			return this;			
		}
		
		public ActivityData create()
		{
			//Assert.assertNotNull("ActivityData must have ID!", mData.mID);
			Assert.assertNotNull("ActivityData must have Title!",mData.mTitle);
			Assert.assertNotNull("ActivityData must have Content!",mData.mContent);
			Assert.assertNotNull("ActivityData must have Begin Date!",mData.mBeginDate);
			Assert.assertNotNull("ActivityData must have Creator!",mData.mCreator);
			//Assert.assertNotNull("ActivityData must have Users!", mData.mUsers);
//			if(mData.mUsers.size()>1)
//			{
//				Assert.assertNotNull("Multiple ActivityData must have GroupID!", mData.mGroupID);
//			}
			return mData;
		}
	}
}
