package com.example.administrator.myeasypieview.piechartview.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by xiongchao on 2016/6/16.
 */
public abstract class AbsPieChartView extends View implements PieChartViewInterface {
    //默认的三种颜色
    protected int defaultPieGreen = Color.parseColor("#9fd461");
    protected int defaultPieYellow = Color.parseColor("#ffd761");
    protected int defaultPieRed = Color.parseColor("#ff7863");

    public AbsPieChartView(Context context) {
        super(context);
    }

    public AbsPieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AbsPieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
