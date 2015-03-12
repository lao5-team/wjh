package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.os.Bundle;
import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.mediator.IFlightSearchMediator;

import java.util.Date;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightSearchActivity extends Activity implements IFlightSearchMediator{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);
    }

    @Override
    public void addFlightTypeView() {

    }

    @Override
    public void addCityView() {

    }

    @Override
    public void addExchangeCityView() {

    }

    @Override
    public void addDateView() {

    }

    @Override
    public void addPassengersView() {

    }

    @Override
    public void addEconomyView() {

    }

    @Override
    public void addSearchView() {

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

    }

    /**
     * 设置出发城市
     *
     * @param city
     */
    @Override
    public void setFromCity(String city) {

    }

    @Override
    public void setToCity(String city) {

    }

    @Override
    public void setDate(Date date) {

    }

    /**
     * 设置乘客数量
     *
     * @param num
     */
    @Override
    public void setPassengerNumber(int num) {

    }

    /**
     * 设置座舱类型，经济，商务，头等
     *
     * @param class_type
     */
    @Override
    public void setFlightClass(String class_type) {

    }

    @Override
    public void search() {

    }
}