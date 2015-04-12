package com.test.juxiaohui.mdxc.app;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.manager.ServerManager;
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
	private LinearLayout mLlFlights;
	private LinearLayout mLlPassengers;
	
	
	
	public static void startActivity(String id, Context context)
	{
		Intent intent = new Intent(context, FlightOrderActivity.class);
		intent.putExtra("id", id);
		context.startActivity(intent);
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight_book);
		String id = getIntent().getStringExtra("id");
		FlightData data  = ServerManager.getInstance().getFlightData(id);	
		List<Passenger> passengers= ServerManager.getInstance().getAllPassengers();
		setFlightData(data);	
		addFlightView();
		addPassengerView();
		addPriceView();
		for(Passenger passenger:passengers)
		{
			addPassenger(passenger);
		}
	}
	
	@Override
	public void setFlightData(FlightData data) {
		mData = data;
	}

	@Override
	public void addFlightView() {
		mLlFlights = (LinearLayout) findViewById(R.id.ll_airlines);
		mLlFlights.addView(FlightData.getItemView(this, this.getLayoutInflater(), null, mData));
	}

	@Override
	public void addPriceView() {
		mTvPrice = (TextView) this.findViewById(R.id.tv_amount_with_current_currency);
		mTvPrice.setText("CNY" + (mData.mPrize.mTicketPrize + mData.mPrize.mTax));
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
		mLlPassengers = (LinearLayout) findViewById(R.id.ll_guests);

	}

	@Override
	public void addPassenger(final Passenger passenger) {
		mPassengers.add(passenger);
		LinearLayout ll = (LinearLayout) this.getLayoutInflater().inflate(R.layout.item_remove_container, null);
		ImageView ivRemove = (ImageView)ll.findViewById(R.id.imageView_remove);
		ivRemove.setClickable(true);
		ivRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				removePassenger(passenger);
			}
		});
		ll.addView(Passenger.getItemView(this, this.getLayoutInflater(), null, passenger),  ll.getChildCount(),
				new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		mLlPassengers.addView(ll);
	}

	@Override
	public void removePassenger(Passenger passenger) {
		int index = mPassengers.indexOf(passenger);
		mPassengers.remove(passenger);
		mLlPassengers.removeViewAt(index);

	}

	@Override
	public void Order() {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(requestCode == REQ_SELECT_PASSENGER)
		{
			final ArrayList<String> ids = data.getStringArrayListExtra("passenger_ids");
			Thread t = new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (String id : ids) {
						final Passenger passenger = ServerManager.getInstance().getPassenger(
								id);
						runOnUiThread(new Runnable() {
							@Override
							public void run() {
								addPassenger(passenger);
							}
						});
						
					}
					
				}
			});
			t.start();
			

		}
	}

}
