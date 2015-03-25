package com.test.juxiaohui.mdxc.data;

/**
 * Created by yihao on 15/3/13.
 */
public class PrizeData {
	
	public static PrizeData NULL = new PrizeData();
	public float mTicketPrize = 0;
	public float mTax = 0;
	
	public PrizeData(float ticketPrize, float tax)
	{
		mTicketPrize = ticketPrize;
		mTax = tax;
	}
	
	public PrizeData()
	{
		
	}

}
