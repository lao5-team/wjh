package com.pineapple.juxiaohui.shop.transaction;

import java.util.List;

import com.pineapple.juxiaohui.shop.data.Chart;
import com.pineapple.juxiaohui.shop.data.Chart.ChartItem;
import com.pineapple.juxiaohui.shop.data.Order;

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
