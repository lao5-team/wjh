package com.test.juxiaohui.shop.mediator;

import com.test.juxiaohui.shop.data.Goods;

public interface IGoodsMediator {
	public void setGoods(Goods goods);
	public void addToFavorate();
	public void addToCart();

}
