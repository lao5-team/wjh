package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

import com.test.juxiaohui.shop.data.Chart.ChartItem;

public class Order {
	protected String mID;
	protected List<ChartItem> mItems;
	protected String mConsigneeName = null;
	protected String mConsigneePhoneNum = null;
	protected String mConsigneeAddress = null;
	
	public static Order NULL = new Order()
	{
		@Override
		public float calcTotal()
		{
			return 0.0f;
		}
	};
	
	private Order()
	{
		
	}
	
	public Order(List<ChartItem> items)
	{
		if(null == items)
		{
			throw new IllegalArgumentException("items is null!");
		}
		mItems = items;
	}

	public void setConsigneeInfo(String name, String phoneNum, String address)
	{
		mConsigneeName = name;
		mConsigneePhoneNum = phoneNum;
		mConsigneeAddress = address;
	}
	
	public float calcTotal()
	{
		float result = 0.0f;
		for(ChartItem item:mItems)
		{
			Goods goods = ShopDataManager.getInstance().getGoods(item.mID);
			result += item.mCount * goods.getPrize(); 
		}
		return result;
	}
}
