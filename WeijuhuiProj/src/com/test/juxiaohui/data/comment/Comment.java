package com.test.juxiaohui.data.comment;

import org.json.JSONObject;

public abstract class Comment {
	public String mID;
	public String mUserName;
	public String mContent;
    abstract public JSONObject toJSON();
    /** 检查该用户是否有权限删除评论
     * @param userID
     * @return 
     */
    abstract public boolean checkDeleteAuthority(String userID);
}
