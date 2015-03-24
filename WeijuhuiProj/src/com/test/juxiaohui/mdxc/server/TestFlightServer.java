package com.test.juxiaohui.mdxc.server;

import java.util.ArrayList;
import java.util.List;

import com.test.juxiaohui.common.dal.IFlightManager;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightData.BEHAVIOR_TYPE;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.mdxc.data.RouteData;

public class TestFlightServer implements IFlightManager{
	private static TestFlightServer mInstance = null;
	private List<FlightData> mDatas = new ArrayList<FlightData>();
	public static TestFlightServer getInstance()
	{
		if(null == mInstance)
		{
			mInstance = new TestFlightServer();
		}
		return mInstance;
	}
	

	@Override
	public FlightData bookabilityRequest(FlightData data, BEHAVIOR_TYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlightData repricingRequest(FlightData data, BEHAVIOR_TYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlightData createOrder(FlightData data, BEHAVIOR_TYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlightData submitOrder(FlightData data, BEHAVIOR_TYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlightData cancelOrder(FlightData data, BEHAVIOR_TYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlightData queryOrderList(FlightData data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FlightData queryOrderDetail(FlightData data, BEHAVIOR_TYPE type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FlightData> flightSearch(FlightSearchRequest request,
			BEHAVIOR_TYPE type) {
		// test mode
		

		FlightData data = FlightData.createTestData();
		data.mAirlineName = "Jixiang";
		RouteData route = new RouteData();
		route.mDepartCity = request.mDepartCity;
		route.mArrivalCity = request.mArrivalCity;
		route.mDepartDate = request.mDepartDate;
		route.mArrivalDate = request.mArrivalDate;
		route.mDepartTime = "8:00";
		route.mArrivalTime = "10:00";
		route.mDepartAirport = "T3";
		route.mArrivalAirport = "hongqiao";
		data.mRoutes.add(route);
		// data.mAirlineLogoUrl =
		mDatas.add(data);

		data = FlightData.createTestData();
		data.mAirlineName = "nanfang";
		route = new RouteData();
		route.mDepartCity = request.mDepartCity;
		route.mArrivalCity = request.mArrivalCity;
		route.mDepartDate = request.mDepartDate;
		route.mArrivalDate = request.mArrivalDate;
		route.mDepartTime = "9:00";
		route.mArrivalTime = "11:00";
		route.mDepartAirport = "T3";
		route.mArrivalAirport = "hongqiao";
		data.mRoutes.add(route);
		mDatas.add(data);
		return mDatas;
	}
	
	@Override
	public FlightData getFlightData(String id)
	{
		for(FlightData data:mDatas)
		{
			if(data.mId.equals(id))
			{
				return data;
			}
		}
		return FlightData.NULL;
	}
	
	private void createTestData()
	{
		
	}

}
