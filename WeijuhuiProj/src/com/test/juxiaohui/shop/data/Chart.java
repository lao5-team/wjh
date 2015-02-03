package com.test.juxiaohui.shop.data;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;
import com.test.juxiaohui.shop.app.ChartActivity;
import com.test.juxiaohui.shop.app.GoodsActivity;
import com.test.juxiaohui.shop.mediator.IChartMediator;
import com.test.juxiaohui.widget.IAdapterItem;

public class Chart {
	public static class ChartItem
	{
		private String mID = null;
		private int mCount;
		private boolean mIsSelected = false;

		public static ChartItem NULL = new ChartItem(null, 0);
		
		public ChartItem(String id, int count)
		{
			setID(id);
			setCount(count);
		}
		
		private void setID(String id)
		{

			if(null!=id && id.length()>0)
			{
				Goods goods = ShopDataManager.getInstance().getGoods(id);
				if(goods!= Goods.NULL)
				{
					mID = id;
				}
			}

		}
		
		public String getID() {
			return mID;
		}
		
		public int getCount() {
			return mCount;
		}
		
		private void setCount(int mCount) {
			if(mCount < 0)
			{
				throw new IllegalArgumentException("invalid count");
			}
			this.mCount = mCount;
		}
		
		public boolean isSelected() {
			return mIsSelected;
		}
		
		private void setSelected(boolean mIsSelected) {
			this.mIsSelected = mIsSelected;
		}

		
		
		
	}
	private static Chart mInstance = null;
	List<ChartItem> mItems = new ArrayList<ChartItem>();
		
	public static Chart getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new Chart();
		}
		return mInstance;
	}
	
	public void addGoods(String id, int count) throws IllegalArgumentException
	{
		if(null == id||count<=0)
		{
			throw new IllegalArgumentException("invalid id or count");
		}
		Goods goods = ShopDataManager.getInstance().getGoods(id);
		if(Goods.NULL == goods)
		{
			throw new IllegalArgumentException("invalid id");
		}
		for(ChartItem chartItem:mItems)
		{
			if(chartItem.mID.endsWith(id))
			{
				chartItem.mCount += count;
				return;
			}
		}

		ChartItem item = new ChartItem(id, count);
		mItems.add(item);
	}

	public void setItemSelected(String id, boolean selected)
	{
		for(ChartItem chartItem:mItems)
		{
			if(chartItem.mID.endsWith(id))
			{
				chartItem.setSelected(selected);
				return;
			}
		}
		throw new IllegalArgumentException("invalid id");

	}

	public void setItemCount(String id, int count)
	{
		if(null == id||count<0)
		{
			throw new IllegalArgumentException("invalid id or count");
		}
		if(count == 0)
		{
			removeItem(id);
		}
		else
		{
			for(ChartItem chartItem:mItems)
			{
				if(chartItem.mID.endsWith(id))
				{
					chartItem.mCount = count;
					return;
				}
			}
			throw new IllegalArgumentException("invalid id");
		}


	}

	public void removeItem(String id)
	{
		if(null == id)
		{
			throw new IllegalArgumentException("invalid id");
		}
		for(ChartItem chartItem:mItems)
		{
			if(chartItem.mID.endsWith(id))
			{
				mItems.remove(chartItem);
				return;
			}
		}
		throw new IllegalArgumentException("invalid id");
	}

	public ChartItem getItem(String id)
	{
		if(null == id)
		{
			throw new IllegalArgumentException("invalid id");
		}
		for(ChartItem chartItem:mItems)
		{
			if(chartItem.mID.endsWith(id))
			{
				return chartItem;
			}
		}
		return ChartItem.NULL;
	}
	
//	public void setItems(List<ChartItem> itemList)
//	{
//		if(null == itemList)
//		{
//			throw new IllegalArgumentException("itemList is null");
//		}
//		else
//		{
//			
//		}
//	}
	
	/**将一组商品添加到购物车里去，如果数组中包含无效id，则添加全部失败
	 * @param IDs
	 * @throws IllegalArgumentException
	 */
//	public void addGoodsList(List<String> IDs) throws IllegalArgumentException
//	{
//		List<Goods> goodsList = ShopDataManager.getInstance().getGoodsList(IDs);
//		for(Goods goods:goodsList)
//		{
//			if(goods!=Goods.NULL )
//			{
//				ChartItem item = new ChartItem();
//				item.mID = goods.mID;
//				item.mCount = 1;
//				item.mIsSelected = false;
//				mItems.add(item);
//			}
//			else
//			{
//				mItems.clear();
//				throw new IllegalArgumentException("goods id " + goods.mID + " not exsits!");
//			}
//		}
//	}
	

	
	public List<String> getGoodsIDs()
	{
		List<String> listIDs = new ArrayList<String>();
		for(ChartItem item:mItems)
		{
			listIDs.add(item.mID);
		}
		return listIDs;
	}
