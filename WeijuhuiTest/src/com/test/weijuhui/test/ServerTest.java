package com.test.weijuhui.test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;
import android.util.Log;

public class ServerTest extends AndroidTestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}
	
	public void testLogin()
	{
		String userName = "yihao";
		String url = "http://192.168.43.172:8080/login?user=" + userName;
        //  第1步：创建HttpGet对象  
        HttpGet httpGet = new HttpGet(url);  
        //  第2步：使用execute方法发送HTTP GET请求，并返回HttpResponse对象  
        HttpResponse httpResponse;
		try {
			httpResponse = new DefaultHttpClient().execute(httpGet);
	        //  判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求  
	        if (httpResponse.getStatusLine().getStatusCode() == 200)  
	        {  
	            //  第3步：使用getEntity方法获得返回结果  
	            String result = EntityUtils.toString(httpResponse.getEntity());  
	            Log.v("weijuhuiTest", result);
	            Header[] headers = httpResponse.getHeaders("Set-Cookie");
	            if(null != headers)
	            {
		            for(int i=0; i<headers.length; i++)
		            {
		            	Log.v("weijuhuiTest", headers[i].getValue());
		            	String str = headers[i].getValue();
		            	if(str.contains("token="))
		            	{
			            	int first = str.indexOf("token=");
			            	int last = str.indexOf(';');	
			            	String cookie = str.substring(first + "token=".length(), last);
			            	Log.v("weijuhuiTest", "cookie " + cookie);
			            	testGetID(userName, cookie);
		            	}
		            }	            	
	            }

	            //  去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格  
	        } 
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
 
	}
	
	private void testGetID(String user, String token)
	{
		//user:= token=
	    HttpGet httpGet = new HttpGet();
	    httpGet.addHeader("Cookie", String.format("user=%s; token=%s", user, token));
	    try {
			httpGet.setURI(new URI("http://192.168.43.172:8080/newid"));
			HttpResponse httpResponse;
			try {
				httpResponse = new DefaultHttpClient().execute(httpGet);
		        if (httpResponse.getStatusLine().getStatusCode() == 200)  
		        {
		            String result = EntityUtils.toString(httpResponse.getEntity());  
		            try {
						JSONObject obj = new JSONObject(result);
						Log.v("weijuhuiTest", "newid = " + obj.getString("id"));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		        }
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
				
	}
}
