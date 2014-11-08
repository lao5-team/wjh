package com.test.juxiaohui.domain;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.message.MyMessage;

import android.util.Log;

public class MyServerManager {
	private static MyServerManager mInstance = null;
	private Object mLock = new Object();
	final String IP_ADDRESS = "http://117.78.3.87:80";
	String mToken;
	MyUser mUser;

	private MyServerManager() {
	}

	public static MyServerManager getInstance() {
		if (mInstance == null) {
			mInstance = new MyServerManager();
		}
		return mInstance;
	}

	public boolean login(String username) {
		final String fUsername = username;
		
		Callable<Boolean> callable = new Callable<Boolean>() {
			@Override
			public Boolean call() throws Exception {
				Boolean result = false;
				String url = String.format("%s/login?user=%s", IP_ADDRESS,
						fUsername);
				HttpGet httpGet = new HttpGet(url);
				HttpResponse httpResponse;
				try {
					httpResponse = new DefaultHttpClient().execute(httpGet);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						EntityUtils.toString(httpResponse
								.getEntity());
						Header[] headers = httpResponse
								.getHeaders("Set-Cookie");
						if (null != headers) {
							for (int i = 0; i < headers.length; i++) {
								String str = headers[i].getValue();
								if (str.contains("token=")) {
									int first = str.indexOf("token=");
									int last = str.indexOf(';');
									mToken = str.substring(
											first + "token=".length(), last);
								}
							}
						}
						result = true;
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}	
				return result;
			}
		};
		
		Future<Boolean>future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get().booleanValue();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getNewActivityID(String username) {
		final String fUsername = username;
		final String ftoken = mToken;
		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				String newID = null;
				HttpGet httpGet = new HttpGet();
				httpGet.addHeader("Cookie",
						String.format("user=%s; token=%s", fUsername, ftoken));
				try {
					httpGet.setURI(new URI(String
							.format("%s/newid", IP_ADDRESS)));// "http://192.168.1.103:8080/newid"
					HttpResponse httpResponse;
					try {
						httpResponse = new DefaultHttpClient().execute(httpGet);
						if (httpResponse.getStatusLine().getStatusCode() == 200) {
							String result = EntityUtils.toString(httpResponse
									.getEntity());
							try {
								JSONObject obj = new JSONObject(result);
								newID = obj.getString("id");
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					} catch (ClientProtocolException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}

				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
				return newID;
			}
		};
		Future<String>future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}		
	}
	
	public String uploadImage(File file) {
		String fileName = file.getName();
		String fileContent;
		try {
			FileInputStream fis = new FileInputStream(file);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			byte buffer[] = new byte[1024 * 100];
			int count = 0;
			count = fis.read(buffer);
			while (-1 != count) {

				baos.write(buffer, 0, count);
				count = fis.read(buffer);
			}
			;
			fileContent = new String(baos.toByteArray(), "ISO-8859-1");

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return null;
		}
		try {
			HttpClient httpClient = new DefaultHttpClient();
			HttpPost post = new HttpPost("http://117.78.3.87:81/upload");
			fileName = mToken + "_" + fileName;
			post.setEntity(new StringEntity(String.format("%s\r\n%s\r\n",
					fileName, fileContent), "ISO-8859-1"));
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

	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @return
	 */
	public boolean updateUserInfo(MyUser user) {
		final MyUser fUser = user;
		Callable<Boolean> callable = new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				Boolean result = false;
				String url = String.format(
						"%s/db?action=set_user&table=user&username=%s",
						IP_ADDRESS, fUser.mName);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							fUser.mName, mToken));
					post.setEntity(new StringEntity(MyUser.toJSON(fUser)
							.toString(), "utf-8"));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = true;
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return result;
			}
		};
		
