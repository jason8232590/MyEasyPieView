package com.example.administrator.myeasypieview;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;

import com.example.administrator.myeasypieview.piechartview.ModuleDataInfo;
import com.example.administrator.myeasypieview.piechartview.anim.AnimationShowByStep;
import com.example.administrator.myeasypieview.piechartview.view.MyPieChartView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends Activity {

    private ArrayList<ModuleDataInfo> moduleDataInfoSortList = new ArrayList<>();
    private MyPieChartView myPieChartView;
    private int pieGreen = Color.parseColor("#9fd461");
    private int pieYellow = Color.parseColor("#ffd761");
    private int pieRed = Color.parseColor("#ff7863");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myPieChartView = new MyPieChartView(this, 155, 0);
//        initPieModuleDataList();
        FrameLayout flPiechartContainer = (FrameLayout) findViewById(R.id.fl_piechart_container);

        flPiechartContainer.addView(myPieChartView);
        Animation overshotAnim = AnimationUtils.loadAnimation(this, R.anim.scaleanim_overshot);
        //一开始的时候没有初始化数据，然后就是背景的动画
        //设置自己的动画//想要啥就给啥
        myPieChartView.setAnimation(new AnimationShowByStep(myPieChartView, initPieChartData()));
        myPieChartView.startAnimation(overshotAnim);
        overshotAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //背景的动画完了之后就是图的动画
                new Thread(myPieChartView).start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 初始化各个模块的信息
     */
    private void initPieModuleDataList() {
        int normalPercent = 0;
        int overMonthPercent = 0;
        int over90dPercent = 0;
        int totalNum = 20;
        int normalNum = 10;
        int overMonthNum = 6;
        int over90dNum = 4;
        if (0 != over90dNum) {
            over90dPercent = over90dNum * 100 / totalNum;
        }
        if (0 != overMonthNum) {
            overMonthPercent = overMonthNum * 100 / totalNum - over90dPercent;
        }

        if (0 != normalNum) {
            normalPercent = 100 - over90dPercent - overMonthPercent;
        }
        if (100 == normalPercent || 100 == overMonthPercent || 100 == over90dPercent) {
            //如果有一个百分百的就不能点击
            myPieChartView.setModuleClickEnable(false);
        } else {
            myPieChartView.setModuleClickEnable(true);
        }
//            System.out.println("xcqw normalPercent "+normalPercent+"overMonthPercent"+overMonthPercent+"over90dPercent"+over90dPercent);

        //各个模块的颜色
        moduleDataInfoSortList.add(new ModuleDataInfo("" + overMonthNum, "逾期账户", overMonthPercent, pieYellow));
        moduleDataInfoSortList.add(new ModuleDataInfo("" + normalNum, "正常账户", normalPercent, pieGreen));
        moduleDataInfoSortList.add(new ModuleDataInfo("" + over90dNum, "逾期超九十天", over90dPercent, pieRed));
        Collections.sort(moduleDataInfoSortList, new percentCompartor());
        myPieChartView.setDataModuleList(moduleDataInfoSortList);
    }

    /**
     * 默认排序是  大到小
     */
    class percentCompartor implements Comparator<ModuleDataInfo> {
        public int compare(ModuleDataInfo s1, ModuleDataInfo s2) {

            if (s1.getPieModulePercent() > s2.getPieModulePercent()) {
                return -1;
            } else if (s1.getPieModulePercent() < s2.getPieModulePercent()) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    /**
     * 把所有饼图的数据（内容（part1 part2 ） 百分比  颜色）
     * //排序后面再写  这里默认第一个是最大的
     */
    private ArrayList<ModuleDataInfo> initPieChartData() {
        moduleDataInfoSortList.add(new ModuleDataInfo("15", "正常账户", 60f, pieGreen));
        moduleDataInfoSortList.add(new ModuleDataInfo("5", "逾期账户", 25f, pieYellow));
        moduleDataInfoSortList.add(new ModuleDataInfo("6", "逾期超九十天", 15f, pieRed));
        return moduleDataInfoSortList;
    }
}
