package com.example.administrator.myeasypieview.piechartview.anim;


import com.example.administrator.myeasypieview.piechartview.ModuleDataInfo;
import com.example.administrator.myeasypieview.piechartview.view.AbsPieChartView;
import com.example.administrator.myeasypieview.piechartview.view.MyPieChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongchao on 2016/6/16.
 */
public class AnimationExpand extends AbsAnimation {


    public AnimationExpand(AbsPieChartView animationView, List<ModuleDataInfo> moduleDataList) {
        super(animationView, moduleDataList);
    }

    @Override
    public void startAnimation() {
        try {
            if(null != moduleDataList) {
                ((MyPieChartView) animationView).setData((ArrayList<ModuleDataInfo>) moduleDataList);
                //也就是每次转的角度
                int count = 360 / 10;

                for (int i = 1; i < count; i++) {
                    Thread.sleep(40);

                    animationView.setTotalAngle(10 * i);

                    //激活点击监听
                    if (count - 1 == i) {
                        //也就是转完了就激活各个模块的点击事件
                        animationView.setTotalAngle(360);
                    }
                    animationView.postInvalidate();
                }
            }else{
                //请先设置数据
                System.out.println("xcqw 请先设置数据");
                return;
            }

        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
