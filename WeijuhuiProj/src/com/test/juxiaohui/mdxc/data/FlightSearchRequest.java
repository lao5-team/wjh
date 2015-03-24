package com.test.juxiaohui.mdxc.data;

import org.json.JSONException;
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
    public int mPassengerNumber = 0;
    /**
     * 座舱类型，经济，商务，头等。。。
     */
    public String mClassType;
    
    public static FlightSearchRequest NULL = new FlightSearchRequest();

    public static JSONObject toJSON(FlightSearchRequest request)
    {
    	
    	try {
    		JSONObject json = new JSONObject();
			json.put("departCity", request.mDepartCity);
			json.put("arrivalCity", request.mArrivalCity);
			json.put("departDate", request.mDepartDate);
			//json.put("arrivalDate", request.mArrivalDate);
			json.put("passengetNumber", request.mPassengerNumber);
			json.put("classType", request.mClassType);
			return json;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return null;
    }

    public static FlightSearchRequest fromJSON(JSONObject json)
    {
    	FlightSearchRequest request = new FlightSearchRequest();
    	try {
			request.mDepartCity = json.getString("departCity");
			request.mArrivalCity = json.getString("arrivalCity");
			request.mDepartDate = json.getString("departDate");
			//request.mArrivalDate = json.getString("arrivalDate");
			request.mPassengerNumber = json.getInt("passengetNumber");
			request.mClassType = json.getString("classType");
			return request;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  	
        return null;
    }
}
