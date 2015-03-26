package com.pineapple.juxiaohui.shop.mediator;

import com.pineapple.juxiaohui.shop.*;
import com.pineapple.juxiaohui.shop.data.Goods;
import com.pineapple.juxiaohui.shop.data.ShopCategory;

import java.util.*;
public interface IShopMediator {
	public void setCategoryList(List<ShopCategory> list);
	public void setSubCategoryList(List<ShopCategory> list);
	public void onClickCategoryItem(String id);
	public void onClickSubCategoryItem(String id);
}
