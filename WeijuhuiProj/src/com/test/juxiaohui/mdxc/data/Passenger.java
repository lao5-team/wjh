package com.test.juxiaohui.mdxc.data;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import com.test.juxiaohui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

/**
 * Created by yihao on 15/4/18.
 * 用来表示旅客,既可以给机票使用，也可以给酒店使用
 */
public class Passenger {
	public static final int ID_TYPE_PASSPORT = 0;
	public static final int ID_TYPE_ID = 1;
	public static Passenger NULL = new Passenger();
	
	public String mId = "";
	
	public String mFirstName = "";
	
	public String mLastName = "";
	
	public int mIdType = ID_TYPE_PASSPORT;
	
	public String mIdNumber = "";
	
	public static Passenger createTestPassenger()
	{
		Passenger passenger = new Passenger();
		passenger.mId = UUID.randomUUID().toString();
		return passenger;
	}
	
	//public Date mBirthDate = new Date(1900, 0, 0);
	
	public static Passenger fromJSON(JSONObject obj){
		try {
			Passenger passenger = new Passenger();
			passenger.mId = obj.getString("id");
			passenger.mFirstName = obj.getString("firstName");
			passenger.mLastName = obj.getString("lastName");
			passenger.mIdType = obj.getInt("idtype");
			passenger.mIdNumber = obj.getString("idNumber");
			return passenger;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//passenger.mBirthDate 
		return NULL;
	}
	
	public static JSONObject toJSON(Passenger passenger)
	{
		try {
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("id", passenger.mId);
			jsonObj.put("firstName", passenger.mFirstName);
			jsonObj.put("lastName", passenger.mLastName);
			jsonObj.put("idtype", passenger.mIdType);
			jsonObj.put("idNumber", passenger.mIdNumber);
			return jsonObj;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	
    public static View getItemView(Context context, LayoutInflater inflator, View convertView, Passenger passenger)
    {
    	View view = inflator.inflate(R.layout.item_passenger, null);
    	TextView tvName = (TextView) view.findViewById(R.id.textView_name);
    	TextView tvID = (TextView) view.findViewById(R.id.textView_ID);
    	tvName.setText(passenger.mLastName + "/" + passenger.mFirstName);
    	if(passenger.mIdType == Passenger.ID_TYPE_ID)
    	{
    		tvID.setText("ID:"+passenger.mId);
    	}
    	else if(passenger.mIdType == Passenger.ID_TYPE_PASSPORT)
    	{
    		tvID.setText("PASSPORT:"+passenger.mId);
    	}
		return view;
    	
    }
	
	
}
	
	