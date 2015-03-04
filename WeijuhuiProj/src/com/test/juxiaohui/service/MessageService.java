package com.test.juxiaohui.service;

import java.util.ArrayList;

import com.test.juxiaohui.R;
import com.test.juxiaohui.activity.MessagesActivity;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.message.MyMessage;
import com.test.juxiaohui.domain.MyServerManager;
import com.test.juxiaohui.domain.UserManager;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

/**
 * 用户消息服务，应该在用户登录成功后启动
 * @author yh
 *
 */
public class MessageService extends Service {

	ArrayList<MyMessage> mMessages = new ArrayList<MyMessage>();
	public class LocalService extends Binder
	{
		public MessageService getService()
		{
			return MessageService.this;
		}
	}
	
	LocalService mBinder = new LocalService();
	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}
	
	@Override
	public void onCreate()
	{
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
				{
					ArrayList<MyMessage> messages;
					MyUser user = UserManager.getInstance().getCurrentUser();
					/**/
					if(null != user)
					{
						messages = MyServerManager.getInstance().getMessages(user.mID);
						if(null!=messages)
						{
							mMessages = messages;
							//MyServerManager.getInstance().removeMessages(user, messages);		
							NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
							for(MyMessage message : messages)
							{
								 Intent intent = new Intent(MessageService.this, MessagesActivity.class);
								 
								 intent.putExtra("Message", message.toJSON().toString());
								 PendingIntent pi = PendingIntent.getActivity(MessageService.this, MessagesActivity.REQ_NOTIFICATION, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
								 Notification noti = new Notification.Builder(MessageService.this)
						         .setContentTitle("new Message")
						         .setContentText(message.toString(MessageService.this))
						         .setSmallIcon(R.drawable.ic_launcher)
						         .setTicker("new Message")
						         .setDefaults(Notification.DEFAULT_SOUND)
						         .setContentIntent(pi)
						         .setAutoCancel(true)
						         .build();
								 nm.notify("test", 0, noti);
							}

						}						
					}
					try {
						Thread.sleep(30000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}				// TODO Auto-generated method stub
				
			}
		});
		t.start();
		
	}
	
	public ArrayList<MyMessage> getMessages()
	{
		return mMessages;
	}

}
