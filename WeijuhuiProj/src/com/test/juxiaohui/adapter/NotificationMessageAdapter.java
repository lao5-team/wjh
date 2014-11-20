package com.test.juxiaohui.adapter;

import java.util.ArrayList;

import com.test.juxiaohui.R;
import com.test.juxiaohui.activity.ActivityDetailActivity;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.message.ActivityMessage;
import com.test.juxiaohui.data.message.MyMessage;
import com.test.juxiaohui.domain.MyServerManager;
import com.test.juxiaohui.domain.activity.Activity;
import com.test.juxiaohui.domain.activity.ActivityManager;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class NotificationMessageAdapter extends BaseAdapter {

	Context mContext = null;
	ArrayList<MyMessage> mMessages = null;
	
	public NotificationMessageAdapter(Context context)
	{
		if(null == context)
		{
			throw new IllegalArgumentException("context can not be null");
		}
		mContext = context;
	}
	
	public void setMessages(ArrayList<MyMessage> messages)
	{
		mMessages = messages;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(null != mMessages)
		{
			return mMessages.size();
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view = View.inflate(mContext, R.layout.item_message, null);
		TextView tv = (TextView) view.findViewById(R.id.textView_content);
		Button button = (Button)view.findViewById(R.id.button_confirm);
		Button btn_refuse = (Button)view.findViewById(R.id.button_refuse);
		final MyMessage message = mMessages.get(position);
		tv.setText(message.toString(mContext));
		tv.setClickable(true);
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(message instanceof ActivityMessage)
				{
					ActivityMessage aMessage = (ActivityMessage)message;
					Intent intent = new Intent(mContext, ActivityDetailActivity.class);
					intent.putExtra("activity id", aMessage.mActivityID);
					mContext.startActivity(intent);
				}

			}
		});
		if(message instanceof ActivityMessage)
		{
			ActivityData data = MyServerManager.getInstance().getActivity(((ActivityMessage)message).mActivityID);
			final Activity activity = ActivityManager.getInstance().createActivity(data);
			button.setText("接受");
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					activity.acceptActivity();
					mMessages.remove(message);
					((android.app.Activity)mContext).finish();
				}
			});
			btn_refuse.setText("拒绝");
			btn_refuse.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					activity.refuseActivity();
					mMessages.remove(message);
					((android.app.Activity)mContext).finish();
				}
			});
		}
		return view;
	}

}
