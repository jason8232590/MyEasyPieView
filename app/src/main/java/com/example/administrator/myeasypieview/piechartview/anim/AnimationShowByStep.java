package com.example.administrator.myeasypieview.piechartview.anim;

import android.graphics.Color;

import com.example.administrator.myeasypieview.piechartview.MathHelper;
import com.example.administrator.myeasypieview.piechartview.ModuleDataInfo;
import com.example.administrator.myeasypieview.piechartview.view.AbsPieChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiongchao on 2016/6/16.
 */
public class AnimationShowByStep extends AbsAnimation {


    public AnimationShowByStep(AbsPieChartView animationView, List<ModuleDataInfo> moduleDataList) {
        super(animationView, moduleDataList);
    }

    @Override
    public void startAnimation() {
        try {
            System.out.println("xcqw startAnimation");
            if(null != moduleDataList) {
                float sum = 0.0f;
                ArrayList<ModuleDataInfo> list = (ArrayList<ModuleDataInfo>) moduleDataList;
                int count = list.size();
                for (int i = 0; i < count; i++) {
                    Thread.sleep(200);

                    ArrayList<ModuleDataInfo> animationData = new ArrayList<ModuleDataInfo>();

                    sum = 0.0f;

                    for (int j = 0; j <= i; j++) {
                        animationData.add(list.get(j));
                        sum = (float) MathHelper.getInstance().add(
                                sum, list.get(j).getPieModulePercent());
                    }
                    //这个就是画剩下的模块,用淡色替代还没出现的模块
                    animationData.add(new ModuleDataInfo("", "", MathHelper.getInstance().sub(100.0f, sum),
                            Color.argb(1, 0, 0, 0)));
                   animationView.setData(animationData);

                    animationView.postInvalidate();
                }
            }else{
                //请先设置数据
                System.out.println("xcqw 请先设置数据");
                return ;
            }

        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }
    }
}
