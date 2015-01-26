package com.test.juxiaohui.shop.app;

import java.util.List;

import com.test.juxiaohui.R;
import com.test.juxiaohui.shop.data.ShopCategory;
import com.test.juxiaohui.shop.mediator.IShopMediator;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

public class ShopSubCategoryFragment extends ListFragment {
	
	List<ShopCategory> mCategoryList = null;
	IShopMediator mShopMediator = null;
	CommonAdapter<ShopCategory> mAdapter = null;
	
	public ShopSubCategoryFragment(List<ShopCategory> dataList, IAdapterItem<ShopCategory> item, IShopMediator mediator)
	{    	
    	if(null == item)
    	{
    		throw new IllegalArgumentException("item is null !");
    	}
    	if(null == mediator)
    	{
    		throw new IllegalArgumentException("mediator is null !");
    	}
		mCategoryList = dataList;
		mAdapter = new CommonAdapter<>(dataList, item);
		mShopMediator = mediator;
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {
    	LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_category, container, false);
//    	this.setListAdapter(mAdapter);
//    	this.getListView().setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				String cate_id = mCategoryList.get(position).getID();
//				mShopMediator.onClickCategoryItem(cate_id);
//			}
//		});
    	return layout;
    }
    
    
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	this.setListAdapter(mAdapter);
    }
    
    @Override
    public void onResume()
    {
    	super.onResume();
    	this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String cate_id = mCategoryList.get(position).getID();
				mShopMediator.onClickSubCategoryItem(cate_id);
			}
		});
    }
    
    public void setData(List<ShopCategory> dataList)
    {
    	if(null == dataList)
    	{
    		throw new IllegalArgumentException("dataList is null !");
    	}
    	mCategoryList = dataList;
    	mAdapter.setData(mCategoryList);
    	mAdapter.notifyDataSetChanged();
    }
    

}
