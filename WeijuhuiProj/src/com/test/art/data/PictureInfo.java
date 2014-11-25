package com.test.art.data;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class PictureInfo {
	
	public String mUrl;
	public String mUserName;
	public ArrayList<CommentInfo> mCommentList;
	public String mID;
	public String mTitle;
	
	public static PictureInfo fromJSON(JSONObject jsonObj)
	{
		PictureInfo info = null;
		try {
			info = new PictureInfo();
			info.mUrl = jsonObj.getString("url");
			if(jsonObj.has("_id"))
			{
				info.mID = jsonObj.getString("_id");
			}
			//info.mID = jsonObj.getString("_id");
			info.mTitle = jsonObj.getString("title");
			info.mUserName = jsonObj.getString("userName");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return info;
	}
	
	public static JSONObject toJSON(PictureInfo info)
	{
		JSONObject jsonObj = new JSONObject();
		try {
			jsonObj.put("url", info.mUrl);
			if(info.mID != null)
			{
				jsonObj.put("_id", info.mID);
			}
			jsonObj.put("title", info.mTitle);
			jsonObj.put("userName", info.mUserName);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jsonObj;
	}
}
