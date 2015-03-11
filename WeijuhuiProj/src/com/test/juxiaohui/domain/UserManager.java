package com.test.juxiaohui.domain;

import com.easemob.chat.EMChatManager;
import com.easemob.exceptions.EaseMobException;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.DbOpenHelper;
import com.test.juxiaohui.data.MyUser;

import java.util.ArrayList;
import java.util.List;

public class UserManager {
	private static UserManager mInstance = null;
	private MyUser mCurrentUser = null;
	public static UserManager getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new UserManager();
		}
		return mInstance;
	}
	
	/** 注册用户
	 * @param user
	 * @return
	 */
	public void register(MyUser user) throws EaseMobException
	{
		// 调用sdk注册方法
		try {
			EMChatManager.getInstance().createAccountOnServer(user.mName, user.mPassword);
			/*将用户信息上传到自己的服务器*/
			MyServerManager.getInstance().login(user.mName);
			MyServerManager.getInstance().updateUserInfo(user);
			MyServerManager.getInstance().logout();
		} catch (EaseMobException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	
	/** 登录用户，登录成功后会返回一个MyUser对象
	 * @param userName
	 * @param password
	 * @return
	 */
	public MyUser login(String userName, String password)
	{
		MyServerManager.getInstance().login(userName);
		mCurrentUser = MyServerManager.getInstance().getUserInfo(userName);
		return mCurrentUser;
	}
	
	/**登出用户
	 * @return
	 */
	public boolean logout()
	{
		// 先调用sdk logout，在清理app中自己的数据
		EMChatManager.getInstance().logout();
		DbOpenHelper.getInstance(DemoApplication.applicationContext).closeDB();
		return true;
	}
	
	
	/**返回当前登录用户
	 * @return
	 */
	public MyUser getCurrentUser()
	{
		return mCurrentUser;
	}
	
	/**读取某个用户的信息
	 * @param userName
	 * @return
	 */
	public MyUser getUser(String userName)
	{
		return MyServerManager.getInstance().getUserInfo(userName);
	}
	
	/**更新一个已经存在的用户信息
	 * @param user
	 */
	public void updateUser(MyUser user)
	{
		if(null != MyServerManager.getInstance().getUserInfo(user.mName))
		{
			MyServerManager.getInstance().updateUserInfo(user);
		}
	}

	public List<MyUser> getUsers(List<String> names)
	{
		return new ArrayList<MyUser>();
	}
	
}
