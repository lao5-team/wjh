package com.pineapple.juxiaohui.shop.mediator;

import com.pineapple.juxiaohui.shop.data.Goods;

public interface IGoodsMediator {
	public void setGoods(Goods goods);
	public void addToFavorate();
	public void addToCart();

}
