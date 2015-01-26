package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

public class Chart {
	public static class ChartItem
	{
		public String mID = null;
		public int mCount;
		public boolean mIsSelected = false;
		
	}
	private static Chart mInstance = null;
	private List<ChartItem> mItems = new ArrayList<ChartItem>();
	
	private Chart()
	{
		
	}
	
	public void addGoodsList(List<String> IDs) throws IllegalArgumentException
	{
		List<Goods> goodsList = ShopDataManager.getInstance().getGoodsList(IDs);
		for(Goods goods:goodsList)
		{
			if(goods!=Goods.NULL )
			{
				ChartItem item = new ChartItem();
				item.mID = goods.mID;
				item.mCount = 1;
				item.mIsSelected = false;
				mItems.add(item);
			}
		}
	}
	
	public Chart getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new Chart();
		}
		return mInstance;
	}
	
	public List<String> getGoodsIDs()
	{
		List<String> listIDs = new ArrayList<String>();
		for(ChartItem item:mItems)
		{
			listIDs.add(item.mID);
		}
		return listIDs;
	}
	
	public int getGoodsCount(String id) throws IllegalArgumentException
	{
		for(ChartItem item:mItems)
		{
			if(id.endsWith(item.mID))
			{
				return item.mCount;
			}
		}
		
		throw new IllegalArgumentException("invalid id");
	}
	
	public void setGoodsCount(String id, int count) throws IllegalArgumentException
	{
		if(count<0)
		{
			throw new IllegalArgumentException("invalid count!");
		}
		
		for(ChartItem item:mItems)
		{
			if(id.endsWith(item.mID))
			{
				item.mCount = count;
			}
		}
		throw new IllegalArgumentException("invalid id!");
	}
	
	public void selectGoods(String id) throws IllegalArgumentException
	{
		for(ChartItem item:mItems)
		{
			if(id.endsWith(item.mID))
			{
				item.mIsSelected = true;
			}
		}
		throw new IllegalArgumentException("invalid id!");
	}
	
	public void unselectGoods(String id) throws IllegalArgumentException
	{
		for(ChartItem item:mItems)
		{
			if(id.endsWith(item.mID))
			{
				item.mIsSelected = false;
			}
		}
		throw new IllegalArgumentException("invalid id!");
	}
	
	public Order createOrder()
	{
		#
	}
	
	

}
