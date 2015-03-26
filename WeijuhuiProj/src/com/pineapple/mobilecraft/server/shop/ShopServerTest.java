package com.pineapple.mobilecraft.server.shop;

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

import com.pineapple.mobilecraft.data.shop.ShopCategory;

import android.test.AndroidTestCase;
import android.util.Log;

public class ShopServerTest extends AndroidTestCase {
	String IP_ADDRESS = "http://www.li960.com/shop/api.php?s=";
	ShopServer mServer;
	protected void setUp() throws Exception {
		super.setUp();
		
		mServer = ShopServer.getInstance();
	}
	
	public void testUserApi()
	{
		//mServer.register("yh", "yhtest");
		mServer.login("yh", "yhtest");
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
