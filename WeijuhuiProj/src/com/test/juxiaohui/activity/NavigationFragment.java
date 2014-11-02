package com.test.juxiaohui.activity;

import java.io.File;

import com.test.juxiaohui.adapter.ActivityAdapter;
import com.test.juxiaohui.adapter.ActivityCategoryAdapter;
import com.test.juxiaohui.domain.MyServerManager;
import com.test.juxiaohui.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
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
	ImageButton mIbNew;
	
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
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				File file = new File("mnt/sdcard/1.png");
				MyServerManager.getInstance().uploadImage(file);
			}
		});
		t.start();
		
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
		
		mIbNew = (ImageButton)getView().findViewById(R.id.imageButton_new);
		mIbNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), CreateActivityActivity2.class);
				getActivity().startActivity(intent);
			}
		});
		
	}
}
