package com.test.juxiaohui.mdxc.server;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.http.client.entity.UrlEncodedFormEntity;

import com.test.juxiaohui.common.dal.IUserServer;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.utils.SyncHTTPCaller;

public class UserServer implements IUserServer {

	
	@Override
	public String register(String username, String password) {
		// TODO Auto-generated method stub
		String url = "http://64.251.7.148:8081/user-web/app/register/register.json";

		JSONObject json = new JSONObject();
		try {
			json.put("userName", username);
			json.put("password", password);
			json.put("confirmPassword", password);
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		List params = new ArrayList();
		params.add(new BasicNameValuePair("userName", username));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("confirmPassword", password));

		SyncHTTPCaller<String> caller;
		caller = new SyncHTTPCaller<String>(url, "", params) {

			@Override
			public String postExcute(String result) {
				String resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		caller.execute();
		return "Success";
	}

	@Override
	public String login(String username, String password) {
		String url = "http://64.251.7.148:8081/user-web/app/login/login.json";
		List params=new ArrayList();
		params.add(new BasicNameValuePair("userName", username));
		params.add(new BasicNameValuePair("password", password));
		SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(
				url, null, params) {

			@Override
			public String postExcute(String result) {
				String resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		caller.execute();
		return "Success";
	}

	@Override
	public User getCurrentUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void logout() {
		// TODO Auto-generated method stub
		String url = "http://64.251.7.148:8081/user-web/app/login/logout";
		SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(
				url, null, "") {

			@Override
			public String postExcute(String result) {
				String resultObj = null;
				try {
					JSONObject json = new JSONObject(result);
					resultObj = json.getString("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return resultObj;
			}
		};
		caller.execute();
	}

}