package com.test.juxiaohui.shop.transaction;

import com.test.juxiaohui.shop.server.ShopServer;

public class CancelOrderTransaction {
	String mID;
	public CancelOrderTransaction(String id)
	{
		mID = id;
	}
	
	public Boolean execute()
	{
		return ShopServer.getInstance().cancelUsersOrder(mID);
	}

}
