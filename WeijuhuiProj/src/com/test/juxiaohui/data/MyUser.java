package com.test.juxiaohui.data;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MyUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204887748188714747L;
	public String mName;
	public String mSex = "male";
	public String mImgUrl = "";
	public ArrayList<String> mDoingActivities = new ArrayList<String>();
	public ArrayList<String> mFinishedActivities = new ArrayList<String>();
	public String mID;
	/**
	 * 用户的聚会状态，包括待确定 {@link UNCONFIRMED}，确定{@link CONFIRMED}
	 * 报名，待支付
	 */
	//public int mActivityState = UNCONFIRMED;
	/**
	 * 待确定状态
	 */
	//public static final int UNCONFIRMED = 0;
	
	/**
	 * 已确认状态
	 */
	//public static final int CONFIRMED = 1;
	
	
	public static JSONObject toJSON(MyUser user)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", user.mName);
			obj.put("sex", user.mSex);
			//obj.put("state", user.mActivityState);
			obj.put("imgUrl", user.mImgUrl);
			if(user.mID != null)
			{
				obj.put("id", user.mID);
			}
			
			/*put doing activities*/
			//JSONArray doingActivities = new JSONArray(user.mDoingActivities);
			//obj.put("doingActivities", doingActivities);
			
			/*put finished activities*/
			//JSONArray finishedActivities = new JSONArray(user.mFinishedActivities);
			//obj.put("finishedActivities", finishedActivities);
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return obj;

	}
	
	public static MyUser fromJSON(JSONObject obj)
	{
		MyUser user = new MyUser();
		try {
			user.mName = obj.getString("name");
			user.mSex = obj.getString("sex");
			//user.mActivityState = obj.getInt("state");
			user.mImgUrl = obj.getString("imgUrl");
			if(obj.has("id"))
			{
				user.mID = obj.getString("id");
			}
			/*get doing activities*/
//			JSONArray doingActivities = obj.getJSONArray("doingActivities");
//			for(int i=0; i<doingActivities.length(); i++)
//			{
//				user.mDoingActivities.add(doingActivities.getString(i));
//			}
			
			/*get finished activities*/
//			JSONArray finishedActivities = obj.getJSONArray("finishedActivities");
//			for(int i=0; i<finishedActivities.length(); i++)
//			{
//				user.mFinishedActivities.add(finishedActivities.getString(i));
//			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
}
