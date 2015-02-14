package com.test.juxiaohui.shop.transaction;

import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.server.ShopServer;

public class PayOrderTransaction {
	private Order mOrder;
	public PayOrderTransaction(Order order)
	{
		mOrder = order;
	}
	
	public boolean pay()
	{
		return ShopServer.getInstance().payOrder(mOrder.getmId());
	}

}
