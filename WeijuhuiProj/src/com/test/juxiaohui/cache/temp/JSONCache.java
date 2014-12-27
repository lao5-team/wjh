package com.test.juxiaohui.cache.temp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class JSONCache extends SQLiteOpenHelper implements IListCache<String, JSONObject> {
	public final static String dataPattern = "yyyy年MM月dd日hh时";
	private static final int TRANSACTION_LIMIT = 10;
	String mName;
	HashMap<String, JSONObject> mMap = new HashMap<String, JSONObject>();
	String mCreateTableString;
	
	public JSONCache(Context context, String name)
	{
		super(context, name + ".db" , null, 1);
		mName = name;
		
		mCreateTableString = "CREATE TABLE "
		+ mName + " ("
		+ "value" +" TEXT, "
		+ "id" + " TEXT PRIMARY KEY);";
		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<JSONObject> valueList = new ArrayList<JSONObject>();
		if(null!=this.getWritableDatabase())
		{
			loadAll(keyList, valueList);
		}
		
	}

//	@Override
//	public JSONObject get(String key) {
//		if(null == key)
//		{
//			throw new IllegalArgumentException("key is null!");
//		}		
//		JSONObject value = null;
//		if(mMap.containsKey(key))
//		{
//			value= mMap.get(key);
//		}
//		else
//		{
//			value = getByDB(key);
//			if(value != null)
//			{
//				mMap.put(key, value);
//			}
//		}
//		return value;
//	}



	
	@Override
	public void remove(String key) {
		if(null == key)
		{
			throw new IllegalArgumentException("key is null");
		}
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(mName, "id=?", new String[]{key});
		db.close();
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(mCreateTableString);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
	private void putByDB(String key, JSONObject value)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put("id", key);
		values.put("value", value.toString());
		if(db.isOpen()){
			db.insertWithOnConflict(mName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
		}
		db.close();
	}
	
	private JSONObject getByDB(String key)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(mName, new String[]{"value"}, "id=?", new String[]{key}, null, null, null);
		String value = null;
		JSONObject result = null;
		while(cursor.moveToNext())
		{
			value = cursor.getString(cursor.getColumnIndex("id"));
		}
		if(null != value)
		{
			try {
				result = new JSONObject(value);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		cursor.close();
		db.close();
		return result;
	}
	
	
	/** 向数据库写一个数组
	 * @param keyList
	 * @param valueList
	 */
	private void putListByDB(List<String> keyList, List<JSONObject> valueList)
	{
		if(keyList.size()<TRANSACTION_LIMIT)
		{
			for(int i=0; i<keyList.size(); i++)
			{
				put(keyList.get(i), valueList.get(i));
			}					
		}
		else
		{
			SQLiteDatabase db = this.getWritableDatabase();
			db.beginTransaction();
			for(int i=0; i<keyList.size(); i++)
			{
				ContentValues values = new ContentValues();
				values.put("id", keyList.get(i));
				values.put("value", valueList.get(i).toString());
				if(db.isOpen()){
					db.insertWithOnConflict(mName, null, values, SQLiteDatabase.CONFLICT_REPLACE);
				}
			}
			db.setTransactionSuccessful();
			db.endTransaction();
			db.close();
		}
	}
	
	
	/**从数据库中读取一个数据
	 * @param keyList
	 * @return
	 */
	private List<JSONObject> getListByDB(List<String> keyList) {
		if(null == keyList)
		{
			throw new IllegalArgumentException("keyList is null!");
		}
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.query(mName, new String[]{"value"}, "id=?", (String[]) keyList.toArray(), null, null, null);
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		while(cursor.moveToNext())
		{
			String value = cursor.getString(cursor.getColumnIndex("value"));
			try {
				JSONObject item = new JSONObject(value);
				result.add(item);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		db.close();
		cursor.close();
		return result;
	}

	/**
	 * 从数据库载入所有数据
	 * @param keyList
	 * @param valueList
	 */
	private void loadAll(ArrayList<String> keyList, ArrayList<JSONObject> valueList)
	{		
		if(null == keyList)
		{
			throw new IllegalArgumentException("keyList is null!");
		}
		if(null == valueList)
		{
	 		throw new IllegalArgumentException("valueList is null!");
		}
		
		SQLiteDatabase db = this.getWritableDatabase();	
		Cursor cursor = db.query(mName, new String[]{"id, value"}, null, null, null, null, null);		
		while(cursor.moveToNext())
		{
			String key = cursor.getString(cursor.getColumnIndex("id"));
			String value = cursor.getString(cursor.getColumnIndex("value"));
			try {
				JSONObject item = new JSONObject(value);
				keyList.add(key);
				valueList.add(item);
			} catch (JSONException e) {
				e.printStackTrace();
			}

		}
		db.close();
		cursor.close();
	}

	@Override
	public List<String> getKeysBeforeItem(String key, int count) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<String> keyList = new ArrayList<String>();
		if(null != key)
		{
			Cursor cursor = db.query(mName, new String[]{"id"}, "id<?", new String[]{key}, null, null, "id DESC");
			String value = null;
			
			while(cursor.moveToNext() && count >0)
			{
				value = cursor.getString(cursor.getColumnIndex("id"));
				count--;
				if(null != value)
				{
					keyList.add(value);
				}
			}

			cursor.close();			
		}
		else
		{
			Cursor cursor = db.query(mName, null, null, null, null, null, "id DESC");
			String value = null;
			
			while(cursor.moveToNext() && count >0)
			{
				value = cursor.getString(cursor.getColumnIndex("id"));
				count--;
				if(null != value)
				{
					keyList.add(value);
				}
			}
		}

		db.close();
		return keyList;
	}

	@Override
	public List<String> getKeysAfterItem(String key, int count) {
		SQLiteDatabase db = this.getWritableDatabase();
		List<String> keyList = new ArrayList<String>();
		if(null != key)
		{		
			Cursor cursor = db.query(mName, new String[]{"id"}, "id>?", new String[]{key}, null, null, "id ASC");
			String value = null;
			
			while(cursor.moveToNext() && count >0)
			{
				value = cursor.getString(cursor.getColumnIndex("id"));
				count--;
				if(null != value)
				{
					keyList.add(value);
				}
			}
			cursor.close();
		}
		else
		{
			Cursor cursor = db.query(mName, null, null, null, null, null, "id ASC");
			String value = null;
			
			while(cursor.moveToNext() && count >0)
			{
				value = cursor.getString(cursor.getColumnIndex("id"));
				count--;
				if(null != value)
				{
					keyList.add(value);
				}
			}
			cursor.close();			
		}
		db.close();
		return keyList;
	}

	@Override
	public List<JSONObject> getItems(List<String> keyList) {
		if(null == keyList)
		{
			throw new IllegalArgumentException("keyList is null!");
		}	
		
		ArrayList<JSONObject> result = new ArrayList<JSONObject>();
		ArrayList<String> keyDBList = new ArrayList<String>();
		for(String key:keyList)
		{
			if(mMap.containsKey(key))
			{
				JSONObject value = mMap.get(key);
				result.add(value);
			}
			/*
			 *当数据量超过一定程度时，可能会出现内存数据少于数据库数据的情况 
			 * */
			else 
			{
				keyDBList.add(key);
			}
		}
		List<JSONObject> listValue = getListByDB(keyDBList);
		for(int i=0; i<keyDBList.size(); i++)
		{
			mMap.put(keyDBList.get(i), listValue.get(i));
		}
		
		result.addAll(listValue);
		return result;
	}

	@Override
	public void putItems(List<String> keyList, List<JSONObject> valueList) {
		if(null == keyList)
		{
			throw new IllegalArgumentException("keyList is null!");
		}	
		
		if(null == valueList)
		{
			throw new IllegalArgumentException("valueList is null!");
		}	
		
		if(keyList.size() != valueList.size())
		{
			throw new IllegalArgumentException("valueList is null!");
		}
		
		for(int i=0; i<keyList.size(); i++)
		{
			mMap.put(keyList.get(i), valueList.get(i));
		}	
		putListByDB(keyList, valueList);
	}
	
	private void put(String key, JSONObject value) {
		if(null == key)
		{
			throw new IllegalArgumentException("key is null!");
		}	
		
		if(null == value)
		{
			throw new IllegalArgumentException("value is null!");
		}	
		
		mMap.put(key, value);
		putByDB(key, value);
		
	}
	
	

}
