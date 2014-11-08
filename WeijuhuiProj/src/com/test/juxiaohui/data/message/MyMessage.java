package com.test.juxiaohui.data.message;

import java.io.Serializable;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;

/**
 * @author yh
 * 
 * 本应用使用的消息数据
 */
public abstract class MyMessage implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1130070227842759145L;

	/**
	 * 定义消息类型，"activity"(对应ActivityMessage)，“conversation”（数据用于对话，暂时不需要）
	 */
	public String mType;
	
	/**
	 * 消息的发送者
	 */
	public MyUser mFromUser; 
	

	
	protected MyMessage()
	{
		mType = null;
	}
	
	public static JSONObject toJSON(MyMessage msg)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", msg.mType);
			obj.put("from", MyUser.toJSON(msg.mFromUser));
			//obj.put("action", msg.mAction);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
//	abstract public static MyMessage fromJSON(JSONObject json);
//	{
//		MyMessage msg = new MyMessage();
//		try {
//			msg.mType = json.getString("type");
//			msg.mFromUser = MyUser.fromJSON(json.getJSONObject("from"));
//			//msg.mAction = json.getString("action");
//			return msg;
//		} catch (JSONException e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
	
	public String getNotifyString()
	{
		if(mType.equals("activity"))
		{
//			if(mAction.equals("create"))
//			{
//				return String.format("你的好友%s邀请您参加活动 %s", data.mCreator.mName, data.mTitle );
//			}
//			else if(mAction.equals("update"))
//			{
//				return String.format("活动%s有更新", data.mTitle);
//			}
		}
		return "未知消息";
	}
	
	abstract public String toString(Context context);
	
//	public static class MessageBuilder
//	{
//		private MyMessage mMessage;
//		public MessageBuilder()
//		{
//			//mMessage = new MyMessage();
//		}
//		
//		public MessageBuilder setType(String type)
//		{
//			mMessage.mType = type;
//			return this;
//		}
//		
////		public MessageBuilder setAction(String action)
////		{
////			mMessage.mAction = action;
////			return this;
////		}
//		
//		public MessageBuilder setData(JSONObject data)
//		{
//			//mMessage.mData = data;
//			return this;
//		}
//		
//		public MyMessage create()
//		{
//			Assert.assertNotNull(mMessage.mType);
//			//Assert.assertNotNull(mMessage.mAction);
//			//Assert.assertNotNull(mMessage.mData);
//			return mMessage;
//		}
//	}
	
}
