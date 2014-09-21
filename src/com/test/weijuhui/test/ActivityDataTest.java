package com.test.weijuhui.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.test.InstrumentationTestRunner;
import android.util.Log;

import com.test.weijuhui.data.ActivityData;
import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.data.User;


public class ActivityDataTest extends InstrumentationTestRunner {

	//String testString = "";
	
	protected void setUp() throws Exception {
	}
	
	public void testFromJSON()
	{
	}
	
	public void testToJSON()
	{
		ComplexBusiness testCB = new ComplexBusiness();
		testCB.mName = "东来顺";
		testCB.mImgUrl = "http://i2.dpfile.com//pc//e6801a8a0b89fa2dd93e582c69d2e7cd(700x700)//thumb.jpg";
		testCB.mPhoneNumber = "12345678";
		
		User testUser = new User();
		testUser.mName = "testCreator";
		
		ArrayList<User> users = new ArrayList<User>();
		User testGuest = new User();
		testGuest.mName = "test0";
		users.add(testGuest);
		
		testGuest = new User();
		testGuest.mName = "test1";
		users.add(testGuest);
		
		testGuest = new User();
		testGuest.mName = "test2";
		users.add(testGuest);	
		
		try {
			ActivityData data = new ActivityData.ActivityBuilder().setBeginTime(new SimpleDateFormat(ActivityData.dataPattern).parse("2014年9月20日16时")
					).setComplexBusiness(testCB).setCreator(testUser).setUsers(users).create();
			
			String stringJSON = ActivityData.toJSON(data).toString();
			Log.v("weijuhui", stringJSON);
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