//	
//	public int getGoodsCount(String id) throws IllegalArgumentException
//	{
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				return item.mCount;
//			}
//		}
//		
//		throw new IllegalArgumentException("invalid id");
//	}
//	
//	public void setGoodsCount(String id, int count) throws IllegalArgumentException
//	{
//		if(count<0)
//		{
//			throw new IllegalArgumentException("invalid count!");
//		}
//		
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				item.mCount = count;
//			}
//		}
//		throw new IllegalArgumentException("invalid id!");
//	}
//	
//	public void selectGoods(String id) throws IllegalArgumentException
//	{
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				item.mIsSelected = true;
//			}
//		}
//		throw new IllegalArgumentException("invalid id!");
//	}
//	
//	public void unselectGoods(String id) throws IllegalArgumentException
//	{
//		for(ChartItem item:mItems)
//		{
//			if(id.endsWith(item.mID))
//			{
//				item.mIsSelected = false;
//			}
//		}
//		throw new IllegalArgumentException("invalid id!");
//	}
	
	public List<ChartItem> getItems()
	{
		return new ArrayList<ChartItem>(mItems);
	}
	
	/**创建一个初步订单
	 * @return
	 */
//	public Order createOrder()
//	{
//		List<ChartItem> selectedChartItems = new ArrayList<ChartItem>();
//		for(ChartItem item:mItems)
//		{
//			if(true == item.mIsSelected)
//			{
//				selectedChartItems.add(item);
//			}
//		}
//		Order order;
//		if(selectedChartItems.size()>0)
//		{
//			order = new Order(selectedChartItems);
//		}
//		else
//		{
//			order = Order.NULL;
//		}
//		return order;
//	}
	
	public float getTotalPrice()
	{
		float total = 0;
		List<ChartItem> listItem = mItems;
		for(ChartItem item:listItem)
		{
			if(item.isSelected())
			{
				final Goods fGoods = ShopDataManager.getInstance().getGoods(item.getID());
				total += item.getCount() * fGoods.getPrize();
			}

		}
		return total;
	}
	
	private Chart()
	{
		
	}
	
	private static class ViewHolder
	{
		CheckBox cbSelect;
		ImageView ivGoods;
		TextView tvName;
		TextView tvPrice;
		ImageButton btnDecrease;
		ImageButton btnIncrease;
		TextView tvCount;
	};
	
	public static class AdapterItem implements IAdapterItem<ChartItem>
	{
		IChartMediator mMediator = null;
		Activity mActivity = null;
		/**
		 * 只有当mediator不为空是，才可以改变物品数量
		 * @param mediator
		 */
		public AdapterItem(Activity activity, IChartMediator mediator)
		{
			mActivity = activity;
			mMediator = mediator;

		}
		@Override
		public View getView(ChartItem data, View convertView) {
			if(null == convertView)
			{
				View itemView = mActivity.getLayoutInflater().inflate(R.layout.item_chart, null);
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
			if(null != mMediator)
			{
				holder.cbSelect.setSelected(data.isSelected());
				holder.cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						mMediator.selectItem(fData.getID(), isChecked);
					}
				});				
			}
			else
			{
				holder.cbSelect.setVisibility(View.GONE);
			}

			Picasso.with(mActivity).load(fGoods.getImageURL()).into(holder.ivGoods);
			holder.ivGoods.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(mActivity, GoodsActivity.class);
					GoodsActivity.IntentWrapper wrapper = new GoodsActivity.IntentWrapper(intent);
					Goods goods = ShopDataManager.getInstance().getGoods(fData.getID());
					wrapper.setGoods(goods);
					mActivity.startActivity(intent);

				}
			});
			holder.tvName.setText(fGoods.getName());
			holder.tvPrice.setText(fGoods.getPrize() + " 元");
			
			if(null != mMediator)
			{
				holder.btnDecrease.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mMediator.changeItemCount(fData.getID(), fData.getCount() - 1);
					}
				});				
			}
			else
			{
				holder.btnDecrease.setVisibility(View.GONE);
			}

			
			holder.tvCount.setText(fData.getCount());
			
			if(null != mMediator)
			{
				holder.btnIncrease.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mMediator.changeItemCount(fData.getID(), fData.getCount() + 1);
					}
				});				
			}
			else
			{
				holder.btnIncrease.setVisibility(View.GONE);
			}

			return convertView;
		}
		 
	}
	
	

}
