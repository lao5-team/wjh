package com.test.weijuhui.activity;

import com.test.weijuhui.R;
import com.test.weijuhui.adapter.ActivityAdapter;
import com.test.weijuhui.adapter.ActivityCategoryAdapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NavigationFragment extends Fragment {

	//ui widgets
	TextView mTvCitySelect;
	SearchView mEtxSearch;
	GridView mGVCategory;
	ListView mLvActivity;
	
	//data
	String mCity;
	ActivityCategoryAdapter mCategoryAdapter;
	ActivityAdapter mHotActivityAdapter;
	//String []mCities =  {"北京","上海","广州","深圳"};
	String []mCategories = {"吃饭", "打球", "恐龙", "泡妹子","喝咖啡","斗地主"};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_navigation, container, false);
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		initUI();
	}
	
	private void initData()
	{
		mCategoryAdapter = new ActivityCategoryAdapter(getActivity());
		mCategoryAdapter.setData(mCategories);

		mHotActivityAdapter = new ActivityAdapter(this);
		
		mCity = "北京";
		
	}
	
	private void initUI()
	{
		mTvCitySelect = (TextView)getView().findViewById(R.id.textView_city);
		mTvCitySelect.setText(mCity);
		mTvCitySelect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//拉起城市选择Activity
				
			}
		});
		
		mEtxSearch = (SearchView)getView().findViewById(R.id.searchView_search);
		mEtxSearch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//拉起搜索Activity
			}
		});
		
		mGVCategory = (GridView)getView().findViewById(R.id.gridView_category);
		mGVCategory.setAdapter(mCategoryAdapter);
		
		mLvActivity = (ListView)getView().findViewById(R.id.listView_activity);
		mLvActivity.setAdapter(mHotActivityAdapter);
		
	}
}