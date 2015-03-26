package com.pineapple.juxiaohui.activity;

import java.util.ArrayList;

import com.pineapple.juxiaohui.R;
import com.pineapple.juxiaohui.adapter.MessageAdapter;
import com.pineapple.juxiaohui.adapter.NotificationMessageAdapter;
import com.pineapple.juxiaohui.data.message.MyMessage;
import com.pineapple.juxiaohui.service.MessageService;
import com.pineapple.juxiaohui.service.MessageService.LocalService;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.ListView;

/**
 * @author yh
 * 显示消息列表的Activity
 */
public class MessagesActivity extends Activity {

	public static final int REQ_NOTIFICATION = 0;
	ListView mListView;
	NotificationMessageAdapter mAdapter;
	ArrayList<MyMessage> mMessages;
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messagesactivity);
		mListView = (ListView)findViewById(R.id.listView_messages);
		mAdapter = new NotificationMessageAdapter(this);
		mListView.setAdapter(mAdapter);
		ServiceConnection conn = new ServiceConnection() {
			
			@Override
			public void onServiceDisconnected(ComponentName name) {
				
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				mMessages = ((LocalService)service).getService().getMessages();
				mAdapter.addMessages(mMessages);
				mAdapter.notifyDataSetChanged();
			}
		};
		bindService(new Intent(MessagesActivity.this, MessageService.class), conn, Context.BIND_AUTO_CREATE);
	}
}
