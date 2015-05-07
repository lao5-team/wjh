package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.adapter.ContactUserListAdapter;
import com.test.juxiaohui.mdxc.adapter.PassengerListAdapter;
import com.test.juxiaohui.mdxc.app.PassengerListActivity.PassengerSelector;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.IContactUserListMediator;
import com.test.juxiaohui.mdxc.widget.CommonTitleBar;

public class ContactUserListActivity extends Activity implements
		IContactUserListMediator {
	
	
	private RelativeLayout mMainView;
	private CommonTitleBar mTitleBar;
	private ListView mContactUserListView;
	private ContactUserListAdapter mContactUserListAdapter;
	private RelativeLayout mConfirmButtonLayout;
	private Button mBtnConfirm;
	
	private Context mContext;
	
	private List<ContactUser> mContactUsers = new ArrayList<ContactUser>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		initData();
		initView();
	}

	public void initData() 
	{

	}

	private void initView()
	{
		mMainView = new RelativeLayout(this);
		
		RelativeLayout.LayoutParams mainParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	//	mMainView.setOrientation(LinearLayout.VERTICAL);
		//mMainView.setLayoutParams(mainParams);
		
		mTitleBar = new CommonTitleBar(mContext);
		TextView titleTextView = new TextView(mContext);
		titleTextView.setGravity(Gravity.CENTER);
		titleTextView.setTextColor(getResources().getColor(R.color.white));
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size_36));
		titleTextView.setText("Contact User");
		titleTextView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mTitleBar.setMarkLayout(titleTextView);
		mTitleBar.setId(10010);
		mTitleBar.setBackIconListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageView addContactUserView = new ImageView(mContext);
		addContactUserView.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_icon_add));
		addContactUserView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(mContext,ContactUserEditorActivity.class);
				startActivityForResult(intent, 0);
			}
		});
		mTitleBar.setTitleIcon(addContactUserView);
		mMainView.addView(mTitleBar);
		
		
		mContactUserListView = new ListView(mContext);
		mContactUserListAdapter = new ContactUserListAdapter(mContext, mContactUsers,this);
		mContactUserListView.setAdapter(mContactUserListAdapter);
		mContactUserListView.setBackgroundColor(getResources().getColor(R.color.color_gray_12));
		mContactUserListView.setId(10086);
	//	RelativeLayout.LayoutParams listViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		RelativeLayout.LayoutParams listViewParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		listViewParams.addRule(RelativeLayout.BELOW, mTitleBar.getId());
		mMainView.addView(mContactUserListView,listViewParams);
		
		RelativeLayout.LayoutParams confirmLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, Math.round(getResources().getDimension(R.dimen.passengerlist_confirmlayout_height)));
		confirmLayoutParams.addRule(RelativeLayout.BELOW, mContactUserListView.getId());
		confirmLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM,RelativeLayout.TRUE);
		confirmLayoutParams.alignWithParent = true;
		mConfirmButtonLayout = new RelativeLayout(mContext);
		mConfirmButtonLayout.setBackgroundColor(getResources().getColor(R.color.color_gray_12));
		mConfirmButtonLayout.setGravity(Gravity.CENTER_HORIZONTAL);
		
		RelativeLayout.LayoutParams confirmButtonParams = new RelativeLayout.LayoutParams(Math.round(getResources().getDimension(R.dimen.passengerlist_confirmbutton_width)),Math.round(getResources().getDimension(R.dimen.passengerlist_confirmbutton_height)));
		confirmButtonParams.setMargins(0, 0, 0, Math.round(getResources().getDimension(R.dimen.size_20dip)));
		confirmButtonParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		mBtnConfirm = new Button(this);
		mBtnConfirm.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_btn_1));
		mBtnConfirm.setText(R.string.done);
		mBtnConfirm.setGravity(Gravity.CENTER);
		mBtnConfirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mConfirmButtonLayout.addView(mBtnConfirm,confirmButtonParams);
		mMainView.addView(mConfirmButtonLayout,confirmLayoutParams);
		
		
		setContentView(mMainView,mainParams);
		
		
	}
	
	
	@Override
	public void addContactUser(ContactUser user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void editContactUser(ContactUser user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setContactUser(ContactUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<ContactUser> getContactUsers() {
		// TODO Auto-generated method stub
		return null;
	}



}
