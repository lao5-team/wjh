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

import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.data.DianpingDao.SimpleBusiness;
import com.test.weijuhui.data.User;

public class ActivityData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3368450357726232660L;
	
	public static int BEGIN = 0;
	public static int PROCESSING = 1;
	public static int END = 2;
	
	public ComplexBusiness mCB;
	public Date mBeginDate;
	public ArrayList<User> mUsers;
	public int mSpent;
	public int mState;
	public User mCreator;
	
	private ActivityData()
	{
		mUsers = new ArrayList<User>();
		mBeginDate = new Date(System.currentTimeMillis());
	}
	
	public static JSONObject toJSON(ActivityData cb)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("business", ComplexBusiness.toJSON(cb.mCB));
			if(null != cb.mBeginDate)
			{
				obj.put("date", DateFormat.format("yyyy年MM月dd日HH时", cb.mBeginDate));				
			}

			JSONArray array = new JSONArray();
			for(int i = 0; i < cb.mUsers.size(); i++)
			{
				array.put(cb.mUsers.get(i).mName);
			}
			obj.put("users", array);
			obj.put("spent", cb.mSpent);
			obj.put("state", cb.mState);
			obj.put("creator", User.toJSON(cb.mCreator));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static ActivityData fromJSON(JSONObject obj)
	{
		ActivityData data = new ActivityData();
		try {
			data.mBeginDate = new SimpleDateFormat("yyyy年MM月dd日HH时").parse(obj.getString("date"));//java.text.DateFormat.getDateTimeInstance().parse();
			data.mCB = ComplexBusiness.fromJSON(obj.getJSONObject("business"));
			data.mSpent = obj.getInt("spent");
			data.mState = obj.getInt("state");
			data.mCreator = User.fromJSON(obj.getJSONObject("creator"));
			JSONArray array = obj.getJSONArray("users");
			for(int i=0; i<array.length(); i++)
			{
				User user = new User();
				user.mName = array.getString(i);
				data.mUsers.add(user);
			}
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
			Assert.assertTrue(state<=END&&state>=BEGIN);
			mData.mState = state;
			return this;
		}
		
		public ActivityBuilder setBeginTime(Date beginTime)
		{
			mData.mBeginDate = beginTime;
			return this;
		}
		
		public ActivityData create()
		{
			return mData;
		}
	}
}
