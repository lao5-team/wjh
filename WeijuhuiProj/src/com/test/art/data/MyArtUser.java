package com.test.art.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class MyArtUser extends MyUser {
	public static final String ARTIST = "artist";
	public static final String TEACHER = "teacher";
	
	public ArrayList<String> mPictureList;
	
	public static JSONObject toJSON(MyArtUser user)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("name", user.mName);
			obj.put("sex", user.mSex);
			//obj.put("state", user.mActivityState);
			obj.put("imgUrl", user.mImgUrl);
			//obj.put("type", user.mType);
			if(user.mID != null)
			{
				obj.put("id", user.mID);
			}			
			
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		return obj;
	}
	
	public static MyArtUser fromJSON(JSONObject jsonObj)
	{
		MyArtUser user = new MyArtUser();
		try {
			user.mName = jsonObj.getString("name");
			user.mSex = jsonObj.getString("sex");
			//user.mActivityState = obj.getInt("state");
			user.mImgUrl = jsonObj.getString("imgUrl");
			//user.mType = jsonObj.getString("type");
			if(jsonObj.has("id"))
			{
				user.mID = jsonObj.getString("id");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	public void uploadPictureInfo(PictureInfo picInfo)
	{
		
	}
}