		Future<Boolean>future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get().booleanValue();
		} catch (InterruptedException e) {
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @param username
	 * @return MyUser
	 */
	public MyUser getUserInfo(String username) {
		final String fUserName = username;
		Callable<MyUser> callable = new Callable<MyUser>() {
			@Override
			public MyUser call() throws Exception {
				MyUser user = null;
				String url = String.format(
						"%s/db?action=get_user&table=user&username=%s",
						IP_ADDRESS, fUserName);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							fUserName, mToken));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						try {
							JSONObject jsonObj = new JSONObject(
									EntityUtils.toString(
											httpResponse.getEntity(), "utf-8"));
							user = MyUser.fromJSON(jsonObj
									.getJSONObject("data"));
							user.mID = jsonObj.getString("_id");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				Log.v(DemoApplication.TAG,
						"getUserInfo result is " + MyUser.toJSON(user).toString());
				return user;
			}
		};
		
		Future<MyUser>future = Executors.newSingleThreadExecutor().submit(callable);
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


	/**
	 * 创建一个活动数据
	 * 
	 * @param data
	 *            活动数据
	 * @return 活动id
	 */
	public String createActivity(ActivityData data) {
		if (null == data) {
			throw new IllegalArgumentException("ActivityData can not be null");
		}
		final ActivityData fData = data;
		Callable<String> callable = new Callable<String>() {
			@Override
			public String call() throws Exception {
				String activityID = null;
				String url = String.format("%s/db?action=set&table=activity",
						IP_ADDRESS);
				HttpPost post = new HttpPost(url);
				// 第2步：使用execute方法发送HTTP GET请求，并返回HttpResponse对象
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					post.setEntity(new StringEntity(ActivityData.toJSON(fData)
							.toString(), "utf-8"));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						try {
							JSONObject jsonObj = new JSONObject(
									EntityUtils.toString(
											httpResponse.getEntity(), "utf-8"));
							activityID = jsonObj.getString("id");
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 	
				return activityID;
			}
		};
		
		Future<String>future = Executors.newSingleThreadExecutor().submit(callable);
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

	/**
	 * 更新一个活动数据
	 * 
	 * @param data
	 *            活动数据
	 * @param id
	 *            活动id
	 * @return
	 */
	public boolean updateActivity(ActivityData data, String id) {
		if (null == data || null == id) {
			throw new IllegalArgumentException(
					"ActivityData or id can not be null");
		}
		final ActivityData fData = data;
		final String fId = id;
		Callable<Boolean> callable = new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				Boolean result = false;
				String url = String.format(
						"%s/db?action=set&table=activity&id=%s", IP_ADDRESS,
						fId);
				HttpPost post = new HttpPost(url);
				// 第2步：使用execute方法发送HTTP GET请求，并返回HttpResponse对象
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					post.setEntity(new StringEntity(ActivityData.toJSON(fData)
							.toString(), "utf-8"));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = true;
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return result;
			}
		};

		Future<Boolean>future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}	
	}

	/**
	 * 获取一条活动数据
	 * 
	 * @param id
	 *            活动id
	 * @return 活动数据，如果此id不存在，则返回null
	 */

	public ActivityData getActivity(String id) {
		if (null == id) {
			throw new IllegalArgumentException("id can not be null");
		}
		final String fId = id;
		Callable<ActivityData> callable = new Callable<ActivityData>() {
			@Override
			public ActivityData call() throws Exception {
				ActivityData activityData = null;
				String url = String.format(
						"%s/db?action=get&table=activity&id=%s", IP_ADDRESS,
						fId);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						try {
							JSONObject jsonObj = new JSONObject(
									EntityUtils.toString(
											httpResponse.getEntity(), "utf-8"));
							activityData = ActivityData.fromJSON(jsonObj
									.getJSONObject("data"));
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return activityData;
			}
		};
		Future<ActivityData>future = Executors.newSingleThreadExecutor().submit(callable);
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

	/**
	 * @param userId
	 *            用户id
	 * @param activityId
	 *            活动id
	 * @param field
	 *            活动的field doing_activity, finish_activity
	 * @return 是否成功
	 */
	public boolean addUserActivity(String userId, String activityId,
			String field) {
		if (null == userId || null == activityId || null == field) {
			throw new IllegalArgumentException(
					"userId or activityId or field can not be null");
		}

		final String fUserId = userId;
		final String fActivityID = activityId;
		final String fField = field;
		
		Callable<Boolean> callable = new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				Boolean result = false;
				String url = String
						.format("%s/db?action=add_user_activity&table=user_activity&user_id=%s&field=%s&activity_id=%s",
								IP_ADDRESS, fUserId, fField, fActivityID);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = true;
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} 
				return result;
			}
		};
		Future<Boolean>future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get().booleanValue();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}		
		
	}


	/**
	 * 获取某个用户的活动列表
	 * 
	 * @param userId
	 * @return 该ArrayList有两个元素，第一个元素为doingActivity的ArrayList，
	 *         第二个为finishActivity的ArrayList
	 */
	public ArrayList<ArrayList<String>> getUserActivity(String userId) {
		if (null == userId) {
			throw new IllegalArgumentException("userId can not be null");
		}
		final String fUserId = userId;
		Callable<ArrayList<ArrayList<String>>> callable = new Callable<ArrayList<ArrayList<String>>>() {
			
			@Override
			public ArrayList<ArrayList<String>> call() throws Exception {
				ArrayList<ArrayList<String>> activityList = null;
				String url = String
						.format("%s/db?action=get_user_activity&table=user_activity&user_id=%s",
								IP_ADDRESS, fUserId);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						try {
							activityList = new ArrayList<ArrayList<String>>();
							JSONObject jsonObj = new JSONObject(
									EntityUtils.toString(
											httpResponse.getEntity(), "utf-8"));
							JSONArray jDoingActivity = jsonObj
									.getJSONArray("doing_activity");
							ArrayList<String> doingActivity = new ArrayList<String>();
							for (int i = 0; i < jDoingActivity.length(); i++) {
								doingActivity.add(jDoingActivity.getString(i));
							}
							activityList.add(doingActivity);

							JSONArray jFinishActivity = jsonObj
									.getJSONArray("finish_activity");
							ArrayList<String> finishActivity = new ArrayList<String>();
							for (int i = 0; i < jFinishActivity.length(); i++) {
								finishActivity.add(jFinishActivity.getString(i));
							}
							activityList.add(finishActivity);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return activityList;
			}
		};
		Future<ArrayList<ArrayList<String>>> future = Executors.newSingleThreadExecutor().submit(callable);
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
	
	/** 向user发送消息
	 * @param user
	 * @param message
	 */
	public boolean sendMessage(MyUser user, MyMessage message)
	{
		if (null == user || null == message) {
			throw new IllegalArgumentException(
					"user or message can not be null");
		}
		final MyUser fUser = user;
		final MyMessage fMessage = message;
		
		Callable<Boolean> callable = new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				Boolean result = false;
				String url = String
						.format("%s/db?action=add_user_message&table=user_message&user_id=%s",
								IP_ADDRESS, fUser.mID);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					JSONArray jsonArray = new JSONArray();
					post.setEntity(new StringEntity(MyMessage.toJSON(fMessage).toString(), "utf-8"));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = true;
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
				}
				return result;
			}
		};
		
		Future<Boolean> future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get().booleanValue();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**获取user的消息，该接口可以用于作为通知提示用户阅读消息
	 * @param user
	 * @return
	 */
	public ArrayList<MyMessage> getMessages(MyUser user)
	{
		if (null == user) {
			throw new IllegalArgumentException("userId can not be null");
		}
		final MyUser fUser = user;
		Callable <ArrayList<MyMessage>> callable = new Callable<ArrayList<MyMessage>>() {
			
			@Override
			public ArrayList<MyMessage> call() throws Exception {
				ArrayList<MyMessage> messages = null;
				String url = String
						.format("%s/db?action=get_user_message&table=user_message&user_id=%s",
								IP_ADDRESS, fUser.mID);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						try {
							messages = new ArrayList<MyMessage>();
							JSONObject jsonObj = new JSONObject(
									EntityUtils.toString(
											httpResponse.getEntity(), "utf-8"));
							JSONArray jMessages = jsonObj
									.getJSONArray("messages");
							for (int i = 0; i < jMessages.length(); i++) {
								messages.add(MyMessage.fromJSON(jMessages.getJSONObject(i)));
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return messages;
			}
		};
		
		Future<ArrayList<MyMessage>> future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	/**
	 * 删除用户消息，该接口可用于阅读完消息后，删除未读的消息
	 * @param messages
	 */
	public boolean removeMessages(MyUser user, ArrayList<MyMessage> messages)
	{
		if (null == user || null == messages) {
			throw new IllegalArgumentException("user or messages can not be null");
		}
		final MyUser fUser = user;
		final ArrayList<MyMessage> fMessages  = messages;
		Callable<Boolean> callable = new Callable<Boolean>() {
			
			@Override
			public Boolean call() throws Exception {
				Boolean result = false;
				String url = String
						.format("%s/db?action=remove_user_message&table=user_message&user_id=%s",
								IP_ADDRESS, fUser.mID);
				HttpPost post = new HttpPost(url);
				HttpResponse httpResponse;
				try {
					post.addHeader("Cookie", String.format("user=%s;token=%s",
							mUser.mName, mToken));
					JSONArray jsonArray = new JSONArray();
					for(MyMessage message : fMessages)
					{
						jsonArray.put(MyMessage.toJSON(message));
					}
					post.setEntity(new StringEntity(jsonArray.toString(), "utf-8"));
					httpResponse = new DefaultHttpClient().execute(post);
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						result = true;				
					}

				} catch (ClientProtocolException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return result;
			}
		};
		Future<Boolean> future = Executors.newSingleThreadExecutor().submit(callable);
		try {
			return future.get().booleanValue();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (ExecutionException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
}
