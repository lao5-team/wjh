package com.test.juxiaohui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.*;
import com.test.juxiaohui.R;
import com.test.juxiaohui.data.Treasure;
import com.test.juxiaohui.data.comment.TreasureComment;
import com.test.juxiaohui.domain.BmobServerManager;
import com.test.juxiaohui.mediator.ITreasureDetailMediator;
import com.test.juxiaohui.widget.CommonAdapter;
import com.test.juxiaohui.widget.IAdapterItem;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by yihao on 15/3/12.
 * 用来查看宝物详情
 */
public class TreasureDetailActivity extends Activity implements ITreasureDetailMediator{

    private TextView mTvTitle;
    private ImageSwitcher mVPImages;
    private TextView mTvDesc;
    private Button mBtnComments;
    private Button mBtnProfComments;
    private ListView mLvComments; //普通评论和专家点评用一个ListView，用两种不同的adapter
    private CommonAdapter mAdapterComments;
    private CommonAdapter mAdapterProfComments;
    //发表评论的按钮和编辑框
    private Button mBtnComment;
    private EditText mEtxComment;

    private Treasure mTreasure;
    private List<TreasureComment> mListComment;
    private List<TreasureComment> mListProfComment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_treasuredetail);
        addTitleView();
    }

    @Override
    public void setTreasure(Treasure treasure) {
        mTreasure = treasure;
        mListComment = BmobServerManager.getInstance().getTreasureComments(mTreasure.mCommentIds);
        mAdapterComments = new CommonAdapter<TreasureComment>(mListComment, new IAdapterItem<TreasureComment>() {
            @Override
            public View getView(TreasureComment data, View convertView) {
                return null;
            }
        });

        mListProfComment = BmobServerManager.getInstance().getTreasureProfComment(mTreasure.mCommentIds);
        mAdapterProfComments = new CommonAdapter<TreasureComment>(mListProfComment, new IAdapterItem<TreasureComment>() {
            @Override
            public View getView(TreasureComment data, View convertView) {
                return null;
            }
        });
    }

    @Override
    public void addTitleView() {
        mTvTitle = (TextView)findViewById(R.id.editText_treasure_name);
        mTvTitle.setText(mTreasure.mName);
    }

    @Override
    public void addImagesView() {
        mVPImages = (ImageSwitcher)findViewById(R.id.imageSwitcher_treasure_imgs);
        #mVPImages.
    }

    @Override
    public void addDescView() {
        mTvDesc = (TextView)findViewById(R.id.editText_treasure_desc);
        mTvDesc.setText(mTreasure.mDesc);
    }

    @Override
    public void addCommentsView() {
        mLvComments = (ListView)findViewById(R.id.listView_comment);
        mBtnComments = (Button)findViewById(R.id.button_comment);
        mBtnComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToComments();
            }
        });
    }

    @Override
    public void addProfCommentsView() {
        mBtnProfComments = (Button)findViewById(R.id.button_profcomment);
        mBtnProfComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToProfcomments();
            }
        });

    }

    @Override
    public void switchToComments() {
        mLvComments.setAdapter(mAdapterComments);
    }

    @Override
    public void switchToProfcomments() {
        mLvComments.setAdapter(mAdapterProfComments);
    }

    @Override
    public void sendComment(TreasureComment comment) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                BmobServerManager.getInstance().sendTreasureComment(comment);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLvComments.invalidate();
                    }
                });
            }
        });
        t.start();


    }
}