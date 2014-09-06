package com.test.weijuhui.data;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.test.weijuhui.data.DianpingDao.Category;
import com.test.weijuhui.data.DianpingDao.SubCategory;
import com.test.weijuhui.data.DianpingDao.TreeData;

import android.os.Environment;
import android.util.Log;


public class DianpingDataHelper {
	public static String App_Key = "50624700";
	public static String App_Secret = "72b35e24d55748af8f065fe381ea8d7c";
	private static DianpingDataHelper mInstance = null;
	
	private TreeData mCacheContentData = null;
	private String ROOT_DIR = Environment.getExternalStorageDirectory().getPath() + File.separator + "Weijuhui";
	private String CACHE_DIR = ROOT_DIR + File.separator + "cache";
	private Boolean OFFLINE_DEBUG = true;
	public static DianpingDataHelper getInstance()
	{
		if(null==mInstance)
		{
			mInstance = new DianpingDataHelper();
		}
		return mInstance;
	}
	
	public DianpingDataHelper()
	{
		File rootDir = new File(ROOT_DIR);
		if(!rootDir.exists())
		{
			rootDir.mkdir();
			File cacheDir = new File(CACHE_DIR);
			cacheDir.mkdir();
		}
	}
	
	public String[] getContentCategories()
	{
		if(null != mCacheContentData)
		{
			String[] results = new String[mCacheContentData.categories.size()];
			for(int i=0; i<mCacheContentData.categories.size(); i++)
			{
				results[i] = mCacheContentData.categories.get(i).name;
			}
			return results;
		}
		else
		{
			String result = null;
			if(OFFLINE_DEBUG)
			{
				result = readContentCategoriesData();
			}
			else
			{
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("city", "北京");
				result = DianpingApiTool.requestApi("http://api.dianping.com/v1/metadata/get_categories_with_businesses", 
						App_Key, App_Secret, map);
				cacheContentCategoriesData(result);
			}

			
			try {
				mCacheContentData = new DianpingDao.TreeData();
				mCacheContentData.name = "content";
				JSONObject json = new JSONObject( result);
				JSONArray jsonArray = json.getJSONArray("categories");
				String[] results = new String[jsonArray.length()];
				for(int i=0; i<jsonArray.length(); i++)
				{
					String str = jsonArray.getString(i);
					JSONObject jsonCategory = new JSONObject(str);
					DianpingDao.Category category = new Category();
					category.name = jsonCategory.getString("category_name");
					results[i] = category.name;
					mCacheContentData.categories.add(category);
					JSONArray jsonSubCategories = jsonCategory.getJSONArray("subcategories");
					for(int j=0; j<jsonSubCategories.length(); j++)
					{
						str = jsonSubCategories.getString(j);
						JSONObject jsonSubCategory = new JSONObject(str);
						DianpingDao.SubCategory subCategory = new SubCategory();
						subCategory.name = jsonSubCategory.getString("category_name");
						category.subCategories.add(subCategory);
					}
					
				}
				return results;
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
			return null;
		}

	}
	
	public String[] getContentSubCategories(String category)
	{
		if(null == mCacheContentData)
		{
			getContentCategories();
		}
		for(int i=0; i<mCacheContentData.categories.size(); i++)
		{
			if(category.equals(mCacheContentData.categories.get(i).name))
			{
				String[] result = new String[mCacheContentData.categories.get(i).subCategories.size()];
				for(int j=0; j<result.length; j++)
				{
					result[j] = mCacheContentData.categories.get(i).subCategories.get(j).name;
				}
				return result;
			}
		}
		return null;
	}
	
	public String[] searchBusiness(HashMap<String,String> paramMap)
	{
		String result = "" ;
		result = DianpingApiTool.requestApi("http://api.dianping.com/v1/business/find_businesses", 
				App_Key, App_Secret, paramMap);
		
		try {
			JSONObject json;
			json = new JSONObject(result);
			JSONArray jsonArray = json.getJSONArray("businesses");
			String[] results = new String[jsonArray.length()];
			for(int i=0; i<results.length; i++)
			{
				results[i] = jsonArray.getString(i);
			}
			return results;
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	public String[] getPlaceCategories()
	{
		return null;
	}
	
	public String[] getPlaceSubCategories(String category)
	{
		return null;
	}
	
	private void cacheContentCategoriesData(String result)
	{
		if(null!=result)
		{
			try {
				FileWriter fw = new FileWriter(CACHE_DIR + File.separator + "ContentCategoriesData.txt");
				fw.write(result);
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String readContentCategoriesData()
	{
		String result = "";
		try {
			FileReader fr = new FileReader(CACHE_DIR + File.separator + "ContentCategoriesData.txt");
			int readNum = 0;
			do
			{
				char[] buffer = new char[1024]; 
				try {
					readNum = fr.read(buffer);
					result += new String(buffer);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			while(readNum == 1024);
			fr.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	

}
