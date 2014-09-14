package com.test.weijuhui.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import android.view.inputmethod.CompletionInfo;

import com.test.weijuhui.data.DianpingDao.ComplexBusiness;
import com.test.weijuhui.data.DianpingDao.SimpleBusiness;

public class ActivityData implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3368450357726232660L;
	
	public ComplexBusiness mCB;
	public Date mBeginDate;
	public ArrayList<User> mUsers;
	public int mSpent;
	public int mState;

}
