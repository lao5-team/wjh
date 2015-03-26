package com.pineapple.mobilecraft.mediator.shop;

import com.pineapple.mobilecraft.data.shop.Goods;
import com.pineapple.mobilecraft.data.shop.ShopCategory;
import com.pineapple.mobilecraft.shop.*;

import java.util.*;
public interface IShopMediator {
	public void setCategoryList(List<ShopCategory> list);
	public void setSubCategoryList(List<ShopCategory> list);
	public void onClickCategoryItem(String id);
	public void onClickSubCategoryItem(String id);
}
