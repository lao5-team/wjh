package com.test.art.activity;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.test.art.DemoApplication;
import com.test.art.R;
import com.test.art.adapter.ActivityAdapter;
import com.test.art.data.ActivityData;
import com.test.art.data.DianpingDao.ComplexBusiness;
import com.test.art.domain.activity.ActivityManager;
import com.test.art.domain.activity.ActivityManager.DataChangedListener;
import com.test.art.domain.activity.IActivityLoader;

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
	private IActivityLoader mLoader;
	private ActivityAdapter mAdapter;
	
	//UI
	ImageView mIvAdd;
	ListView mListActivities;
	ImageView mIvClear;
	
	public ActivityListFragment(IActivityLoader loader)
	{
		if(null==loader)
		{
			throw new IllegalArgumentException("IActivityLoader can not be null!");
		}
		mLoader = loader;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		initData();
		
		return initUI(inflater);
	}
	
	private void initData()
	{
		mAdapter = new ActivityAdapter(this, mLoader);
		//mAdapter.setData(ActivityManager.getInstance().getActivities());
//		ActivityManager.getInstance().registerDataChangedListener(new DataChangedListener() {
//			
//			@Override
//			public void onDataChanged() {
//				//mAdapter.setData(ActivityManager.getInstance().getActivities());
//				getActivity().runOnUiThread(new Runnable() {
//					
//					@Override
//					public void run() {
//						mAdapter.notifyDataSetChanged();
//					}
//				});
//				
//			}
//		});
	}
	
	private View initUI(LayoutInflater inflater)
	{
		LinearLayout layout = (LinearLayout) inflater.inflate(R.layout.fragment_activity_list, null);
		mIvAdd = (ImageView) layout.findViewById(R.id.iv_new_activity);
		mIvAdd.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent intent = new Intent();
				intent.setClass(getActivity(), CreateActivityActivity2.class);
				CreateActivityActivity2.IntentBuilder ib = new CreateActivityActivity2.IntentBuilder(intent);
				ib.setUseType(CreateActivityActivity2.USE_CREATE);
				ActivityListFragment.this.startActivityForResult(intent, 0);
			}
		});
		mListActivities = (ListView) layout.findViewById(R.id.list);
		mListActivities.setAdapter(mAdapter);
		
		mIvClear = (ImageView) layout.findViewById(R.id.iv_clear_activity);
		mIvClear.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityManager.getInstance().clearActivityTest();
			}
		});		
		return layout;
	}
	
	@Override
	public void onResume ()
	{
		//mAdapter.setData(ActivityManager.getInstance().getActivities());
		mAdapter.notifyDataSetChanged();
		super.onResume();
	}
	
}
