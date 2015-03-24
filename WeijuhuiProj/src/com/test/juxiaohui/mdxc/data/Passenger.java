package com.test.juxiaohui.mdxc.data;

import java.util.Date;

public class Passenger {
	public static final int ID_TYPE_PASSPORT = 0;
	
	public String mFirstName = "";
	
	public String mLastName = "";
	
	public int mIdType = ID_TYPE_PASSPORT;
	
	public String mIdNumber = "";
	
	public Date mBirthDate = new Date(1900, 0, 0);
}
	
	