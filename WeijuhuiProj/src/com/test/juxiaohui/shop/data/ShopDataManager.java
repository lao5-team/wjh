package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

import com.test.juxiaohui.shop.server.ShopServer;

public class ShopDataManager {
	
	private static ShopDataManager mInstance = null;
	
	public static ShopDataManager getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new ShopDataManager();
		}
		return mInstance;
	}
	
	private ShopDataManager()
	{
		
	}
	
	/**
	 * 获取所有一级分类
	 * @return
	 */
	public List<ShopCategory> getMainCategoryList()
	{
		return ShopServer.getInstance().getCategoryList();
	}
	
	/**通过一级分类id获取所有商品
	 * @param id 一级分类
	 * @return
	 */
	public List<ShopCategory> getSubCategoryList(String id)
	{
		return ShopServer.getInstance().getSubCategoryList(id);
	}
	
	/**
	 * 通过二级分类id获取所有商品
	 * @param id 二级分类id
	 * @return
	 */
	public List<Goods> getGoodsListinCategory(String id, int startIndex, int endIndex)
	{
		return ShopServer.getInstance().getGoodsListinCategory(id, startIndex, endIndex);
	}
	
	
	/** 获取某一个商品信息
	 * @param IDs
	 * @return
	 */
	public List<Goods> getGoodsList(List<String> IDs)
	{
		return ShopServer.getInstance().getGoodsListbyIDs(IDs);
	}
	
	public Goods getGoods(String id)
	{
		if(null == id)
		{
			return Goods.NULL;
		}
		List<String> mIDs = new ArrayList<String>();
		mIDs.add(id);
		List<Goods> goodsList = ShopServer.getInstance().getGoodsListbyIDs(mIDs);
		if(goodsList.size()>0)
		{
			return goodsList.get(0);
		}
		else
		{
			return Goods.NULL;
		}
	}
	
	
	
	
	

}
