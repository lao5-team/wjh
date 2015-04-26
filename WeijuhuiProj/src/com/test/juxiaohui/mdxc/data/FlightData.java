package com.test.juxiaohui.mdxc.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightData {
	
	public static enum BEHAVIOR_TYPE
	{
		DOMISTIC, INTERNATIONAL
	}

	public static enum TRIP_TYPE
	{
		DEPART, RETURN
	}
	
	public String mId = "";
	public String mNumber = "";
    public List<RouteData> mRoutes = new ArrayList<RouteData>();
    public String mAirlineName = "unknown airline";
    public String mAirlineLogoUrl = "";
    public PrizeData mPrize = PrizeData.NULL;
    public String mFromCity;
	public String mToCity;
	public String mFromCode;
	public String mToCode;
	public String mFromTime;
	public String mToTime;
	public String mDurTime = "60";
	public TRIP_TYPE mTripType = TRIP_TYPE.DEPART;

    public static FlightData NULL = new FlightData();
    public static FlightData createTestData()
    {
    	FlightData data = new FlightData();
    	data.mId = UUID.randomUUID().toString();
    	return data;
    }

	public String getId()
	{
		mId = mNumber + mFromTime;
		return mId;
	}
    
    public static class ViewHolder
    {
    	TextView mTvAirlineName;
    	ImageView mIvAirlineLogo;
    	TextView mTvDepartTime;
    	TextView mTvArrivalTime;
    	TextView mTvDepartCity;
    	TextView mTvArrivalCity;
    	TextView mTvDistance;
    	TextView mTvCurrency;
    	TextView mTvPrize; 	
    }

	public static FlightData fromJSON(JSONObject jsonObject)
	{
		FlightData flightData = new FlightData();
		try {
			flightData.mNumber = jsonObject.getString("number");
			flightData.mAirlineName = jsonObject
					.getString("airline");
			flightData.mFromCity = jsonObject.getString("fromCity");
			flightData.mToCity = jsonObject.getString("toCity");
			flightData.mFromCode = jsonObject
					.getString("fromAirport");
			flightData.mToCode = jsonObject.getString("toAirport");
			flightData.mFromTime = jsonObject.getString("fromTime");
			flightData.mToTime = jsonObject.getString("toTime");
			flightData.mDurTime = jsonObject.getString("duration");
			if (jsonObject.has("price")) {
				flightData.mPrize = new PrizeData();
				flightData.mPrize.mTicketPrize = Float
						.valueOf(jsonObject.getString("price"));
				if (jsonObject.getString("trip_type").equals("depart"))
				{
					flightData.mTripType = FlightData.TRIP_TYPE.DEPART;
				}
				else if(jsonObject.getString("trip_type").equals("return"))
				{
					flightData.mTripType = FlightData.TRIP_TYPE.RETURN;
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			flightData = FlightData.NULL;
		}

		return flightData;
	}

	public static JSONObject toJSON(FlightData flightData)
	{
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("number", flightData.mNumber);
			jsonObject.put("airline", flightData.mAirlineName);
			jsonObject.put("fromCity", flightData.mFromCity);
			jsonObject.put("toCity", flightData.mToCity);
			jsonObject.put("fromAirport", flightData.mFromCode);
			jsonObject.put("toAirport", flightData.mToCode);
			jsonObject.put("fromTime", flightData.mFromTime);
			jsonObject.put("toTime", flightData.mToTime);
			jsonObject.put("duration", flightData.mDurTime);
			if(flightData.mPrize!=null)
			{
				jsonObject.put("price", flightData.mPrize.mTicketPrize);
				if(flightData.mTripType == FlightData.TRIP_TYPE.DEPART)
				{
					jsonObject.put("trip_type", "depart");
				}
				else if(flightData.mTripType == TRIP_TYPE.RETURN)
				{
					jsonObject.put("trip_type", "return");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
			jsonObject = null;
		}
		return jsonObject;
	}
    
    public static View getItemView(Context context, LayoutInflater inflator, View convertView, FlightData data)
    {
    	ViewHolder holder;
    	if(null == convertView)
    	{
    		convertView = inflator.inflate(R.layout.item_flight, null); 
    		holder = new ViewHolder();
    		holder.mTvAirlineName = (TextView)convertView.findViewById(R.id.textView_aireline_name);
    		holder.mIvAirlineLogo = (ImageView)convertView.findViewById(R.id.imageView_airline_logo);
    		holder.mTvDepartTime = (TextView)convertView.findViewById(R.id.tv_depart_date);
    		holder.mTvDepartCity = (TextView)convertView.findViewById(R.id.tv_depart_city);
    		holder.mTvArrivalTime = (TextView)convertView.findViewById(R.id.tv_arrival_date);
    		holder.mTvArrivalCity = (TextView)convertView.findViewById(R.id.tv_arrival_city);
    		holder.mTvDistance = (TextView)convertView.findViewById(R.id.tv_duration);
    		holder.mTvCurrency = (TextView)convertView.findViewById(R.id.tv_currency);
    		holder.mTvPrize = (TextView)convertView.findViewById(R.id.tv_price);
    		convertView.setTag(holder);
    	}
    	holder = (ViewHolder) convertView.getTag();
    	if(holder == null)
    		return convertView;
    	holder.mTvAirlineName.setText(data.mAirlineName);
    	if(data.mAirlineLogoUrl.length()>0)
    	{
    		//Picasso.with(context).load(data.mAirlineLogoUrl).into(holder.mIvAirlineLogo);
    		holder.mIvAirlineLogo.setImageResource(new Integer(data.mAirlineLogoUrl));
    	}
    	if(data.mRoutes.size()>0)
    	{
    		holder.mTvDepartTime.setText(data.mRoutes.get(0).mDepartTime);
    		holder.mTvDepartCity.setText(data.mRoutes.get(0).mDepartCity);
    		holder.mTvArrivalTime.setText(data.mRoutes.get(data.mRoutes.size()-1).mArrivalTime);
    		holder.mTvArrivalCity.setText(data.mRoutes.get(data.mRoutes.size()-1).mArrivalCity);
    	}
		holder.mTvDepartTime.setText(data.mFromTime);
		holder.mTvDepartCity.setText(data.mFromCode);
		holder.mTvArrivalTime.setText(data.mToTime);
		holder.mTvArrivalCity.setText(data.mToCode);
    	holder.mTvDistance.setText(data.mDurTime + " min");
    	holder.mTvCurrency.setText(data.mPrize.mCurrency);
    	holder.mTvPrize.setText(data.mPrize.mTicketPrize + data.mPrize.mTax + "");
    	
    	return convertView;
    }
}
