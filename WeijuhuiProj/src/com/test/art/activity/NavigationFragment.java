package com.test.art.activity;

import java.io.File;
import java.util.ArrayList;

import com.test.art.R;
import com.test.art.adapter.ActivityAdapter;
import com.test.art.adapter.ActivityCategoryAdapter;
import com.test.art.adapter.PictureInfoAdapter;
import com.test.art.data.ActivityData;
import com.test.art.data.MyArtUser;
import com.test.art.data.PictureInfo;
import com.test.art.domain.MyServerManager;
import com.test.art.domain.activity.ActivityManager;
import com.test.art.domain.activity.IActivityLoader;
import com.test.art.domain.activity.IPictureInfoLoader;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NavigationFragment extends Fragment {

	//ui widgets
	ListView mLvPicInfo;
	ImageButton mIbNew;
	Button mBtnRefresh;
	
	//data
	PictureInfoAdapter mAdapter;
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
		mAdapter = new PictureInfoAdapter(getActivity(), new IPictureInfoLoader() {
			
			@Override
			public ArrayList<MyArtUser> getUserList(ArrayList<PictureInfo> pictureList) {
				ArrayList<MyArtUser> userList = new ArrayList<MyArtUser>();
				for(PictureInfo picInfo : pictureList)
				{
					userList.add(MyServerManager.getInstance().getArtUserInfo(picInfo.mUserName));
				}
				return userList;
			}
			
			@Override
			public ArrayList<PictureInfo> getPictureList() {
				// TODO Auto-generated method stub
				return MyServerManager.getInstance().getAllPictureInfo();
			}
		});
	}
	
	private void initUI()
	{
		mLvPicInfo = (ListView)getView().findViewById(R.id.listView_picture_list);
		mLvPicInfo.setAdapter(mAdapter);
		
		mIbNew = (ImageButton)getView().findViewById(R.id.imageButton_new);
		mIbNew.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), CreatePictureActivity.class);
				getActivity().startActivity(intent);
			}
		});
		
		mBtnRefresh = (Button)getView().findViewById(R.id.button_refresh);
		mBtnRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAdapter.refresh();
			}
		});
		
	}
}
