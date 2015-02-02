package com.test.juxiaohui.shop.transaction;

import com.test.juxiaohui.shop.data.Order;

public class PayOrderTransaction {
	private Order mOrder;
	public PayOrderTransaction(Order order)
	{
		mOrder = order;
	}
	
	public boolean pay()
	{
		//#
		return false;
	}

}
