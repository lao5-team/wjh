package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.test.juxiaohui.R;
import com.test.juxiaohui.common.manager.ServerManager;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.mdxc.mediator.ISearchResultMediator;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightSearchResultActivity extends Activity implements ISearchResultMediator{
	
	ListView mListView;
	List<FlightData> mFlightsList = new ArrayList<FlightData>();
	FlightSearchRequest mRequest = FlightSearchRequest.NULL;
	RelativeLayout mRlprogress;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightsearchresult);
        mRlprogress = (RelativeLayout)findViewById(R.id.progressLayout);
        //setRequest(request);        
        try {
        	Intent intent = getIntent();
			mRequest = FlightSearchRequest.fromJSON(new JSONObject(intent.getStringExtra("request")));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        search();
        addResultView();
    }

    @Override
    public void setResult(List<FlightData> results) {
    	((CommonAdapter<FlightData>)mListView.getAdapter()).setData(results);
    	mListView.invalidate();
    }

    @Override
    public void addResultView() {
        mListView = (ListView)findViewById(R.id.listView_flight);
        mListView.setAdapter(new CommonAdapter<FlightData>(mFlightsList, new IAdapterItem<FlightData>() {

			@Override
			public View getView(FlightData data, View convertView) {
				// TODO Auto-generated method stub
				return FlightData.getItemView(FlightSearchResultActivity.this, getLayoutInflater(), convertView, data);
			}
		}));
        mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectOneFlight(mFlightsList.get(position));
				
			}
		});
    }

    @Override
    public void selectOneFlight(FlightData data) {
    	 FlightOrderActivity.startActivity(data.mId, this);	
    }

	@Override
	public void setRequest(FlightSearchRequest request) {		
		mRequest = request;
	}

	@Override
	public void search() {
		if(mRequest != FlightSearchRequest.NULL)
		{
			Thread thread = new Thread(new Runnable() {
				
				@Override
				public void run() {
					mFlightsList = ServerManager.getInstance().flightSearch(mRequest, FlightData.BEHAVIOR_TYPE.DOMISTIC);
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							mRlprogress.setVisibility(View.GONE);
							setResult(mFlightsList);
						}
					});
				}
			});
			thread.start();			
		}
		else
		{
			finish();
		}
		
	}
}