package com.test.juxiaohui.mdxc.data;

/**
 * Created by yihao on 15/4/18.
 * 用来表示联系人,既可以给机票使用，也可以给酒店使用
 */
public class ContactUser {
    public String mFirstName = "";
    public String mLastName = "";
    public String mEmail = "";
    public String mPhoneNumber = "";

    public static ContactUser NULL = new ContactUser();
}
