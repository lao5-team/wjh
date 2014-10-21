package com.test.weijuhui.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class MyUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204887748188714747L;
	public String mName;
	public String mSex = "female";
	public String mImgUrl = "";
	/**
	 * 用户的聚会状态，包括待确定 {@link UNCONFIRMED}，确定{@link CONFIRMED}
	 * 报名，待支付
	 */
	public int mActivityState = UNCONFIRMED;
	/**
	 * 待确定状态
	 */
	public static final int UNCONFIRMED = 0;
	
	/**
	 * 已确认状态
	 */
	public static final int CONFIRMED = 1;
	
	
	public static JSONObject toJSON(MyUser user)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", user.mName);
			obj.put("sex", user.mSex);
			obj.put("state", user.mActivityState);
			obj.put("imgUrl", user.mImgUrl);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return obj;

	}
	
	public static MyUser fromJSON(JSONObject obj)
	{
		try {
			MyUser user = new MyUser();
			user.mName = obj.getString("name");
			user.mSex = obj.getString("sex");
			user.mActivityState = obj.getInt("state");
			user.mImgUrl = obj.getString("imgUrl");
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
