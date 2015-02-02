package com.test.juxiaohui.shop.app;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;
import com.test.juxiaohui.domain.UserManager;
import com.test.juxiaohui.shop.app.ChartActivity.ViewHolder;
import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.data.OrderManager;
import com.test.juxiaohui.shop.data.ShopDataManager;
import com.test.juxiaohui.shop.data.Chart.ChartItem;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class OrderListActivity extends Activity {
	
	ListView mListView;
	CommonAdapter<Order> mAdapter;
	List<Order> mOrderList;
	
	static class ViewHolder
	{
		TextView tvStatus;
		TextView tvIId;
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
					View itemView = getLayoutInflater().inflate(R.layout.item_chart, null);
					ViewHolder holder = new ViewHolder();
					holder.cbSelect = (CheckBox)itemView.findViewById(R.id.checkBox_select);
					holder.ivGoods = (ImageView)itemView.findViewById(R.id.imageView_goods);
					holder.tvName = (TextView)itemView.findViewById(R.id.textView_goods_name);
					holder.tvPrice = (TextView)itemView.findViewById(R.id.textView_price);
					holder.btnDecrease = (ImageButton)itemView.findViewById(R.id.imageButton_sub);
					holder.btnIncrease = (ImageButton)itemView.findViewById(R.id.imageButton_add);
					holder.tvCount = (TextView)itemView.findViewById(R.id.textView_count);
					itemView.setTag(holder);
					convertView = itemView;
				}
				final ChartItem fData = data;
				final Goods fGoods = ShopDataManager.getInstance().getGoods(data.getID());
				ViewHolder holder = (ViewHolder)convertView.getTag();
				holder.cbSelect.setSelected(data.isSelected());
				holder.cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						selectItem(fData.getID(), isChecked);
					}
				});
				Picasso.with(ChartActivity.this).load(fGoods.getImageURL()).into(holder.ivGoods);
				holder.tvName.setText(fGoods.getName());
				holder.tvPrice.setText(fGoods.getPrize() + " 元");
				holder.btnDecrease.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						changeItemCount(fData.getID(), fData.getCount() - 1);
					}
				});
				
				holder.tvCount.setText(fData.getCount());
				
				holder.btnIncrease.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						changeItemCount(fData.getID(), fData.getCount() + 1);
					}
				});
				return null;
			}
		});
	}

}
