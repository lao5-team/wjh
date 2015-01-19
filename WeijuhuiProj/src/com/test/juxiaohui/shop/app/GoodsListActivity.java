package com.test.juxiaohui.shop.app;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GoodsListActivity extends Activity {
	
	private class GoodsItem implements IAdapterItem<Goods>
	{
		@Override
		public View getView(Goods data, View convertView) {
			// TODO Auto-generated method stub
			return null;
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
	
	private CommonAdapter mAdapter;
	private List<Goods> mDataList;
	private ListView mListView;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initData();
		initUI();
		mAdapter = new CommonAdapter<>(mDataList, new GoodsItem());
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
		#
	}
	
	private void initUI()
	{
		#
	}
	
	private void initData()
	{
		#
	}

}
