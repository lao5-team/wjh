package com.test.juxiaohui.shop.data;

import java.util.List;

import com.test.juxiaohui.shop.server.ShopServer;
import com.test.juxiaohui.shop.transaction.CreateOrderTransaction;

import com.test.juxiaohui.shop.transaction.SubmitOrderTransaction;
import junit.framework.Assert;
import android.test.AndroidTestCase;

public class OrderTest extends AndroidTestCase {

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
	}
	
	public void test()
	{
		//有效id 7，6
		//添加包含非法id的商品
		ShopServer.getInstance().login("yh", "yhtest");

		try
		{
			Chart.getInstance().addGoods("-1", 1);
			fail("invalid id, should throw exception");
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			Assert.assertEquals(0, Chart.getInstance().getItems().size());
			
		}

		//添加包含非法个数的商品
		String validId = "7";
		try
		{
			Chart.getInstance().addGoods(validId, 0);
			fail("invalid id, should throw exception");
		}
		catch(IllegalArgumentException e)
		{
			e.printStackTrace();
			Assert.assertEquals(0,  Chart.getInstance().getItems().size());

		}
		
		//将可用的商品id添加到购物车里去
		Chart.getInstance().addGoods(validId, 2);
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
		
		//生成订单
		Chart.getInstance().addGoods("7", 1);
		Chart.getInstance().addGoods("6", 1);
		Order order = CreateOrderTransaction.createOrderFromChart(Chart.getInstance());
		Assert.assertTrue(Math.abs(16.0-order.calcTotalPrice())<0.00001);

		//提交订单
		SubmitOrderTransaction transaction = new SubmitOrderTransaction(order);
		transaction.execute();
		List<Order> listOrder = OrderManager.getInstance().getUsersOrderList("yh");
		boolean found = false;
		for(Order orderIter:listOrder)
		{
			if(orderIter.getmId().equals(order.getmId()))
			{
				found = true;
			}
		}
		Assert.assertTrue(found);
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
		Chart.getInstance().removeItem("7");
		Chart.getInstance().removeItem("6");
	}

}
