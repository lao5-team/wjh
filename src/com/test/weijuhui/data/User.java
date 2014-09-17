package com.test.weijuhui.data;

import java.io.Serializable;

import org.json.JSONException;
import org.json.JSONObject;

public class User implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204887748188714747L;
	public String mName;
	public String mSex;
	
	public static JSONObject toJSON(User user)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", user.mName);
			obj.put("sex", user.mSex);
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return obj;

	}
	
	public static User fromJSON(JSONObject obj)
	{
		
		try {
			User user = new User();
			user.mName = obj.getString("name");
			user.mSex = obj.getString("sex");
			return user;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
}
