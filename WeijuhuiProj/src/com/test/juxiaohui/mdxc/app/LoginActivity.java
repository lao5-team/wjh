package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.test.juxiaohui.R;
import com.test.juxiaohui.common.manager.ServerManager;
import com.test.juxiaohui.mdxc.mediator.ILoginMediator;

/**
 * Created by yihao on 15/3/4.
 */
public class LoginActivity extends Activity implements ILoginMediator{

    EditText mEtxUsername;
    EditText mEtxPassword;
    Button mBtnOK;
    Button mBtnCancel;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mdxc_activity_login);
    }

    @Override
    public void addUsernameView() {
        mEtxUsername  = (EditText)findViewById(R.id.editText_username);
    }

    @Override
    public void addPasswordView() {
        mEtxPassword = (EditText)findViewById(R.id.editText_username);
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
            String result = ServerManager.getInstance().login(mEtxUsername.getEditableText().toString(), mEtxPassword.getEditableText().toString());
            if(result.contains("Success"))
            {
                showErrorMessage("Login Success");
            }
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
}