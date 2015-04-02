package com.test.juxiaohui.mdxc.server;

import com.test.juxiaohui.common.dal.IFlightManager;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.utils.SyncHTTPCaller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihao on 15/3/31.
 */
public class FlightServer implements IFlightManager {
    /**
     * 查询航班信息，区分国内航班与国际航班
     * 请求示例 http://64.251.7.148/flight/list?from=LAX&to=SHA&departDate=2015/04/14&returnDate=2015/04/24&cabin=Economy
     * @param request
     * @param type    BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
     * @return
     */
    @Override
    public List<FlightData> flightSearch(FlightSearchRequest request, FlightData.BEHAVIOR_TYPE type) {
        String url = "http://64.251.7.148/flight/list?";
        url += "from=" + request.mDepartCode;
        url += "&to=" + request.mArrivalCode;
        url += "&departDate=" + request.mDepartDate;
        url += "&returnDate=" + request.mReturnDate;
        url += "&cabin=" + request.mClassType;
        SyncHTTPCaller<List<FlightData>> caller;
        caller = new SyncHTTPCaller<List<FlightData>>(url) {

            @Override
            public List<FlightData> postExcute(String result) {
                List<FlightData> resultObjects = new ArrayList<FlightData>();
                try {
                    JSONObject json = new JSONObject(result);
                    JSONArray prices = json.getJSONArray("prices");
                    JSONArray flights = json.getJSONArray("flights");
                    for(int i=0; i<flights.length(); i++)
                    {
                        JSONObject jsonObject = flights.getJSONObject(i);
                        FlightData flightData = new FlightData();
                        flightData.mId = jsonObject.getString("number");
                        flightData.mFromCity = jsonObject.getString("fromCity");
                        flightData.mToCity = jsonObject.getString("toCity");
                        flightData.mFromCode = jsonObject.getString("fromAirport");
                        flightData.mToCode = jsonObject.getString("toAirport");
                        flightData.mFromTime = jsonObject.getString("fromTime");
                        flightData.mToTime = jsonObject.getString("toTime");
                        resultObjects.add(flightData);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return resultObjects;
            }
        };
        return caller.execute();
    }

    /**
     * 仓位验证
     *
     * @param data
     * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
     * @return
     */
    @Override
    public FlightData bookabilityRequest(FlightData data, FlightData.BEHAVIOR_TYPE type) {
        return null;
    }

    /**
     * 价格验证
     *
     * @param data
     * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
     * @return
     */
    @Override
    public FlightData repricingRequest(FlightData data, FlightData.BEHAVIOR_TYPE type) {
        return null;
    }

    /**
     * 生成订单
     *
     * @param data
     * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
     * @return
     */
    @Override
    public FlightData createOrder(FlightData data, FlightData.BEHAVIOR_TYPE type) {
        return null;
    }

    /**
     * 提交订单
     *
     * @param data
     * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
     * @return
     */
    @Override
    public FlightData submitOrder(FlightData data, FlightData.BEHAVIOR_TYPE type) {
        return null;
    }

    /**
     * 取消订单
     *
     * @param data
     * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
     * @return
     */
    @Override
    public FlightData cancelOrder(FlightData data, FlightData.BEHAVIOR_TYPE type) {
        return null;
    }

    /**
     * 订单列表查询
     *
     * @param data
     * @return
     */
    @Override
    public FlightData queryOrderList(FlightData data) {
        return null;
    }

    /**
     * 订单详情查询
     *
     * @param data
     * @param type BEHAVIOR_TYPE.DOMISTIC or BEHAVIOR_TYPE.INTERNATIONAL
     * @return
     */
    @Override
    public FlightData queryOrderDetail(FlightData data, FlightData.BEHAVIOR_TYPE type) {
        return null;
    }

    /**
     * 获取某一条航班数据
     *
     * @param id
     * @return
     */
    @Override
    public FlightData getFlightData(String id) {
        return null;
    }
}