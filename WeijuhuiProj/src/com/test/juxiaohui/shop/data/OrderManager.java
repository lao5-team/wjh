package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

import com.test.juxiaohui.shop.server.ShopServer;

public class OrderManager {
	List<Order> mOrderList;
	
	private OrderManager()
	{
		mOrderList = new ArrayList<Order>();
	}
	public List<String> getOrders()
	{
		List<String> IDs= new ArrayList<String>();
		for(Order order:mOrderList)
		{
			IDs.add(order.mID);
		}
		return IDs;
	}
	
	public Order getOrder(String id)
	{
		for(Order order:mOrderList)
		{
			if(id.equals(order.mID))
			{
				return order;
			}
		}
		return Order.NULL;
	}
	
	public String submit(Order order) throws IllegalArgumentException
	{
		if(null==order||!checkOrder(order))
		{
			throw new IllegalArgumentException("invalid order");
		}
					
		String orderId = ShopServer.getInstance().submitOrder(order);
		return orderId;
	}
	
	public List<String> getUsersOrderIDList(String userID)
	{
		return ShopServer.getInstance().getUsersOrderIDList(userID);
		
	}
	
	private boolean checkOrder(Order order)
	{
		return true;
	}

}
