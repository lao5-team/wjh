package com.test.juxiaohui.mdxc.mediator;

import com.test.juxiaohui.common.data.FlightData;

import java.util.List;

/**
 * Created by yihao on 15/3/13.
 *
 */
public interface ISearchResultMediator {
    public void setResult(List<FlightData> results);

    public void addResultView();

    public void selectOneFlight(FlightData data);
}
