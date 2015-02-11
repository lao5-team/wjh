package com.test.juxiaohui.shop.app;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;
import com.test.juxiaohui.domain.UserManager;
import com.test.juxiaohui.shop.data.Chart;
import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.data.OrderManager;
import com.test.juxiaohui.shop.data.Chart.ChartItem;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class OrderListActivity extends Activity {
	
	ListView mListView;
	CommonAdapter<Order> mAdapter;
	List<Order> mOrderList;
	
	static class ViewHolder
	{
		TextView tvStatus;
		TextView tvId;
		TextView tvPay;
		Button btnRebuy;
		ListView lvGoods;
		
	}
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mOrderList = OrderManager.getInstance().getUsersOrderList(UserManager.getInstance().getCurrentUser().mID);
		mAdapter = new CommonAdapter<Order>(mOrderList, new IAdapterItem<Order>() {

			@Override
			public View getView(Order data, View convertView) {
				// TODO Auto-generated method stub
				if(null == convertView)
				{
					View itemView = getLayoutInflater().inflate(R.layout.item_order, null);
					ViewHolder holder = new ViewHolder();
					holder.tvStatus = (TextView)itemView.findViewById(R.id.textView_state);
					holder.tvId = (TextView)itemView.findViewById(R.id.textView_id);
					holder.tvPay = (TextView)itemView.findViewById(R.id.textView_price);
					holder.btnRebuy = (Button)itemView.findViewById(R.id.button_buy);
					holder.lvGoods = (ListView)itemView.findViewById(R.id.listView_goods);
					itemView.setTag(holder);
					convertView = itemView;
				}
				final Order fOrder = data;
				ViewHolder holder = (ViewHolder) convertView.getTag();
				holder.tvStatus.setText(getStateString(fOrder));
				holder.tvId.setText("订单号 " + fOrder.getmId());
				holder.tvPay.setText("支付金额 " + fOrder.calcTotalPrice() + " 元");
				holder.btnRebuy.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						//#
					}
				});
				holder.lvGoods.setAdapter(new CommonAdapter<ChartItem>(fOrder.getItems(), new Chart.AdapterItem(OrderListActivity.this, null)));
				return convertView;
			}
		});
	}
	
	String getStateString(Order order)
	{
//		if(order.getPayState() == Order.PAYSTATE_UNDONE)
//		{
//			return "待支付";
//		}
//		else if(order.getPayState() == Order.PAYSTATE_DONE)
//		{
//			return "支付完成";
//		}
		
		if(order.getState() == order.STATE_REFUSE)
		{
			return "拒绝订单";
		}
		else if(order.getState() == order.STATE_SENDING)
		{
			return "正在发货";
		}
		else if(order.getState() == order.STATE_RECEIVED)
		{
			return "已经收获";
		}
		return "未知状态";
	}

}
