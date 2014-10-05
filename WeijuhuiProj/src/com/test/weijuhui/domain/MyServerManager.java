package com.test.weijuhui.domain;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.weijuhui.DemoApplication;

import android.util.Log;

public class MyServerManager {
	private static MyServerManager mInstance = null;
	private Object mLock = new Object();
	final String IP_ADDRESS = "http://117.78.3.87:80";
	String mToken;
	boolean mLoginResult = false;
	String mNewID;
	private MyServerManager()
	{
	}
	
	public static MyServerManager getInstance()
	{
		if(mInstance == null)
		{
			mInstance = new MyServerManager();
		}
		return mInstance;
	}
	
	public boolean login(String username) {
		mLoginResult = false;
		final String userName = username;

		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (mLock) {
				String url = String.format("%s/login?user=%s", IP_ADDRESS, userName);
				// 第1步：创建HttpGet对象
				HttpGet httpGet = new HttpGet(url);
				// 第2步：使用execute方法发送HTTP GET请求，并返回HttpResponse对象
				HttpResponse httpResponse;
				try {
					httpResponse = new DefaultHttpClient().execute(httpGet);
					// 判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						// 第3步：使用getEntity方法获得返回结果
						String result = EntityUtils.toString(httpResponse.getEntity());
						Log.v("weijuhuiTest", result);
						Header[] headers = httpResponse.getHeaders("Set-Cookie");
						if (null != headers) {
							for (int i = 0; i < headers.length; i++) 
							{
								Log.v("weijuhuiTest", headers[i].getValue());
								String str = headers[i].getValue();
								if (str.contains("token="))
								{
									int first = str.indexOf("token=");
									int last = str.indexOf(';');
									mToken = str.substring(first + "token=".length(), last);
								}
							}
						}
					}
					mLoginResult = true;
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					mLoginResult = false;
				} catch (IOException e) {
					e.printStackTrace();
					mLoginResult = false;
				}
				finally
				{
					mLock.notifyAll();
				}
				}
			}
		});
		t.start();		


		synchronized (mLock) {
			try {
				mLock.wait();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v(DemoApplication.TAG, "login result is " + mLoginResult);
		}
		return mLoginResult;
	}
	
	public String getNewActivityID(String username)
	{
		final String fUsername = username;
		final String ftoken = mToken;
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				synchronized (mLock) {			
				 HttpGet httpGet = new HttpGet();
				    httpGet.addHeader("Cookie", String.format("user=%s; token=%s", fUsername, ftoken));
				    try {
						httpGet.setURI(new URI(String.format("%s/newid", IP_ADDRESS)));//"http://192.168.1.103:8080/newid"
						HttpResponse httpResponse;
						try {
							httpResponse = new DefaultHttpClient().execute(httpGet);
					        if (httpResponse.getStatusLine().getStatusCode() == 200)  
					        {
					            String result = EntityUtils.toString(httpResponse.getEntity());  
					            try {
									JSONObject obj = new JSONObject(result);
									Log.v("weijuhuiTest", "newid = " + obj.getString("id"));
									mNewID = obj.getString("id");
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
					finally
					{
						mLock.notifyAll();
					}
				
			}
			}
		});
		t.start();

		synchronized (mLock) {
			try {
				mLock.wait();
				
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Log.v(DemoApplication.TAG, "login result is " + mLoginResult);
		}
		return mNewID;
	}
	
}
