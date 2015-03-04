package com.test.juxiaohui.common.manager;

import com.test.juxiaohui.common.dal.IUserServer;

/**
 * Created by yihao on 15/3/4.
 */
public class ServerManager {

    private static ServerManager mInstance = null;
    private IUserServer mUserServer;

    private ServerManager()
    {

    }

    public void register(String username ,String password)
    {
        mUserServer.register(username, password);
    }

    public String login(String username ,String password)
    {
        mUserServer.login(username, password);
        return "Success";
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
