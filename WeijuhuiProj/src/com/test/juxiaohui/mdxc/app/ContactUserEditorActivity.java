package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.mediator.IContactUserEditorMediator;
import com.test.juxiaohui.mdxc.widget.CommonTitleBar;

public class ContactUserEditorActivity extends Activity implements
		IContactUserEditorMediator {
	

	private Context mContext;
	private LayoutInflater mInflater;
	
	private LinearLayout mMainView;
	private CommonTitleBar mTitleBar;
	private RelativeLayout mContentView;
	private ScrollView mScrollView;
	
	private EditText mEdtName,mEdtPhone,mEdtEmail,mEdtRecipientName,mEdtRecipientPhone,mEdtRecipientAddress,mEdtRecipientPostalCode;
	private LinearLayout mCountryCodeLayout;
	private TextView mTvCountryCode;
	private Button mConfirmButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		//设置edittext自动聚焦时不弹出键盘
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED);
		mContext = this;
		mInflater = getLayoutInflater();
		initData();
		initView();
	}
	
	private void initData()
	{
		
	}
	
	private void initView()
	{
		mInflater = getLayoutInflater();
		mMainView = new LinearLayout(mContext);
		mMainView.setOrientation(LinearLayout.VERTICAL);
		mTitleBar = new CommonTitleBar(mContext);
		TextView titleTextView = new TextView(mContext);
		titleTextView.setGravity(Gravity.CENTER);
		titleTextView.setTextColor(getResources().getColor(R.color.white));
		titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX,getResources().getDimension(R.dimen.font_size_36));
		titleTextView.setText("Contact Info");
		titleTextView.setLayoutParams(new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		mTitleBar.setMarkLayout(titleTextView);
		
		mTitleBar.setBackIconListener(new OnClickListener() {		
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mMainView.addView(mTitleBar);
		
		mScrollView = (ScrollView) mInflater.inflate(R.layout.activity_contact_user_editor, null);
		mContentView = (RelativeLayout) mScrollView.findViewById(R.id.contact_user_editor_view);
		addNameView();
		addPhoneNumberView();
		addEmailView();
		addrecipentView();
		addConfirmView();
		mMainView.addView(mScrollView);
		setContentView(mMainView);
	}
	
	@Override
	public void addNameView() 
	{
		// TODO Auto-generated method stub
		mEdtName = (EditText)mContentView.findViewById(R.id.edt_name);
	}

	@Override
	public void addPhoneNumberView() 
	{
		// TODO Auto-generated method stub
		mEdtPhone = (EditText)mContentView.findViewById(R.id.edt_phone_number);
		mCountryCodeLayout = (LinearLayout)mContentView.findViewById(R.id.layout_country_code);
		mTvCountryCode = (TextView)mCountryCodeLayout.findViewById(R.id.tv_country_code);
	}

	@Override
	public void addrecipentView() 
	{
		// TODO Auto-generated method stub
		mEdtRecipientName = (EditText)mContentView.findViewById(R.id.edt_phone_number);
		mEdtRecipientName = (EditText)mContentView.findViewById(R.id.edt_phone_number);
		mEdtRecipientName = (EditText)mContentView.findViewById(R.id.edt_phone_number);
		mEdtRecipientName = (EditText)mContentView.findViewById(R.id.edt_phone_number);
	}

	@Override
	public void insertOrReplaceContactUser(ContactUser user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addEmailView() 
	{
		// TODO Auto-generated method stub
		mEdtEmail = (EditText)mContentView.findViewById(R.id.edt_email);
	}

	@Override
	public void addConfirmView() {
		// TODO Auto-generated method stub
		mConfirmButton = (Button)mContentView.findViewById(R.id.btn_confirm);
	}

}
