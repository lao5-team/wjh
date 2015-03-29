package com.test.juxiaohui.mdxc.app.view;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.app.EntryActivity;
import com.test.juxiaohui.mdxc.app.FlightSearchActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebStorage.Origin;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FragmentHomeView extends RelativeLayout {

	
	private long mDownActionTime = 0l;
	private float lastX = 0;
	private float lastY = 0;
	private float mDownActionX = 0;
	private float mDownActionY = 0;
	
	private LayoutInflater mInflater;
	RelativeLayout mContentView;
	RelativeLayout mSelf;
	private Context mContext;
	
	private TextView mTvHotel;
	private TextView mTvFlights;
	private ImageView mLeftDrawer;
	
	private float maxDistanceX;
	private float maxShrinkY;
	private float orginHeight = 0;
	private float orginWidth = 0;
	private float moveScale;
	private float viewScale;
	private float MAXSHRINKSCALE;
	private float mTextViewOriginHeight;
	private float mTextViewOriginWidth;
	private float screenHeight;
	private float startY;
	private boolean init = false;
	
	private Bitmap mScreenShot;
	private ImageView mTempContentView;
	
	
	public FragmentHomeView(Context context) {
		super(context);
		mContext = context;
		mSelf = this;
		initView();
		// TODO Auto-generated constructor stub
	}

	private void initScrollData()
	{
		WindowManager wm = (WindowManager) getContext() 
                .getSystemService(Context.WINDOW_SERVICE); 
		screenHeight = wm.getDefaultDisplay().getHeight();
		orginWidth = this.getWidth();	
        orginHeight = this.getHeight();
        startY = this.getY();
        maxDistanceX = (orginWidth*4)/5;
        maxShrinkY = orginHeight/6;
        MAXSHRINKSCALE = (2f/3f);
        moveScale = maxDistanceX/maxShrinkY;
        mTextViewOriginWidth = mTvHotel.getWidth();
        mTextViewOriginHeight = mTvHotel.getHeight();
        
        mContentView.setDrawingCacheEnabled(true);
        mContentView.buildDrawingCache();
		mScreenShot = mContentView.getDrawingCache().copy(Bitmap.Config.ARGB_4444, true);
		mTempContentView.setImageBitmap(mScreenShot);
		this.addView(mTempContentView);
		mTempContentView.setVisibility(View.GONE);
       // viewScale = (((View)this.getParent()).getWidth())/(((View)this.getParent()).getHeight());	
	}
	private void initView()
	{
		mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mContentView = (RelativeLayout) mInflater.inflate(R.layout.fragment_home, null);
		mTvHotel = (TextView) mContentView.findViewById(R.id.tv_hotel_button);
		mTvFlights = (TextView) mContentView.findViewById(R.id.tv_flight_button);
		mLeftDrawer = (ImageView) mContentView.findViewById(R.id.rl_left_drawer);
		mLeftDrawer.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!init)
				{
					initScrollData();
					init = true;
				}
				
				transformToMiniView(mContentView.getLeft(),mContentView.getTop(),orginWidth,orginHeight);
			}
		});
		addFlightView();

		mTempContentView = new ImageView(mContext);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		mTempContentView.setLayoutParams(params);
		this.addView(mContentView);
		mTempContentView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!init)
				{
					initScrollData();
					init = true;
				}
				
				//transformToFullView(mSelf.getLeft(),mSelf.getTop(),mSelf.getRight() - mSelf.getLeft(),mSelf.getBottom() - mSelf.getTop());
				mSelf.layout(0, 0, Math.round(orginWidth), Math.round(orginHeight));
				/*mSelf.layout(0, 0 , Math.round(orginWidth),  Math.round(orginHeight));
				mTempContentView.layout(0, 0,Math.round(orginWidth), Math.round(orginHeight));*/
				mTempContentView.setClickable(false);
				mTempContentView.setVisibility(View.GONE);
				
			}
		});
		mTempContentView.setClickable(false);

	}
	
	private void transformToFullView(final float cx, final float cy,float cw, float ch)
	{
		
		AnimationSet mAnimationSet = new AnimationSet(true);

		TranslateAnimation translateAnimation = new TranslateAnimation(0, -(0.5f*cw -( orginWidth -cx) + orginWidth/2), 0, 0);
		ScaleAnimation scaleAnimation =new ScaleAnimation(1f, orginWidth / cw, 1f, orginHeight / ch,   
				Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f); 
		
		mAnimationSet.addAnimation(scaleAnimation);
		mAnimationSet.addAnimation(translateAnimation);
		mAnimationSet.setFillAfter(true);
		mAnimationSet.setDuration(200);
		mAnimationSet.setAnimationListener(new AnimationListener() {	
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub		
			}		
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub			
			}
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub	
				
				
		//		mContentView.layout(0, 0,Math.round(orginWidth), Math.round(orginHeight));
			//	mTempContentView.layout(0, 0,Math.round(orginWidth), Math.round(orginHeight));
			
				mTempContentView.setClickable(false);
				//mContentView.setVisibility(View.VISIBLE);
				//mTempContentView.setVisibility(View.GONE);
				invalidate();
			}
		});
		this.startAnimation(mAnimationSet);
		
	}
	
	private void transformToMiniView(float cx, float cy,float cw, float ch)
	{
		AnimationSet mAnimationSet = new AnimationSet(true);
		mTempContentView.setVisibility(VISIBLE);
	//	mContentView.setVisibility(View.GONE);
		TranslateAnimation translateAnimation = new TranslateAnimation(cx, maxDistanceX, cy, cy);
		ScaleAnimation scaleAnimation =new ScaleAnimation(1f, (orginWidth * MAXSHRINKSCALE)/cw, 1f, (orginHeight * MAXSHRINKSCALE)/ch,   
				Animation.RELATIVE_TO_SELF, MAXSHRINKSCALE, Animation.RELATIVE_TO_SELF, 0.5f); 
		mAnimationSet.addAnimation(translateAnimation);
		mAnimationSet.addAnimation(scaleAnimation);
		
		mAnimationSet.setFillAfter(false);
		mAnimationSet.setDuration(200);
		mAnimationSet.setAnimationListener(new AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				// TODO Auto-generated method stub
				
				mSelf.layout(Math.round(maxDistanceX), Math.round(maxShrinkY) , Math.round(maxDistanceX + orginWidth * MAXSHRINKSCALE), Math.round( maxShrinkY + orginHeight*MAXSHRINKSCALE));
				mTempContentView.layout(0, 0,Math.round(orginWidth * MAXSHRINKSCALE), Math.round(orginHeight*MAXSHRINKSCALE));
				//mContentView.layout(0, 0,Math.round(orginWidth * MAXSHRINKSCALE), Math.round(orginHeight*MAXSHRINKSCALE));
				mTempContentView.setClickable(true);
			}
		});
		this.startAnimation(mAnimationSet);
	}
	
	private void addFlightView()
	{
		mTvFlights = (TextView)mContentView.findViewById(R.id.tv_flight_button);
		mTvFlights.setClickable(true);
		mTvFlights.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FlightSearchActivity.startActivity(mContext);
				
			}
		});
	}
	
	 @Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(ev);
	}
	 
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		return super.onInterceptTouchEvent(ev);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		float x = ev.getRawX();
		if(!init)
		{
			initScrollData();
			init = true;
		}
		Log.d("qinyy", "action:" + ev.getAction() + "   x: " + x + "  lastX: " + lastX);
     //   orginHeight = ((View)this.getParent()).getHeight();
      //  orginWidth = ((View)this.getParent()).getWidth();
		//float y = ev.getY();
		switch (ev.getAction())
		{
		case MotionEvent.ACTION_DOWN:
			mDownActionTime = System.currentTimeMillis();
			mDownActionX = x;	
			mTempContentView.setVisibility(VISIBLE);
		//	mContentView.setVisibility(View.GONE);
			/*
			if(mTempContentView.getParent() != null && !mTempContentView.getParent().equals(this))
				((ViewGroup)(mTempContentView.getParent())).removeView(mTempContentView);
			if(mTempContentView.getParent() == null)
				this.addView(mTempContentView);
			this.removeView(mContentView);*/
		//	mDownActionY = y;
		
			break;
		case MotionEvent.ACTION_MOVE:
			float distanceX = x - lastX;
/*			if(Math.abs(distanceX) < 20)
				return true;*/
			float leftbound = this.getLeft() + Math.round(distanceX);

			float topbound = Math.round(this.getTop() + (distanceX/moveScale));
			if((leftbound/maxDistanceX) > 1 && distanceX > 0)
			{
				lastX = x;
				return true;
			}
			else if(leftbound < 0)
			{
				this.layout(0,0,Math.round(orginWidth), Math.round(orginHeight));
				mTempContentView.layout(0,0,Math.round(orginWidth), Math.round(orginHeight));
				//mContentView.layout(0,0,Math.round(orginWidth), Math.round(orginHeight));
			}
			else
			{
				float currentScale = (1 - (leftbound/maxDistanceX)*(1 - MAXSHRINKSCALE));
				this.layout(Math.round(leftbound), Math.round(topbound), Math.round(leftbound + orginWidth * currentScale), Math.round( topbound + orginHeight*currentScale));
				mTempContentView.layout(0, 0, Math.round(orginWidth * currentScale), Math.round(orginHeight*currentScale));
			//	mContentView.layout(0,0,Math.round(orginWidth), Math.round(orginHeight));
				Log.d("qinyy", "distanceX: " + distanceX +"  l:" + leftbound + " t:" + topbound + " r:" +Math.round(leftbound + orginWidth * currentScale) + " b" + Math.round( topbound + orginHeight*currentScale));
			}
				
			//float distanceYthis
			break;
		case MotionEvent.ACTION_UP:
		
			break;
		default:
			break;
		}

		lastX = x;

		return true;
	}
	
}
