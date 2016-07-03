package com.example.administrator.myeasypieview.piechartview;

/**
 * Created by xiongchao on 2016/2/26.
 * 饼图板块的  内容  所占百分比  所使用的颜色
 */
public class ModuleDataInfo {

    private String pieModuleContentPartOne;//饼图模块显示的内容部分one
    private String pieModuleContentPartTwo;//饼图模块显示的内容部分two
    private float PieModulePercent;//饼图模块所占的百分比
    private int pieModuleColor; //饼图模块的颜色
    private boolean Selected = false; //默认不选中(不偏移)
    //被选中后板块偏移的百分比
    private float SelectedOffset = 10.0f;

    public float getSelectedOffset() {
        return SelectedOffset;
    }

    public void setSelectedOffset(float selectedOffset) {
        SelectedOffset = selectedOffset;
    }

    public boolean getSelected() {
        return Selected;
    }

    public void setSelected(boolean mSelected) {
        this.Selected = mSelected;
    }

    public ModuleDataInfo(String pieModuleContentPartOne, String pieModuleContentPartTwo, float pieModulePercent, int pirModuleColor) {
        this.pieModuleContentPartOne = pieModuleContentPartOne;
        this.pieModuleContentPartTwo = pieModuleContentPartTwo;
        PieModulePercent = pieModulePercent;
        this.pieModuleColor = pirModuleColor;
    }

    public String getPieModuleContentPartOne() {
        return pieModuleContentPartOne;
    }

    public void setPieModuleContentPartOne(String pieModuleContentPartOne) {
        this.pieModuleContentPartOne = pieModuleContentPartOne;
    }

    public String getPieModuleContentPartTwo() {
        return pieModuleContentPartTwo;
    }

    public void setPieModuleContentPartTwo(String pieModuleContentPartTwo) {
        this.pieModuleContentPartTwo = pieModuleContentPartTwo;
    }

    public float getPieModulePercent() {
        return PieModulePercent;
    }

    public void setPieModulePercent(float pieModulePercent) {
        PieModulePercent = pieModulePercent;
    }

    public int getPieModuleColor() {
        return pieModuleColor;
    }

    public void setPieModuleColor(int pieModuleColor) {
        this.pieModuleColor = pieModuleColor;
    }
}
