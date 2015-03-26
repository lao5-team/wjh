package com.pineapple.mobilecraft.mediator.shop;

import com.pineapple.mobilecraft.data.shop.Goods;

public interface IGoodsMediator {
	public void setGoods(Goods goods);
	public void addToFavorate();
	public void addToCart();

}
