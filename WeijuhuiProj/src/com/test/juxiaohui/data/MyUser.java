package com.test.juxiaohui.data;

import java.io.Serializable;
import java.util.ArrayList;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.comment.Comment;
import com.test.juxiaohui.data.message.ActivityMessage;
import com.test.juxiaohui.domain.MyServerManager;
import com.test.juxiaohui.domain.UserManager;

public class MyUser implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7204887748188714747L;
	public static String CREATOR = "creator";
	public static String JOINER = "joiner";
	public String mName;
	public String mPassword;
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

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return user;
	}
	
	/**
	 * 发起活动。此方法会进行网络通信，且不会涉及任何UI的操作，所以建议放在子线程中调用
	 * 
	 */
	public void startActivity(ActivityData data)
	{
		//Assert.assertTrue(mData.mState == ActivityData.UNBEGIN);
		//mData.mState = ActivityData.BEGIN;
		data.mID = MyServerManager.getInstance().createActivity(data);
		
		/*该代码是为了增强数据的完整性*/
		for(int i=0; i<data.mUsers.size(); i++)
		{
			MyUser user = MyServerManager.getInstance().getUserInfo(data.mUsers.get(i).mName);
			data.mUsers.set(i, user);  
		}
		
		//自己正在进行的活动+1
		MyServerManager.getInstance().addUserActivity(UserManager.getInstance().getCurrentUser().mID, data.mID, "doing_activity");
		
		//向被邀请者发送消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = data.mID;
		message.mAction = ActivityMessage.ACTION_INVITE;
		message.mActivityName = data.mTitle;
		message.mFromUser = UserManager.getInstance().getCurrentUser();
		
		for(MyUser user:data.mUsers)
		{
			MyServerManager.getInstance().sendMessage(user.mID, message);
		}
		
	}
	
	/**
	 * 确认活动开始，进入进行中状态
	 */
//	public void confirmActivity()
//	{
//		//Assert.assertTrue(mData.mState == ActivityData.BEGIN);
//		//mData.mState = ActivityData.PROCESSING;
//		
//		if(mData.mUsers.size() == 1)
//		{
//			sendActivityToSingle(mData, "update");
//		}
//		else if(mData.mUsers.size() > 1)
//		{
//			sendActivityToGroup(mData, "update");
//		}	
//		ActivityManager.getInstance().updateActivity(this);
//		
//	}
	
	/**
	 * 取消活动
	 * @param data 活动数据
	 */
	public void dismissActivity(ActivityData data)
	{
		Assert.assertTrue(data.mStatus == ActivityData.BEGIN);
		
		//设置活动状态
		data.mStatus = ActivityData.CANCELED;
		
		//删除活动
		MyServerManager.getInstance().removeActivity(data.mID);
		
		//向活动中的所有成员发送消息
		ActivityMessage aMessage = new ActivityMessage.MessageBuilder()
		.setAction(ActivityMessage.ACTION_DISMISS)
		.setActivityID(data.mID)
		.setActivityTitle(data.mTitle)
		.setFromUser(UserManager.getInstance().getCurrentUser()).create();
		for(MyUser user:data.mUsers)
		{
			MyServerManager.getInstance().sendMessage(user.mID, aMessage);
		}
		
		//所有活动成员，正在进行的活动-1
		for(MyUser user:data.mUsers)
		{
			MyServerManager.getInstance().removeUserActivity(user.mID, data.mID, "doing_activity");
		}		
	}
	
	/**
	 * 完成活动
	 * @param data 活动数据
	 */
	public void finishActivity(ActivityData data)
	{
		//活动状态设置为完成
		data.mStatus = ActivityData.FINISH;
		MyServerManager.getInstance().updateActivity(data, data.mID);
				
		//发送完成消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = data.mID;
		message.mAction = ActivityMessage.ACTION_FINISH;
		message.mActivityName = data.mTitle;
		message.mFromUser = UserManager.getInstance().getCurrentUser();
		for(MyUser user:data.mUsers)
		{
			MyServerManager.getInstance().sendMessage(user.mID, message);
		}
		
		//将活动移至完成活动列表中
		MyServerManager.getInstance().moveActivity(data.mCreator.mID, data.mID, "doing_activity", "finish_activity");
		for(MyUser user:data.mUsers)
		{
			MyServerManager.getInstance().moveActivity(user.mID, data.mID, "doing_activity", "finish_activity");
		}		
	}
	
	/**
	 * 同意加入
	 * @param data 活动数据
	 */
	public void acceptActivity(ActivityData data)
	{
		//向创建者即消息发送者返回消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = data.mID;
		message.mAction = "accept_invite";
		message.mActivityName = data.mTitle;
		message.mFromUser = UserManager.getInstance().getCurrentUser();
		MyServerManager.getInstance().sendMessage(data.mCreator.mID, message);
		
		//自己正在进行的活动+1
		MyServerManager.getInstance().addUserActivity(UserManager.getInstance().getCurrentUser().mID, message.mActivityID, "doing_activity");
		
		//活动参与者添加自己
		data.addUser(UserManager.getInstance().getCurrentUser());
		MyServerManager.getInstance().updateActivity(data, data.mID);
	}
	
	/**
	 * 拒绝加入活动
	 * @param data 活动数据
	 */
	public void refuseActivity(ActivityData data)
	{
		//向创建者即消息发送者返回消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = data.mID;
		message.mAction = ActivityMessage.ACTION_REFUSE_INVITE;
		message.mActivityName = data.mTitle;
		message.mFromUser = UserManager.getInstance().getCurrentUser();
		MyServerManager.getInstance().sendMessage(data.mCreator.mID, message);	
		
		//活动参与者移除自己
		data.removeUser(UserManager.getInstance().getCurrentUser());
		MyServerManager.getInstance().updateActivity(data, data.mID);	
		
		
	}
	
	/**
	 * 中途退出活动
	 * @param data 活动数据
	 */
	public void quitActivity(ActivityData data)
	{
		//向创建者即消息发送者返回消息
		ActivityMessage message = new ActivityMessage.MessageBuilder()
		.setAction(ActivityMessage.ACTION_QUIT)
		.setActivityID(data.mID)
		.setActivityTitle(data.mTitle)
		.setFromUser(this)
		.create();
		MyServerManager.getInstance().sendMessage(data.mCreator.mID, message);	
		
		//活动参与者移除自己
		data.removeUser(UserManager.getInstance().getCurrentUser());
		MyServerManager.getInstance().updateActivity(data, data.mID);	
		
		//自己的活动列表中-1
		MyServerManager.getInstance().removeUserActivity(this.mID, data.mID, "doing_activity");
	}
	
	/**
	 * 返回自己在当前聚会的角色
	 * @param data 活动数据
	 * @return Creator 活动创建者, Joiner参与者
	 * 
	 */
	public String getMyRoleType(ActivityData data)
	{
		String currentName = DemoApplication.getInstance().getUserName();
		if(data.mCreator.mName.equals(currentName))
		{
			return CREATOR;
		}
		else
		{
			return JOINER;
		}
	}
	
	/**
	 * 发表一个评论
	 * @param comment
	 * @return 评论的ID
	 */
	public String postComment(Comment comment)
	{		
		comment.mUserName = mName;
		return MyServerManager.getInstance().addComment(comment);
	}
	
	/** 删除一个评论
	 * @param commentID
	 */
	public void removeComment(String commentID)
	{
		Comment comment = MyServerManager.getInstance().getComment(commentID);
		if(null != comment)
		{
			if(true == comment.checkDeleteAuthority(mID))
			{
				MyServerManager.getInstance().removeComment(commentID);
			}
		}
	}
	
}
