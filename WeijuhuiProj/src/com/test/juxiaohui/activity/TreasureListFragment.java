package com.test.juxiaohui.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.test.juxiaohui.R;
import com.test.juxiaohui.data.Treasure;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;

import java.util.List;

/**
 * Created by yihao on 15/3/13.
 */
public class TreasureListFragment extends Fragment {
    ListView mLvTreasure;
    List<Treasure> mListTreasure;

    private class ViewHolder
    {
        TextView mTvName;
        TextView mTvUsername;
        ImageView mIvImage;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_alltreasure, null);
        mLvTreasure = (ListView)view.findViewById(R.id.listView_treasures);
        mLvTreasure.setAdapter(new CommonAdapter<Treasure>(mListTreasure, new IAdapterItem<Treasure>() {
            @Override
            public View getView(Treasure data, View convertView) {
                if(null==convertView)
                {
                    View view = getActivity().getLayoutInflater().inflate(R.layout.item_treasure, null);
                    ViewHolder holder = new ViewHolder();
                    holder.mTvName = (TextView) view.findViewById(R.id.textView_treasure_name);
                    holder.mTvUsername = (TextView) view.findViewById(R.id.textView_owner_name);
                    holder.mIvImage = (ImageView) view.findViewById(R.id.imageView_treasure);
                    view.setTag(holder);
                    convertView = view;
                }
                ViewHolder holder = (ViewHolder) convertView.getTag();
                holder.mTvName.setText(data.mName);
                holder.mTvUsername.setText(data.mOwnerName);
                Picasso.with(getActivity()).load(data.mImgs.get(0)).into(holder.mIvImage);
                return convertView;
            }
        }));
        return view;
    }
}