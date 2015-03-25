package com.test.juxiaohui.common.manager;

import java.util.List;

import com.test.juxiaohui.common.dal.IFlightManager;
import com.test.juxiaohui.common.dal.IPassengerServer;
import com.test.juxiaohui.common.dal.IUserServer;
import com.test.juxiaohui.mdxc.data.FlightData;
import com.test.juxiaohui.mdxc.data.FlightSearchRequest;
import com.test.juxiaohui.mdxc.data.FlightData.BEHAVIOR_TYPE;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.server.TestFlightServer;
import com.test.juxiaohui.mdxc.server.TestPassengerServer;
import com.test.juxiaohui.mdxc.server.TestUserServer;
import com.test.juxiaohui.utils.SyncHTTPCaller;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by yihao on 15/3/4.
 */
public class ServerManager {

    private static ServerManager mInstance = null;
    private IUserServer mUserServer;
    private IFlightManager mFlightManager;
    private IPassengerServer mPassengerServer;

    private ServerManager()
    {
        mUserServer = new TestUserServer();
        mFlightManager = new TestFlightServer();
        mPassengerServer = new TestPassengerServer();
    }

    public String register(String username ,String password)
    {
        return mUserServer.register(username, password);
    }

    public String login(String username ,String password)
    {
        return mUserServer.login(username, password);

    }

    public void logout()
    {
        mUserServer.logout();
    }

    public boolean isLogin()
    {
        return false;
    }

    public static ServerManager getInstance()
    {
        if(null == mInstance)
        {
            mInstance = new ServerManager();
        }
        return mInstance;
    }
    
	public List<FlightData> flightSearch(FlightSearchRequest request,
			BEHAVIOR_TYPE type) {
		return mFlightManager.flightSearch(request, type);

	}
	
	public FlightData getFlightData(String id)
	{
		return mFlightManager.getFlightData(id);
	}
	
	public void addPassenger(Passenger passenger)
	{
		mPassengerServer.addPassenger(passenger);
	}
	
	public Passenger getPassenger(String id)
	{
		return mPassengerServer.getPassenger(id);
	}
	
	public List<Passenger> getAllPassengers() {
		return mPassengerServer.getAllPassengers();
	}


}
