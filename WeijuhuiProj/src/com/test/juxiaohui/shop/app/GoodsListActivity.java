package com.test.juxiaohui.shop.app;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;
import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.ShopDataManager;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @author yh
 * 商品列表
 */
public class GoodsListActivity extends Activity {
	
	private class GoodsItem implements IAdapterItem<Goods>
	{
		private class ViewHolder
		{
			ImageView mIvPic;
			TextView mTvName;
			TextView mTvPrize;
		}
		@Override
		public View getView(Goods data, View convertView) {
			// TODO Auto-generated method stub
			if(null == convertView)
			{
				View view = getLayoutInflater().inflate(R.layout.item_goods, null);
				ViewHolder holder = new ViewHolder();
				holder.mIvPic = (ImageView)view.findViewById(R.id.imageView_goods);
				holder.mTvName = (TextView)view.findViewById(R.id.textView_name);
				holder.mTvPrize = (TextView)view.findViewById(R.id.textView_prize);
				view.setTag(holder);
				convertView = view;
			}
			ViewHolder holder = (ViewHolder)convertView.getTag();
			holder.mTvName.setText(data.getName());
			holder.mTvPrize.setText(data.getPrize() + "元");
			Picasso.with(GoodsListActivity.this).load(data.getImageURL()).into(holder.mIvPic);
			return convertView;
		}		
	}
	
	public static class IntentWrapper
	{
		Intent mIntent = null;
		public IntentWrapper(Intent intent)
		{
			mIntent = intent;
		}
		
		public void setGoods(Goods goods)
		{
			JSONObject json = Goods.toJSON(goods);
			mIntent.putExtra("goods", json.toString());
		}
		
		public Goods getGoods()
		{
			try {
				JSONObject json = new JSONObject(mIntent.getStringExtra("goods"));
				Goods goods = Goods.fromJSON(json);
				return goods;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
			
		}
	}
	
	private CommonAdapter<Goods> mAdapter;
	private List<Goods> mDataList = null;
	private ListView mListView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initUI();
	}
	
	private void initUI()
	{
		setContentView(R.layout.activity_goods_list);
		mListView = (ListView)findViewById(R.id.listView_goods);
		mAdapter = new CommonAdapter<Goods>(mDataList, new GoodsItem());
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Goods goods = mDataList.get(position);
				Intent intent = new Intent(GoodsListActivity.this, GoodsActivity.class);
				IntentWrapper wrapper = new IntentWrapper(intent);
				wrapper.setGoods(goods);
				GoodsListActivity.this.startActivity(intent);
			}
		});
	}
	
	private void initData()
	{
		Intent intent = getIntent();
		ShopActivity.IntentWrapper wrapper = new ShopActivity.IntentWrapper(intent);
		mDataList = ShopDataManager.getInstance().getGoodsList(wrapper.getSubCateID());
	}

}
