package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.CityData;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.mdxc.mediator.IFlightSearchMediator;
import com.test.juxiaohui.widget.CalendarActivity;
import com.test.juxiaohui.widget.CalendarActivity.onDataSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightSearchActivity extends Activity implements IFlightSearchMediator{
	public static boolean IS_TEST_MODE = false;
    Button mBtnRoundTrip;
    Button mBtnOneWay;
    TextView mTvDepartCity;
    TextView mTvDepartCode;
    TextView mTvDepartTip;
    ImageView mIvExchange;
    TextView mTvArrivalCity;
    TextView mTvArrivalCode;
    TextView mTvArrivalTip;
    LinearLayout mLlDepart;
    LinearLayout mLlArrival;
    LinearLayout mLlDepartDate;
    LinearLayout mLlReturnlDate;
    TextView mTvDepartTime;
    TextView mTvReturnTime;
    TextView mTvPassengerNumber;
    ImageView mIvMinus;
    ImageView mIvPlus;
    LinearLayout mLlSearch;
    TextView mTvClass;

    /*Data*/
    FlightSearchRequest mSearchRequest = new FlightSearchRequest();
    
    SimpleDateFormat mDataFormat = new SimpleDateFormat("yyyy/MM/dd");
    
    public static void startActivity(Context context)
    {
    	Intent intent = new Intent(context, FlightSearchActivity.class);
    	context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight_search);
        addFlightTypeView();
        addCityView();
        addPassengersView();
        addDateView();
        addClassView();
        addSearchView();

        //set default passenger number
        setPassengerNumber(1);
    }

    @Override
    public void addFlightTypeView() {
        mBtnRoundTrip = (Button) findViewById(R.id.button_roundtrip);
        mBtnRoundTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlightType(FlightSearchRequest.TYPE_ROUNDTRIP);
            }
        });
        mBtnOneWay = (Button) findViewById(R.id.button_oneway);
        mBtnOneWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFlightType(FlightSearchRequest.TYPE_ONEWAY);
            }
        });
    }

    @Override
        public void addCityView() {
        mLlDepart = (LinearLayout) findViewById(R.id.ll_departCity_container);
        mLlDepart.setClickable(true);
        mLlDepart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCities(IFlightSearchMediator.CITY_DEPART);
            }
        });
        mTvDepartCity = (TextView) findViewById(R.id.tv_depart_city);
        mTvDepartCode = (TextView) findViewById(R.id.tv_depart_code);
        mTvDepartTip = (TextView) findViewById(R.id.tv_depart_tip);
        
        mIvExchange = (ImageView) findViewById(R.id.iv_airport_exchange);
        mIvExchange.setClickable(true);
        mIvExchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tempCity = mSearchRequest.mArrivalCity;
                String tempCode = mSearchRequest.mArrivalCode;

                mSearchRequest.mArrivalCity = mSearchRequest.mDepartCity;
                mSearchRequest.mArrivalCode = mSearchRequest.mDepartCode;
                mSearchRequest.mDepartCity = tempCity;
                mSearchRequest.mDepartCode = tempCode;
                
                mTvDepartCity.setText(mSearchRequest.mDepartCity);
                mTvArrivalCity.setText(mSearchRequest.mArrivalCity);
            }
        });
        mLlArrival = (LinearLayout) findViewById(R.id.ll_arrivalCity_container);
        mLlArrival.setClickable(true);
        mLlArrival.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCities(IFlightSearchMediator.CITY_ARRIVAL);
            }
        });
        mTvArrivalCity = (TextView) findViewById(R.id.tv_arrival_city);
        mTvArrivalCode = (TextView) findViewById(R.id.tv_arrival_code);
        mTvArrivalTip = (TextView) findViewById(R.id.tv_arrival_tip);
        if(IS_TEST_MODE)
        {
        	mLlDepart.setVisibility(View.VISIBLE);
        	mTvDepartCity.setText("Beijing");
        	mTvDepartTip.setVisibility(View.GONE);
        	mSearchRequest.mDepartCity = mTvDepartCity.getText().toString();
            mSearchRequest.mDepartCode = "LAX";

        	mLlArrival.setVisibility(View.VISIBLE);
        	mTvArrivalCity.setText("Shanghai");
        	mTvArrivalTip.setVisibility(View.GONE);
        	mSearchRequest.mArrivalCity = mTvArrivalCity.getText().toString();
            mSearchRequest.mArrivalCode = "SHA";
        }
    }


    @Override
    public void addDateView() {
        mLlDepartDate = (LinearLayout) findViewById(R.id.ll_departDate_container);
        mLlReturnlDate = (LinearLayout) findViewById(R.id.flight_search_returnDate_container);
        mTvDepartTime = (TextView) findViewById(R.id.tv_depart_date);
        mTvDepartTime.setText("Depart Time");
        mTvReturnTime = (TextView)findViewById(R.id.tv_return_date);
        mTvReturnTime.setText("Return Time");
        mLlDepartDate.setClickable(true);
        mLlDepartDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openCalendar(true);
			}
		});
        
        mLlReturnlDate.setClickable(true);
        mLlReturnlDate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openCalendar(false);
			}
		});
        
        if(IS_TEST_MODE)
        {
            Date date = new Date();
            mTvDepartTime.setText(date.getMonth() + "/" + date.getDate()); 
            mSearchRequest.mDepartDate = "2015/04/14";//= mTvDepartTime.getText().toString();
            mSearchRequest.mReturnDate = "2015/04/15";
        }

    }

    @Override
    public void addPassengersView() {
        mTvPassengerNumber = (TextView) findViewById(R.id.tv_passenger_count);
        mIvMinus = (ImageView)findViewById(R.id.iv_minus);
        mIvMinus.setClickable(true);
        mIvMinus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassengerNumber(mSearchRequest.mPassengerNumber-1);
            }
        });
        mIvPlus = (ImageView)findViewById(R.id.iv_plus);
        mIvPlus.setClickable(true);
        mIvPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPassengerNumber(mSearchRequest.mPassengerNumber+1);
            }
        });
    }

    @Override
    public void addClassView() {
    	mTvClass = (TextView)findViewById(R.id.tv_flight_class);
    	if(IS_TEST_MODE)
    	{
        	mTvClass.setText("ECONOMY");
        	mSearchRequest.mClassType = FlightSearchRequest.CLASS_ECONOMY;
    	}
    	mSearchRequest.mClassType = FlightSearchRequest.CLASS_ECONOMY;
    }

    @Override
    public void addSearchView() {
        mLlSearch = (LinearLayout)findViewById(R.id.search_button_container);
        mLlSearch.setClickable(true);
        mLlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search();
            }
        });
    }

    /**
     * 添加广告栏
     */
    @Override
    public void addAdvView() {

    }

    /**
     * 设置航班类型，往返，单程
     *
     * @param type
     */
    @Override
    public void setFlightType(String type) {
        mSearchRequest.mTripType = type;
    }

    /**
     * 设置出发城市,城市码
     *
     * @param city
     * @param code
     */
    @Override
    public void setDepart(String city, String code) {
        mSearchRequest.mDepartCity = city;
        mSearchRequest.mDepartCode = code;
    }

    /**
     * 设置到达城市,城市码
     *
     * @param city
     * @param code
     */
    @Override
    public void setArrival(String city, String code) {
        mSearchRequest.mArrivalCity = city;
        mSearchRequest.mArrivalCode = code;
    }


    /**
     * 设置乘客数量
     *
     * @param num
     */
    @Override
    public void setPassengerNumber(int num) {
        mSearchRequest.mPassengerNumber = num;
        mTvPassengerNumber.setText(""+num);
        if(num <= 1)
        {
            mIvMinus.setEnabled(false);
            mIvMinus.setClickable(false);
        }
        else
        {
            mIvMinus.setEnabled(true);
            mIvMinus.setClickable(true);
        }

    }

    /**
     * 设置座舱类型，经济，商务，头等
     *
     * @param class_type
     */
    @Override
    public void setFlightClass(String class_type) {
        mSearchRequest.mClassType = class_type;
    }

    @Override
    public void search() {
    	Intent intent = new Intent(this, FlightSearchResultActivity.class);
    	intent.putExtra("request", FlightSearchRequest.toJSON(mSearchRequest).toString());
    	startActivity(intent);
    }
    /**
     * 打开可选城市列表
     * @param cityType 0
     */
    @Override
    public void openCities(int cityType) {
    	Intent intent = new Intent(this,CitySearchActivity.class);
    	startActivityForResult(intent, cityType);
    }

    @Override
    public void openCalendar(final boolean isDepart) {
    	CalendarActivity.PopupWindows popwindow = new CalendarActivity.PopupWindows(this, getWindow().getDecorView());
    	popwindow.setDateSelectedListener(new onDataSelectedListener() {
			
			@Override
			public void onDateSelected(final String str_date) {
				// TODO Auto-generated method stub
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						try {
							final Date date = mDataFormat.parse(str_date);
							if(isDepart)
							{
								setDepartDate(date);
							}
							else
							{
								setArrivalDate(date);
							}
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
				
			}
		});
    }

    @Override
    public void setDepartDate(Date date) {
    	String str = mDataFormat.format(date);
		mTvDepartTime.setText(str);
		mSearchRequest.mDepartDate = str;
		mTvReturnTime.setText(getDayafter(str));
		mSearchRequest.mReturnDate = mTvReturnTime.getText().toString();
    }

    @Override
    public void setArrivalDate(Date date) {
    	String str = mDataFormat.format(date);
		mTvReturnTime.setText(str);
		mSearchRequest.mReturnDate = str;
    }
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(null!=data)
		{
			CityData citydata = (CityData)data.getSerializableExtra("city");
			switch(requestCode)
			{
			case IFlightSearchMediator.CITY_DEPART:
				mTvDepartCity.setText(citydata.cityName);
				mSearchRequest.mDepartCity = citydata.cityName;
				mSearchRequest.mDepartCode = citydata.cityCode;
                mTvDepartTip.setVisibility(View.GONE);

				break;
			case IFlightSearchMediator.CITY_ARRIVAL:
				mTvArrivalCity.setText(citydata.cityName);
				mSearchRequest.mArrivalCity = citydata.cityName;
				mSearchRequest.mArrivalCode = citydata.cityCode;
                mTvArrivalTip.setVisibility(View.GONE);
			}		
		}
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private String getDayafter(String str_date)
	{
		try {
			Date date = mDataFormat.parse(str_date);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			int day = calendar.get(Calendar.DATE);
			calendar.set(Calendar.DATE, day + 1);
			return mDataFormat.format(calendar.getTime());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
	}
}