package com.pineapple.juxiaohui.shop.transaction;

import com.pineapple.juxiaohui.shop.data.Order;
import com.pineapple.juxiaohui.shop.server.ShopServer;

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
