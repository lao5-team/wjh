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

/**
 * Created by yihao on 15/3/13.
 */
public class FlightData {
	
	public static enum BEHAVIOR_TYPE
	{
		DOMISTIC, INTERNATIONAL
	}
	
	public String mId = "";
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
	public String mAirLine;
	public String mDurTime = "60";


    public static FlightData NULL = new FlightData();
    public static FlightData createTestData()
    {
    	FlightData data = new FlightData();
    	data.mId = UUID.randomUUID().toString();
    	return data;
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
    	holder.mTvCurrency.setText("CNY");
    	holder.mTvPrize.setText(data.mPrize.mTicketPrize + data.mPrize.mTax + "");
    	
    	return convertView;
    }
}
