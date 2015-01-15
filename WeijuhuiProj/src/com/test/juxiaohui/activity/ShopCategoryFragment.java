package com.test.juxiaohui.activity;

import java.util.List;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mediator.IShopMediator;
import com.test.juxiaohui.shop.server.ShopCategory;

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

public class ShopCategoryFragment extends ListFragment {
	
	private BaseAdapter mAdapter = new BaseAdapter() {
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
		
		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}
		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 0;
		}
	};
	List<ShopCategory> mCategoryList;
	IShopMediator mShopMediator;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
            Bundle savedInstanceState) {
    	LinearLayout layout = (LinearLayout)inflater.inflate(R.layout.fragment_category, container, false);
    	this.setListAdapter(mAdapter);
    	this.getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String cate_id = mCategoryList.get(position).getID();
				mShopMediator.onClickCategoryItem(cate_id);
			}
		});
    	return layout;
    }
    
    public void setData(List<ShopCategory> list)
    {
    	if(null == list)
    	{
    		throw new IllegalArgumentException("list is null !");
    	}
    	mCategoryList = list;
    }

}
