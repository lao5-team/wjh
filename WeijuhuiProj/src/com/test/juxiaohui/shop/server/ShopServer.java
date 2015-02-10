package com.test.juxiaohui.shop.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.util.JsonWriter;
import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.shop.data.Chart.ChartItem;
import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.data.ShopCategory;
import com.test.juxiaohui.utils.SyncHTTPCaller;

import android.util.Log;

public class ShopServer {
	final String SERVER_ADDRESS_ROOT = "http://www.li960.com/shop/api.php?s=";
	final String SERVER_ADDRESS_PRODUCT = "http://www.li960.com/shop/api.php?s=Product";
	final String SERVER_ADDRESS_ORDER = "http://www.li960.com/shop/api.php?s=Order";
	public static ShopServer mInstance  = null;
	String mSession = "";
	
	
	public static ShopServer getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new ShopServer();
		}
		return mInstance;
	}
	//测试用户名 yh, yhtest
	public void register(String username, String password)
	{
		String url = String.format("%sUser/reg&name=%s&password=%s", SERVER_ADDRESS_ROOT, username, password);
		HttpPost post = new HttpPost(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);
			Assert.assertTrue(httpResponse.getStatusLine().getStatusCode() == 200);
			String str = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			try {
				JSONObject obj = new JSONObject(str);
				Log.v("test", obj.toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	public void login(String username, String password)
	{
		String url = String.format("%sUser/login&name=%s&password=%s", SERVER_ADDRESS_ROOT, username, password);
		HttpPost post = new HttpPost(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);
			Assert.assertTrue(httpResponse.getStatusLine().getStatusCode() == 200);
			String str = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
			try {
				JSONObject obj = new JSONObject(str);
				Log.v("test", obj.toString());
				obj = obj.getJSONObject("data");
				mSession = obj.getString("authid");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<ShopCategory> getCategoryList()
	{
		String url = String.format("%s/cate", SERVER_ADDRESS_PRODUCT);
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
		
		String url = String.format("%s/cate2&id=%s", SERVER_ADDRESS_PRODUCT, id);
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

		
		String url = String.format("%s/getCate2Data&start=%d&end=%d&id=%s", SERVER_ADDRESS_PRODUCT, startIndex, endIndex, id);
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
	public Goods getGoodsByID(String id)
	{
		String url = String.format("%s/getListById&id=%s",SERVER_ADDRESS_PRODUCT, id);
		SyncHTTPCaller<Goods> caller = new SyncHTTPCaller<Goods>(
				url, "PHPSESSID=" + mSession) {

			@Override
			public Goods postExcute(String result) {
				Goods goods = Goods.NULL;
				try {
					JSONObject json = new JSONObject(result);
					json  = json.getJSONObject("data");
					goods = Goods.fromJSON(json);
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return goods;
			}
		};
		return caller.execute();

	}
	
	public List<Goods> getGoodsListbyIDs(List<String> list_id)
	{
		//#
		List<Goods> result = new ArrayList<Goods>();
		//TO DO
		return result;
	}
	
	public String submitOrder(Order order) throws JSONException
	{
		JSONObject goodsJson = new JSONObject();

		List<ChartItem> items = order.getItems();
		for(ChartItem item:items)
		{
			goodsJson.put(item.getID(), item.getCount());
		}

/*		String tempJSON = JSONObject.quote(goodsJson.toString());
		tempJSON = JSONObject.quote(tempJSON);
		try
		{
			tempJSON = tempJSON.substring(1, tempJSON.length()-1);
		}
		catch (IndexOutOfBoundsException e)
		{
			e.printStackTrace();
		}*/

/*		String url = String.format("%s/add&products=%s&other=%s&username=%s&mobile=%s&address=%s&authid=%s", SERVER_ADDRESS_ORDER,
				 "yhtest",order.getOtherMessage(), order.getmConsigneeName(), order.getConsigneePhoneNumber(), order.getmConsigneeAddress(), mSession);*/
		String url = String.format("%s/add&authid=%s", SERVER_ADDRESS_ORDER, mSession);
		
		SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(
				url, "PHPSESSID=" + mSession, goodsJson.toString()) {

			@Override
			public String postExcute(String result) {
				String resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("data");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		return caller.execute();
		
	}
	
	public List<Order> getUsersOrderList(String id)
	{
		String url = String.format("%s/getUserOrder&authid=%s", SERVER_ADDRESS_ORDER, mSession);
		SyncHTTPCaller<List<Order>> caller = new SyncHTTPCaller<List<Order>>(
				url, "PHPSESSID=" + mSession) {

			@Override
			public List<Order> postExcute(String result) {
				List<Order> resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					JSONArray array = json.getJSONArray("data");
					resultObj = new ArrayList<Order>();
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						Order order = Order.fromJSON(object);
						resultObj.add(order);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		return caller.execute();
	}
	
	private ShopServer()
	{
		
	}

}
