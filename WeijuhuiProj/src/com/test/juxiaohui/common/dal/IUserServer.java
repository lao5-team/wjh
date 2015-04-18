package com.test.juxiaohui.common.dal;

import com.test.juxiaohui.common.data.User;

/**
 * Created by yihao on 15/3/4.
 */
public interface IUserServer {
	/**
	 * 注册
	 * @param username 
	 * @param password
	 * @param checkcode 手机校验码
	 * @return
	 */
    public String register(String username, String password, String checkcode);

    /**
     * 登陆接口，登陆的结果用String返回
     * @param username
     * @param password
     * @return 返回登陆结果
     */
    public String login(String username, String password);

    public User getCurrentUser();

    public String logout();
    
    public void sendCheckcode(String phoneNumber);
}
