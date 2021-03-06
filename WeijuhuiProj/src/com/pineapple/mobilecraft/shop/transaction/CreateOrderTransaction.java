package com.pineapple.mobilecraft.shop.transaction;

import java.util.List;

import com.pineapple.mobilecraft.data.shop.Chart;
import com.pineapple.mobilecraft.data.shop.Order;
import com.pineapple.mobilecraft.data.shop.Chart.ChartItem;

public class CreateOrderTransaction {
	//private List<ChartItem> mItems;
	
	public static Order createOrderFromChart(Chart chart)
	{
		//CreateOrderTransaction transaction = new CreateOrderTransaction();
		Order order = new Order(chart.getItems());
		return order;
	}
	
	public static Order createOrderFromOrder(Order order)
	{
		Order new_order = new Order(order.getItems());
		return new_order;
	}

}
