package com.test.juxiaohui.cache.temp;

import java.util.List;

import org.json.JSONObject;

public class JSONCache implements ICache<String, JSONObject> {


	@Override
	public void setRemoteLoader(IRemoteLoader<String, JSONObject> loader) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void put(String key, JSONObject value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<JSONObject> getList(List<String> keyList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void putList(List<String> keyList, List<JSONObject> valueList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setSwaper(ISwapper<String, JSONObject> swapper) {
		// TODO Auto-generated method stub
		
	}

}
