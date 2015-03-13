package com.test.juxiaohui.mdxc.mediator;

import java.util.Date;

/**
 * Created by yihao on 15/3/5.
 */
public interface IFlightSearchMediator {

    public void addFlightTypeView();

    public void addCityView();

    public void addDateView();

    public void addPassengersView();

    public void addClassView();

    public void addSearchView();

    /**
     * 添加广告栏
     */
    public void addAdvView();

    /**
     * 设置航班类型，往返，单程
     * @param type
     */
    public void setFlightType(String type);

    /**
     * 设置出发城市
     * @param city
     * @param code
     */
    public void setDepart(String city, String code);

    /**
     * 设置到达城市
     * @param city
     * @param code
     */
    public void setArrival(String city, String code);


    /**
     * 设置乘客数量
     * @param num
     */
    public void setPassengerNumber(int num);

    /**
     * 设置座舱类型，经济，商务，头等
     * @param class_type
     */
    public void setFlightClass(String class_type);

    public void search();

    /**
     * 打开城市列表
     */
    public void openCities();

    public void openCalendar();

    public void setDepartDate(Date date);

    public void setArrivalDate(Date date);





}
