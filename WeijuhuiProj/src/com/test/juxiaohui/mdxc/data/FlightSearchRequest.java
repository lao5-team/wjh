package com.test.juxiaohui.mdxc.data;

import org.json.JSONObject;

/**
 * Created by yihao on 15/3/5.
 */
public class FlightSearchRequest {
    public String mTripType;
    public String mFromCity;
    public String mToCity;
    public String mFromDate;
    public String mToDate;
    public int mPassengerNumber;
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
