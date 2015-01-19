package com.test.juxiaohui.shop.data;

import java.util.List;

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
		return null;
	}
	
	/**通过一级分类id获取所有商品
	 * @param id 一级分类
	 * @return
	 */
	public List<ShopCategory> getSubCategoryList(String id)
	{
		return null;
	}
	
	/**
	 * 通过二级分类id获取所有商品
	 * @param id 二级分类id
	 * @return
	 */
	public List<Goods> getGoodsList(String id)
	{
		return null;
	}
	
	
	

}
