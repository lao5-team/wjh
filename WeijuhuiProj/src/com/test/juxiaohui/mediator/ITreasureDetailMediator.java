package com.test.juxiaohui.mediator;

import com.test.juxiaohui.data.MyUser;
import com.test.juxiaohui.data.Treasure;
import com.test.juxiaohui.data.comment.TreasureComment;

/**
 * Created by yihao on 15/3/12.
 */
public interface ITreasureDetailMediator {

    /**
     *
     * @param treasure
     */
    public void setTreasure(Treasure treasure);

    public void setUser(MyUser user);

    public void addUserView();

    public void addTitleView();

    public void addImagesView();

    /**
     * 点击按钮，显示图文详情
     */
    public void addDescView();


    /**
     * 点击按钮，显示普通评论
     */
    public void addCommentsView();

    /**
     * 点击按钮，显示专家点评
     */
    public void addProfCommentsView();

    /**
     * 添加评论按钮和输入框
     */
    public void addCommentControl();

    /**
     * 切换到普通评论
     */

    public void switchToComments();

    /**
     * 切换到专家评论
     */
    public void switchToProfcomments();



    public void sendComment(TreasureComment comment);

}
