package com.test.juxiaohui.cache.temp;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.test.juxiaohui.DemoApplication;

import junit.framework.Assert;
import junit.framework.TestCase;

public class JSONCacheTest extends TestCase {

	JSONCache mCache;
	JSONDBSwapper mSwapper;
	protected void setUp() throws Exception {
		super.setUp();
		mCache = new JSONCache();
		mSwapper = new JSONDBSwapper(DemoApplication.getInstance(), "test");
		mCache.setSwaper(mSwapper);
	}
	
	//即使数据从内存中被清除掉，也能获取。
	public void testGet()
	{
		
		ArrayList<String> keyList = new ArrayList<String>();
		List<JSONObject> valueList = new ArrayList<JSONObject>();
		for(int i=0; i<10 ;i++)
		{
			keyList.add(""+i);
			JSONObject testObj = new JSONObject();
			valueList.add(testObj);
		}
		mCache.putList(keyList, valueList);
		
		
		JSONCache newCache = new JSONCache();
		JSONDBSwapper newSwapper = new JSONDBSwapper(DemoApplication.getInstance(), "test");
		newCache.setSwaper(newSwapper);
		valueList = newCache.getList(keyList);
		Assert.assertTrue(valueList.size() == 10);
	}
	
	public void testRemove()
	{
		
	}
	

}
