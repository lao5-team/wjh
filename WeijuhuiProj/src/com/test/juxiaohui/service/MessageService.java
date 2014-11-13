package com.test.juxiaohui.service;

import java.util.ArrayList;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.R;
import com.test.juxiaohui.data.MyUser;
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
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(true)
				{
					ArrayList<MyMessage> messages;
					MyUser user = DemoApplication.getInstance().getUser();
					/**/
					if(null != user)
					{
						messages = MyServerManager.getInstance().getMessages(user);
						if(null!=messages)
						{
							//MyServerManager.getInstance().removeMessages(user, messages);		
							NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
							for(MyMessage message : messages)
							{
								 Notification noti = new Notification.Builder(MessageService.this)
						         .setContentTitle("new Message")
						         .setContentText(message.toString(MessageService.this))
						         .setSmallIcon(R.drawable.ic_launcher)
						         .setDefaults(Notification.DEFAULT_SOUND)
						         .build();
								 nm.notify("test", 0, noti);
							}

						}						
					}
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}				
				}				// TODO Auto-generated method stub
				
			}
		});
		t.start();
		
	}

}
