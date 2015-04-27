package com.test.juxiaohui.mdxc.app.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.FlightOrder;
import com.test.juxiaohui.mdxc.manager.FlightOrderManager;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import java.util.List;

/**
 * Created by yihao on 15/4/27.
 */
public class FlightOrdersView extends LinearLayout {

    private RelativeLayout mContentLayout;
    private Context mContext;

    private LayoutInflater mInflater;
    private ListView mLvFlightOrders;
    public FlightOrdersView(Context context) {
        super(context);
        mContext = context;
        initView();
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final List<FlightOrder> listOrders = FlightOrderManager.getInstance().getFlightOrderList();
                ((Activity)mContext).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLvFlightOrders.setAdapter(new CommonAdapter<FlightOrder>(listOrders, new IAdapterItem<FlightOrder>() {
                            @Override
                            public View getView(FlightOrder data, View convertView) {
                                return FlightOrder.getView(mContext, mInflater, convertView, data);
                            }
                        }));
                    }
                });
            }
        });
        t.start();
        // TODO Auto-generated constructor stub
    }

    private void initView()
    {
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentLayout = (RelativeLayout) mInflater.inflate(R.layout.view_order, null);
        LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        mContentLayout.setLayoutParams(params);
        this.addView(mContentLayout);
        mLvFlightOrders = (ListView)findViewById(R.id.listView_flight_orders);
    }
}
