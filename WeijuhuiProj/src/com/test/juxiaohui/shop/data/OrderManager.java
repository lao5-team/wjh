package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

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
	
	public Order getOrder(String ID)
	{
		for(Order order:mOrderList)
		{
			if(ID.equals(order.mID))
			{
				return order;
			}
		}
		return Order.NULL;
	}
	
	public String submit(Order order) throws IllegalArgumentException
	{
		#
	}
	
	public List<String> getOrderIDList(String userID) throws IllegalArgumentException
	{
		#
	}

}
