package com.test.juxiaohui.mdxc.manager;

import com.test.juxiaohui.common.dal.IUserServer;
import com.test.juxiaohui.common.data.User;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.server.UserServer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihao on 15/4/10.
 */
public class UserManager {
	public static String LOGIN_SUCCESS = "login_success";
	public static String INVALID_USERNAME_PASSWORD = "invalid_username_password";
	public static String ALREADY_LOGIN = "you_have_already_login";
	public static String LOGOUT_SUCCESS = "logout_success";
	public static String LOGOUT_FAILED = "logout_failed";

	private IUserServer mUserServer;
	private static UserManager mInstance = null;
	private User mCurrentUser = User.NULL;
	private ContactUser mContatctUser = ContactUser.NULL;
	private List<Passenger> mPassengerList = new ArrayList<Passenger>();


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

	/**
	 * 执行注册
	 * @param username
	 * @param password
	 * @param checkcode
	 * @return
	 */
    public String register(String username, String password, String checkcode)
    {
    	return mUserServer.register(username, password, checkcode);
    }

	/**
	 * 执行登陆
	 * @param username
	 * @param password
	 * @return
	 */
    public String login(String username, String password)
    {
		if(mCurrentUser==User.NULL)
		{
			String result = mUserServer.login(username, password);
			if(result.equals("Success"))
			{
				User user = new User();
				user.setUsername(username);
				user.setPassword(password);
				mCurrentUser = user;
				return LOGIN_SUCCESS;
			}
			else
			{
				mCurrentUser = User.NULL;
				return INVALID_USERNAME_PASSWORD;
			}
		}
		else
		{
			return ALREADY_LOGIN;
		}
    }

	/**
	 * 获取当前已经登陆的用户，可以用来验证是否已经登陆
	 * @return 如果当前已登陆，则返回当前用户信息，否则返回User.NULL
	 */
    public User getCurrentUser()
    {
    	return mCurrentUser;
    }

	/**
	 * 注销当前用户
	 * @return 返回注销的结果
	 */
    public String logout()
    {
    	String result = mUserServer.logout();
		if(result.equals("Success"))
		{
			mCurrentUser = User.NULL;
			return LOGOUT_SUCCESS;
		}
		else
		{
			return LOGOUT_FAILED;
		}
    }

	/**
	 * 向指定手机号发送验证码
	 * @param phoneNumber
	 */
    public void sendCheckcode(String phoneNumber)
    {
    	mUserServer.sendCheckcode(phoneNumber);
    }

	/**
	 * 设置当前用户的联系人
	 * @param contactUser
	 */
	public void setContactUser(ContactUser contactUser)
	{
		//have already login
		if(mCurrentUser!=User.NULL)
		{
			mContatctUser = contactUser;
		}
	}

	/**
	 * 获取当前用户的联系人
	 * @return
	 */
	public ContactUser getContactUser()
	{
		//have already login
		if(mCurrentUser!=User.NULL)
		{
			return mContatctUser;
		}
		else
		{
			return ContactUser.NULL;
		}
	}

	/**
	 *
	 * @param listPassenger
	 */
	public void setPassengerList(List<Passenger> listPassenger)
	{
		if(mCurrentUser!=User.NULL)
		{
			mPassengerList.clear();
			mPassengerList.addAll(listPassenger);
		}
		else
		{
			;
		}

	}

	/**
	 *
	 * @return
	 */
	public List<Passenger> getPassengerList()
	{
		if(mCurrentUser!=User.NULL)
		{
			return mPassengerList;
		}
		else
		{
			return new ArrayList<Passenger>();
		}
	}


}
