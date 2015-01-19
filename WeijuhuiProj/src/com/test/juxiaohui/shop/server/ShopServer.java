package com.test.juxiaohui.shop.server;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.ShopCategory;
import com.test.juxiaohui.utils.SyncHTTPCaller;

import android.util.Log;

public class ShopServer {
	final String SERVER_ADDRESS = "http://www.li960.com/shop/api.php?s=Product";
	
	public List<ShopCategory> getCategoryList()
	{
		String url = String.format("%s/cate", SERVER_ADDRESS);
		SyncHTTPCaller<List<ShopCategory>> caller = new SyncHTTPCaller<List<ShopCategory>>(
				url) {

			@Override
			public List<ShopCategory> postExcute(String result) {
				List<ShopCategory> resultObj = null;
				try {
					JSONArray array = new JSONArray(result);
					resultObj = new ArrayList<ShopCategory>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						ShopCategory category = ShopCategory.fromJSON(object);
						resultObj.add(category);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		return caller.execute();
	}
	
	public List<ShopCategory> getSubCategoryList(int id)
	{
		
		String url = String.format("%s/cate2&id=", SERVER_ADDRESS, id);
		SyncHTTPCaller<List<ShopCategory>> caller = new SyncHTTPCaller<List<ShopCategory>>(
				url) {

			@Override
			public List<ShopCategory> postExcute(String result) {
				List<ShopCategory> resultObj = null;
				try {
					JSONArray array = new JSONArray(result);
					resultObj = new ArrayList<ShopCategory>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						ShopCategory category = ShopCategory.fromJSON(object);
						resultObj.add(category);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		return caller.execute();
	}
	
	public List<Goods> getGoodsList(int id)
	{
		return null;
	}

}
