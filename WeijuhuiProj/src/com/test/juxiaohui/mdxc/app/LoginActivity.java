package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.manager.ServerManager;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.ILoginMediator;

/**
 * Created by yihao on 15/3/4.
 */
public class LoginActivity extends Activity implements ILoginMediator{

    EditText mEtxUsername;
    EditText mEtxPassword;
    Button mBtnOK;
    Button mBtnCancel;
    Button mBtnRegister;
    String mLoginResult = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdxc_activity_login);
        Thread t= new Thread(new Runnable() {
			@Override
			public void run() {
				//UserManager.getInstance().logout();
				login();
				
			}
		});
        t.start();
        addUsernameView();
        addPasswordView();

        mBtnOK = (Button)findViewById(R.id.button_OK);
        mBtnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });

        mBtnCancel = (Button)findViewById(R.id.button_Cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
        
    }

    @Override
    public void addUsernameView() {
        mEtxUsername  = (EditText)findViewById(R.id.editText_username);
    }

    @Override
    public void addPasswordView() {
        mEtxPassword = (EditText)findViewById(R.id.editText_password);
    }

    @Override
    public void confirm() {
        boolean isValid = true;
        if(mEtxUsername.getEditableText().toString().length()==0)
        {
            showErrorMessage("Username is empty!");
            isValid = false;
        }

        if(mEtxPassword.getEditableText().toString().length()==0)
        {
            showErrorMessage("Password is empty!");
            isValid = false;
        }

        if(isValid)
        {
            showErrorMessage("Waiting for login...");
            final String username = mEtxUsername.getEditableText().toString();
            final String password = mEtxPassword.getEditableText().toString();
    		DemoApplication.getInstance().setUserName(username);
    		DemoApplication.getInstance().setPassword(password);
            Thread t = new Thread(new Runnable() {

                @Override
                public void run() {
                    mLoginResult = UserManager.getInstance().login(username, password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if(mLoginResult.contains("Success"))
                            {
                                showErrorMessage("Login Success");
                                EntryActivity.startActivity(LoginActivity.this);
                                finish();
                            }
                            else
                            {
                                showErrorMessage(mLoginResult);
                            }
                        }
                    });
                }
            });
            t.start();

        }

    }

    @Override
    public void cancel() {
        finish();
    }

    public void showErrorMessage(final String message)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this, message,Toast.LENGTH_SHORT).show();
            }
        });
    }

    public String getLoginResult()
    {
        Log.v(DemoApplication.TAG, "Login Result = " + mLoginResult);
        return mLoginResult;
    }
    
    public void login()
    {
    	String username = DemoApplication.getInstance().getUserName();
    	String password = DemoApplication.getInstance().getPassword();
    	if(null!=username && null!=password)
    	{
            mLoginResult = UserManager.getInstance().login(username, password);
            if(mLoginResult.contains("Success"))
            {
                EntryActivity.startActivity(LoginActivity.this);
                finish();
            }  	   		
    	}

    }
    
    public void onClickRegister(View view)
    {
    	Intent intent = new Intent(this, RegisterActivity.class);
    	startActivity(intent);
    }

	@Override
	public void addCheckcodeView() {
		// TODO Auto-generated method stub
		
	}
}