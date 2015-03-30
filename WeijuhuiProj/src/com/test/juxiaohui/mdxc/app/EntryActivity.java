package com.test.juxiaohui.mdxc.app;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.app.view.FragmentHomeView;

import android.animation.AnimatorSet;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class EntryActivity extends FragmentActivity {

	
	LayoutInflater mInflater;
	
	FrameLayout mRootLayout;
	RelativeLayout mNavigationLayout;
	RelativeLayout mHomeLayout;

	AnimationSet mAnimationSet = new AnimationSet(true);
	private TextView mTvHotel;
	private TextView mTvFlights;
	public static void startActivity(Context context)
	{
		Intent intent = new Intent(context, EntryActivity.class);
		context.startActivity(intent);
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		initView();	
	}
	
	public void initView()
	{
		mInflater = LayoutInflater.from(this);
		mRootLayout = (FrameLayout) mInflater.inflate(R.layout.fragment_root, null);
		mNavigationLayout = (RelativeLayout) mRootLayout.findViewById(R.id.navigation_layout);
		mHomeLayout = new FragmentHomeView(this);
		mRootLayout.addView(mHomeLayout);
		setContentView(mRootLayout);
		addFlightView();
	}
	
	 @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	 
	public void addFlightView()
	{
		mTvFlights = (TextView)findViewById(R.id.tv_flight_button);
		mTvFlights.setClickable(true);
		mTvFlights.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FlightSearchActivity.startActivity(EntryActivity.this);
				
			}
		});
	}
	 
	
}
