package com.test.juxiaohui.mdxc.app;

import com.test.juxiaohui.R;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class EntryActivity extends FragmentActivity {
	TextView mTvHotel;
	TextView mTvFlights;
	
	public static void startActivity(Context context)
	{
		Intent intent = new Intent(context, EntryActivity.class);
		context.startActivity(intent);
	}
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_home);
		addFlightView();
		
		
	}
	
	private void addFlightView()
	{
		mTvFlights = (TextView)findViewById(R.id.tv_flight_button);
		mTvFlights.setClickable(true);
		mTvFlights.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FlightSearchActivity.startActivity(EntryActivity.this);
				
			}
		});
	}
}
