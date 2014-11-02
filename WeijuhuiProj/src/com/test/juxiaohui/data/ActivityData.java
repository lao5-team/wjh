package com.test.juxiaohui.data;

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

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.DianpingDao.ComplexBusiness;
import com.test.juxiaohui.data.DianpingDao.SimpleBusiness;

public class ActivityData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3368450357726232660L;
	
	/*活动状态可选值*/
	public final static int UNBEGIN = 0; //活动未开始，创建者还在填写活动信息，未发起的状态
	public final static int BEGIN = 1;   //活动开始，创建者已经发起，等待报名的状态
	public final static int PROCESSING = 2;  //活动进行中，指参与者已经报名和支付完成，正式开始的状态
	public final static int END = 3;   //完成，活动发起人点击完成
	public final static int CANCELED = 4;   //活动取消，发起人中途终止活动
	
	public final static String dataPattern = "yyyy年MM月dd日hh时";
	public String mID;
	public ComplexBusiness mCB;
	public Date mBeginDate;
	public ArrayList<MyUser> mUsers;  //活动参与者，注意不包括创建者
	public float mSpent;    //活动所需金额
	public String mGroupID;  //多人活动的groupID，供环信使用
	public int mState;       //活动状态
	public MyUser mCreator;  //活动创建者
	public String mTitle;    //活动标题
	public String mContent;  //活动内容介绍
	
	private ActivityData()
	{
		mUsers = null;
		mBeginDate = null;
		mTitle = null;
		mContent = null;
		mCreator = null;
		mState = UNBEGIN;
		mCB = null;
		mGroupID = null;
		mID = null;
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

			JSONArray array = new JSONArray();
			for(int i = 0; i < cb.mUsers.size(); i++)
			{
				array.put(MyUser.toJSON(cb.mUsers.get(i)));
			}
			if(null != cb.mID)
			{
				obj.put("id", cb.mID);
			}
			obj.put("users", array);
			obj.put("spent", cb.mSpent);
			obj.put("state", cb.mState);
			obj.put("creator", MyUser.toJSON(cb.mCreator));
			obj.put("title", cb.mTitle);
			obj.put("content", cb.mContent);
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
			data.mState = obj.getInt("state");
			data.mCreator = MyUser.fromJSON(obj.getJSONObject("creator"));
			data.mContent = obj.getString("content");
			data.mTitle = obj.getString("title");
			if(obj.has("id"))
			{
				data.mID = obj.getString("id");
			}
			JSONArray array = obj.getJSONArray("users");

			data.mUsers = new ArrayList<MyUser>();
			for(int i=0; i<array.length(); i++)
			{
				MyUser user = MyUser.fromJSON(array.getJSONObject(i));
				data.mUsers.add(user);
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
		ComplexBusiness testCB = new ComplexBusiness();
		testCB.mName = "东来顺";
		testCB.mImgUrl = "http://i2.dpfile.com//pc//e6801a8a0b89fa2dd93e582c69d2e7cd(700x700)//thumb.jpg";
		testCB.mPhoneNumber = "12345678";
		
		MyUser testUser = new MyUser();
		testUser.mName = "testCreator";
		
		ArrayList<MyUser> users = new ArrayList<MyUser>();
		MyUser testGuest = new MyUser();
		testGuest.mName = "test0";
		users.add(testGuest);
		
		testGuest = new MyUser();
		testGuest.mName = "test1";
		users.add(testGuest);
		
		testGuest = new MyUser();
		testGuest.mName = "test2";
		users.add(testGuest);	
		
			try {
				ActivityData data = new ActivityData.ActivityBuilder().setBeginTime(new SimpleDateFormat(ActivityData.dataPattern).parse("2014年1月1日16时")
						).setComplexBusiness(testCB).setCreator(testUser).setUsers(users).setTitle("test_title").setContent("test_content").setGroupID("testGroup0").create();
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
			mData.mUsers = (ArrayList<MyUser>)users.clone();
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
			Assert.assertTrue(state<=CANCELED&&state>=UNBEGIN);
			mData.mState = state;
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
		
		public ActivityData create()
		{
			//Assert.assertNotNull("ActivityData must have ID!", mData.mID);
			Assert.assertNotNull("ActivityData must have Title!",mData.mTitle);
			Assert.assertNotNull("ActivityData must have Content!",mData.mContent);
			Assert.assertNotNull("ActivityData must have Begin Date!",mData.mBeginDate);
			Assert.assertNotNull("ActivityData must have Creator!",mData.mCreator);
			Assert.assertNotNull("ActivityData must have Users!", mData.mUsers);
			if(mData.mUsers.size()>1)
			{
				Assert.assertNotNull("Multiple ActivityData must have GroupID!", mData.mGroupID);
			}
			return mData;
		}
	}
}
