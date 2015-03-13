package com.test.juxiaohui.mediator;

import com.test.juxiaohui.data.Treasure;

import java.util.List;

/**
 * Created by yihao on 15/3/13.
 * 用来显示所有宝物
 */
public interface IAllTreasureMediator {
    public void setTreasures(List<Treasure> treasures);

    public void addTreasuresView();

    public void onSelectOneTreasure(Treasure treasure);
}
