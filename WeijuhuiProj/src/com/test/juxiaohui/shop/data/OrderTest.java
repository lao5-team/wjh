package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import android.test.AndroidTestCase;

public class OrderTest extends AndroidTestCase {
	
	public void setup() throws Exception
	{
		super.setUp();
	}
	
	public void test()
	{
		//有效id 7，6
		//添加包含非法id的商品
		try
		{
			Chart.getInstance().addGoods("-1", 1);
			fail("invalid id, should throw exception");
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			Assert.assertEquals(0,  Chart.getInstance().mItems.size());
			
		}

		//添加包含非法个数的商品
		String validId = "";
		try
		{
			Chart.getInstance().addGoods(validId, 0);
			fail("invalid id, should throw exception");
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			Assert.assertEquals(0,  Chart.getInstance().mItems.size());

		}
		
		//将可用的商品id添加到购物车里去
		Chart.getInstance().addGoods(validId, 2);
		List<String> ids = Chart.getInstance().getGoodsIDs();
		Assert.assertEquals(1, ids.size());
		Assert.assertEquals(ids.get(0), validId);
		Chart.ChartItem item = Chart.getInstance().getItem(validId);
		Assert.assertEquals(2, item.getCount());
		Assert.assertEquals(false, item.isSelected());

		//选择该商品
		Chart.getInstance().setItemSelected(validId, true);
		item = Chart.getInstance().getItem(validId);
		Assert.assertEquals(true, item.isSelected());

		//取消该商品
		Chart.getInstance().setItemSelected(validId, false);
		item = Chart.getInstance().getItem(validId);
		Assert.assertEquals(false, item.isSelected());

		//移除该商品
		Chart.getInstance().removeItem(validId);
		Assert.assertTrue(Chart.getInstance().getItem(validId)== Chart.ChartItem.NULL);
	}

}
