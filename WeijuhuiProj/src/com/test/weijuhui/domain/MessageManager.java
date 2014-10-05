package com.test.weijuhui.domain;

import java.util.ArrayList;

import junit.framework.Assert;
import android.util.Log;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.exceptions.EaseMobException;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.Message;

/**
 * @author yh
 * 消息发送和接受的管理者
 * @see Message
 */
public class MessageManager {

	private static MessageManager mInstance = null;
	private ArrayList<MessageListener> mMsgListeners;
	
	public static abstract class MessageListener
	{
		public String filterType = null;
		abstract public void onReceiveMessage(Message msg);
	}
	
	private MessageManager()
	{
		mMsgListeners = new ArrayList<MessageManager.MessageListener>();
	}
	
	public static MessageManager getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new MessageManager();
		}
		return mInstance;
	}
	
	public boolean sendMessagetoSingle(Message msg, String username)
	{
		Assert.assertNotNull(username);
		Assert.assertNotNull(msg);
		EMConversation conversation = EMChatManager.getInstance().getConversation(username);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		String str = Message.toJSON(msg).toString();
		Log.v("weijuhui", "Message to single: " + str);
		TextMessageBody txtBody = new TextMessageBody(str);
		message.addBody(txtBody);
		message.setReceipt(username);
		conversation.addMessage(message);
		try {
			EMChatManager.getInstance().sendMessage(message);
			return true;
		} catch (EaseMobException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean sendMessagetoGroup(Message msg, String[] usernames)
	{
		EMGroup group;
		try {
			group = EMGroupManager.getInstance().createPrivateGroup("", "", usernames, false);
			EMConversation conversation = EMChatManager.getInstance().getConversation(group.getGroupId());										
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			message.setChatType(ChatType.GroupChat);
			String str = Message.toJSON(msg).toString();
			Log.v("weijuhui", "Message to group " + str);			
			TextMessageBody txtBody = new TextMessageBody(str);
			message.addBody(txtBody);
		    message.setReceipt(group.getGroupId());
		    conversation.addMessage(message);
			try {
				EMChatManager.getInstance().sendMessage(message);
				return true;
			} catch (EaseMobException e) {
				e.printStackTrace();
				return false;
			}
		} catch (EaseMobException e) {
			e.printStackTrace();
			return false;
		}		
	}
	
	public void receiveMessage(Message msg)
	{
		for(MessageListener listener:mMsgListeners)
		{
			if(msg.mType.equals(listener.filterType))
			{
				listener.onReceiveMessage(msg);
			}
		}
	}
	
	public void addMessageListener(MessageListener listener)
	{
		mMsgListeners.add(listener);
	}
	
	public void removeMessageListener(MessageListener listener)
	{
		mMsgListeners.remove(listener);
	}
}
