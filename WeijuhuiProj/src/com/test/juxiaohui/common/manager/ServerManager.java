package com.test.juxiaohui.common.manager;

import com.test.juxiaohui.common.dal.IUserServer;
import com.test.juxiaohui.mdxc.server.TestUserServer;
import com.test.juxiaohui.utils.SyncHTTPCaller;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/4.
 */
public class ServerManager {

    private static ServerManager mInstance = null;
    private IUserServer mUserServer;



    private ServerManager()
    {
        mUserServer = new TestUserServer();
    }

    public String register(String username ,String password)
    {
        return mUserServer.register(username, password);
    }

    public String login(String username ,String password)
    {
        return mUserServer.login(username, password);

    }

    public void logout()
    {
        mUserServer.logout();
    }

    public boolean isLogin()
    {
        return false;
    }

    public static ServerManager getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new ServerManager();
        }
        return mInstance;
    }



}
