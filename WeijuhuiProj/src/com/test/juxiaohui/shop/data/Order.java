package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.shop.data.Chart.ChartItem;

public class Order {

	public static int STATE_INIT = -1;
	public static int STATE_SENDING = 0;
	public static int STATE_RECEIVED = 1;
	public static int STATE_REFUSE = 2;
	
	public static int PAYSTATE_UNDONE = 0;
	public static int PAYSTATE_DONE = 1;
	protected String mId;

	protected List<ChartItem> mItems;

	protected String mConsigneeName = "";

	protected String mConsigneePhoneNum = "";

	protected String mConsigneeAddress = "";

	protected String mOtherMessage = "";
	
	protected int mState = 0;
	
	protected int mPayState = 0;
	
	public static Order NULL = new Order()
	{
		@Override
		public float calcTotalPrice()
		{
			return 0.0f;
		}
	};
	
	private Order()
	{
		
	}
	
	public Order(List<ChartItem> items)
	{
		if(null == items)
		{
			throw new IllegalArgumentException("items is null!");
		}
		mItems = items;
	}

	public void setConsigneeInfo(String name, String phoneNum, String address)
	{
		mConsigneeName = name;
		mConsigneePhoneNum = phoneNum;
		mConsigneeAddress = address;
	}

	/**
	 * 计算订单总金额
	 * @return
	 */
	public float calcTotalPrice()
	{
		float result = 0.0f;
		for(ChartItem item:mItems)
		{
			Goods goods = ShopDataManager.getInstance().getGoods(item.getID());
			result += item.getCount() * goods.getPrize();
		}
		return result;
	}

	public String getmConsigneeName() {
		return mConsigneeName;
	}

	public String getOtherMessage() {
		return mOtherMessage;
	}

	public String getmConsigneeAddress() {
		return mConsigneeAddress;
	}

	public String getmId() {
		return mId;
	}

	public void setmId(String mId) {
		this.mId = mId;
	}
	
	public List<ChartItem> getItems() {
		return mItems;
	}

	public int getState() {
		return mState;
	}

	public void setState(int mState) {
		this.mState = mState;
	}
	
	public int getPayState()
	{
		return mPayState;
	}
	
	public void setPayState(int payState)
	{
		 this.mPayState = payState;
	}
	
	public String getConsigneePhoneNumber()
	{
		return mConsigneePhoneNum;
	}
	
	public static Order fromJSON(JSONObject object)
	{
		/*            "id": "4",
            "oid": "oid14219975453510474",                        //订单编号
            "products": "{"1":2,"2":3}",							  //订单内容
            "other": "aaaa",                                      //其他需求
            "status": "0",             //订单状态 0发货中 1表示完成 2表示拒绝
"pay": "0",             //支付状态    0未支付  1已支付
            "address": "aaa",      //订单地址
            "create_time": "2015-01-23 15:19:05",  //下单时间
            "username": "aaa",				//收件人姓名
            "mobile": "1111",				//收件人电话
            "uid": "4"    
*/
		Order order = new Order();
		try {
			order.mId = object.getString("oid");
			JSONObject products = object.getJSONObject("products");
			Iterator<String> iter = products.keys();
			ArrayList<ChartItem> items = new ArrayList<ChartItem>();
			while(iter.hasNext())
			{
				String key = iter.next();
				ChartItem item = new ChartItem(key, products.getInt(key));
				items.add(item);
			}
			order.mItems = items;
			order.mConsigneeAddress = object.getString("address");
			order.mConsigneeName = object.getString("username");
			order.mConsigneePhoneNum = object.getString("mobile");
			order.mState = object.getInt("status");
			order.mPayState = object.getInt("pay");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			order = Order.NULL;
		}
		return order;
	}

	public static JSONObject toJSON(Order order)
	{
		JSONObject obj = new JSONObject();
		try {
			obj.put("oid", order.mId);
			obj.put("address", order.mConsigneeAddress);
			obj.put("username", order.mConsigneeName);
			obj.put("mobile", order.mConsigneePhoneNum);
			obj.put("status", order.mState);
			obj.put("pay", order.mPayState);
			JSONObject products = new JSONObject();
			for(ChartItem item:order.mItems)
			{
				products.put(item.getID(), item.getCount());
			}
			obj.put("products", products);

		} catch (JSONException e) {
			e.printStackTrace();
			obj = null;
		}

		return obj;
	}
}
