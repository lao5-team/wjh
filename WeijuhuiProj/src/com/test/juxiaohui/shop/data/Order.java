package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

import com.test.juxiaohui.shop.data.Chart.ChartItem;

public class Order {


	protected String mId;

	protected List<ChartItem> mItems;

	protected String mConsigneeName = "";

	protected String mConsigneePhoneNum = "";

	protected String mConsigneeAddress = "";

	protected String mOtherMessage = "";
	
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
			Goods goods = ShopDataManager.getInstance().getGoods(item.getID());
			result += item.getCount() * goods.getPrize();
		}
		return result;
	}

	public String getmConsigneeName() {
		return mConsigneeName;
	}

	public String getmOtherMessage() {
		return mOtherMessage;
	}

	public String getmConsigneeAddress() {
		return mConsigneeAddress;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}
}
