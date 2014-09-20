package com.test.weijuhui.service;

import java.util.ArrayList;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.test.weijuhui.R;
import com.test.weijuhui.activity.EntryActivity;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.ActivityManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.widget.Toast;

public class ActivityService extends Service {

	NotificationManager mNM;
	/** Keeps track of all current registered clients. */
	ArrayList<Messenger> mClients = new ArrayList<Messenger>();
	/** Holds last value set by a client. */
	int mValue = 0;

	/**
	 * Command to the service to register a client, receiving callbacks from the
	 * service. The Message's replyTo field must be a Messenger of the client
	 * where callbacks should be sent.
	 */
	public static final int MSG_REGISTER_CLIENT = 1;

	/**
	 * Command to the service to unregister a client, ot stop receiving
	 * callbacks from the service. The Message's replyTo field must be a
	 * Messenger of the client as previously given with MSG_REGISTER_CLIENT.
	 */
	public static final int MSG_UNREGISTER_CLIENT = 2;

	/**
	 * Command to service to set a new value. This can be sent to the service to
	 * supply a new value, and will be sent by the service to any registered
	 * clients with the new value.
	 */
	public static final int MSG_SET_VALUE = 3;

	/**
	 * Handler of incoming messages from clients.
	 */
	class IncomingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_REGISTER_CLIENT:
				mClients.add(msg.replyTo);
				break;
			case MSG_UNREGISTER_CLIENT:
				mClients.remove(msg.replyTo);
				break;
			case MSG_SET_VALUE:
				mValue = msg.arg1;
				for (int i = mClients.size() - 1; i >= 0; i--) {
					try {
						mClients.get(i).send(
								Message.obtain(null, MSG_SET_VALUE, mValue, 0));
					} catch (RemoteException e) {
						// The client is dead. Remove it from the list;
						// we are going through the list from back to front
						// so this is safe to do inside the loop.
						mClients.remove(i);
					}
				}
				break;
			default:
				super.handleMessage(msg);
			}
		}
	}

	/**
	 * Target we publish for clients to send messages to IncomingHandler.
	 */
	final Messenger mMessenger = new Messenger(new IncomingHandler());
	
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
			// conversation =
			// EMChatManager.getInstance().getConversation(toChatUsername);
			// 通知adapter有新消息，更新ui
			// 记得把广播给终结掉
			ActivityData data;
            if(message.getType() == EMMessage.Type.TXT)
            {
            	//data.mCB.mName = ((TextMessageBody)message.getBody()).getMessage();
            	ComplexBusiness cb = new ComplexBusiness();
            	cb.mName = ((TextMessageBody)message.getBody()).getMessage();
            	data = new ActivityData.ActivityBuilder().setComplexBusiness(cb).create();
    			ActivityManager.getInstance().addActivity(data);
    			showNotification(cb.mName);
    			abortBroadcast();
            }

//			mAdapter.setData(ActivityManager.getInstance().getActivities());
//			mAdapter.notifyDataSetChanged();
		}
	}

	@Override
	public void onCreate() {
//		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//
//		
//		NewMessageBroadcastReceiver receiver;
//		receiver = new NewMessageBroadcastReceiver();
//		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
//		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
//		intentFilter.setPriority(5);
//		registerReceiver(receiver, intentFilter);
	}

	@Override
	public void onDestroy() {
	//	mNM.cancel(0);

	}

	/**
	 * When binding to the service, we return an interface to our messenger for
	 * sending messages to the service.
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification(String activityName) {
		// In this sample, we'll use the same text for the ticker and the
		// expanded notification

		// Set the icon, scrolling text and timestamp
		Notification notification = new Notification(R.drawable.logo_uidemo,
				"有新的聚会邀请", System.currentTimeMillis());

		// The PendingIntent to launch our activity if the user selects this
		// notification
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				new Intent(this, EntryActivity.class), 0);

		// Set the info for the views that show in the notification panel.
		notification.setLatestEventInfo(this,
				"有新的聚会邀请", activityName, contentIntent);

		// Send the notification.
		// We use a string id because it is a unique number. We use it later to
		// cancel.
		mNM.notify(0, notification);
	}

}
