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

import com.test.weijuhui.DemoApplication;
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
		loadFromFile();
	}
	
	public void addActivity(ActivityData activity)
	{
		mActivities.add(activity);
		notifyDataChanged();
		saveToFile();
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
	
	private void saveToFile()
	{
		try {
			FileWriter fw = new FileWriter(DemoApplication.getInstance().getCacheDir() + File.separator + "activities.txt");
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			for(int i=0; i<mActivities.size(); i++)
			{
				array.put(ActivityData.toJSON(mActivities.get(i)));
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
					mActivities.add(data);
				}				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}

	
	
}
