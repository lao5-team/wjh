package com.test.juxiaohui.mdxc.data;

import java.io.Serializable;

public class CityData implements Serializable
{
	public String cityName;
	public String cityCode;
	public String countryName;
	public String countryCode;
	public String provinceName;
	public String provinceCode;
	
	//离我的距离 单位米  赤道长40075000米 long型可以
	public long distanceFromme;
	
	public String portName;
	public String portCode;
}
