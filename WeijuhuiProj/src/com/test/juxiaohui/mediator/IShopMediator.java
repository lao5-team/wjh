package com.test.juxiaohui.mediator;

import com.test.juxiaohui.shop.*;
import com.test.juxiaohui.shop.server.Goods;
import com.test.juxiaohui.shop.server.ShopCategory;

import java.util.*;
public interface IShopMediator {
	public void setCategoryList(List<ShopCategory> list);
	public void setSubCategoryList(List<ShopCategory> list);
	public void setGoodsList(List<Goods> list);
	public void onClickCategoryItem(String id);
	public void onClickSubCategoryItem(String id);
	public void onClickGoodsItem(String id);
}
