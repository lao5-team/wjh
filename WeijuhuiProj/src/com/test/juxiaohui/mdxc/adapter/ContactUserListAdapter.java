package com.test.juxiaohui.mdxc.adapter;

import java.util.List;

import com.test.juxiaohui.R;
import com.test.juxiaohui.mdxc.adapter.PassengerListAdapter.PassengerHoler;
import com.test.juxiaohui.mdxc.app.PassengerListActivity.PassengerSelector;
import com.test.juxiaohui.mdxc.data.ContactUser;
import com.test.juxiaohui.mdxc.data.Passenger;
import com.test.juxiaohui.mdxc.mediator.IContactUserListMediator;
import com.test.juxiaohui.mdxc.mediator.IPassengerListMediator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ContactUserListAdapter extends BaseAdapter {

	LayoutInflater mInflater;
	Context mContext;
	List<ContactUser> mContactUserList;
	IContactUserListMediator mContactUserActivity;
	
	public ContactUserListAdapter(Context context, List<ContactUser> allContactUsers,IContactUserListMediator contactUserActivity)
	{
		mContext = context;
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//mSelecktedList = selectPassengers;
		mContactUserList = allContactUsers;
		mContactUserActivity = contactUserActivity;	
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mContactUserList == null)
			return 0;
		return mContactUserList.size();
	}

	@Override
	public ContactUser getItem(int arg0) {
		// TODO Auto-generated method stub
		if(mContactUserList == null)
			return null;
		return mContactUserList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ContactUserHolder holder;
		ContactUser data;
		data = (ContactUser)getItem(position);

		if(data == null || ! (data instanceof ContactUser))
			return null;
		if (convertView == null) 
		{
			holder = new ContactUserHolder();
			convertView = mInflater.inflate(R.layout.view_guests_item,parent, false);
			holder.title =  (TextView) convertView.findViewById(R.id.tv_title);
			holder.subTitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
			holder.checkView = (ImageView) convertView.findViewById(R.id.iv_check);
			holder.checkSection = (LinearLayout) convertView.findViewById(R.id.view_check_section);
			holder.checkSection.setVisibility(View.INVISIBLE);

			holder.editView = (ImageView) convertView.findViewById(R.id.iv_edit);
			holder.editView.setTag(position);
			holder.editView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					int p = (Integer) arg0.getTag();
					mContactUserActivity.editContactUser(getItem(p));
				}
			});
			
			
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ContactUserHolder) convertView.getTag();
		}
		
		holder.title.setText(data.contactName);
		holder.subTitle.setText(data.contPhone);
		
		return convertView;
	}
	
	class ContactUserHolder
	{
		TextView title;
		TextView subTitle;
		ImageView checkView;
		ImageView editView;
		LinearLayout checkSection;
		
	}
		
	public void refresh()
	{
		this.notifyDataSetChanged();
	}

}
