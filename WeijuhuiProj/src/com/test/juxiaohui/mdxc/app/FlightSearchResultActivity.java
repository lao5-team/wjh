package com.test.juxiaohui.mdxc.app;

import android.app.Activity;
import android.os.Bundle;
import com.test.juxiaohui.R;
import com.test.juxiaohui.common.data.FlightData;
import com.test.juxiaohui.mdxc.mediator.ISearchResultMediator;

import java.util.List;

/**
 * Created by yihao on 15/3/13.
 */
public class FlightSearchResultActivity extends Activity implements ISearchResultMediator{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flightsearchresult);
    }

    @Override
    public void setResult(List<FlightData> results) {
        
    }

    @Override
    public void addResultView() {

    }

    @Override
    public void selectOneFlight(FlightData data) {

    }
}