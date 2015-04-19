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
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.manager.ServerManager;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.manager.UserManager;
import com.test.juxiaohui.mdxc.mediator.IFlightOrderMediator;

public class FlightOrderActivity extends Activity implements
		IFlightOrderMediator {
	public static int REQ_SELECT_PASSENGER = 0;
	private FlightData mFromData,mToData;
	private List<Passenger> mPassengers = new ArrayList<Passenger>();
	
	
	private TextView mTvPerAireFarePrice,mTvPerAireFareCurrency,mTvPerTaxPrice,mTvPerTaxCurrency,mTvTotalPrice;
	private ImageButton mIbAddPassenger;
	private LinearLayout mLlFlights;
	private LinearLayout mLlPassengers;
	private TextView mTvAddPassengers;
	private UserManager mUserManager = UserManager.getInstance();
	
	private boolean isOneWay;
	
	
	
	public static void startActivity(String fromId,String toId, Context context)
	{
		Intent intent = new Intent(context, FlightOrderActivity.class);
		intent.putExtra("form_id", fromId);
		intent.putExtra("to_id", toId);
		context.startActivity(intent);
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_flight_book);	
		List<Passenger> passengers= ServerManager.getInstance().getAllPassengers();
		setFlightData(ServerManager.getInstance().getFlightData( getIntent().getStringExtra("form_id")),
						ServerManager.getInstance().getFlightData( getIntent().getStringExtra("to_id")));	
		addFlightView();
		addPassengerView();
		addPriceView();
		for(Passenger passenger:passengers)
		{
			addPassenger(passenger);
		}
	}
	
	@Override
	public void setFlightData(FlightData fromData,FlightData toData) {
		mFromData = fromData;
		mToData = toData;
		if(mFromData == null)
			finish();
		if(toData == null)
			isOneWay = true;
		else
			isOneWay = false;
	}

	@Override
	public void addFlightView() {
		mLlFlights = (LinearLayout) findViewById(R.id.ll_airlines);
		mLlFlights.addView(FlightData.getItemView(this, this.getLayoutInflater(), null, mFromData));
		if(!isOneWay)
			mLlFlights.addView(FlightData.getItemView(this, this.getLayoutInflater(), null, mToData));
	}

	@Override
	public void addPriceView() {
		String temp;
		//机票单价
		mTvPerAireFarePrice = (TextView) this.findViewById(R.id.tv_price);
		temp = isOneWay? String.valueOf(mFromData.mPrize.mTicketPrize):String.valueOf(mFromData.mPrize.mTicketPrize + mToData.mPrize.mTicketPrize);
		mTvPerAireFarePrice.setText(temp);	
		//机票单价币种
		mTvPerAireFareCurrency = (TextView) this.findViewById(R.id.tv_price_currency);
		temp = String.valueOf(mFromData.mPrize.mCurrency);
		mTvPerAireFareCurrency.setText(temp);
		//税费单价
		mTvPerTaxPrice = (TextView) this.findViewById(R.id.tv_tax);
		temp = isOneWay? String.valueOf(mFromData.mPrize.mTax):String.valueOf(mFromData.mPrize.mTax + mToData.mPrize.mTax);
		mTvPerTaxPrice.setText(temp);
		//税费单价币种
		mTvPerTaxCurrency = (TextView) this.findViewById(R.id.tv_tax_currency);
		temp = String.valueOf(mFromData.mPrize.mCurrency);
		mTvPerTaxCurrency.setText(temp);
		//总价
		mTvTotalPrice = (TextView) this.findViewById(R.id.tv_amount_with_current_currency);
		if(isOneWay)
			mTvTotalPrice.setText("RMB" + (mFromData.mPrize.mTicketPrize + mFromData.mPrize.mTax));
		else
			mTvTotalPrice.setText("RMB" + 
									(mFromData.mPrize.mTicketPrize + mFromData.mPrize.mTax + 
											mToData.mPrize.mTicketPrize + mToData.mPrize.mTax));

	}

	@Override
	public void addContactView() 
	{
		
	}

	@Override
	public void addPassengerView() 
	{
	//	mIbAddPassenger = (ImageButton)findViewById(R.id.imageButton_addPassenger);
		mTvAddPassengers = (TextView)findViewById(R.id.tv_addPassenger);
		mTvAddPassengers.setOnClickListener(new OnClickListener() {
			
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
	public void removePassenger(final Passenger passenger) {
		int index = mPassengers.indexOf(passenger);
		mPassengers.remove(passenger);
		mLlPassengers.removeViewAt(index);
	}

	@Override
	public void setContact(ContactUser contactUser) {
		mUserManager.setContactUser(contactUser);
	}

	@Override
	public void submitOrder() {

	}

	@Override
	public void cancel() {
		finish();
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
