package com.test.weijuhui.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMContactListener;
import com.easemob.chat.EMContactManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.util.HanziToPinyin;
import com.test.weijuhui.Constant;
import com.test.weijuhui.DemoApplication;
import com.test.weijuhui.R;
import com.test.weijuhui.activity.EntryActivity;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.InviteMessgeDao;
import com.test.weijuhui.data.UserDao;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.ActivityManager;
import com.test.weijuhui.domain.InviteMessage;
import com.test.weijuhui.domain.User;
import com.test.weijuhui.domain.InviteMessage.InviteMesageStatus;
import com.test.weijuhui.receiver.NewActivityBroadcastReceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class ContactChangeService extends Service {

    static final String TAG = "weijuhui";
	NotificationManager mNM;
	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	private IBinder mBinder = new Binder(){
		public ContactChangeService getService()
		{
			return ContactChangeService.this;
		}
	};
	
	private EMContactListener MyContactListener = new EMContactListener()
	{
		@Override
		public void onContactRefused(String arg0) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onContactInvited(String username, String reason) {
			Log.v("weijuhui", "有新的好友申请");
			showInvitedNotification(username);

			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getGroupId() == null
						&& inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			Log.d(TAG, username + "请求加你为好友,reason: " + reason);
			// 设置相应status
			msg.setStatus(InviteMesageStatus.BEINVITEED);
			inviteMessgeDao.saveMessage(msg);
			// 未读数加1
			User user = DemoApplication.getInstance().getContactList().get(Constant.NEW_FRIENDS_USERNAME);
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
		}
		
		@Override
		public void onContactDeleted(List<String> usernameList) {
			// 被删除
			Map<String, User> localUsers = DemoApplication.getInstance()
					.getContactList();
			for (String username : usernameList) {
				localUsers.remove(username);
				userDao.deleteContact(username);
				inviteMessgeDao.deleteMessage(username);
			}

		}
		
		@Override
		public void onContactAgreed(String username) {
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(username)) {
					return;
				}
			}
			// 自己封装的javabean
			InviteMessage msg = new InviteMessage();
			msg.setFrom(username);
			msg.setTime(System.currentTimeMillis());
			Log.d(TAG, username + "同意了你的好友请求");
			msg.setStatus(InviteMesageStatus.BEAGREED);
			
		}
		
		@Override
		public void onContactAdded(List<String> usernameList) {
			// 保存增加的联系人
			Map<String, User> localUsers = DemoApplication.getInstance()
					.getContactList();
			Map<String, User> toAddUsers = new HashMap<String, User>();
			for (String username : usernameList) {
				User user = new User();
				user.setUsername(username);
				String headerName = null;
				if (!TextUtils.isEmpty(user.getNick())) {
					headerName = user.getNick();
				} else {
					headerName = user.getUsername();
				}
				if (username.equals(Constant.NEW_FRIENDS_USERNAME)) {
					user.setHeader("");
				} else if (Character.isDigit(headerName.charAt(0))) {
					user.setHeader("#");
				} else {
					user.setHeader(HanziToPinyin.getInstance()
							.get(headerName.substring(0, 1)).get(0).target
							.substring(0, 1).toUpperCase());
					char header = user.getHeader().toLowerCase().charAt(0);
					if (header < 'a' || header > 'z') {
						user.setHeader("#");
					}
				}
				// 暂时有个bug，添加好友时可能会回调added方法两次
				if (!localUsers.containsKey(username)) {
					userDao.saveContact(user);
				}
				toAddUsers.put(username, user);
			}
			localUsers.putAll(toAddUsers);

		}
	};
	
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String username = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);
			// 如果是群聊消息，获取到group id
			if (message.getChatType() == ChatType.GroupChat) {
				username = message.getTo();
			}
			ActivityData data;
	        if(message.getType() == EMMessage.Type.TXT)
	        {
	        	ComplexBusiness cb = new ComplexBusiness();
	        	try {
					JSONObject obj = new JSONObject(((TextMessageBody)message.getBody()).getMessage());
	            	data = ActivityData.fromJSON(obj);
	    			ActivityManager.getInstance().addActivity(data);
	    			showNotification(data.mCB.mName);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	
				abortBroadcast();
	        }
		}
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    // We want this service to continue running until it is explicitly
	    // stopped, so return sticky.
		EMContactManager.getInstance().setContactListener(MyContactListener);
	    return START_STICKY;
	}
	
	@Override
	public void onCreate() {
		Log.v("weijuhui", "ActivityService onCreate");
		inviteMessgeDao = new InviteMessgeDao(this);
		userDao = new UserDao(this);
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		NewMessageBroadcastReceiver receiver;
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		intentFilter.setPriority(5);
		registerReceiver(receiver, intentFilter);
	}

	@Override
	public void onDestroy() {
	//	mNM.cancel(0);
		Log.v("weijuhui", "Activity Service onDestory");

	}

	/**
	 * When binding to the service, we return an interface to our messenger for
	 * sending messages to the service.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification(String activityName) {
		Notification notification = new Notification(R.drawable.logo_uidemo,
				"有新的聚会邀请", System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, EntryActivity.class), 0);

		notification.setLatestEventInfo(this,
				"有新的聚会邀请", activityName, contentIntent);

		mNM.notify(0, notification);
	}
	
	private void showInvitedNotification(String str)
	{
		Notification notification = new Notification(R.drawable.logo_uidemo,
				"新的好友邀请", System.currentTimeMillis());

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, EntryActivity.class), 0);

		notification.setLatestEventInfo(this,
				"新的好友邀请", String.format("%s请求添加您为好友", str), contentIntent);

		mNM.notify(0, notification);
	}

}
