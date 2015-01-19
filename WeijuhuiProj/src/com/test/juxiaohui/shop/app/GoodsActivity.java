package com.test.juxiaohui.shop.app;

import com.squareup.picasso.Picasso;
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
		#
	}

	@Override
	public void setGoods(Goods goods) {
		mGoods = goods;
		
	}

	@Override
	public void addToFavorate() {
		// TODO Auto-generated method stub
		Log.v(TAG, String.format("addToFavorate %s", mGoods.getID()));
	}

	@Override
	public void addToCart() {
		// TODO Auto-generated method stub
		Log.v(TAG, String.format("addToCart %s", mGoods.getID()));
	}
	
	private void initUI()
	{
		setContentView();
		mTvName.setText(mGoods.getName());
		mTvPrize.setText(mGoods.getPrize() + " 元");
		mTvDesc.setText(mGoods.getDesc());
		Picasso.with(this).load(mGoods.getImageURL()).into(mIvPic);
		mBtnFav.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToFavorate();
			}
		});
		
		mBtnCart.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				addToCart();
			}
		});
		#
	}
	
	private void initData()
	{
		Intent intent = getIntent();
		IntentWrapper intentWrapper = new IntentWrapper(intent);
		Goods goods = intentWrapper.getGoods(); 
		setGoods(goods);
	}
	
}
