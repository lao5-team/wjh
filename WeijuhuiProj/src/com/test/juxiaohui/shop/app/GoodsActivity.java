package com.test.juxiaohui.shop.app;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;
import com.test.juxiaohui.shop.app.GoodsListActivity.IntentWrapper;
import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.mediator.IGoodsMediator;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class GoodsActivity extends Activity implements IGoodsMediator{
	String TAG = GoodsActivity.class.getName();
	Goods mGoods = null;
	Button mBtnFav = null;
	Button mBtnCart = null;
	TextView mTvName = null;
	ImageView mIvPic = null;
	TextView mTvPrize = null;
	TextView mTvDesc = null;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initData();
		initUI();
	}

	@Override
	public void setGoods(Goods goods) {
		mGoods = goods;
		
	}

	@Override
	public void addToFavorate() {
		// TODO Auto-generated method stub
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(GoodsActivity.this, "没做收藏的功能", Toast.LENGTH_SHORT).show();
			}
		});
		Log.v(TAG, String.format("addToFavorate %s", mGoods.getID()));
	}

	@Override
	public void addToCart() {
		runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(GoodsActivity.this, "没做加入购物车的功能", Toast.LENGTH_SHORT).show();
			}
		});
		Log.v(TAG, String.format("addToCart %s", mGoods.getID()));
	}
	
	private void initUI()
	{
		setContentView(R.layout.activity_goods_layout);
		mTvName = (TextView)findViewById(R.id.textView_name);
		mTvName.setText(mGoods.getName());
		mTvPrize = (TextView)findViewById(R.id.textView_prize);
		mTvPrize.setText(mGoods.getPrize() + " 元");
		mTvDesc = (TextView)findViewById(R.id.textView_desc);
		mTvDesc.setText(mGoods.getDesc());
		mIvPic = (ImageView)findViewById(R.id.imageView_goods);
		Picasso.with(this).load(mGoods.getImageURL()).into(mIvPic);
		
		mBtnFav = (Button)findViewById(R.id.button_favorite);
		mBtnFav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToFavorate();
			}
		});
		
		mBtnCart = (Button)findViewById(R.id.button_add_cart);
		mBtnCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToCart();
			}
		});
		
	}
	
	private void initData()
	{
		Intent intent = getIntent();
		IntentWrapper intentWrapper = new IntentWrapper(intent);
		Goods goods = intentWrapper.getGoods(); 
		setGoods(goods);
	}
	
}
