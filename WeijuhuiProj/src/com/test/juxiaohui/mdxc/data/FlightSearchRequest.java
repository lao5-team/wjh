package com.test.juxiaohui.mdxc.data;

import org.json.JSONObject;

/**
 * Created by yihao on 15/3/5.
 */
public class FlightSearchRequest {
    public static final String TYPE_ROUNDTRIP = "round_trip";
    public static final String TYPE_ONEWAY = "round_oneway";
    public String mTripType;
    public String mDepartCity;
    public String mArrivalCity;
    public String mDepartCode;
    public String mArrivalCode;
    public String mDepartDate;
    public String mArrivalDate;
    public int mPassengerNumber;

    /**
     * 座舱类型，经济，商务，头等。。。
     */
    public String mClassType;

    public JSONObject toJSON()
    {
        return null;
    }

    public FlightSearchRequest fromJSON(JSONObject json)
    {
        return null;
    }
}
