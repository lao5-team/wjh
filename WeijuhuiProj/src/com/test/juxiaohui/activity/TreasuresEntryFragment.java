package com.test.juxiaohui.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.test.juxiaohui.R;
import com.test.juxiaohui.domain.TreasureManager;

/**
 * Created by yihao on 15/3/14.
 */
public class TreasuresEntryFragment extends Fragment {

    Button mBtn_upload_treasure;
    TreasureListFragment mFragment;
    public static final int REQUEST_CREATE_TREASURE = 0;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_treasures_entry);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_treasures_entry, container, false);
        mBtn_upload_treasure = (Button)view.findViewById(R.id.button_upload_treasure);
        mBtn_upload_treasure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TreasureCreateActivity.startActivity(getActivity(), TreasuresEntryFragment.this);
            }
        });
        mFragment = new TreasureListFragment();
        FragmentTransaction trx = getChildFragmentManager().beginTransaction();
        trx.add(R.id.fragment_container, mFragment).commit();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if(requestCode == REQUEST_CREATE_TREASURE)
        {
            Thread t= new Thread(new Runnable() {
                @Override
                public void run() {
                    mFragment.setTreasures(TreasureManager.getInstance().getAllTreasureIds());
                }
            });
            t.start();

        }
    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        FragmentTransaction trx = getFragmentManager().beginTransaction();
//        trx.remove(mFragment).commit();
//    }
}