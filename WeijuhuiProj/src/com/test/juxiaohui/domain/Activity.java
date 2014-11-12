package com.test.juxiaohui.domain;

import junit.framework.Assert;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.message.ActivityMessage;
import com.test.juxiaohui.data.message.MyMessage;

/**
 * 用来进行活动相关处理的类，该类大部分方法都会涉及到网络通信，应该放到线程中调用
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
		Assert.assertTrue(mData.mState == ActivityData.UNBEGIN);
		mData.mState = ActivityData.BEGIN;
		mData.mID = MyServerManager.getInstance().createActivity(mData);
		
		/*该代码是为了增强数据的完整性*/
		for(int i=0; i<mData.mUsers.size(); i++)
		{
			MyUser user = MyServerManager.getInstance().getUserInfo(mData.mUsers.get(i).mName);
			mData.mUsers.set(i, user);  
		}
		
		//创建者添加活动
		MyServerManager.getInstance().addUserActivity(mData.mCreator.mID, mData.mID, "doingActivity");
		
		//向被邀请者发送消息
		ActivityMessage message = new ActivityMessage();
		message.mType = "activity";
		message.mActivityID = mData.mID;
		message.mAction = "invite";
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
	public void confirmActivity()
	{
		Assert.assertTrue(mData.mState == ActivityData.BEGIN);
		mData.mState = ActivityData.PROCESSING;
		
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
	 * 同意加入
	 */
	public void acceptActivity()
	{
		for(com.test.juxiaohui.data.MyUser user: mData.mUsers)
		{
			if(user.mName.equals(DemoApplication.getInstance().getUserName()))
			{
				//user.mActivityState = com.test.juxiaohui.data.MyUser.CONFIRMED;
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
//		TextMessageBody txtBody = new TextMessageBody(ActivityData.toJSON(data).toString());
//		message.addBody(txtBody);
//		message.setReceipt(data.mUsers.get(0).mName);
//		conversation.addMessage(message);
//		try {
//			EMChatManager.getInstance().sendMessage(message);
//		} catch (EaseMobException e) {
//			e.printStackTrace();
//		}
//		MyMessage msg = new MyMessage.MessageBuilder().setType("activity").setAction(action)
//				.setData(ActivityData.toJSON(data)).create();
//		if(data.mCreator.mName.equals(DemoApplication.getInstance().getUserName()))
//		{
//			MessageManager.getInstance().sendMessagetoSingle(msg, data.mUsers.get(0).mName);
//		}
//		else
//		{
//			MessageManager.getInstance().sendMessagetoSingle(msg, data.mCreator.mName);
//		}
		
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
//		Assert.assertTrue(data.mGroupID!=null);
//		MyMessage msg = new MyMessage.MessageBuilder().setType("activity").setAction(action)
//				.setData(ActivityData.toJSON(data)).create();
//		String[] names = new String[data.mUsers.size()];
//		for(int i=0; i<data.mUsers.size(); i++)
//		{
//			names[i] = data.mUsers.get(i).mName;
//		}
//		MessageManager.getInstance().sendMessagetoGroup(msg, data.mGroupID);
	}

}
