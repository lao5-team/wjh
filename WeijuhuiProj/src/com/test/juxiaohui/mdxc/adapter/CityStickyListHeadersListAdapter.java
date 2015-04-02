package com.test.juxiaohui.mdxc.adapter;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.data.CityData;
import com.test.juxiaohui.mdxc.server.CitySearchServer;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import android.content.Context;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class CityStickyListHeadersListAdapter extends BaseAdapter implements StickyListHeadersAdapter, SectionIndexer {

	private ArrayList<CityData> mResultCities;
	private ArrayList<CityData> mNearbyPorts;
	private ArrayList<CityData> mLastSearchCities;
	private ArrayList<CityData> mHotCities;
	private SparseIntArray mPositionOfSection;
	private SparseIntArray mSectionOfPosition;
	private boolean isShowResult = true;
	
	private LayoutInflater mInflater;
	
	private Context mContext;
	
	public CityStickyListHeadersListAdapter(ArrayList<CityData> nearbyPorts, ArrayList<CityData> lastSearchCitys, ArrayList<CityData> hotCitys,Context context)
	{
		mHotCities = hotCitys;
		mNearbyPorts = nearbyPorts;
		mLastSearchCities = lastSearchCitys;
		mResultCities = CitySearchServer.getInstance().getSearchResult("");
		mInflater = LayoutInflater.from(context);
		mContext = context;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(isShowResult && mResultCities != null)
			return mResultCities.size();
		else if(!isShowResult)
		{
			int a = mNearbyPorts == null ? 0 : mNearbyPorts.size();
			int b = mLastSearchCities == null ? 0 : mLastSearchCities.size();
			int c = mHotCities == null ? 0 : mHotCities.size();
			return a + b + c;
		}
		else
			return 0;
	}

	@Override
	public CityData getItem(int arg0) {
		// TODO Auto-generated method stub
		if(isShowResult && mResultCities != null)
			return mResultCities.get(arg0);
		else if(!isShowResult)
		{
			int a = mNearbyPorts == null ? 0 : mNearbyPorts.size();
			int b = mLastSearchCities == null ? 0 : mLastSearchCities.size();
			int c = mHotCities == null ? 0 : mHotCities.size();
			
			if(arg0 >= 0 && arg0 < a)
			{
				return mNearbyPorts.get(arg0);
			}
			if(arg0 >= a && arg0 < a + b)
			{
				return mLastSearchCities.get(arg0 - a);
			}
			if(arg0 >= a + b && arg0 < a + b + c)
			{
				return mHotCities.get(arg0 - a - b);
			}
		}
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemViewHoler holder;
		CityData data;
	//	try
	//	{
			data = (CityData)getItem(position);
	//	}
	//	catch(Exception e)
	//	{
			
	//	}
		
		if(data == null || ! (data instanceof CityData))
			return null;
		if (convertView == null) {
			holder = new ItemViewHoler();
			convertView = mInflater.inflate(R.layout.view_city_item,parent, false);
			holder.title =  (TextView) convertView.findViewById(R.id.tv_title);
			holder.subTitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
			holder.distance = (TextView) convertView.findViewById(R.id.tv_distance);
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ItemViewHoler) convertView.getTag();
		}
		
		holder.title.setText(data.cityName);
		holder.subTitle.setText(data.countryName + " - " + data.portName + "(" + data.portCode + ")");
		
		if(mNearbyPorts != null && position < mNearbyPorts.size())
			holder.distance.setText("" + Float.valueOf(data.distanceFromme/1000) + "km");

		return convertView;
	}

	@Override
	public long getHeaderId(int position) {
		// TODO Auto-generated method stub
//		int a = mNearbyPorts == null ? 0 : mNearbyPorts.size();
//		int b = mLastSearchCities == null ? 0 : mLastSearchCities.size();
//		int c = mHotCities == null ? 0 : mHotCities.size();
//		if(position >= 0 && position < a)
//		{
//			return 0;
//		}
//		if(position >= a && position < a + b)
//		{
//			return 1;
//		}
//		if(position >= a + b && position < a + b + c)
//		{
//			return 2;
//		}
//		return position;
		return -1;
	}

	@Override
	public View getHeaderView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		HeaderViewHolder holder;
		
		if (convertView == null) {
			convertView = new TextView(mContext);
			//holder.description = new TextView(mContext);
			LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,Math.round(mContext.getResources().getDimension(R.dimen.margin_36)));
			convertView.setLayoutParams(params);
			convertView.setBackgroundColor(mContext.getResources().getColor(R.color.color_gray_9));
			
			((TextView)convertView).setTextColor(mContext.getResources().getColor(R.color.color_333333));
			((TextView)convertView).setTextSize(mContext.getResources().getDimension(R.dimen.font_size_small));
			convertView.setPadding(Math.round(mContext.getResources().getDimension(R.dimen.margin_10)), 0, 0, 0);
			//convertView.setTag(holder);
			
		} /*else {
			holder = (HeaderViewHolder) convertView.getTag();
		}
*/

		
		switch((int)(getHeaderId(position)))
		{
		case 0:
			((TextView)convertView).setText("附近的机场");
			break;
		case 1:
			((TextView)convertView).setText("最近查看过");
			break;
		case 2:
			((TextView)convertView).setText("人气目的地");
			break;
		}
		
		return convertView;
	}
	
	class ItemViewHoler
	{
		TextView title;
		TextView subTitle;
		TextView distance;
	}
	
	class HeaderViewHolder
	{
		TextView description;
	}
	
	public CityData getDataByPosition(int position)
	{
		return (CityData)getItem(position);
	}

	public int getPositionForSection(int section) {
		return mPositionOfSection.get(section);
	}

	public int getSectionForPosition(int position) {
		return mSectionOfPosition.get(position);
	}

	@Override
	public Object[] getSections() {
		mPositionOfSection = new SparseIntArray();
		mSectionOfPosition = new SparseIntArray();
		int count = getCount();
		List<String> list = new ArrayList<String>();
		list.add(mContext.getString(R.string.search_header));
		mPositionOfSection.put(0, 0);
		mSectionOfPosition.put(0, 0);
		for (int i = 0; i < count; i++) {

			String letter = getItem(i).cityName.substring(0, 1);
			int section = list.size()-1;
			if (list.get(section) != null && !list.get(section).equals(letter)) {
				list.add(letter);
				section++;
				mPositionOfSection.put(section, i);
			}
			mSectionOfPosition.put(i, section);
		}
		return list.toArray(new String[list.size()]);
	}


}
