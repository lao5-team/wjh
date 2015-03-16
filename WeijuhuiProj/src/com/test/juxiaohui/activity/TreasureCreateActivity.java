package com.test.juxiaohui.activity;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.Toast;
import com.test.juxiaohui.R;
import com.test.juxiaohui.adapter.ActivityAdapter;
import com.test.juxiaohui.data.Treasure;
import com.test.juxiaohui.domain.BmobServerManager;
import com.test.juxiaohui.domain.TreasureManager;
import com.test.juxiaohui.domain.UserManager;
import com.test.juxiaohui.mediator.ITreasureCreateMediator;

/**
 * Created by yihao on 15/3/12.
 */
public class TreasureCreateActivity extends Activity implements ITreasureCreateMediator {

    private Button mBtnCreate;
    private Button mBtnCancel;
    private EditText mEtxTitle;
    private EditText mEtxDesc;
    private ImageSwitcher mISImages;

    public static void startActivity(Activity activity, Fragment fragment){
        Intent intent = new Intent(activity, TreasureCreateActivity.class);
        fragment.startActivityForResult(intent, TreasuresEntryFragment.REQUEST_CREATE_TREASURE);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_create);
        addTitleView();
        addDescView();
        addImgsView();
        mBtnCreate = (Button)findViewById(R.id.button_create);
        mBtnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
        mBtnCancel = (Button)findViewById(R.id.button_cancel);
        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });
    }

    @Override
    public void addTitleView() {
        mEtxTitle = (EditText)findViewById(R.id.editText_treasure_name);
        
    }

    @Override
    public void addDescView() {
        mEtxDesc = (EditText)findViewById(R.id.editText_treasure_desc);
    }

    @Override
    public void addImgsView() {

    }

    @Override
    public void confirm() {

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                Treasure treasure = new Treasure();
                treasure.mName = mEtxTitle.getText().toString();
                treasure.mDesc = mEtxDesc.getText().toString();
                if(treasure.mName.length()==0||treasure.mDesc.length()==0)
                {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TreasureCreateActivity.this, "请填写宝物名称和描述", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                treasure.mOwnerName = UserManager.getInstance().getCurrentUser().mName;
                TreasureManager.getInstance().uploadTreasure(treasure);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                });

            }
        });
        t.start();

    }

    @Override
    public void cancel() {
        finish();
    }
}