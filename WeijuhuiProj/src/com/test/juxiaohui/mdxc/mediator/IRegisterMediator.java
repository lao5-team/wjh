package com.test.juxiaohui.mdxc.mediator;


/**
 * Created by qinyy on 15/3/5.
 */
public interface IRegisterMediator {
   
	public void addPhoneNumberView();
    public void addPasswordView();
    public void addConfirmPasswordView();
    public void addRegisterButton();
    public void confirm();
    public void cancel();
}