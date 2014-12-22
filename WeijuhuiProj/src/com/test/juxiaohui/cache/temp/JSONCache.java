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

public class JSONCache extends SQLiteOpenHelper implements ICache<String, JSONObject> {

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
		
		
	}

	@Override
	public JSONObject get(String key) {
		if(null == key)
		{
			throw new IllegalArgumentException("key is null!");
		}		
		JSONObject value = null;
		if(mMap.containsKey(key))
		{
			value= mMap.get(key);
		}
		else
		{
			value = getByDB(key);
			if(value != null)
			{
				mMap.put(key, value);
			}
		}
		return value;
	}

	@Override
	public void put(String key, JSONObject value) {
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

	@Override
	public List<JSONObject> getList(List<String> keyList) {
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
			else
			{
				keyDBList.add(key);
			}
		}
		result.addAll(getListByDB(keyDBList));
		return result;
	}

	@Override
	public void putList(List<String> keyList, List<JSONObject> valueList) {
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

	@Override
	public void setSwaper(ISwapper<String, JSONObject> swapper) {
		if(null == swapper)
		{
			throw new IllegalArgumentException("swapper is null!");
			
		}	
		swapper.setName(mName);
	}
	
	@Override
	public void remove(String key, JSONObject value) {
		
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
			String value = cursor.getString(cursor.getColumnIndex("id"));
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
	
	

}
