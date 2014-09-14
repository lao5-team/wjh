package com.test.weijuhui.data;

import java.io.Serializable;
import java.util.ArrayList;

public class DianpingDao {
	
	public static class TreeData
	{
		public String name;
		public ArrayList<Category> categories = new ArrayList<DianpingDao.Category>();
	}
	
	public static class Category
	{
		String name;
		ArrayList<SubCategory> subCategories = new ArrayList<DianpingDao.SubCategory>();
	}
	
	public static class SubCategory
	{
		String name;
		//ArrayList<Item> items = new ArrayList<DianpingDao.Item>();
	}
	
	public static class SimpleBusiness implements Serializable
	{
		/**
		 * 
		 */
		private static final long serialVersionUID = -8431283130416112594L;
		public String mName;
		public String mAddress;
		public String mSmallImgUrl;
		public String mBusinessID;
	}
	
	public static class ComplexBusiness implements Serializable
	{

		/**
		 * 
		 */
		private static final long serialVersionUID = 6843458871362781648L;
		public String mName;
		public String mBranchName = "";
		public String mAddress = null;
		public String mPhoneNumber = null;
		public String mImgUrl;
	}
}
