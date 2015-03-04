package com.test.juxiaohui.common.dal;

import com.test.juxiaohui.common.data.User;

/**
 * Created by yihao on 15/3/4.
 */
public interface IUserServer {
    public void register(String username, String password);

    public void login(String username, String password);

    public User getCurrentUser();

    public void logout();
}
