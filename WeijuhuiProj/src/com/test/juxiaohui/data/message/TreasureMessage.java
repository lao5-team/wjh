package com.test.juxiaohui.data.message;

import cn.bmob.v3.BmobObject;

/**
 * Created by yihao on 15/3/12.
 */
public class TreasureMessage extends BmobObject {
    public static int TYPE_TREASURE = 0; //新的宝物
    public static int TYPE_COMMENT = 1; //新的评论或者鉴定
    public static int TYPE_ACTIVITY = 2; //新的活动
    public int mType = -1;
    public String mContent = "";
    public String mTargetId = "";
    public String mUser = "";
}
