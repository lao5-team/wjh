package com.test.juxiaohui.mdxc.app;


import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.juxiaohui.R;
import com.test.juxiaohui.common.manager.ServerManager;
import com.test.juxiaohui.mdxc.mediator.IRegisterMediator;

public class RegisterActivity extends Activity implements IRegisterMediator {

	private EditText mPhoneNumberEditText;
	private EditText mPasswordEditText;
	private EditText mConfirmPwdEditText;
	
	private Button mRegiserButton;
	
	private Context mContext;
	
	private String mRegisterResult;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mContext = this;
		initView();
	}
	
	public void initView()
	{
		setContentView(R.layout.mdxc_activity_register);
		addPhoneNumberView();
		addPasswordView();
		addConfirmPasswordView();
		addRegisterButton();
	}
	
	@Override
	public void addPhoneNumberView() {
		// TODO Auto-generated method stub
		mPhoneNumberEditText = (EditText) findViewById(R.id.et_phone_number);
		mPhoneNumberEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
	}

	@Override
	public void addPasswordView() {
		// TODO Auto-generated method stub
		mPasswordEditText = (EditText) findViewById(R.id.et_password);
	}


	@Override
	public void addConfirmPasswordView() {
		// TODO Auto-generated method stub
		mConfirmPwdEditText = (EditText) findViewById(R.id.et_confirm_password);
	}

	@Override
	public void addRegisterButton() {
		// TODO Auto-generated method stub
		mRegiserButton = (Button) findViewById(R.id.button_register);
		mRegiserButton.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				confirm();
			}
		});
	}

	
	@Override
	public void confirm() {
		// TODO Auto-generated method stub
		String name;
		String password;
		String confirmPassword;
		name = mPhoneNumberEditText.getText().toString();
		password = mPasswordEditText.getText().toString();
		confirmPassword = mConfirmPwdEditText.getText().toString();
		if(name == null || name.equalsIgnoreCase(""))
		{
			showErrorMessage(mContext.getString(R.string.name_is_null));
			mRegisterResult = "name_is_null";
			return;
		}
		
		if(name.length() != 11)
		{
			showErrorMessage(mContext.getString(R.string.user_name_invalid));
			mRegisterResult = "user_name_invalid";
			return;
		}
		
		if(password == null || confirmPassword  == null || password.equalsIgnoreCase("") || confirmPassword.equalsIgnoreCase(""))
		{
			showErrorMessage(mContext.getString(R.string.password_is_null));
			mRegisterResult = "password_is_null";
			return;
		}
		if(!password.equalsIgnoreCase(confirmPassword))
		{
			showErrorMessage(mContext.getString(R.string.password_confirm_error));
			mRegisterResult = "password_confirm_error";
			return;
		}
		String[] params = {name,password};
		new RegisterTask().execute(params);
	}
	
	//注册调用不要在主线程中，否则会抛异常。
	private class RegisterTask extends AsyncTask<String, Integer, String>
	{

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			return ServerManager.getInstance().register(arg0[0], arg0[1]);
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			mRegisterResult = result;
			super.onPostExecute(result);
		}
		
	}

	@Override
	public void cancel() {
		// TODO Auto-generated method stub

	}
	
    public void showErrorMessage(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(mContext, message,Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    @Override
    protected void onDestroy()
    {
    	mConfirmPwdEditText = null;
    	mContext = null;
    	mPasswordEditText = null;
    	mPhoneNumberEditText = null;
    	mRegiserButton = null;
    	super.onDestroy();
    }
    
    public static void startActivity(Context context)
    {
    	Intent intent = new Intent(context,RegisterActivity.class);
    	context.startActivity(intent);
    }
    
    public String getRegisterResult()
    {
    	return mRegisterResult;
    }

}
