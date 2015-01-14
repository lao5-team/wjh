package com.test.juxiaohui.shop.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ShopServer {
	final String SERVER_ADDRESS = "http://www.li960.com/shop/api.php?s=Product";
	
	public List<ShopCategory> getCategoryList()
	{
		Callable<List<ShopCategory>> callable = new Callable<List<ShopCategory>>() {
			@Override
			public List<ShopCategory> call() throws Exception {
				List<ShopCategory> result = null;
				String url = String.format("%s/cate", SERVER_ADDRESS);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
//					post.addHeader("Cookie", String.format("user=%s;token=%s",
//							mUserName, mToken));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = new ArrayList<ShopCategory>();
						String str = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
						try {
							JSONArray array = new JSONArray(str);
							for(int i=0; i<array.length(); i++)
							{
								JSONObject object = array.getJSONObject(i);
								ShopCategory category = ShopCategory.fromJSON(object);
								result.add(category);
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return result;
			}
		};
		Future<List<ShopCategory>>future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}	
	}
	
	public List<ShopCategory> getSubCategoryList(int id)
	{
		return null;
	}
	
	public List<Goods> getGoodsList(int id)
	{
		return null;
	}

}
