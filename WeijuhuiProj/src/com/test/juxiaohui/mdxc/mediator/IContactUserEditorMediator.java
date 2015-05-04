package com.test.juxiaohui.mdxc.mediator;

import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.Passenger;

public interface IContactUserEditorMediator {
		
	public void addNameView();
	
	public void addPhoneNumberView();

	public void addrecipentView();
	
	public void addEmailView();
	
	public void addConfirmView();
	
	public void insertOrReplaceContactUser(ContactUser user);
	
}
