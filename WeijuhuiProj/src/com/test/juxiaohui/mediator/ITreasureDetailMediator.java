package com.test.juxiaohui.mediator;

import com.test.juxiaohui.data.Treasure;
import com.test.juxiaohui.data.comment.TreasureComment;

/**
 * Created by yihao on 15/3/12.
 */
public interface ITreasureDetailMediator {

    public void setTreasure(Treasure treasure);

    public void addTitleView();

    public void addImagesView();

    //点击按钮，显示图文详情
    public void addDescView();

    //点击按钮，显示普通评论
    public void addCommentsView();

    //点击按钮，显示专家点评
    public void addProfCommentsView();

    //切换到普通评论
    public void switchToComments();

    //切换到专家评论
    public void switchToProfcomments();

    public void sendComment(TreasureComment comment);

}
