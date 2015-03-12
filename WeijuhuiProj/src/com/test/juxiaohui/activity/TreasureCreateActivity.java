package com.test.juxiaohui.activity;

import android.app.Activity;
import android.os.Bundle;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mediator.ITreasureCreateMediator;

/**
 * Created by yihao on 15/3/12.
 */
public class TreasureCreateActivity extends Activity implements ITreasureCreateMediator {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasure_create);
    }

    @Override
    public void addTitleView() {
        
    }

    @Override
    public void addDescView() {

    }

    @Override
    public void addImgsView() {

    }

    @Override
    public void confirm() {

    }

    @Override
    public void cancel() {

    }
}