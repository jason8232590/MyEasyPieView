/**
 * Copyright 2014  XCL-Charts
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * @Project XCL-Charts
 * @Description Android图表基类库
 * @author XiongChuanLiang<br/>(xcl_168@aliyun.com)
 * @license http://www.apache.org/licenses/  Apache v2 License
 * @version 1.0
 */
package com.example.administrator.myeasypieview.piechartview;

import android.graphics.PointF;

/**
 *模块的一些位置信息
 * */
public class ModulePosition extends PositionRecord {

    protected float mOffsetAngle = 0.0f;
    protected float mCurrentAngle = 0.0f;
    protected float mRadius = 0.0f;
    protected float mSelectedOffset = 0.0f;

    //初始偏移角度
    protected float mInitAngle = 0.0f;//180;

    //子类中ModulePosRecord 给了值
    protected PointF mCirXY = null;


    public ModulePosition() {
    }


    public float getAngle() {
        System.out.println("xcan getAngle moffsetAngle-*  "+mOffsetAngle+"  *-mCurrentAngle-*  "+mCurrentAngle);
        return MathHelper.getInstance().add(mOffsetAngle, mCurrentAngle);
    }


    /**
     * 饼图(pie chart)起始偏移角度
     * @param Angle 偏移角度
     */
    public void saveInitialAngle(float Angle) {
        mInitAngle = Angle;
    }


    public float getRadius() {
        return mRadius;
    }

    public PointF getPointF() {
        return mCirXY;
    }

    public float getStartAngle() {
        return MathHelper.getInstance().add(mOffsetAngle, this.mInitAngle);
    }

    public float getSweepAngle() {
        return mCurrentAngle;
    }

    public float getSelectedOffset() {
        return mSelectedOffset;
    }

    @Override
    protected boolean compareRange(float x, float y, float initOffsetAngle) {
        if (null == mCirXY) return false;
        return compareRadius(x, y, initOffsetAngle);
    }

    /**
     * 判断触摸点是不是在圆内
     * @param touchX 触摸点X坐标
     * @param touchY 触摸点Y坐标
     * */
    private boolean compareRadius(float touchX, float touchY, float initOffsetAngle) {
        //前面的savearcPosition中保存了mCirXY  mCirXY
        double distance = MathHelper.getInstance().getDistance(mCirXY.x, mCirXY.y, touchX, touchY);
        //是不是在半径内，也就是圆内
        if (Double.compare(distance, mRadius) == 0 || Double.compare(distance, mRadius) == -1) {

            float touchPointAngle = (float) MathHelper.getInstance().getDegree(mCirXY.x, mCirXY.y, touchX, touchY) + 360.0f;
            //获取当前的角度
            //这个角度是 偏移角度+自身角度
            float currAngle = getAngle();
            System.out.println("xcan ComPareRadius touchPointAngle-* " + touchPointAngle + " *-currAngle-* " + currAngle + "*-initOffsetAngle-*" + initOffsetAngle);
            if (touchPointAngle > (360.0f + initOffsetAngle)) {
                touchPointAngle -= 360.0f;
            }
            //木有问题  mInitAngle这个角度貌似用不上
            if (Float.compare(currAngle, touchPointAngle) == 1
                    || Float.compare(currAngle, touchPointAngle) == 0) {
                return true;
            }
        }
        return false;
    }


}
