package com.example.administrator.myeasypieview.piechartview.anim;


import com.example.administrator.myeasypieview.piechartview.ModuleDataInfo;
import com.example.administrator.myeasypieview.piechartview.view.AbsPieChartView;

import java.util.List;

/**
 * Created by xiongchao on 2016/6/16.
 */
public abstract class AbsAnimation {
    public AbsPieChartView animationView;
    public AnimationContactInterface mContactInterface;
    //展示动画的所有数据
    public List<ModuleDataInfo> moduleDataList;

    public void setmContactInterface(AnimationContactInterface mContactInterface) {
        this.mContactInterface = mContactInterface;
    }

    public AbsAnimation(AbsPieChartView animationView, List<ModuleDataInfo> moduleDataList) {
        this.animationView = animationView;
        this.moduleDataList = moduleDataList;
    }

    public abstract void startAnimation();

}
