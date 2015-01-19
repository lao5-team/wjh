package com.test.juxiaohui.shop.app;

import java.util.List;

import com.test.juxiaohui.R;
import com.test.juxiaohui.data.DianpingDataHelper;
import com.test.juxiaohui.shop.data.Goods;
import com.test.juxiaohui.shop.data.ShopCategory;
import com.test.juxiaohui.shop.data.ShopDataManager;
import com.test.juxiaohui.shop.mediator.IShopMediator;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

public class ShopActivity extends FragmentActivity implements IShopMediator{

	
	@Override
	public void setCategoryList(List<ShopCategory> list) {
		if(null!=mCategoryFragment)
		{
			mCategoryFragment.setData(list);
		}
	}

	@Override
	public void setSubCategoryList(List<ShopCategory> list) {
		if(null!=mSubCategoryFragment)
		{
			mSubCategoryFragment.setData(list);
		}
	}

	@Override
	public void onClickCategoryItem(String id) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
//		if (null != fm.findFragmentByTag("location")) {
//			ft.remove(mFragLocation);
//			// ft.add(R.id.layout_middle, mFragCategory, "category");
//			// mFragCategory.getListView().invalidate();
//		} else {
		mSubCategoryFragment.setData(mListSubCategory);
		if(null == fm.findFragmentByTag(SUB_CAT))
		{
			ft.add(R.id.layout_middle, mSubCategoryFragment, SUB_CAT);
			ft.commit();
		}
			
//		}
		
//		if (null != fm.findFragmentByTag("MAIN_CAT")) {
//			ft.remove(mCategoryFragment);
//		}
		
		
	}

	@Override
	public void onClickSubCategoryItem(String id) {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(mSubCategoryFragment);
		ft.commit();
		Intent intent = new Intent(this, GoodsListActivity.class);
		IntentWrapper wrapper = new IntentWrapper(intent);
		wrapper.setSubCateID(id);
		this.startActivity(intent);
		
	}

	
	public static class IntentWrapper
	{
		Intent mIntent;
		public IntentWrapper(Intent intent)
		{
			mIntent = intent;
		}
		
		public void setSubCateID(String id)
		{
			mIntent.putExtra("id", id);
		}
		
		public String getSubCateID()
		{
			return mIntent.getStringExtra("id");
		}
	}
	
	private class CategoryItem implements IAdapterItem<ShopCategory>
	{
		@Override
		public View getView(ShopCategory data, View convertView) {
			// TODO Auto-generated method stub
			TextView tv = new TextView(ShopActivity.this);
			tv.setText(data.getName());
			return tv;
		}
		
	}
	
	
	public final String MAIN_CAT = "main_cat";
	public final String SUB_CAT = "sub_cat";
	
	private ShopCategoryFragment mCategoryFragment = null;
	private ShopSubCategoryFragment mSubCategoryFragment = null;
	private List<ShopCategory> mListCategory = null;
	private List<ShopCategory> mListSubCategory = null;
	private List<Goods> mListGoods = null;
	private IShopMediator mShopMediator = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initData();
		initUI();
		

	}
	
	private void initUI()
	{
		##setContentView(R.layout.activity_newactivity);
		mCategoryFragment = new ShopCategoryFragment(mListCategory, new CategoryItem(), mShopMediator);
		mSubCategoryFragment = new ShopSubCategoryFragment(mListSubCategory, new CategoryItem(), mShopMediator);
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(R.id.layout_middle, mCategoryFragment, "location");
		ft.commit();		
	}
	
	private void initData()
	{
		mListCategory = ShopDataManager.getInstance().getMainCategoryList();
		
	}

	

}
