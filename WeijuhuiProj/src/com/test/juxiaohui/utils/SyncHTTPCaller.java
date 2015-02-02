package com.test.juxiaohui.utils;

import java.io.IOException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
public abstract class SyncHTTPCaller<T> {
	
	String mURL;
	String mCookie = "";
	public SyncHTTPCaller(String URL)
	{
		mURL = URL;
	}
	
	public SyncHTTPCaller(String URL, String cookie)
	{
		mURL = URL;
		mCookie = cookie;
	}
	
	public T execute()
	{
		Callable<T> callable = new Callable<T>() {
			@Override
			public T call() throws Exception {
				T result = null;
				HttpPost post = new HttpPost(mURL);
				post.addHeader("Cookie", mCookie);
				HttpResponse httpResponse;
					try {
						httpResponse = new DefaultHttpClient().execute(post);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							String str = EntityUtils.toString(httpResponse.getEntity(), "utf-8");
							result = postExcute(str);
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return result;
				}

		};
		
		Future<T>future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return null;

	}
	
	public abstract T postExcute(String result);
	
}
