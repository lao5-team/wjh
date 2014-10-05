package com.test.weijuhui.data;

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

import com.test.weijuhui.DemoApplication;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.data.DianpingDao.SimpleBusiness;
import com.test.weijuhui.data.User;

public class ActivityData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3368450357726232660L;
	
	/*
	 * 活动状态可选值
	 */
	public static int UNBEGIN = 0;
	public static int BEGIN = 1;
	public static int PROCESSING = 2;
	public static int END = 3;
	public static int CANCELED = 4;
	
	public String mID;
	public ComplexBusiness mCB;
	public Date mBeginDate;
	public ArrayList<User> mUsers;
	public int mSpent;
	/*
	 * 活动状态
	 */
	public int mState;
	public User mCreator;
	public final static String dataPattern = "yyyy年MM月dd日 hh时";
	public String mTitle;
	public String mContent;
	private ActivityData()
	{
		mUsers = new ArrayList<User>();
		mBeginDate = null;
		mTitle = "";
		mContent = "";
		mCreator = new User();
		mState = UNBEGIN;
		mCB = null;
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
				array.put(User.toJSON(cb.mUsers.get(i)));
			}
			obj.put("id", cb.mID);
			obj.put("users", array);
			obj.put("spent", cb.mSpent);
			obj.put("state", cb.mState);
			obj.put("creator", User.toJSON(cb.mCreator));
			obj.put("title", cb.mTitle);
			obj.put("content", cb.mContent);
		} catch (JSONException e) {
			e.printStackTrace();
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
			data.mCreator = User.fromJSON(obj.getJSONObject("creator"));
			data.mContent = obj.getString("content");
			data.mTitle = obj.getString("title");
			data.mID = obj.getString("id");
			JSONArray array = obj.getJSONArray("users");
			for(int i=0; i<array.length(); i++)
			{
				User user = User.fromJSON(array.getJSONObject(i));
				data.mUsers.add(user);
			}
			data.mBeginDate = new SimpleDateFormat(dataPattern).parse(obj.getString("date"));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
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
		
		public ActivityBuilder setUsers(ArrayList<User> users)
		{
			mData.mUsers = (ArrayList<User>)users.clone();
			return this;
		}
		
		public ActivityBuilder setCreator(User user)
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
			mData.mBeginDate = beginTime;
			return this;
		}
		
		public ActivityBuilder setContent(String content)
		{
			mData.mContent = content;
			return this;
		}

		public ActivityBuilder setTitle(String title)
		{
			mData.mTitle = title;
			return this;
		}	
		
		public ActivityBuilder setID(String ID)
		{
			mData.mID = ID;
			return this;
		}
		
		public ActivityData create()
		{
			return mData;
		}
	}
}
