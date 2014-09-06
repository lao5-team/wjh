package com.test.weijuhui.data;

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
		ArrayList<Item> items = new ArrayList<DianpingDao.Item>();
	}
	
	public static class Item
	{
		
	}
}
