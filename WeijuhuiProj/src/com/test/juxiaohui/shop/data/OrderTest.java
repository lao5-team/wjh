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
		List<String> goodsIds = new ArrayList<String>();
		
		//添加包含非法id的数组
		goodsIds.add(object);
		goodsIds.add("-1");
		try
		{
			Chart.getInstance().addGoodsList(goodsIds);
			fail("invalid id, should throw exception");
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			Assert.assertEquals(0,  Chart.getInstance().mItems.size());
			
		}
		
		//将两个可用的商品id添加到购物车里去
		goodsIds.clear();
		goodsIds.add("")
		goodsIds.add(object);
		Chart.getInstance().addGoodsList(goodsIds);
		List<String> ids = Chart.getInstance().getGoodsIDs();
		Assert.assertEquals(goodsIds.size(), ids.size());
		for(int i=0; i<ids.size(); i++)
		{
			Assert.assertEquals(ids.get(i), goodsIds.get(i));
		}
		
		Assert.assertEquals(1, Chart.getInstance().getGoodsCount(id));
		Assert.assertEquals(1, Chart.getInstance().getGoodsCount(id));
		
		
		
	}

}
