package com.test.juxiaohui.mdxc.manager;

import com.test.juxiaohui.common.dal.IUserServer;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.mdxc.server.UserServer;

/**
 * Created by yihao on 15/4/10.
 */
public class UserManager {
	IUserServer mUserServer;
	private static UserManager mInstance = null;
	public static UserManager getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new UserManager();
		}
		return mInstance;
	}
	
	private UserManager()
	{
		mUserServer = new UserServer();
	}
	
    public String register(String username, String password, String checkcode)
    {
    	return mUserServer.register(username, password, checkcode);
    }

    public String login(String username, String password)
    {
    	return mUserServer.login(username, password);
    }

    public User getCurrentUser()
    {
    	return null;
    }

    public void logout()
    {
    	mUserServer.logout();
    }
    
    public void sendCheckcode(String phoneNumber)
    {
    	mUserServer.sendCheckcode(phoneNumber);
    }
}
