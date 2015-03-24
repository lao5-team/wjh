package com.test.juxiaohui.mdxc.mediator;

import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.Passenger;

public interface IFlightOrderMediator {
	public void setFlightData(FlightData data);
	
	public void addFlightView();
	
	public void addPriceView();
	
	public void addPassengerView();
	
	public void addPassenger(Passenger passenger);
	
	public void removePassenger(Passenger passenger);

	public void Order();
}
