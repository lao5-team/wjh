package com.test.juxiaohui.data.comment;

import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.domain.MyServerManager;

public class ActivityComment extends Comment {

	public String mActivityID;
	
	public static ActivityComment fromJSON(JSONObject json)	
	{
		ActivityComment comment = new ActivityComment();
		try {
			if(json.has("id"))
			{
				comment.mID = json.getString("id");
			}
			comment.mContent = json.getString("content");
			comment.mActivityID = json.getString("activity_id");
			comment.mUserName = json.getString("user_name");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			comment = null;
		}		
		return comment;
	}
	
	@Override
	public JSONObject toJSON() {
		JSONObject obj = new JSONObject();
		try {
			if(null != this.mID)
			{
				obj.put("id", this.mID);
			}
			obj.put("content", this.mContent);
			obj.put("activity_id", this.mActivityID);
			obj.put("user_name", this.mUserName);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see com.test.juxiaohui.data.comment.Comment#checkDeleteAuthority(java.lang.String)
	 * 检查是否有权限进行删除评论，如果评论是该用户发的，则有权限。如果活动是该用户创建的，也有权限
	 */
	@Override
	public boolean checkDeleteAuthority(String userName) {
		ActivityData activityData = MyServerManager.getInstance().getActivity(mActivityID);
		if(mUserName.equals(userName))
			return true;
		else if(activityData.mCreator.mID.equals(userName))
			return true;
		else 
			return false;
	}
	
	

}
