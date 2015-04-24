package com.test.juxiaohui.mdxc.manager;

import com.test.juxiaohui.mdxc.data.FlightOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihao on 15/4/10.
 */
public class OrderManager {

    /**
     *  提交一个订单
     * @param flightOrder
     * @return 若提交成功，则会返回一个id，否则会返回错误信息
     */
    public String submitFlightOrder(FlightOrder flightOrder)
    {
        return "";
    }

    /**
     * 返回所有机票订单
     * @return 正常情况下，返回机票订单列表，否则返回一个空的列表
     * 比如没有登录，则未异常情况
     */
    public List<FlightOrder> getFlightOrderList()
    {
        return new ArrayList<FlightOrder>();
    }

    /**
     * 通过id查找订单，应该用不到
     * @param id
     * @return 如果未登录，则返回FlightData.NULL，如果已登录，且id存在，则返回一个订单信息，否则也返回FlightData.NULL
     */
    public FlightOrder getFlightOrderbyId(String id)
    {
        return FlightOrder.NULL;
    }

    /**
     * 取消订单，也需要登录
     * @param id
     * @return
     */
    public String cancelFlightOrder(String id)
    {
        return "";
    }

    public String finishFlightOrder(String id)
    {
        return "";
    }


    /**
     * 删除已完成的订单
     * @param id
     * @return
     */
    public String removeFlightOrder(String id)
    {
        return "";
    }







}
