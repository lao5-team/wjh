package com.test.juxiaohui.domain;

import com.test.juxiaohui.data.Treasure;
import com.test.juxiaohui.data.comment.TreasureComment;

import java.util.List;

/**
 * Created by yihao on 15/3/12.
 */
public class TreasureManager {

    public static TreasureManager mInstance = null;
    public static TreasureManager getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new TreasureManager();
        }

        return mInstance;
    }

    public String uploadTreasure(Treasure treasure){
        return BmobServerManager.getInstance().uploadTreasure(treasure);
    }

    /**
     * 通过id获取宝物
     * @param ids id数组
     * @return
     */
    public List<Treasure> getTreasuresByIds(List<String> ids)
    {

    }

    /**
     * 返回所有宝物id
     * @return
     */
    public List<String> getAllTreasureIds()
    {

    }

    /**
     *
     * @param username 用户名
     * @return
     */
    public List<String> getUserTreasureIds(String username)
    {

    }

    /**
     * 获取所有未鉴定的宝物id
     * @return
     */
    public List<String> getUnidentifiedIds()
    {

    }

    /**
     * 对宝物进行鉴定
     * @param comment 鉴宝信息
     */
    public void identify(TreasureComment comment)
    {

    }

    /**
     * 队伍宝物进行评价
     * @param comment 评论信息
     */
    public void comment(TreasureComment comment)
    {

    }


}
