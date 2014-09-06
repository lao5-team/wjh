package com.test.weijuhui;

import com.test.weijuhui.data.DianpingDataHelper;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class CreateActivityActivity extends FragmentActivity{
    //UI controls 
	private ViewPager mViewPager;  
    private Button mBtnLocaction;
    private Button mBtnCategory;
    private Button mBtnSearch;
    private ListView mListViewContent;
    
    //Fragments
    private CategoryFragment mFragCategory;
    private CategoryFragment mFragLocation;
    //private CategoryFragment mFragCategory;
    //private TabFragment mFragments[];
    //public static final int TAB_SIZE = 3;  
    public static final String ARGUMENTS_NAME = "args";  
    private String TAG = CreateActivityActivity.class.getName();
    //private TabFragmentPagerAdapter mAdapter;  
	  @Override  
	    protected void onCreate(Bundle savedInstanceState) {  
	        super.onCreate(savedInstanceState);  
	        setContentView(R.layout.activity_newactivity);
	        mFragCategory = new CategoryFragment(null, null, "category");
	        mFragLocation = new CategoryFragment(null, null, "location");
	        initView();
	        loadDataTest();
	  }
	  
	    private void initView() {  
//	    	mViewPager = (ViewPager) this.findViewById(R.id.pager);    
//	        final ActionBar mActionBar = getActionBar();  
//	          
//	        mActionBar.setDisplayHomeAsUpEnabled(false);  
//	          
//	        mActionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);  
//	          
//	        mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());  
//	        mViewPager.setAdapter(mAdapter);  
//	        mViewPager.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					((TabFragment)v.getTag()).onClick();
//				}
//			});
//	        mViewPager.setOnPageChangeListener(new OnPageChangeListener() {  
//	              
//	            @Override  
//	            public void onPageSelected(int arg0) {  
//	            	mAdapter.mFragments[arg0].onClick();
//	            	//mAdapter.mFragments[tab.getPosition()].onClick();
//	                mActionBar.setSelectedNavigationItem(arg0);  
//	            }  
//	              
//	            @Override  
//	            public void onPageScrolled(int arg0, float arg1, int arg2) {  
//	                  
//	            }  
//	              
//	            @Override  
//	            public void onPageScrollStateChanged(int arg0) {  
//	                  
//	            }  
//	        });  
//	          
//	        for(int i=0;i<TAB_SIZE;i++){  
//	            Tab tab = mActionBar.newTab();  
//	            tab.setText(mAdapter.getPageTitle(i)).setTabListener(this);  
//	            mActionBar.addTab(tab);  
//	        }  
		mBtnLocaction = (Button) this.findViewById(R.id.button_location);
		mBtnCategory = (Button) this.findViewById(R.id.button_category);
		mBtnSearch = (Button) this.findViewById(R.id.button_search);
		mListViewContent = (ListView) this.findViewById(R.id.listView_content);
		
		mBtnLocaction.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				mFragLocation.setData(DianpingDataHelper.getInstance().getLocationCategories());
				if(null!=fm.findFragmentByTag("location"))
				{
					ft.remove(mFragLocation);
					//ft.add(R.id.layout_middle, mFragCategory, "category");
					//mFragCategory.getListView().invalidate();
				}
				else
				{
					ft.add(R.id.layout_middle, mFragLocation, "location");
				}
				if(null!=fm.findFragmentByTag("category"))
				{
					ft.remove(mFragCategory);
				}
				ft.commit();
			}
		});
		
		mBtnCategory.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentManager fm = getSupportFragmentManager();
				FragmentTransaction ft = fm.beginTransaction();
				mFragCategory.setData(DianpingDataHelper.getInstance().getContentCategories());
				if(null!=fm.findFragmentByTag("category"))
				{
					ft.remove(mFragCategory);
					//ft.add(R.id.layout_middle, mFragCategory, "category");
					mFragCategory.getListView().invalidate();
				}
				else
				{
					ft.add(R.id.layout_middle, mFragCategory, "category");
				}
				if(null!=fm.findFragmentByTag("location"))
				{
					ft.remove(mFragLocation);
				}
				ft.commit();
			}
		});		
		
		//mFragments[2] = new TabFragment(titles[2]);
	    	
	    }  
	    
	    private void loadDataTest()
	    {
//	    	Thread t = new Thread(new Runnable() {
//				
//				@Override
//				public void run() {
//					DianpingDataHelper.getInstance().getContentCategories();
//				}
//			});
//	    	t.start();
	    	Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					DianpingDataHelper.getInstance().getLocationCategories();
				}
			});
	    	t.start();
	    	
	    }
	  
//	    public  class TabFragmentPagerAdapter extends FragmentPagerAdapter{  
//	        
//	    	TabFragment mFragments[];
//	        public TabFragmentPagerAdapter(FragmentManager fm) {  
//	            super(fm); 
//	            String[] titles = getResources().getStringArray(R.array.new_activity_tabs);
//	            mFragments = new TabFragment[3];
//	            mFragments[0] = new TabFragment(titles[0]);
//	            mFragments[1] = new TabFragment(titles[1]);
//	            mFragments[2] = new TabFragment(titles[2]);
//	            
//	        }  
//	  
//	        @Override  
//	        public Fragment getItem(int arg0) {  
//	        	
//	        	return mFragments[arg0];
//	        }  
//	  
//	        @Override  
//	        public int getCount() {  
//	              
//	            return TAB_SIZE;  
//	        }  
//	         @Override  
//	        public CharSequence getPageTitle(int position) {  
//	        	 String[] titles = getResources().getStringArray(R.array.new_activity_tabs);
//	            return titles[position];  
//	        }  
//	    }  
	      
	    @Override  
	    public boolean onCreateOptionsMenu(Menu menu) {  
	        // Inflate the menu; this adds items to the action bar if it is present.  
	        getMenuInflater().inflate(R.menu.main, menu);  
	        return true;  
	    }  
	  
//	    @Override  
//	    public void onTabSelected(Tab tab, FragmentTransaction ft) {  
//	        mViewPager.setCurrentItem(tab.getPosition());  
//	        
//	    }  
//	  
//	    @Override  
//	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {  
//	          
//	    }  
//	  
//	    @Override  
//	    public void onTabReselected(Tab tab, FragmentTransaction ft) {  
//	          Log.v(TAG, "onTabReselected tab " + tab.getPosition());
//	          //mAdapter.mFragments[tab.getPosition()].onClick();
//	    }  
}
