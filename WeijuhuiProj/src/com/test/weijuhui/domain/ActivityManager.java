package com.test.weijuhui.domain;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.test.weijuhui.DemoApplication;
import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.Message;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.domain.MessageManager.MessageListener;

public class ActivityManager {
	private static ActivityManager mInstance = null;
	private ArrayList<Activity> mActivities = null;
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
		mActivities = new ArrayList<Activity>();
		mListeners = new ArrayList<ActivityManager.DataChangedListener>();
		MessageListener msglistener = new MessageListener() {
			
			@Override
			public void onReceiveMessage(Message msg) {
				ActivityData data = ActivityData.fromJSON(msg.mData);
				
				if(null != data)
				{
					Activity activity = createActivity(data);
					if(msg.mAction.equals("create"))
					{
						addActivity(activity);
					}
					else if(msg.mAction.equals("update"))
					{
						updateActivity(activity);
					}
					Log.v("weijuhui", "ActivityManager receive activity");
				}
			}
		};
		msglistener.filterType = "activity";
		MessageManager.getInstance().addMessageListener(msglistener);
		loadFromFile();
	}
	
	public void addActivity(Activity activity)
	{
		mActivities.add(activity);
		notifyDataChanged();
		saveToFile();
	}
	
	public void removeActivity(Activity activity)
	{
		mActivities.remove(activity);
		notifyDataChanged();
	}
	
	public void updateActivity(Activity activity)
	{
		for(int i=0; i<mActivities.size(); i++)
		{
			if(mActivities.get(i).mData.mID.equals(activity.mData.mID))
			{
				mActivities.set(i, activity);
			}
		}
		notifyDataChanged();
		saveToFile();
	}
	
	public int getActivitySize()
	{
		return mActivities.size();
	}
	
	public Activity getActivity(int pos)
	{
		return mActivities.get(pos);
	}
	
//	public ArrayList<Activity> getActivities()
//	{
//		return mActivities;
//	}
	
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
	
	/** 创建一个新的活动
	 * @param data 活动数据,不允许为空。 {@link ActivityData}
	 * @return 返回新的活动 {@link Activity}
	 */
	public Activity createActivity(ActivityData data)
	{
		Activity activity = new Activity(data);
		return activity;
	}
	
	private void notifyDataChanged()
	{
		for(int i=0; i<mListeners.size(); i++)
		{
			mListeners.get(i).onDataChanged();
		}
	}
	
	private void saveToFile()
	{
		try {
			FileWriter fw = new FileWriter(DemoApplication.getInstance().getCacheDir() + File.separator + "activities.txt");
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			for(int i=0; i<mActivities.size(); i++)
			{
				array.put(ActivityData.toJSON(mActivities.get(i).mData));
			}
			try {
				obj.put("activities", array);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			fw.write(obj.toString());
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadFromFile()
	{
		try {
			FileReader fr = new FileReader(DemoApplication.getInstance().getCacheDir() + File.separator + "activities.txt");
			String str = "";
			char[]buffer = new char[256];
			int readSize = 0;
			do
			{
				readSize = fr.read(buffer);
				str += String.valueOf(buffer);
			}
			while(readSize > 0);
			JSONObject obj;
			try {
				obj = new JSONObject(str);
				JSONArray array = obj.getJSONArray("activities");
				for(int i=0; i<array.length(); i++)
				{
					ActivityData data = ActivityData.fromJSON(array.getJSONObject(i));
					mActivities.add(createActivity(data));
				}				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public void clearActivityTest()
	{
		File file = new File(DemoApplication.getInstance().getCacheDir() + File.separator + "activities.txt");
		file.delete();
		mActivities.clear();
		notifyDataChanged();
	}

	
	
}
