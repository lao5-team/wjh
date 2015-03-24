package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;
import java.util.List;

import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.mediator.IPassengerListMediator;

import android.app.Activity;

/**
 * @author yh 
 * Create at 2015-3-23
 * passenger select for FlightOrderActivity
 */
public class PassengerListActivity extends Activity implements IPassengerListMediator {

	private static class PassengerSelector
	{
		Passenger mPassenger;
		boolean mIsSelected = false;
	}
	private List<Passenger> mPassengers = new ArrayList<Passenger>();
	private List<PassengerSelector> mSelectors = new ArrayList<PassengerSelector>();
	
	@Override
	public void addPassenger(Passenger passenger) {
		// TODO Auto-generated method stub
		mPassengers.add(passenger);
		PassengerSelector selector = new PassengerSelector();
		selector.mPassenger = passenger;
		mSelectors.add(selector);
	}

	@Override
	public void editPassenger(Passenger passenger) {
		// TODO Auto-generated method stub
		//open Passenger Edit Activity
	}

	@Override
	public void setPassengerSelect(Passenger passenger, boolean selected) {
		// TODO Auto-generated method stub
		for(PassengerSelector selector:mSelectors)
		{
			if(passenger.equals(selector.mPassenger))
			{
				selector.mIsSelected = selected;
			}
		}
	}

	@Override
	public List<Passenger> getSelectedPassengers() {
		// TODO Auto-generated method stub
		ArrayList<Passenger> passengers = new ArrayList<Passenger>();
		for(PassengerSelector selector:mSelectors)
		{
			if(selector.mIsSelected == true)
			{
				passengers.add(selector.mPassenger);
			}
		}
		return passengers;
	}
	
	

}
