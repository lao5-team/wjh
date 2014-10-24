package com.test.weijuhui.domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.weijuhui.DemoApplication;
import com.test.weijuhui.data.MyUser;

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
	
	public String uploadImage(File file)
	{
		String fileName = file.getName();
		String fileContent;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024*100];
			int count = 0;
			count = fis.read(buffer);
			while(-1 != count)
			{
				
				baos.write(buffer, 0, count);
				count = fis.read(buffer);
			};
			fileContent = new String(baos.toByteArray(), "ISO-8859-1");
				
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://117.78.3.87:81/upload");
			post.setEntity(new StringEntity(
					String.format(
							"%s\r\n%s\r\n",
							fileName,
							fileContent), "ISO-8859-1"));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				Log.v(DemoApplication.TAG, "Upload Image Success");
	            String result = EntityUtils.toString(response.getEntity());  
	            try {
					JSONObject obj = new JSONObject(result);
					return obj.getString("url");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return null;
	}
	
	public boolean updateUserInfo(MyUser user) {
		mLoginResult = false;
		final MyUser fUser = user;
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (mLock) {
					String url = String.format(
							"%s/db?action=set_user&table=user&username=%s",
							IP_ADDRESS, fUser.mName);
					HttpPost post = new HttpPost(url);
					// 第2步：使用execute方法发送HTTP GET请求，并返回HttpResponse对象
					HttpResponse httpResponse;
					try {
						post.addHeader("Cookie",
								String.format("user=%s;token=%s", fUser.mName, mToken));
						post.setEntity(new StringEntity(MyUser.toJSON(fUser)
								.toString(), "utf-8"));
						httpResponse = new DefaultHttpClient().execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							mLoginResult = true;
						}

					} catch (ClientProtocolException e) {
						e.printStackTrace();
						mLoginResult = false;
					} catch (IOException e) {
						e.printStackTrace();
						mLoginResult = false;
					} finally {
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
			Log.v(DemoApplication.TAG, "updateUserInfo result is "
					+ mLoginResult);
		}
		return mLoginResult;
	}
	
	//uploadUser
	
}
