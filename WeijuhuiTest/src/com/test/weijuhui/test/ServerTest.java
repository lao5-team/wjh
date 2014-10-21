package com.test.weijuhui.test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import junit.framework.Assert;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.MyUser;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;

import android.test.AndroidTestCase;
import android.util.Log;

public class ServerTest extends AndroidTestCase {

	final String IP_ADDRESS = "http://117.78.3.87:80";
	final String TAG = "weijuhuitest";
	String cookie = "";
	protected void setUp() throws Exception {
		super.setUp();
	}
	
//	public void testLogin()
//	{
//		String userName = "yihao";
//		String url = String.format("%s/login?user=%s", IP_ADDRESS, userName);//"http://192.168.1.103:8080/login?user=" + userName;
//        //  第1步：创建HttpGet对象  
//        HttpGet httpGet = new HttpGet(url);  
//        //  第2步：使用execute方法发送HTTP GET请求，并返回HttpResponse对象  
//        HttpResponse httpResponse;
//		try {
//			httpResponse = new DefaultHttpClient().execute(httpGet);
//	        //  判断请求响应状态码，状态码为200表示服务端成功响应了客户端的请求  
//	        if (httpResponse.getStatusLine().getStatusCode() == 200)  
//	        {  
//	            //  第3步：使用getEntity方法获得返回结果  
//	            String result = EntityUtils.toString(httpResponse.getEntity());  
//	            Log.v("weijuhuiTest", result);
//	            Header[] headers = httpResponse.getHeaders("Set-Cookie");
//	            if(null != headers)
//	            {
//		            for(int i=0; i<headers.length; i++)
//		            {
//		            	Log.v("weijuhuiTest", headers[i].getValue());
//		            	String str = headers[i].getValue();
//		            	if(str.contains("token="))
//		            	{
//			            	int first = str.indexOf("token=");
//			            	int last = str.indexOf(';');	
//			            	String cookie = str.substring(first + "token=".length(), last);
//			            	Log.v("weijuhuiTest", "cookie " + cookie);
//			            	String id = testGetID(userName, cookie);
//			            	
//			            	
//			        		ComplexBusiness testCB = new ComplexBusiness();
//			        		testCB.mName = "东来顺";
//			        		testCB.mImgUrl = "http://i2.dpfile.com//pc//e6801a8a0b89fa2dd93e582c69d2e7cd(700x700)//thumb.jpg";
//			        		testCB.mPhoneNumber = "12345678";
//			        		
//			        		User testUser = new User();
//			        		testUser.mName = "testCreator";
//			        		
//			        		ArrayList<User> users = new ArrayList<User>();
//			        		User testGuest = new User();
//			        		testGuest.mName = "test0";
//			        		users.add(testGuest);
//			        		
//			        		testGuest = new User();
//			        		testGuest.mName = "test1";
//			        		users.add(testGuest);
//			        		
//			        		testGuest = new User();
//			        		testGuest.mName = "test2";
//			        		users.add(testGuest);	
//			        		
//			        		try {
//			        			ActivityData data = new ActivityData.ActivityBuilder().setBeginTime(new SimpleDateFormat(ActivityData.dataPattern).parse("2014年9月20日16时")
//			        					).setComplexBusiness(testCB).setCreator(testUser).setUsers(users).create();
//			        			testSaveActivity(userName, cookie, id, data);
//			        			testGetActivity(userName, cookie, id);
//			        			testGetActivity(userName, cookie, "54282ad0e13823452d8a16c7");
//			        		}catch (ParseException e) {
//			        			// TODO Auto-generated catch block
//			        			e.printStackTrace();
//			        		}
//		            	}
//		            }	            	
//	            }
//
//	            //  去掉返回结果中的"\r"字符，否则会在结果字符串后面显示一个小方格  
//	        } 
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}  
// 
//	}
//	
//	public void testUploadFile()
//	{
//        String end ="\r\n";
//        String twoHyphens ="--";
//        String boundary ="*****";
//	      try
//	        {
//	    	  HttpClient httpClient = new DefaultHttpClient();
//	          HttpPost post = new HttpPost("http://117.78.3.87:81/upload");
//	          post.setEntity(new StringEntity(String.format("wo_ri_o.txt\r\n%s\r\n", "55So5LmQ6Zif5ZCN5a2X5YGa5qG25ZCN5LiA54K56YO95LiN6YW3\r\n5a+56Z+z5LmQ55qE5omn552A6L+95rGC5L2g5LiN5aaC5oiR")));
//	          HttpResponse response = httpClient.execute(post);
//	          if(response.getStatusLine().getStatusCode()==200){
//	        	  Log.v("weijuhuitest", "Upload Success");
//	          }
//	        }
//	      catch(Exception e)
//	      {
//	    	  e.printStackTrace();
//	      }
//	}
//	
//	private String testGetID(String user, String token)
//	{
//	    HttpGet httpGet = new HttpGet();
//	    httpGet.addHeader("Cookie", String.format("user=%s; token=%s", user, token));
//	    try {
//			httpGet.setURI(new URI(String.format("%s/newid", IP_ADDRESS)));//"http://192.168.1.103:8080/newid"
//			HttpResponse httpResponse;
//			try {
//				httpResponse = new DefaultHttpClient().execute(httpGet);
//		        if (httpResponse.getStatusLine().getStatusCode() == 200)  
//		        {
//		            String result = EntityUtils.toString(httpResponse.getEntity());  
//		            try {
//						JSONObject obj = new JSONObject(result);
//						Log.v("weijuhuiTest", "newid = " + obj.getString("id"));
//						return obj.getString("id");
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		        }
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			return null;	
//				
//	}
//	
//	private void testSaveActivity(String user, String token, String id, ActivityData data)
//	{
//		 HttpPost httpPost = new HttpPost(String.format("%s/saveid", IP_ADDRESS));//"http://192.168.1.103:8080/saveid"
//		 httpPost.addHeader("Cookie", String.format("user=%s; token=%s", user, token));
//		 try {
//			HttpEntity entity = new StringEntity(String.format("%s\r\n%s\r\n", id, ActivityData.toJSON(data).toString()));
//			httpPost.setEntity(entity);
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		try {
//			HttpResponse httpResp;
//			httpResp = new DefaultHttpClient().execute(httpPost);
//			if (httpResp.getStatusLine().getStatusCode() != 200) {
//				assertFalse(true);
//			}
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 
//
//	}
//	
//	private String testGetActivity(String user, String token, String id)
//	{
//		//user:= token=
//	    HttpGet httpGet = new HttpGet();
//	    httpGet.addHeader("Cookie", String.format("user=%s; token=%s", user, token));
//	    try {
//			httpGet.setURI(new URI(String.format("%s/loadid?id=%s", IP_ADDRESS, id)));//"http://192.168.1.103:8080/newid"
//			HttpResponse httpResponse;
//			try {
//				httpResponse = new DefaultHttpClient().execute(httpGet);
//		        if (httpResponse.getStatusLine().getStatusCode() == 200)  
//		        {
//		            String result = EntityUtils.toString(httpResponse.getEntity());  
//		            try {
//						JSONObject obj = new JSONObject(result);
//						Log.v("weijuhuiTest", "getActivity = " + obj.getString("data"));
//						return obj.getString("activity");
//					} catch (JSONException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//		        }
//			} catch (ClientProtocolException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			
//		} catch (URISyntaxException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//			return null;	
//				
//	}
	
	public void testLogin()
	{
		String userName = "yihao";
		String url = String.format("%s/login?user=%s", IP_ADDRESS, userName);//"http://192.168.1.103:8080/login?user=" + userName;
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
			            	cookie = str.substring(first + "token=".length(), last);
			            	break;
		            	}
		            }
	            }
	        }
	        else
	        {
	        	Assert.fail("login failed");
	        }
		}
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		testAddData(cookie);
	}
	
	public void testAddData(String cookie)
	{
  
		String id = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(String.format("http://117.78.3.87:80/db?action=set&table=%s", "ut"));
			post.addHeader("Cookie", String.format("token=%s", cookie));
			JSONObject obj = new JSONObject();
			obj.put("data", "论对音乐的执着追求");
			post.setEntity(new StringEntity(obj.toString(), "utf-8"));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity()); 
				JSONObject jsonResult = new JSONObject(result);
				id = jsonResult.getString("id");
			}
			else
			{
				Assert.fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		testGetData(cookie, id);
		testDelData(cookie, id);
		
	}
	
	private void testGetData(String cookie, String id)
	{
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(String.format("http://117.78.3.87:80/db?action=get&table=ut&id=%s", id));
			post.addHeader("Cookie", String.format("token=%s", cookie));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity(), "utf-8"); 
				JSONObject jsonResult = new JSONObject(result);
				String data = jsonResult.getString("data");
				Log.v(TAG, data);
			}
			else
			{
				Assert.fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void testDelData(String cookie, String id)
	{
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost(String.format("http://117.78.3.87:80/db?action=del&table=ut&id=%s", id));
			post.addHeader("Cookie", String.format("token=%s", cookie));
			HttpResponse response = httpClient.execute(post);
			if (response.getStatusLine().getStatusCode() == 200) {
				String result = EntityUtils.toString(response.getEntity()); 
				JSONObject jsonResult = new JSONObject(result);
				String data = jsonResult.getString("id");
				Log.v(TAG, data);
			}
			else
			{
				Assert.fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

//	private void testGetData()
//	{
//		
//	}
//	
//	private void testSetData()
//	{
//		
//	}
//	
//	private void testDelData()
//	{
//		
//	}
}
