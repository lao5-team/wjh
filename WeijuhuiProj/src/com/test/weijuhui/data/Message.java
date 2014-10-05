package com.test.weijuhui.data;

import java.io.Serializable;

import junit.framework.Assert;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author yh
 * 
 * 本应用使用的消息数据
 */
public class Message implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1130070227842759145L;

	/**
	 * 定义消息类型，"activity"(数据用于活动)，“conversation”（数据用于对话，暂时不需要）
	 */
	public String mType;
	/**
	 * 定义消息行为 创建者可以使用"create","update","cancel","finish"
                                  参与者可以使用"agree","refuse"
	 */
	public String mAction;
	/**
	 * 定义消息数据
	 */
	public JSONObject mData;
	
	private Message()
	{
		mType = null;
		mAction = null;
		mData = null;
	}
	
	public static JSONObject toJSON(Message msg)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("type", msg.mType);
			obj.put("action", msg.mAction);
			obj.put("data", msg.mData);
			return obj;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static Message fromJSON(JSONObject json)
	{
		Message msg = new Message();
		try {
			msg.mType = json.getString("type");
			msg.mAction = json.getString("action");
			msg.mData = json.getJSONObject("data");
			return msg;
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	
	public static class MessageBuilder
	{
		private Message mMessage;
		public MessageBuilder()
		{
			mMessage = new Message();
		}
		
		public MessageBuilder setType(String type)
		{
			mMessage.mType = type;
			return this;
		}
		
		public MessageBuilder setAction(String action)
		{
			mMessage.mAction = action;
			return this;
		}
		
		public MessageBuilder setData(JSONObject data)
		{
			mMessage.mData = data;
			return this;
		}
		
		public Message create()
		{
			Assert.assertNotNull(mMessage.mType);
			Assert.assertNotNull(mMessage.mAction);
			Assert.assertNotNull(mMessage.mData);
			return mMessage;
		}
	}
	
}
