package com.test.juxiaohui.shop.app;

import java.util.List;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;
import com.test.juxiaohui.shop.app.GoodsActivity.IntentWrapper;
import com.test.juxiaohui.shop.data.Chart;
import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.Order;
import com.test.juxiaohui.shop.data.ShopDataManager;
import com.test.juxiaohui.shop.data.Chart.ChartItem;
import com.test.juxiaohui.shop.mediator.IChartMediator;
import com.test.juxiaohui.shop.transaction.CreateOrderTransaction;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.content.Intent;

public class ChartActivity extends Activity implements IChartMediator{
	
	private Chart mChart = null;
	
	private CommonAdapter<ChartItem> mAdapter = null;
	
	private ListView mListView;
	private CheckBox mCBSelectAll;
	private TextView mTvPrice;
	private Button mBtnPay;
	


		
		@Override
		public void showTotalPrice() {
			float totalPrice = mChart.getTotalPrice();
			ChartActivity.this.mTvPrice.setText("总金额:" + totalPrice +"元");
			if(totalPrice > 0)
			{
				mBtnPay.setEnabled(true);
			}
			else
			{
				mBtnPay.setEnabled(false);
			}
			
		}
		
		@Override
		public void showItems() {
			ChartActivity.this.mAdapter = new CommonAdapter<ChartItem>(mChart.getItems(), new Chart.AdapterItem(this, this));
			mListView.setAdapter(mAdapter);
		}
		
		@Override
		public void setChart(Chart chart) {
			ChartActivity.this.mChart = chart;
			
		}
		
		@Override
		public void selectItem(String id, boolean selected) {
			ChartActivity.this.mChart.setItemSelected(id, selected);
			showTotalPrice();
		}
		
		@Override
		public void selectAll(boolean selected) {
			List<ChartItem> itemList = mChart.getItems();
			for(ChartItem item:itemList)
			{
				ChartActivity.this.mChart.setItemSelected(item.getID(), selected);	
			}
			ChartActivity.this.mAdapter.notifyDataSetChanged();
			showTotalPrice();
		}
		
		@Override
		public void removeItem(String id) {
			// TODO Auto-generated method stub
			ChartActivity.this.mChart.removeItem(id);
			showTotalPrice();
		}
		
		@Override
		public void buyChart() {
			// TODO Auto-generated method stub
			//#
			Order order = CreateOrderTransaction.createOrderFromChart(Chart.getInstance());
			OrderActivity.startActivity(this, order);
		}
		
		@Override
		public void openGoodsDetail(Goods goods) {
			// TODO Auto-generated method stub
			Intent intent = new Intent();
			intent.setClass(this, GoodsActivity.class);
			IntentWrapper wrapper = new IntentWrapper(intent);
			wrapper.setGoods(goods);
			startActivity(intent);
		};
		
		@Override
		public void changeItemCount(String id, int count) {
			mChart.setItemCount(id, count);
			mAdapter.notifyDataSetChanged();
			showTotalPrice();
		}

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setChart(Chart.getInstance());
		setContentView(R.layout.activity_chart);
		mListView = (ListView)findViewById(R.id.listView_chart);
		mCBSelectAll = (CheckBox)findViewById(R.id.checkBox_selectAll);
		mCBSelectAll.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				selectAll(isChecked);
			}
		});
		
		mTvPrice = (TextView)findViewById(R.id.textView_price);
		mBtnPay = (Button)findViewById(R.id.button_buy);
		mBtnPay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				buyChart();
			}
		});
		showItems();
		showTotalPrice();
	}

}
