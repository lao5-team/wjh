package com.test.juxiaohui.data.message;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.test.juxiaohui.R;
import com.test.juxiaohui.data.MyUser;

/**
 * @author yh
 * 聚会邀请，申请，接受，拒绝相关消息
 */
public class ActivityMessage extends MyMessage {

	private static final long serialVersionUID = -6511827765134217999L;

	/**
	 * 定义消息行为 创建者可以使用"invite","accept_apply","refuse_apply","quit"
	 *             分别表示邀请好友，接受陌生人申请，拒绝申请，退出聚会。
                                  参与者可以使用"apply","accept_invite","refuse_invite","quit"
     *             分别表示申请加入聚会，接受创建者邀请，拒绝邀请，退出聚会          
	 */
	public String mAction;
	
	/**
	 * 聚会ID
	 */
	public String mActivityID;
	
	/**
	 * 聚会名称，为了直观显示用
	 */
	public String mActivityName;
	
	public ActivityMessage()
	{
		mAction = null;
	}
	
	/** 消息转化为字符串
	 * @param context
	 * @return
	 */
	public String toString(Context context)
	{
		if(null != mAction && mAction.length()>0)
		{
			if(mAction.equals("invite"))
			{
				return context.getString(R.string.invite_message, mFromUser.mName, mActivityName);
			}
			if(mAction.equals("accept_invite"))
			{
				return context.getString(R.string.accept_invite_message, mFromUser.mName);
			}
			if(mAction.equals("refuse_invite"))
			{
				return context.getString(R.string.refuse_invite_message, mFromUser.mName);
			}			
			if(mAction.equals("apply"))
			{
				return context.getString(R.string.apply_message, mFromUser.mName, mActivityName);
			}
			if(mAction.equals("accept_apply"))
			{
				return context.getString(R.string.accept_apply_message, mFromUser.mName);
			}
			if(mAction.equals("refuse_apply"))
			{
				return context.getString(R.string.refuse_apply_message, mFromUser.mName);
			}			
		}
		else
		{
			throw new NullPointerException("ActivityMessage's mAction is null or is empty!");
		}

		return mAction;
		
	}
	
	
	
	/** 从JSONObject转化为ActivityMessage
	 * @param JSONObject 将要转化的JSONObject
	 * @return
	 */
	public static ActivityMessage fromJSON(JSONObject json)
	{
		try {
			ActivityMessage message = new ActivityMessage();
			message.mType = json.getString("type");
			message.mFromUser = MyUser.fromJSON(json.getJSONObject("from"));
			message.mAction = json.getString("action");
			message.mActivityID = json.getString("activity_id");
			message.mActivityName = json.getString("activity_name");
			return message;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	/**从ActivityMessage转化为JSONObject
	 * @param message
	 * @return
	 */
	public static JSONObject toJSON(ActivityMessage message)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", message.mType);
			obj.put("from", MyUser.toJSON(message.mFromUser));
			obj.put("action", message.mAction);
			obj.put("activity_id", message.mActivityID);
			obj.put("activity_name", message.mActivityName);
			//obj.put("action", msg.mAction);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
