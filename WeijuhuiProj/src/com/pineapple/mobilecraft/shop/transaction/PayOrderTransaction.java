package com.pineapple.mobilecraft.shop.transaction;

import com.pineapple.mobilecraft.data.shop.Order;
import com.pineapple.mobilecraft.server.shop.ShopServer;

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
