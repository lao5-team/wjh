package com.test.juxiaohui.domain;

import com.test.juxiaohui.data.Treasure;
import com.test.juxiaohui.data.comment.TreasureComment;

import java.util.ArrayList;
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
        return BmobServerManager.getInstance().getTreasuresByIds(ids);
    }

    public Treasure getTreasureById(String id)
    {
        ArrayList<String> ids = new ArrayList<String>();
        ids.add(id);
        List<Treasure> treasures = BmobServerManager.getInstance().getTreasuresByIds(ids);
        return treasures.get(0);
    }

    /**
     * 返回所有宝物id
     * @return
     */
    public List<Treasure> getAllTreasureIds()
    {
        return BmobServerManager.getInstance().getAllTreasure();
    }

    /**
     *
     * @param username 用户名
     * @return
     */
    public List<String> getUserTreasureIds(String username)
    {
        return null;
    }

    /**
     * 获取所有未鉴定的宝物id
     * @return
     */
    public List<String> getUnidentifiedIds()
    {
        return null;
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
    public void sendComment(TreasureComment comment)
    {
        BmobServerManager.getInstance().sendTreasureComment(comment);
    }


}
