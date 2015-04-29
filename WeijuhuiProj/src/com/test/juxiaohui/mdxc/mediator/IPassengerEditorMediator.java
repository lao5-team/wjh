package com.test.juxiaohui.mdxc.mediator;

import com.test.juxiaohui.mdxc.data.Passenger;

public interface IPassengerEditorMediator 
{
	public static final int CREATEPASSENGER = 0;
	public static final int MODIFYPASSENGER = 1;
	
	public void addNameView();
	
	public void addPassengerIdView();

	public void addBasicInformationView();
	
	public void addConfirmView();
	
	public void insertOrReplacePassenger(Passenger passenger);
}
