package com.test.juxiaohui.mdxc.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihao on 15/4/18.
 */
public class FlightOrder {
    public FlightData mFlightData = FlightData.NULL;
    public ContactUser mContactUser = ContactUser.NULL;
    public List<Passenger> mListPassenger = new ArrayList<Passenger>();

    public static FlightOrder NULL = new FlightOrder();
}
