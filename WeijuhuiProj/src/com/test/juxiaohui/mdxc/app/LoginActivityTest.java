package com.test.juxiaohui.mdxc.app;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import android.widget.TextView;
import com.test.juxiaohui.R;
import junit.framework.Assert;

/**
 * Created by yihao on 15/3/4.
 */
public class LoginActivityTest extends ActivityInstrumentationTestCase2<LoginActivity> {

    LoginActivity mActivity;
    TextView mTvUsername;
    TextView mPassword;
    EditText mEtxUsername;

    public LoginActivityTest()
    {
        super(LoginActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mActivity = getActivity();
        mTvUsername = (TextView) mActivity.findViewById(R.id.textView_username);
        mEtxUsername = (EditText) mActivity.findViewById(R.id.editText_username);

    }

    public void testUI()
    {
        Assert.assertEquals(mTvUsername.getText(), "Username");

        getInstrumentation().runOnMainSync(new Runnable() {
            @Override
            public void run() {
                mEtxUsername.requestFocus();
            }
        });
        getInstrumentation().waitForIdleSync();
        getInstrumentation().sendStringSync("test");
        getInstrumentation().waitForIdleSync();


    }
}
