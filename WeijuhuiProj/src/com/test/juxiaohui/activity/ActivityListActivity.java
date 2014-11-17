package com.test.juxiaohui.activity;

import java.util.ArrayList;

import com.test.juxiaohui.R;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.domain.activity.ActivityManager;
import com.test.juxiaohui.domain.activity.IActivityLoader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

/**
 * @author yh
 *  需要在intent 中传递字符串"user_id","type"
 */
public class ActivityListActivity extends FragmentActivity {
	
	public static final String TYPE_DOING = "doing";
	public static final String TYPE_FINISH = "finish";
	
	private String mUserID = null;
	private String mType = null;
	private ActivityListFragment mFragment;
	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		initData();
		initUI();
	}
	
	private void initData()
	{
		Intent intent = getIntent();
		mUserID = intent.getStringExtra("user_id");
		mType = intent.getStringExtra("type");

		
	}
	
	private void initUI()
	{
		setContentView(R.layout.activity_activitylist);
		mFragment = new ActivityListFragment(new IActivityLoader() {
			
			@Override
			public ArrayList<ActivityData> getActivityList() {
				if(mType.endsWith(TYPE_DOING))
				{
					return ActivityManager.getInstance().getDoingActivities(mUserID);
				}
				if(mType.endsWith(TYPE_FINISH))
				{
					return ActivityManager.getInstance().getFinishActivities(mUserID);
				}
				return null;
			}
		});
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment)
		.show(mFragment).commit();
	}
}
