package com.test.juxiaohui.cache.temp;

import java.util.List;

import org.json.JSONObject;

public class JSONFileSwapper implements ISwapper<String, JSONObject> {

	@Override
	public boolean contains(String key) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void put(String key, JSONObject value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public JSONObject get(String key) {
		// TODO Auto-generated method stub
		return null;
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
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

}
