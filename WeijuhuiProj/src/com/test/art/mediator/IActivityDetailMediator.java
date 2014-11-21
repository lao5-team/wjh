package com.test.art.mediator;

import java.util.ArrayList;
import java.util.Date;

import com.test.art.data.ActivityData;
import com.test.art.data.MyUser;

public interface IActivityDetailMediator {
	public void setActivityData(ActivityData data);
	
	/**
	 * 显示活动标题
	 * @param title
	 */
	public void showTitle(String title);
	
	/**
	 * 显示活动内容
	 * @param content
	 */
	public void showContent(String content);
	
	/**
	 * 显示活动开始时间
	 * @param date
	 */
	public void showTime(Date date);
	
	/**
	 * 显示付费方式
	 * @param type
	 */
	public void showPayType(int type);
	
	/**
	 * 显示活动成员
	 * @param users
	 */
	public void showMembers(ArrayList<MyUser> users);
	
	/**修改活动的参与成员，现在该接口还欠考虑
	 * @param users
	 */
	public void changeMembers(ArrayList<MyUser> users);
	
	/**
	 * 当左（确认）按钮按下时触发
	 */
	public void onOKClicked();
	
	/**
	 * 当右（取消）按钮按下时触发
	 */
	public void onCancelClicked();
	
	
}
