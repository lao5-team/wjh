package com.test.juxiaohui.service;

import java.util.ArrayList;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.R;
import com.test.juxiaohui.data.message.MyMessage;
import com.test.juxiaohui.domain.MyServerManager;

import android.app.Notification;
import android.app.NotificationManager;
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

	public class LocalService extends Binder
	{
		MessageService getService()
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
		while(true)
		{
			ArrayList<MyMessage> messages;
			messages = MyServerManager.getInstance().getMessages(DemoApplication.getInstance().getUser());
			NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
			for(MyMessage message : messages)
			{
				 Notification noti = new Notification.Builder(this)
		         .setContentTitle("new Message")
		         .setContentText(message.toString(this))
		         .setSmallIcon(R.drawable.ic_launcher)
		         .build();
				nm.notify("test", 0, noti);
			}
		}
	}

}
