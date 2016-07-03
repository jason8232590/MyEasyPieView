package com.example.administrator.myeasypieview.piechartview.view;


import com.example.administrator.myeasypieview.piechartview.ModuleDataInfo;

import java.util.ArrayList;

/**
 * Created by xiongchao on 2016/6/16.
 */
public interface PieChartViewInterface {

    void setData(ArrayList<ModuleDataInfo> list);
    /**
     * 设置总圆心角度,默认360
     *
     * @param total 总角度
     */
    void setTotalAngle(float total);
}
