package com.test.juxiaohui.service;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.activity.LoginActivity;
import com.test.juxiaohui.domain.UserManager;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class StateMonitorService extends Service {

	private long mCurrentTime = 0;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	UserManager.LoginStateListener mLoginStateListener = new UserManager.LoginStateListener() {
		
		@Override
		public void onDisconnected() {
			long newTime = System.currentTimeMillis();
			if((newTime-mCurrentTime)>10000)
			{
				DemoApplication.getInstance().logout();
				Intent intent = new Intent(DemoApplication.applicationContext, LoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				DemoApplication.applicationContext.startActivity(intent);
				
			}
			mCurrentTime = newTime;
			
		}
	};
	
	@Override
	public void onCreate()
	{
		UserManager.getInstance().registerStateListener(mLoginStateListener);
	}

}
