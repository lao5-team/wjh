package com.test.juxiaohui.common.dal;

import com.test.juxiaohui.common.data.User;

/**
 * Created by yihao on 15/3/4.
 */
public interface IUserServer {
    public String register(String username, String password);

    public String login(String username, String password);

    public User getCurrentUser();

    public void logout();
}
