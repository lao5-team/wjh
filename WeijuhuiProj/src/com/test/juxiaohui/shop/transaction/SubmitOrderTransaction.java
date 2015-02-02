package com.test.juxiaohui.shop.transaction;

import org.json.JSONException;

import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.server.ShopServer;

public class SubmitOrderTransaction {
	
	private Order mOrder;
	public SubmitOrderTransaction(Order order)
	{
		mOrder = order;
	}
	
	/**执行提交，返回得到的id
	 * @return
	 */
	public String execute()
	{
		try {
			return ShopServer.getInstance().submitOrder(mOrder);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "fail";
		}
	}

}
