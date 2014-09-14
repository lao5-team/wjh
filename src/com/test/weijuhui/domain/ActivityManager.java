package com.test.weijuhui.domain;

import java.util.ArrayList;

import com.test.weijuhui.data.ActivityData;

public class ActivityManager {
	private static ActivityManager mInstance = null;
	private ArrayList<ActivityData> mActivities = null;
	public static ActivityManager getInstance()
	{
		if(mInstance == null)
		{
			mInstance = new ActivityManager();
		}
		return mInstance;
	}
	
	private ActivityManager()
	{
		mActivities = new ArrayList<ActivityData>();
	}
	
	public void addActivity(ActivityData activity)
	{
		mActivities.add(activity);
	}
	
	public void removeActivity(ActivityData activity)
	{
		mActivities.remove(activity);
	}
	
	public ArrayList<ActivityData> getActivities()
	{
		return (ArrayList<ActivityData>) mActivities.clone();
	}

	
	
}
