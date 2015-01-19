package com.test.juxiaohui.shop.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.shop.data.ShopCategory;

import android.test.AndroidTestCase;
import android.util.Log;

public class ShopServerTest extends AndroidTestCase {
	String IP_ADDRESS = "http://www.li960.com/shop/api.php?s=";
	ShopServer mServer;
	protected void setUp() throws Exception {
		super.setUp();
		
		mServer = new ShopServer();
	}
	
	public void testUserApi()
	{
		//Register 
		//http://www.li960.com/shop/api.php?s=User/reg&name=xxxxxx&password=xxxxx
		String userName = "yh";
		String password = "yhtest";
		String url = String.format("%sUser/reg&name=%s&password=%s", IP_ADDRESS,userName,password);
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
		
		//Login
		//http://www.li960.com/shop/api.php?s=User/login&name=xxxxxx&password=xxxxx
		url = String.format("%sUser/login&name=%s&password=%s", IP_ADDRESS,userName,password);
		post = new HttpPost(url);
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
	
	public void testShopApi()
	{
		//Register 
		//http://www.li960.com/shop/api.php?s=User/reg&name=xxxxxx&password=xxxxx
		String userName = "yh";
		String password = "yhtest";
		String url = String.format("%sUser/reg&name=%s&password=%s", IP_ADDRESS,userName,password);
		HttpPost post = new HttpPost(url);
		try {
			HttpResponse httpResponse = new DefaultHttpClient().execute(post);
			Assert.assertTrue(httpResponse.getStatusLine().getStatusCode() == 200);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testGetCategoryList()
	{
		List<ShopCategory> list = mServer.getCategoryList();
		Assert.assertNotNull(list);
	}
	
	
	
	
}
