package com.test.juxiaohui.domain;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UploadFileListener;
import com.test.juxiaohui.DemoApplication;
import com.test.juxiaohui.data.ActivityData;
import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.utils.SyncCallback;

import java.io.File;
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

            }
        };
        //
        return callback.executeBegin();

    }

    @Override
    public String uploadImage(File file) {

        final File fFile = file;
        SyncCallback<String> callback = new SyncCallback<String>()
        {

            @Override
            public void executeImpl() {
                final BmobFile bmobFile = new BmobFile(fFile);
                bmobFile.uploadblock(DemoApplication.applicationContext, new UploadFileListener() {

                    @Override
                    public void onSuccess() {
                        // TODO Auto-generated method stub
                        //bmobFile.getUrl()---返回的上传文件的地址（不带域名）
                        //bmobFile.getFileUrl(context)--返回的上传文件的完整地址（带域名）
                        //toast("上传文件成功:" + bmobFile.getFileUrl(context));
                        onResult(bmobFile.getFileUrl(DemoApplication.applicationContext));
                    }

                    @Override
                    public void onProgress(Integer value) {
                        // TODO Auto-generated method stub
                        // 返回的上传进度（百分比）
                    }

                    @Override
                    public void onFailure(int code, String msg) {
                        // TODO Auto-generated method stub
                        //toast("上传文件失败：" + msg);
                        onResult("null");
                    }
                });
            }
        };

        return callback.executeBegin();

    }
}
