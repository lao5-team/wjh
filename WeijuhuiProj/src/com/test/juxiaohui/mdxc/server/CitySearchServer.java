package com.test.juxiaohui.mdxc.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.common.dal.ICitySearchServer;
import com.test.juxiaohui.mdxc.data.CityData;

public class CitySearchServer implements ICitySearchServer {
	
	private static CitySearchServer mInstance = null;
	public static CitySearchServer getInstance()
	{
		if(mInstance == null)
		{
			mInstance = new CitySearchServer();
		}
		return mInstance;
	}
	
	public CitySearchServer()
	{
		createFromFile();
	}
	

	@Override
	public ArrayList<CityData> getNearbyPort() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<CityData> getLastSearchCities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<CityData> getHostCities() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<CityData> getSearchResult(String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<CityData> mCities = new ArrayList<CityData>();
	private void createFromFile()
	{	
		
		try {
			InputStream is = DemoApplication.applicationContext.getAssets().open("cities.txt");
	        InputStreamReader isr=new InputStreamReader(is, "UTF-8");
	        BufferedReader br = new BufferedReader(isr);
	        
	        String line="";
	        String[] arrs=null;
	        while ((line=br.readLine())!=null) {
	            arrs=line.split(",");
	            CityData city = new CityData();
	            city.cityName = arrs[2].trim();
	            city.portCode = arrs[0].trim();
	            mCities.add(city);
	        }
	        is.close();
	        br.close();
	        isr.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
}
