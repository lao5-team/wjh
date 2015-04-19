package com.test.juxiaohui.mdxc.server;

import android.util.Log;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.common.dal.IUserServer;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.domain.MyServerManager;
import com.test.juxiaohui.utils.SyncHTTPCaller;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/4.
 */
public class TestUserServer implements IUserServer {

    String mLoginResult = "";

    User mCurrentUser = User.NULL;

    String mUsername;
    String mPassword;
    String mToken;
    final Object mLock = new Object();

    public TestUserServer()
    {
        //getHuanxinToken();
    }

    @Override
    public String register(String username, String password, String checkcode) {
        try {
            EMChatManager.getInstance().createAccountOnServer(username, password);
            return "Success";
        } catch (EaseMobException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return e.getMessage();
        }
    }

    @Override
     public String login(String username, final String password) {
        mUsername = username;
        mPassword = password;


            try {
                EMChatManager.getInstance().login(username, password, new EMCallBack() {
                    @Override
                    public void onSuccess()
                    {
                            mLoginResult = "Success";
                            mCurrentUser = new User();
                            mCurrentUser.setUsername(mUsername);
                            mCurrentUser.setPassword(mPassword);


                    }

                    @Override
                    public void onError(int i, String s) {
                            mLoginResult = s;


                    }

                    @Override
                    public void onProgress(int i, String s) {

                    }
                });
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }



            return mLoginResult;

    }

    @Override
    public User getCurrentUser() {
        return mCurrentUser;
    }

    @Override
    public String logout() {
        EMChatManager.getInstance().logout();
        Log.v(DemoApplication.TAG, "logout");
        mCurrentUser = User.NULL;
        return "Success";
    }

    private void getHuanxinToken()
    {
        String url = "https://a1.easemob.com/weijuhui/weijuhui/token";
        JSONObject json = new JSONObject();
        try {
            json.put("grant_type", "client_credentials");
            json.put("client_id", "YXA6qLPj0DvSEeS79oNYb4PjuA");
            json.put("client_secret", "YXA6m7v34IYEeN95LZ0ypssoZXfsKDE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SyncHTTPCaller<String> caller = new SyncHTTPCaller<String>(url, null, json.toString())
        {

            @Override
            public String postExcute(String result) {
                try {
                    JSONObject json = new JSONObject(result);
                    mToken = json.getString("access_token");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return null;
            }
        };
        caller.execute();

    }

	@Override
	public void sendCheckcode(String phoneNumber) {
		// TODO Auto-generated method stub
		
	}
}
