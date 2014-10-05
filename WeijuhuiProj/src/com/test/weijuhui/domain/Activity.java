package com.test.weijuhui.domain;

import junit.framework.Assert;

import com.test.weijuhui.DemoApplication;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.Message;

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
	 * 发起活动，进入待确定状态
	 */
	public void startActivity()
	{
		Assert.assertTrue(mData.mState == ActivityData.UNBEGIN);
		mData.mState = ActivityData.BEGIN;
		if(mData.mUsers.size() == 1)
		{
			sendActivityToSingle(mData, "create");
		}
		else if(mData.mUsers.size() > 1)
		{
			sendActivityToGroup(mData, "create");
		}
		ActivityManager.getInstance().addActivity(this);
	}
	
	/**
	 * 确认活动开始，进入进行中状态
	 */
	public void confirmActivity()
	{
		Assert.assertTrue(mData.mState == ActivityData.BEGIN);
		mData.mState = ActivityData.PROCESSING;
		
	}
	
	/**
	 * 取消活动
	 */
	public void cancelActivity()
	{
		Assert.assertTrue(mData.mState != ActivityData.UNBEGIN||mData.mState != ActivityData.END);
		mData.mState = ActivityData.CANCELED;
		if(mData.mUsers.size() == 1)
		{
			sendActivityToSingle(mData, "cancel");
		}
		else if(mData.mUsers.size() > 1)
		{
			sendActivityToGroup(mData, "cancel");
		}
	}
	
	/**
	 * 完成活动
	 */
	public void finishActivity()
	{
		Assert.assertTrue(mData.mState == ActivityData.PROCESSING);
		mData.mState = ActivityData.END;
	}
	
	/**
	 * 同意加入
	 */
	public void acceptActivity()
	{
		for(com.test.weijuhui.data.User user: mData.mUsers)
		{
			if(user.mName.equals(DemoApplication.getInstance().getUserName()))
			{
				user.mActivityState = com.test.weijuhui.data.User.CONFIRMED;
			}
		}
		if(mData.mUsers.size() == 1)
		{
			sendActivityToSingle(mData, "update");
		}
		else if(mData.mUsers.size() > 1)
		{
			sendActivityToGroup(mData, "update");
		}	
		ActivityManager.getInstance().updateActivity(this);
	}
	
	/**
	 * 拒绝加入活动
	 */
	public void refuseActivity()
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
		return mData.mState;
	}
	
	public ActivityData getData()
	{
		return mData;
	}
	
	private void sendActivityToSingle(ActivityData data, String action)
	{
//		EMConversation conversation = EMChatManager.getInstance().getConversation(data.mUsers.get(0).mName);
//		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//		Log.v("weijuhui", ActivityData.toJSON(data).toString());
//		TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
//		message.addBody(txtBody);
//		message.setReceipt(data.mUsers.get(0).mName);
//		conversation.addMessage(message);
//		try {
//			EMChatManager.getInstance().sendMessage(message);
//		} catch (EaseMobException e) {
//			e.printStackTrace();
//		}
		Message msg = new Message.MessageBuilder().setType("activity").setAction(action)
				.setData(ActivityData.toJSON(data)).create();
		MessageManager.getInstance().sendMessagetoSingle(msg, data.mUsers.get(0).mName);
	}
	
	private void sendActivityToGroup(ActivityData data, String action)
	{
//		EMGroup group;
//		try {
//			String[] names = new String[data.mUsers.size()];
//			for(int i=0; i<data.mUsers.size(); i++)
//			{
//				names[i] = data.mUsers.get(i).mName;
//			}
//			group = EMGroupManager.getInstance().createPrivateGroup("", "", names, false);
//			EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());										
//			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
//			message.setChatType(ChatType.GroupChat);
//			TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
//			message.addBody(txtBody);
//		    message.setReceipt(group.getGroupId());
//		    conversation.addMessage(message);
//			try {
//				EMChatManager.getInstance().sendMessage(message);
//			} catch (EaseMobException e) {
//				e.printStackTrace();
//			}
//		} catch (EaseMobException e) {
//			e.printStackTrace();
//		}
		Message msg = new Message.MessageBuilder().setType("activity").setAction(action)
				.setData(ActivityData.toJSON(data)).create();
		String[] names = new String[data.mUsers.size()];
		for(int i=0; i<data.mUsers.size(); i++)
		{
			names[i] = data.mUsers.get(i).mName;
		}
		MessageManager.getInstance().sendMessagetoGroup(msg, names);
	}

}
