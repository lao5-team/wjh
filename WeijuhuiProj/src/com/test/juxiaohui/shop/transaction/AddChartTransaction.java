package com.test.juxiaohui.shop.transaction;

import com.test.juxiaohui.shop.data.Chart;

/**
 * Created by yihao on 15/2/6.
 */
public class AddChartTransaction
{
    private String mId;
    private int mCount;
    public AddChartTransaction(String id, int count)
    {
        mId = id;
        mCount = count;
    }

    public void execute()
    {
        Chart.getInstance().addGoods(mId, mCount);
    }
}
