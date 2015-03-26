package com.pineapple.mobilecraft.mediator;

import java.util.ArrayList;
import java.util.Date;

import com.pineapple.mobilecraft.data.social.ActivityData;
import com.pineapple.mobilecraft.data.social.MyUser;

public interface IActivityCreateMediator {
	
	public void setCreator(MyUser user);
	/**
	 * 设置活动标题
	 * @param title
	 */
	public void setTitle(String title);
	
	/**
	 * 设置活动内容
	 * @param content
	 */
	public void setContent(String content);
	
	/**
	 * 设置活动开始时间
	 * @param date
	 */
	public void setTime(Date date);
	
	/**
	 * 设置付费方式
	 * @param type
	 */
	public void setPayType(int type);
	
	/**
	 * 设置活动成员
	 * @param users
	 */
	public void setMembers(ArrayList<MyUser> users);
	
	/**
	 * 当左（确认）按钮按下时触发
	 */
	public void onOKClicked();
	
	/**
	 * 当右（取消）按钮按下时触发
	 */
	public void onCancelClicked();
	
	public ActivityData createActivityData();
	

}
