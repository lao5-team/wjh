package com.test.juxiaohui.mdxc.mediator;

/**
 * Created by yihao on 15/3/4.
 */
public interface ILoginMediator {
    public void addUsernameView();

    public void addPasswordView();
    
    public void addCheckcodeView();

    public void confirm();

    public void cancel();
}
