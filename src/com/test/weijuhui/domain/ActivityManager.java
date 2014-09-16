package com.test.weijuhui.domain;

import java.util.ArrayList;
import java.util.Date;

import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;

public class ActivityManager {
	private static ActivityManager mInstance = null;
	private ArrayList<ActivityData> mActivities = null;
	private ArrayList<DataChangedListener> mListeners;
	

	
	
	public static interface DataChangedListener
	{
		public void onDataChanged();
	}
	
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
		mListeners = new ArrayList<ActivityManager.DataChangedListener>();
	}
	
	public void addActivity(ActivityData activity)
	{
		mActivities.add(activity);
		notifyDataChanged();
	}
	
	public void removeActivity(ActivityData activity)
	{
		mActivities.remove(activity);
		notifyDataChanged();
	}
	
	public ArrayList<ActivityData> getActivities()
	{
		return (ArrayList<ActivityData>) mActivities.clone();
	}
	
	public void registerDataChangedListener(DataChangedListener listener)
	{
		if(null != listener)
		{
			mListeners.add(listener);
		}
	}
	
	public void unregisterDataChangedListener(DataChangedListener listener)
	{
		if(null != listener && mListeners.contains(listener))
		{
			mListeners.remove(listener);
		}
	}
	
	private void notifyDataChanged()
	{
		for(int i=0; i<mListeners.size(); i++)
		{
			mListeners.get(i).onDataChanged();
		}
	}

	
	
}
