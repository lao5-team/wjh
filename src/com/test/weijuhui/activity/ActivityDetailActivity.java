package com.test.weijuhui.activity;

import java.util.ArrayList;

import com.squareup.picasso.Picasso;
import com.test.weijuhui.R;
import com.test.weijuhui.R.id;
import com.test.weijuhui.R.layout;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.data.DianpingDataHelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
/*
 * 1 显示活动的详细内容和地点
 * 2 抽取活动内容和地点（包括经纬度）
 * 3 添加好友
 * 4 发起聚会，向好友发送邀请单
 * */
public class ActivityDetailActivity extends FragmentActivity {

	//data
	private ComplexBusiness mCBData;
	private String mBusinessID;
	private ArrayList<String> mFriends = new ArrayList<String>( );
	//UI
	private RelativeLayout mView;
	private TextView mTvName;
	private ImageView mImgContent;
	private RelativeLayout mLayoutAddress;
	private TextView mTvAddress;
	private RelativeLayout mLayoutPhone;
	private TextView mTvPhone;
	private Button mBtnAddFriends;
	private TextView mTvFriends;
	private Button mBtnConfirm;
	private Button mBtnCancel;
	
	//Handler
	private Handler mUIHandler;
	
	@Override
	protected void onCreate(Bundle savedInstance)
	{
		super.onCreate(savedInstance);
		initData();
		initUI();
		
		mUIHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				updateUI();
			}
		};
	}
	
	private void initData()
	{
		Intent intent = getIntent();
		mBusinessID = intent.getStringExtra("businessID");
		if(null != mBusinessID)
		{
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					mCBData = DianpingDataHelper.getInstance().getBusinessByID(mBusinessID);
					if(null != mCBData)
					{
						Message msg = mUIHandler.obtainMessage();
						mUIHandler.sendMessage(msg);
					}
				}
			});
			t.start();
		}
		else
		{
			finish();
		}
	}
	
	private void initUI()
	{
		RelativeLayout layout = (RelativeLayout) this.getLayoutInflater().inflate(R.layout.fragment_activity_detail, null);
		mView = layout;
		setContentView(layout);
		
		mTvName = (TextView) layout.findViewById(R.id.textView_name);
		
		mImgContent = (ImageView) layout.findViewById(R.id.imageView_img);
		
		mLayoutAddress = (RelativeLayout) layout.findViewById(R.id.relativeLayout_address);
		
		mTvAddress = (TextView) layout.findViewById(R.id.textView_address);
		
		mLayoutPhone = (RelativeLayout) layout.findViewById(R.id.relativeLayout_phone);
		
		mTvPhone = (TextView) layout.findViewById(R.id.textView_phone);
		
		mBtnAddFriends = (Button) layout.findViewById(R.id.button_addFriends);
		
		mTvFriends = (TextView) layout.findViewById(R.id.textView_friends);
		
		mBtnConfirm = (Button) layout.findViewById(R.id.button_confirm);
		
		mBtnCancel = (Button)layout.findViewById(R.id.button_cancel);
		mBtnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ActivityDetailActivity.this.finish();
			}
		});
	}
	
	private void updateUI()
	{
		mTvName.setText(mCBData.mName + mCBData.mBranchName);
		Picasso.with(this).load(mCBData.mImgUrl).into(mImgContent);
		if(null!=mCBData.mAddress)
		{
			mTvAddress.setText(mCBData.mAddress);
		}
		else
		{
			mLayoutAddress.setVisibility(View.INVISIBLE);
		}
		if(null!=mCBData.mPhoneNumber)
		{
			mTvPhone.setText(mCBData.mPhoneNumber);
		}
		else
		{
			mLayoutPhone.setVisibility(View.INVISIBLE);
		}
		String buffer = "";
		if(mFriends.size() > 1)
		{
			for(int i=0; i<mFriends.size() - 1; i++)
			{
				buffer += mFriends.get(i) + " , ";
				
			}			
		}

		if(mFriends.size() > 0)
		{
			buffer += mFriends.get(mFriends.size()-1);
		}
		
		mTvFriends.setText(buffer);
		mView.invalidate();
		
	}
	
	
	
	
	
}
