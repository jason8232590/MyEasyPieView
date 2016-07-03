package com.example.administrator.myeasypieview.piechartview;

/**
 * Created by Administrator on 2016/2/28 0028.
 */
public class ModulePosInfo {
    //模块的ID
    public int ID = -1;
    //当前的圆心的X坐标
    public float centerX = 0.0f;
    //当前的圆心的Y坐标
    public float centerY = 0.0f;
    //当前圆的半径
    public float Radius = 0.0f;
    //偏移的角度 //270度为中线,第一个弧度以这个中线对半开
    //模块A（第一块）  占用70度，中线左边35度，右边35度,这时候他的偏移角度就是235度
    //模块B (第二块)  占用80  偏移角度就是 235+70
    public float OffsetAngle = 0.0f;
    public float CurrentAngle = 0.0f; //当前弧形所占的角度

    public ModulePosInfo(int ID, float x, float y, float radius, float offsetAngle, float currentAngle) {
        this.ID = ID;
        centerX = x;
        centerY = y;
        Radius = radius;
        OffsetAngle = offsetAngle;
        CurrentAngle = currentAngle;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getRadius() {
        return Radius;
    }

    public void setRadius(float radius) {
        Radius = radius;
    }

    public float getOffsetAngle() {
        return OffsetAngle;
    }

    public void setOffsetAngle(float offsetAngle) {
        OffsetAngle = offsetAngle;
    }

    public float getCurrentAngle() {
        return CurrentAngle;
    }

    public void setCurrentAngle(float currentAngle) {
        CurrentAngle = currentAngle;
    }
}
