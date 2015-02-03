package com.test.juxiaohui.shop.app;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.test.juxiaohui.R;
import com.test.juxiaohui.shop.data.Chart;
import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.mediator.IOrderMediator;

import android.app.Activity;
import android.os.Bundle;
import com.test.juxiaohui.shop.transaction.CreateOrderTransaction;
import com.test.juxiaohui.widget.CommonAdapter;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderActivity extends Activity implements IOrderMediator{

	public static void startActivity(Activity activity, Order order)
	{
		Intent intent = new Intent();
		intent.setClass(activity, OrderActivity.class);
		IntentWrapper wrapper = new IntentWrapper(intent);
		wrapper.setOrder(order);
		activity.startActivity(intent);
	}


	public static class IntentWrapper
	{
		Intent mIntent = null;
		public IntentWrapper(Intent intent)
		{
			mIntent = intent;
		}

		public void setOrder(Order order)
		{
			mIntent.putExtra("order", Order.toJSON(order).toString());
		}

		public Order getOrder()
		{
			try {
				JSONObject obj = new JSONObject(mIntent.getStringExtra("order"));
				return Order.fromJSON(obj);
			} catch (JSONException e) {
				e.printStackTrace();
				return Order.NULL;
			}

		}
	}

	Order mOrder = null;
	//textView_cname
	TextView mTvConsigneeName;

	//textView_cphone
	TextView mTvConsigneePhone;

	//textView_caddress
	TextView mTvConsigneeAddress;

	ListView mListView;

	CommonAdapter<Chart.ChartItem> mChartItemAdapter;

	TextView mTvPaySum;

	Button mBtnOrder;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		IntentWrapper wrapper = new IntentWrapper(intent);
		setOrder(wrapper.getOrder());
		setContentView(R.layout.activity_order_detail);
		showConsignee();
	}

	@Override
	public void setOrder(Order order) {
		if(order!=Order.NULL)
		{
			mOrder = order;
		}
		else
		{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(OrderActivity.this, "无效订单", Toast.LENGTH_SHORT).show();
					try {
						Thread.currentThread().sleep(1000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					finish();
				}
			});

		}
		mChartItemAdapter = new CommonAdapter<Chart.ChartItem>(mOrder.getItems(), new Chart.AdapterItem(null, null));
	}

	@Override
	public void openGoods() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showConsignee() {
		mTvConsigneeName = (TextView)findViewById(R.id.textView_cname);
		mTvConsigneeName.setText(mOrder.getmConsigneeName());

		mTvConsigneePhone = (TextView)findViewById(R.id.textView_cphone);
		mTvConsigneePhone.setText(mOrder.getConsigneePhoneNumber());

		mTvConsigneeAddress = (TextView)findViewById(R.id.textView_caddress);
		mTvConsigneeAddress.setText(mOrder.getmConsigneeAddress());
		
	}

	@Override
	public void showGoods() {
		mListView = (ListView)findViewById(R.id.listView_goods);
		mListView.setAdapter(mChartItemAdapter);
		
	}

	@Override
	public void showPayment() {
		mTvPaySum = (TextView)findViewById(R.id.textView_paysum);
		mTvPaySum.setText("实付款 " + mOrder.calcTotalPrice());
		
	}

	@Override
	public void submitOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void payOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void cancelOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void confirmOrder() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOrderState() {

		mBtnOrder = (Button)findViewById(R.id.button_order);

		if(mOrder.getState()==Order.STATE_INIT)
		{
			mBtnOrder.setText("提交订单");
		}
		else if(mOrder.getState()==Order.PAYSTATE_UNDONE)
		{
			mBtnOrder.setText("支付订单");
		}
		else if(mOrder.getState()==Order.STATE_SENDING)
		{
			mBtnOrder.setText("确认收货");
		}
		else if(mOrder.getState()==Order.STATE_RECEIVED)
		{
			mBtnOrder.setText("重新购买");
		}

		mBtnOrder.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(mOrder.getState()==Order.STATE_INIT)
				{
					submitOrder();
				}
				else if(mOrder.getState()==Order.PAYSTATE_UNDONE)
				{
					payOrder();
				}
				else if(mOrder.getState()==Order.STATE_SENDING)
				{
					confirmOrder();
				}
				else if(mOrder.getState()==Order.STATE_RECEIVED)
				{
					mBtnOrder.setText("重新购买");
					Order order = CreateOrderTransaction.createOrderFromOrder(mOrder);
				}
			}
		});
	}
}
