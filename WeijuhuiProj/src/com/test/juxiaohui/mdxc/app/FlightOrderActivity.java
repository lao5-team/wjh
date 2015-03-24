package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.mediator.IFlightOrderMediator;

public class FlightOrderActivity extends Activity implements
		IFlightOrderMediator {
	public static int REQ_SELECT_PASSENGER = 0;
	private FlightData mData;
	private List<Passenger> mPassengers = new ArrayList<Passenger>();
	private TextView mTvPrice;
	private ImageButton mIbAddPassenger;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight_book);
	}
	
	@Override
	public void setFlightData(FlightData data) {
		mData = data;
	}

	@Override
	public void addFlightView() {
		
	}

	@Override
	public void addPriceView() {
		mTvPrice = (TextView) this.findViewById(R.id.tv_amount_with_current_currency);
	}

	@Override
	public void addPassengerView() {
		mIbAddPassenger = (ImageButton)findViewById(R.id.imageButton_addPassenger);
		mIbAddPassenger.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(FlightOrderActivity.this, PassengerListActivity.class);
				startActivityForResult(intent, REQ_SELECT_PASSENGER);
			}
		});

	}

	@Override
	public void addPassenger(Passenger passenger) {
		mPassengers.add(passenger);
	}

	@Override
	public void removePassenger(Passenger passenger) {
		mPassengers.remove(passenger);

	}

	@Override
	public void Order() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		
	}

}
