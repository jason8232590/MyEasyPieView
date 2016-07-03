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

/**
 * 因为有很多其他图形（这里我只抽取了一个饼图），所以才会抽出这个抽象类
 * */
public abstract class PositionRecord {

    //private static final String TAG = "PositionRecord";
    protected int mDataID = -1;
    protected int mDataChildID = -1;

    public PositionRecord() {

    }

    /**
     * 确认是否在范围内
     * */
    protected abstract boolean compareRange(float x, float y, float initOffsetAngle);


    /**
     * 模块号  从 0 开始
     * */
    public int getDataID() {
        return mDataID;
    }

    //当前记录所属数据集的行号
    public int getDataChildID() {
        return mDataChildID;
    }

    public int getRecordID() {
        if (-1 == mDataID && -1 == mDataChildID) return -1;

        int id = 0;
        if (mDataID > 0) id += mDataChildID;
        if (mDataChildID > 0) id += mDataChildID;

        return id;
    }


    //当前记录在数据源中行号
    protected void saveDataID(int num) {
        mDataID = num;
    }

    //当前记录所属数据集的行号
    protected void saveDataChildID(int num) {
        mDataChildID = num;
    }


}
