package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.mdxc.mediator.IFlightSearchMediator;

import java.util.Date;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightSearchActivity extends Activity implements IFlightSearchMediator{
    Button mBtnRoundTrip;
    Button mBtnOneWay;
    TextView mTvDepartCity;
    TextView mTvDepartCode;
    ImageView mIvExchange;
    TextView mTvArrivalCity;
    TextView mTvArrivalCode;
    LinearLayout mLlDepart;
    LinearLayout mLlArrival;
    LinearLayout mLlDepartDate;
    LinearLayout mLlArrivalDate;
    TextView mTvPassengerNumber;
    ImageView mIvMinus;
    ImageView mIvPlus;
    LinearLayout mLlSearch;
    //tv_depart_city
    //tv_depart_code
    //iv_airport_exchange
    //tv_arrival_city
    //tv_arrival_code

    /*Data*/
    FlightSearchRequest mSearchRequest;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);
        addFlightTypeView();
        addCityView();
    }

    @Override
    public void addFlightTypeView() {
        mBtnRoundTrip = (Button) findViewById(R.id.button_roundtrip);
        mBtnRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlightType(FlightSearchRequest.TYPE_ROUNDTRIP);
            }
        });
        mBtnOneWay = (Button) findViewById(R.id.button_oneway);
        mBtnOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlightType(FlightSearchRequest.TYPE_ONEWAY);
            }
        });
    }

    @Override
        public void addCityView() {
        mLlDepart = (LinearLayout) findViewById(R.id.ll_departCity_container);
        mLlDepart.setClickable(true);
        mLlDepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCities();
            }
        });
        mTvDepartCity = (TextView) findViewById(R.id.tv_depart_city);
        mTvDepartCode = (TextView) findViewById(R.id.tv_depart_code);
        mIvExchange = (ImageView) findViewById(R.id.iv_airport_exchange);
        mIvExchange.setClickable(true);
        mIvExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempCity = mSearchRequest.mArrivalCity;
                String tempCode = mSearchRequest.mArrivalCode;

                mSearchRequest.mArrivalCity = mSearchRequest.mDepartCity;
                mSearchRequest.mArrivalCode = mSearchRequest.mDepartCode;
                mSearchRequest.mDepartCity = tempCity;
                mSearchRequest.mDepartCode = tempCode;
            }
        });
        mLlArrival = (LinearLayout) findViewById(R.id.ll_arrivalCity_container);
        mLlArrival.setClickable(true);
        mLlArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCities();
            }
        });
        mTvArrivalCity = (TextView) findViewById(R.id.tv_arrival_city);
        mTvArrivalCode = (TextView) findViewById(R.id.tv_arrival_code);
    }


    @Override
    public void addDateView() {
        mLlDepartDate = (LinearLayout) findViewById(R.id.ll_departDate_container);
        mLlArrivalDate = (LinearLayout) findViewById(R.id.flight_search_returnDate_container);
    }

    @Override
    public void addPassengersView() {
        mTvPassengerNumber = (TextView) findViewById(R.id.tv_passenger_count);
        mIvMinus = (ImageView)findViewById(R.id.iv_minus);
        mIvMinus.setClickable(true);
        mIvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassengerNumber(mSearchRequest.mPassengerNumber-1);
            }
        });
        mIvPlus = (ImageView)findViewById(R.id.iv_plus);
        mIvPlus.setClickable(true);
        mIvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassengerNumber(mSearchRequest.mPassengerNumber+1);
            }
        });
    }

    @Override
    public void addClassView() {

    }

    @Override
    public void addSearchView() {
        mLlSearch = (LinearLayout)findViewById(R.id.search_button_container);
        mLlSearch.setClickable(true);
        mLlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    /**
     * 添加广告栏
     */
    @Override
    public void addAdvView() {

    }

    /**
     * 设置航班类型，往返，单程
     *
     * @param type
     */
    @Override
    public void setFlightType(String type) {
        mSearchRequest.mTripType = type;
    }

    /**
     * 设置出发城市,城市码
     *
     * @param city
     * @param code
     */
    @Override
    public void setDepart(String city, String code) {
        mSearchRequest.mDepartCity = city;
        mSearchRequest.mDepartCode = code;
    }

    /**
     * 设置到达城市,城市码
     *
     * @param city
     * @param code
     */
    @Override
    public void setArrival(String city, String code) {
        mSearchRequest.mArrivalCity = city;
        mSearchRequest.mArrivalCode = code;
    }


    /**
     * 设置乘客数量
     *
     * @param num
     */
    @Override
    public void setPassengerNumber(int num) {
        mSearchRequest.mPassengerNumber = num;
        mTvPassengerNumber.setText(""+num);
        if(num <= 1)
        {
            mIvMinus.setEnabled(false);
            mIvMinus.setClickable(false);
        }
        else
        {
            mIvMinus.setEnabled(true);
            mIvMinus.setClickable(true);
        }

    }

    /**
     * 设置座舱类型，经济，商务，头等
     *
     * @param class_type
     */
    @Override
    public void setFlightClass(String class_type) {
        mSearchRequest.mClassType = class_type;
    }

    @Override
    public void search() {

    }

    /**
     * 打开可选城市列表
     */
    @Override
    public void openCities() {

    }

    @Override
    public void openCalendar() {

    }

    @Override
    public void setDepartDate(Date date) {

    }

    @Override
    public void setArrivalDate(Date date) {

    }
}