package com.test.juxiaohui.shop.mediator;

import com.test.juxiaohui.shop.*;
import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.ShopCategory;

import java.util.*;
public interface IShopMediator {
	public void setCategoryList(List<ShopCategory> list);
	public void setSubCategoryList(List<ShopCategory> list);
	public void onClickCategoryItem(String id);
	public void onClickSubCategoryItem(String id);
}
