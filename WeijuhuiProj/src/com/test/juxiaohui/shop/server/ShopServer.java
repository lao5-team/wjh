package com.test.juxiaohui.shop.server;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.data.ShopCategory;
import com.test.juxiaohui.shop.data.ShopDataManager;
import com.test.juxiaohui.utils.SyncHTTPCaller;

import android.util.Log;

public class ShopServer {
	final String SERVER_ADDRESS = "http://www.li960.com/shop/api.php?s=Product";
	public static ShopServer mInstance  = null;
	public static ShopServer getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new ShopServer();
		}
		return mInstance;
	}
	
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
	
	public List<ShopCategory> getSubCategoryList(String id)
	{
		
		String url = String.format("%s/cate2&id=%s", SERVER_ADDRESS, id);
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
	
	public List<Goods> getGoodsListinCategory(String id, int startIndex, int endIndex)
	{
		if(startIndex <0||startIndex > endIndex)
		{
			throw new IllegalArgumentException("invalid startIndex or endIndex");
		}
		String url = String.format("%s/getCate2Data&start=%d&end=%d&id=%s", SERVER_ADDRESS, startIndex, endIndex, id);
		SyncHTTPCaller<List<Goods>> caller = new SyncHTTPCaller<List<Goods>>(
				url) {

			@Override
			public List<Goods> postExcute(String result) {
				List<Goods> resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					JSONArray array = json.getJSONArray("data");
					resultObj = new ArrayList<Goods>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						Goods goods = Goods.fromJSON(object);
						resultObj.add(goods);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		return caller.execute();
	}
	
	public List<Goods> getGoodsListbyIDs(List<String> listID)
	{
		//#
		List<Goods> result = new ArrayList<Goods>();
		//TO DO
		return result;
	}
	
	public void submitOrder(Order order)
	{
		#
	}
	

}
