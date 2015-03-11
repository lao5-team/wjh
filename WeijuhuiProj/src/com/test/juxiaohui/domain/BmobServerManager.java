package com.test.juxiaohui.domain;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.utils.SyncCallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yihao on 15/3/9.
 */
public class BmobServerManager extends MyServerManager {

    protected static BmobServerManager mCurrInstance = null;

    protected BmobServerManager()
    {

    }

    public static BmobServerManager getInstance()
    {
        if(mCurrInstance == null)
        {
            mCurrInstance = new BmobServerManager();
        }
        return (BmobServerManager)mCurrInstance;
    }


    public MyUser getUserInfo(String username) {
        return null;
    }

    public List<String> getActivityIdsByType(String type)
    {
        final String fType = type;
        SyncCallback<List<String>> callback = new SyncCallback<List<String>>() {

            @Override
            public void executeImpl() {
                BmobQuery<ActivityData> query = new BmobQuery<ActivityData>();
                query.addWhereEqualTo("mJewelType", fType);
                query.findObjects(DemoApplication.applicationContext, new FindListener<ActivityData>() {
                    @Override
                    public void onSuccess(List<ActivityData> object) {
                        List<String> ids = new ArrayList<String>();
                        for(ActivityData data:object)
                        {
                            ids.add(data.getObjectId());
                        }
                        onResult(ids);

                    }

                    @Override
                    public void onError(int code, String msg) {
                        onResult(null);
                    }
                });
            }
        };

        return callback.executeBegin();
    }

    public List<ActivityData> getActivityByIds(List<String> ids)
    {
        final List<String> fIds = ids;
        SyncCallback<List<ActivityData>> callback = new SyncCallback<List<ActivityData>>() {

            @Override
            public void executeImpl() {
                BmobQuery<ActivityData> query = new BmobQuery<ActivityData>();
                query.addWhereContainedIn("objectId", fIds);
                query.findObjects(DemoApplication.applicationContext, new FindListener<ActivityData>() {
                    @Override
                    public void onSuccess(List<ActivityData> object) {
                        onResult(object);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        onResult(null);
                    }
                });
            }
        };

        return callback.executeBegin();
    }

    public List<String> getAllActivityIds()
    {
        SyncCallback<List<String>> callback = new SyncCallback<List<String>>() {

            @Override
            public void executeImpl() {
                BmobQuery<ActivityData> query = new BmobQuery<ActivityData>();
                query.findObjects(DemoApplication.applicationContext, new FindListener<ActivityData>() {
                    @Override
                    public void onSuccess(List<ActivityData> object) {
                        List<String> ids = new ArrayList<String>();
                        for(ActivityData data:object)
                        {
                            ids.add(data.getObjectId());
                        }
                        onResult(ids);
                    }

                    @Override
                    public void onError(int code, String msg) {
                        onResult(null);
                    }
                });

//                try {
//                    Thread.sleep(10000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
        };
        //
        return callback.executeBegin();
        //return new ArrayList<String>();
    }
}
