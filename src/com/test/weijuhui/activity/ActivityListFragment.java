package com.test.weijuhui.activity;

import java.util.ArrayList;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.test.weijuhui.R;
import com.test.weijuhui.adapter.ActivityAdapter;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.ActivityManager;
import com.test.weijuhui.domain.ActivityManager.DataChangedListener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ActivityListFragment extends Fragment {
	//Data
	private ActivityAdapter mAdapter;
	
	//UI
	ImageView mIvAdd;
	ListView mListActivities;
	
	//
	private Handler mUIHandler;
	
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
    			abortBroadcast();
            }

//			mAdapter.setData(ActivityManager.getInstance().getActivities());
//			mAdapter.notifyDataSetChanged();
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		initData();
		
		mUIHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				updateUI();
			}
		};
		
		return initUI(inflater);
	}
	
	private void initData()
	{
		mAdapter = new ActivityAdapter(getActivity());
		mAdapter.setData(ActivityManager.getInstance().getActivities());
		ActivityManager.getInstance().registerDataChangedListener(new DataChangedListener() {
			
			@Override
			public void onDataChanged() {
				mAdapter.setData(ActivityManager.getInstance().getActivities());
				mAdapter.notifyDataSetChanged();
			}
		});
		
		NewMessageBroadcastReceiver receiver;
		receiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(5);
		getActivity().registerReceiver(receiver, intentFilter);
	}
	
	private View initUI(LayoutInflater inflater)
	{
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_activity_list, null);
		mIvAdd = (ImageView) layout.findViewById(R.id.iv_new_activity);
		mIvAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent =new  Intent();
				intent.setClass(getActivity(), CreateActivityActivity.class);
				ActivityListFragment.this.startActivity(intent);
			}
		});
		mListActivities = (ListView) layout.findViewById(R.id.list);
		mListActivities.setAdapter(mAdapter);
		return layout;
	}
	
	private void updateUI()
	{
		
	}
}
