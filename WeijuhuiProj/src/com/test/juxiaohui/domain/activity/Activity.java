package com.test.juxiaohui.domain.activity;

import junit.framework.Assert;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.exceptions.EaseMobException;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.message.ActivityMessage;
import com.test.juxiaohui.data.message.MyMessage;
import com.test.juxiaohui.domain.MessageManager;
import com.test.juxiaohui.domain.MyServerManager;

/**
 * 用来进行活动相关处理的类，该类大部分方法都会涉及到网络通信，建议放到线程中调用
 * @author yh
 *
 */
public class Activity {
	public static String CREATOR = "creator";
	public static String JOINER = "joiner";
	ActivityData mData;
	
    Activity(ActivityData data)
	{
		Assert.assertNotNull(data);
		mData = data;
	}
	
	/**
	 * 发起活动。此方法会进行网络通信，且不会涉及任何UI的操作，所以建议放在子线程中调用
	 * 
	 */
	public void startActivity()
	{
		//Assert.assertTrue(mData.mState == ActivityData.UNBEGIN);
		//mData.mState = ActivityData.BEGIN;
		mData.mID = MyServerManager.getInstance().createActivity(mData);
		
		/*该代码是为了增强数据的完整性*/
		for(int i=0; i<mData.mUsers.size(); i++)
		{
			MyUser user = MyServerManager.getInstance().getUserInfo(mData.mUsers.get(i).mName);
			mData.mUsers.set(i, user);  
		}
		
		//自己正在进行的活动+1
		MyServerManager.getInstance().addUserActivity(DemoApplication.getInstance().getUser().mID, mData.mID, "doing_activity");
		
		//向被邀请者发送消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = mData.mID;
		message.mAction = ActivityMessage.ACTION_INVITE;
		message.mActivityName = mData.mTitle;
		message.mFromUser = DemoApplication.getInstance().getUser();
		
		for(MyUser user:mData.mUsers)
		{
			MyServerManager.getInstance().sendMessage(user, message);
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
	 */
	public void dismissActivity()
	{
		Assert.assertTrue(mData.mStatus == ActivityData.BEGIN);
		
		//设置活动状态
		mData.mStatus = ActivityData.CANCELED;
		
		//删除活动
		MyServerManager.getInstance().removeActivity(mData.mID);
		
		//向活动中的所有成员发送消息
		ActivityMessage aMessage = new ActivityMessage.MessageBuilder()
		.setAction(ActivityMessage.ACTION_DISMISS)
		.setActivityID(mData.mID)
		.setActivityTitle(mData.mTitle)
		.setFromUser(DemoApplication.getInstance().getUser()).create();
		for(MyUser user:mData.mUsers)
		{
			MyServerManager.getInstance().sendMessage(user, aMessage);
		}
		
		//所有活动成员，正在进行的活动-1
		for(MyUser user:mData.mUsers)
		{
			MyServerManager.getInstance().removeUserActivity(user.mID, mData.mID, "doing_activity");
		}		
//		if(mData.mUsers.size() == 1)
//		{
//			sendActivityToSingle(mData, "cancel");
//		}
//		else if(mData.mUsers.size() > 1)
//		{
//			sendActivityToGroup(mData, "cancel");
//		}
	}
	
	/**
	 * 完成活动
	 */
	public void finishActivity()
	{
		//活动状态设置为完成
		mData.mStatus = ActivityData.FINISH;
		MyServerManager.getInstance().updateActivity(mData, mData.mID);
				
		//发送完成消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = mData.mID;
		message.mAction = ActivityMessage.ACTION_FINISH;
		message.mActivityName = mData.mTitle;
		message.mFromUser = DemoApplication.getInstance().getUser();
		for(MyUser user:mData.mUsers)
		{
			MyServerManager.getInstance().sendMessage(user, message);
		}
		
		//将活动移至完成活动列表中
		MyServerManager.getInstance().moveActivity(mData.mCreator.mID, mData.mID, "doing_activity", "finish_activity");
		for(MyUser user:mData.mUsers)
		{
			MyServerManager.getInstance().moveActivity(user.mID, mData.mID, "doing_activity", "finish_activity");
		}		
	}
	
	/**
	 * 同意加入
	 */
	public void acceptActivity()
	{
		//向创建者即消息发送者返回消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = mData.mID;
		message.mAction = "accept_invite";
		message.mActivityName = mData.mTitle;
		message.mFromUser = DemoApplication.getInstance().getUser();
		MyServerManager.getInstance().sendMessage(mData.mCreator, message);
		
		//自己正在进行的活动+1
		MyServerManager.getInstance().addUserActivity(DemoApplication.getInstance().getUser().mID, message.mActivityID, "doing_activity");
		
		//活动参与者添加自己
		mData.addUser(DemoApplication.getInstance().getUser());
		MyServerManager.getInstance().updateActivity(mData, mData.mID);
	}
	
	/**
	 * 拒绝加入活动
	 */
	public void refuseActivity()
	{
		//向创建者即消息发送者返回消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = mData.mID;
		message.mAction = ActivityMessage.ACTION_REFUSE_INVITE;
		message.mActivityName = mData.mTitle;
		message.mFromUser = DemoApplication.getInstance().getUser();
		MyServerManager.getInstance().sendMessage(mData.mCreator, message);	
		
		//活动参与者移除自己
		mData.removeUser(DemoApplication.getInstance().getUser());
		MyServerManager.getInstance().updateActivity(mData, mData.mID);	
		
		
	}
	
	/**
	 * 中途退出活动
	 */
	public void quitActivity()
	{
		
	}
	
	/**
	 * 返回自己在当前聚会的角色
	 * @return Creator, Joiner
	 */
	public String getMyRoleType()
	{
		String currentName = DemoApplication.getInstance().getUserName();
		if(mData.mCreator.mName.equals(currentName))
		{
			return CREATOR;
		}
		else
		{
			return JOINER;
		}
	}
	
	public int getState()
	{
		return mData.mStatus;
	}
	
	public ActivityData getData()
	{
		return mData;
	}
	
	/*  通过环信的通道发送消息，现在已经废弃
	private void sendActivityToSingle(ActivityData data, String action)
	{
		EMConversation conversation = EMChatManager.getInstance().getConversation(data.mUsers.get(0).mName);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
		message.addBody(txtBody);
		message.setReceipt(data.mUsers.get(0).mName);
		conversation.addMessage(message);
		try {
			EMChatManager.getInstance().sendMessage(message);
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		MyMessage msg = new MyMessage.MessageBuilder().setType("activity").setAction(action)
				.setData(ActivityData.toJSON(data)).create();
		if(data.mCreator.mName.equals(DemoApplication.getInstance().getUserName()))
		{
			MessageManager.getInstance().sendMessagetoSingle(msg, data.mUsers.get(0).mName);
		}
		else
		{
			MessageManager.getInstance().sendMessagetoSingle(msg, data.mCreator.mName);
		}
		
	}
	
	private void sendActivityToGroup(ActivityData data, String action)
	{
		EMGroup group;
		try {
			String[] names = new String[data.mUsers.size()];
			for(int i=0; i<data.mUsers.size(); i++)
			{
				names[i] = data.mUsers.get(i).mName;
			}
			group = EMGroupManager.getInstance().createPrivateGroup("", "", names, false);
			EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());										
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			message.setChatType(ChatType.GroupChat);
			TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
			message.addBody(txtBody);
		    message.setReceipt(group.getGroupId());
		    conversation.addMessage(message);
			try {
				EMChatManager.getInstance().sendMessage(message);
			} catch (EaseMobException e) {
				e.printStackTrace();
			}
		} catch (EaseMobException e) {
			e.printStackTrace();
		}
		Assert.assertTrue(data.mGroupID!=null);
		MyMessage msg = new MyMessage.MessageBuilder().setType("activity").setAction(action)
				.setData(ActivityData.toJSON(data)).create();
		String[] names = new String[data.mUsers.size()];
		for(int i=0; i<data.mUsers.size(); i++)
		{
			names[i] = data.mUsers.get(i).mName;
		}
		MessageManager.getInstance().sendMessagetoGroup(msg, data.mGroupID);
	}
	*/

}
