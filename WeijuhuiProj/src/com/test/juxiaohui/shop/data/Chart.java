package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

public class Chart {
	public static class ChartItem
	{
		private String mID = null;
		private int mCount;
		private boolean mIsSelected = false;
		
		public ChartItem(String id, int count)
		{
			setID(id);
			setCount(count);
		}
		
		private void setID(String id)
		{
			Goods goods = ShopDataManager.getInstance().getGoods(id);
			if(goods!= Goods.NULL)
			{
				mID = id;
			}
			else
			{
				throw new IllegalArgumentException("invalid id!");
			}
		}
		
		public String getID() {
			return mID;
		}
		
		public int getCount() {
			return mCount;
		}
		
		public void setCount(int mCount) {
			if(mCount < 0)
			{
				throw new IllegalArgumentException("invalid count");
			}
			this.mCount = mCount;
		}
		
		public boolean isSelected() {
			return mIsSelected;
		}
		
		public void setSelected(boolean mIsSelected) {
			this.mIsSelected = mIsSelected;
		}

		
		
		
	}
	private static Chart mInstance = null;
	List<ChartItem> mItems = new ArrayList<ChartItem>();
		
	public static Chart getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new Chart();
		}
		return mInstance;
	}
	
	public void addItem(ChartItem item)
	{
		if(null == item)
		{
			throw new IllegalArgumentException("item is null");
		}
		for(ChartItem chartItem:mItems)
		{
			if(chartItem.mID.endsWith(item.mID))
			{
				chartItem.mCount += item.mCount;
				return;
			}
		}
		mItems.add(item);
	}
	
	public void setItem(ChartItem item)
	{
		if(null == item)
		{
			throw new IllegalArgumentException("item is null");
		}
		for(ChartItem chartItem:mItems)
		{
			if(chartItem.mID.endsWith(item.mID))
			{
				chartItem.mCount += item.mCount;
				return;
			}
		}		
	}
	
//	public void setItems(List<ChartItem> itemList)
//	{
//		if(null == itemList)
//		{
//			throw new IllegalArgumentException("itemList is null");
//		}
//		else
//		{
//			
//		}
//	}
	
	/**将一组商品添加到购物车里去，如果数组中包含无效id，则添加全部失败
	 * @param IDs
	 * @throws IllegalArgumentException
	 */
//	public void addGoodsList(List<String> IDs) throws IllegalArgumentException
//	{
//		List<Goods> goodsList = ShopDataManager.getInstance().getGoodsList(IDs);
//		for(Goods goods:goodsList)
//		{
//			if(goods!=Goods.NULL )
//			{
//				ChartItem item = new ChartItem();
//				item.mID = goods.mID;
//				item.mCount = 1;
//				item.mIsSelected = false;
//				mItems.add(item);
//			}
//			else
//			{
//				mItems.clear();
//				throw new IllegalArgumentException("goods id " + goods.mID + " not exsits!");
//			}
//		}
//	}
	

	
//	public List<String> getGoodsIDs()
//	{
//		List<String> listIDs = new ArrayList<String>();
//		for(ChartItem item:mItems)
//		{
//			listIDs.add(item.mID);
//		}
//		return listIDs;
//	}
//	
//	public int getGoodsCount(String id) throws IllegalArgumentException
//	{
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				return item.mCount;
//			}
//		}
//		
//		throw new IllegalArgumentException("invalid id");
//	}
//	
//	public void setGoodsCount(String id, int count) throws IllegalArgumentException
//	{
//		if(count<0)
//		{
//			throw new IllegalArgumentException("invalid count!");
//		}
//		
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				item.mCount = count;
//			}
//		}
//		throw new IllegalArgumentException("invalid id!");
//	}
//	
//	public void selectGoods(String id) throws IllegalArgumentException
//	{
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				item.mIsSelected = true;
//			}
//		}
//		throw new IllegalArgumentException("invalid id!");
//	}
//	
//	public void unselectGoods(String id) throws IllegalArgumentException
//	{
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				item.mIsSelected = false;
//			}
//		}
//		throw new IllegalArgumentException("invalid id!");
//	}
	
	/**创建一个初步订单
	 * @return
	 */
	public Order createOrder()
	{
		List<ChartItem> selectedChartItems = new ArrayList<ChartItem>();
		for(ChartItem item:mItems)
		{
			if(true == item.mIsSelected)
			{
				selectedChartItems.add(item);
			}
		}
		Order order;
		if(selectedChartItems.size()>0)
		{
			order = new Order(selectedChartItems);
		}
		else
		{
			order = Order.NULL;
		}
		return order;
	}
	
	private Chart()
	{
		
	}
	
	

}
