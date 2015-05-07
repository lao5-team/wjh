package com.test.juxiaohui.mdxc.mediator;

import java.util.List;

import com.test.juxiaohui.mdxc.data.ContactUser;


public interface IContactUserListMediator {
	
	public void addContactUser(ContactUser user);
	
	public void editContactUser(ContactUser user);
	
	public void setContactUser(ContactUser user);
	
	public  List<ContactUser> getContactUsers();

}
