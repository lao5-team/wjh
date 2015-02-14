package com.test.juxiaohui.shop.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.test.juxiaohui.R;
import com.test.juxiaohui.shop.server.ShopServer;

/**
 * Created by yihao on 15/2/14.
 */
public class ShopMainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ShopServer.getInstance().login("yh", "yhtest");
        setContentView(R.layout.activity_shop_main);

        Button btnShop = (Button) findViewById(R.id.btn_shop);
        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopMainActivity.this, ShopActivity.class);
                startActivity(intent);
            }
        });

        Button btnChart = (Button) findViewById(R.id.btn_chart);
        btnChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopMainActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

        Button btnOrder = (Button) findViewById(R.id.btn_my);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopMainActivity.this, OrderListActivity.class);
                startActivity(intent);
            }
        });
    }
}