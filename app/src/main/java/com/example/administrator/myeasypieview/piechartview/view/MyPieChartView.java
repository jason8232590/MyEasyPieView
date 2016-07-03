package com.example.administrator.myeasypieview.piechartview.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.example.administrator.myeasypieview.piechartview.MathHelper;
import com.example.administrator.myeasypieview.piechartview.ModuleDataInfo;
import com.example.administrator.myeasypieview.piechartview.ModulePosInfo;
import com.example.administrator.myeasypieview.piechartview.ModulePosRecord;
import com.example.administrator.myeasypieview.piechartview.ModulePosition;
import com.example.administrator.myeasypieview.piechartview.anim.AbsAnimation;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by xiongchao on 2016/2/26.
 */
public class MyPieChartView extends AbsPieChartView implements Runnable, PieChartViewInterface {
    //存储饼图中所有板块的 内容  百分比  颜色 //按  从大到小  排序
    private ArrayList<ModuleDataInfo> dataModuleList = new ArrayList<>();


    //背景色
    private int backGroundColor;
    //默认背景色
    private int defaultBackGroudColor = Color.parseColor("#ECF1F2");


    //各个模块的颜色
    private int pieGreen;
    private int pieYellow;
    private int getPieRed;



    //画模块的画笔
    private Paint modulePaint;
    //画模块内容的画笔
    private Paint ArcContentPaintPartOne;
    //画模块内容的画笔
    private Paint ArcContentPaintPartTwo;
    //画模块内容的画笔
    private Paint pieChartBackgroudPaint;


    //总角度
    private float mTotalAngle = 360.f;
    //初始偏移的角度
    //一般情况是不变的
    //只有在动画模式是SHOW_FROM_MIDDLE 才会随着totalAngle变大而变大
    private float mInitOffsetAngle = 0.0f;


    private RectF mRectF = null;
    /**
     * 模块是否可以点击
     */
    private boolean moduleClickEnable = true;
    //模块位置信息的儲存
    private ArrayList allModulePositionInfoList = null;
    private Context context;

    //保存扇形内容的一些信息
    protected ArrayList<ModulePosInfo> mModulePosList = new ArrayList<>();
    private int mSelectID;
    private int mSelectDataID;
    private int mSelectDataChildID;

    /**
     * 被选中的模块号
     */
    private int mSelectedID = -1;
    private PointF point;
    private int centerX;
    private int centerY;
    //最大模块圆的半径
    private float outSideRadius;
    //默认是所有模块都没有被选中
    private boolean defaultChooseFlag = false;

    private final static int SHOW_FROM_STALE_ANGLE = 1;
    private final static int SHOW_FROM_MIDDLE = 2;
    //饼图的半径
    private int pieRadius;
    private AbsAnimation animation;

    //设置动画
    public void setAnimation(AbsAnimation animation) {
        this.animation = animation;
    }

    public MyPieChartView(Context context, int radius, int backGroudColor) {
        super(context);
        this.pieRadius = radius;
        this.backGroundColor = backGroudColor;
        initView(context);
    }

    public MyPieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyPieChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        this.context = context;
    }

    public ArrayList<ModuleDataInfo> getDataModuleList() {
        return dataModuleList;
    }

    public void setDataModuleList(ArrayList<ModuleDataInfo> dataModuleList) {
        this.dataModuleList = dataModuleList;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //圆的中心
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        //半径
        outSideRadius = Dp2Px(pieRadius) / 2;

        float currentAngle;

        //然后设置这个图形的ltrd四个点的坐标
        float left = MathHelper.getInstance().sub(centerX, outSideRadius);
        float top = MathHelper.getInstance().sub(centerY, outSideRadius);
        float right = MathHelper.getInstance().add(centerX, outSideRadius);
        float bottom = MathHelper.getInstance().add(centerY, outSideRadius);
        //清楚之前的记录
        if (null != mModulePosList && 0 != mModulePosList.size()) {
            mModulePosList.clear();
        }
        //清楚之前各个模块保存的记录
        if (null != allModulePositionInfoList && 0 != allModulePositionInfoList.size()) {
            allModulePositionInfoList.clear();
        }

        //背景色  画背景
        getPieChartBackgourdPaint().setColor(backGroundColor == 0 ? defaultBackGroudColor : backGroundColor);
        canvas.drawArc(new RectF(left, top, right, bottom), 0, 360, true, getPieChartBackgourdPaint());

        if (0 == dataModuleList.size()) {
            //没有数据就不用画了
            return;
        }

        mInitOffsetAngle = setPieViewShowMethod(SHOW_FROM_STALE_ANGLE);
        float offsetAngle = mInitOffsetAngle;

        //分别画各个板块
        int count = dataModuleList.size(); //饼图板块数量
        for (int i = 0; i < count; i++) {
            //取出板块板块的信息
            ModuleDataInfo moduleData = dataModuleList.get(i);
            //获取当前板块百分比占的角度//动画的效果就是在这里改变
            // 让currentAngle越来越大直到正常这就形成了展开的效果 ！！！
            currentAngle = MathHelper.getInstance().getSliceAngle(getTotalAngle(), (float) moduleData.getPieModulePercent());
            System.out.println("xcqww position-" + i + "-currentAngle-" + currentAngle + "-mOffsetAngle-" + mInitOffsetAngle);

            //避免角度小于0或者等于0  就不走下面的代码  直接进入下个count
            if (!validateAngle(currentAngle)) continue;

            //获取当前板块的颜色
            geArcPaint().setColor(moduleData.getPieModuleColor());

            //指定突出哪个块
            if (moduleData.getSelected()) {
                //选中模块圆心偏移的距离(默认偏移半径的1/10)
                float selModCenterOffset = MathHelper.getInstance().div(outSideRadius, moduleData.getSelectedOffset());

                //计算百分比  //这个是计算偏移后的圆心
                point = MathHelper
                        .getInstance()
                        .calcArcEndPointXY(
                                centerX,
                                centerY,
                                selModCenterOffset,
                                MathHelper.getInstance().add(offsetAngle, currentAngle / 2f));

                //计算移动后的圆块所在的矩形
                //构造的圆弧是内嵌矩形的
                initRectF(i, MathHelper.getInstance().sub(point.x, outSideRadius), MathHelper.getInstance().sub(point.y, outSideRadius),
                        MathHelper.getInstance().add(point.x, outSideRadius), MathHelper.getInstance().add(point.y, outSideRadius));

                mModulePosList.add(new ModulePosInfo(i, point.x, point.y, outSideRadius, offsetAngle, currentAngle));
            } else {
                initRectF(i, left, top, right, bottom);
                //记录当前模块的  圆心 半径  偏移角度   自身角度
                mModulePosList.add(new ModulePosInfo(i, centerX, centerY, outSideRadius, offsetAngle, currentAngle));
            }

            //在饼图中显示所占比例
            //画各个模块
            canvas.drawArc(mRectF, offsetAngle, currentAngle, true, geArcPaint());
            //保存模块的具体位置，方便后面点击事件判断是点在那个模块
            //这里的
            // offsetAngle  代表的是每个模块启示的偏移角度
            saveArcRecord(i, centerX, centerY,
                    outSideRadius, offsetAngle, currentAngle,
                    moduleData.getSelectedOffset(), getInitOffsetAngle());
            //下次的起始角度
            offsetAngle = MathHelper.getInstance().add(offsetAngle, currentAngle);
        }
        defaultChooseFlag = checkAllModuleSelectedStatus(dataModuleList);
        //画圆弧中的内容
        DrawArcContent(canvas, dataModuleList, defaultChooseFlag);
    }

    /**
     * 设置展示方式
     * 1.从第一个饼图的区块的偏移处开始展开
     * 2.从中线开始开战
     */
    private float setPieViewShowMethod(int method) {
        float offsetAngle;
        if (SHOW_FROM_MIDDLE == method) {
            //这种动画的原理  改变totalAngle  //不固定offsetAngle
            //current 会随着totalAngle改变而改变
            //offsetAngle  在这里也会随着totalAngle改变而改变
            //然后随着totalAngle逐渐变大,current和offset也会逐渐变成正常值
            //因为这里是从270 开始算  所以效果就是从270度开始像两边逐渐展开
            offsetAngle = 270.0f - MathHelper.getInstance().getSliceAngle(getTotalAngle(), dataModuleList.get(0).getPieModulePercent()) / 2;
        } else if (SHOW_FROM_STALE_ANGLE == method) {
            //这种动画的原理  改变totalAngle   //固定ofsetAngle
            //current 会随着totalAngle改变而改变
            //offsetAngle  在这里也会随着totalAngle改变而改变
            //然后随着totalAngle逐渐变大,current和offset也会逐渐变成正常值
            //因为offsetAngle是固定的  所以效果就是从固定的那个offsetAngle逐渐展开
            offsetAngle = 270.0f - MathHelper.getInstance().getSliceAngle(360.0f, (float) dataModuleList.get(0).getPieModulePercent()) / 2;
        } else {
            //默认是以中间这根线为对称轴
            //最大圆的角度从0到360，这样就可以导致圆的展开方式是从中间这根线展开
            offsetAngle = 270.0f - MathHelper.getInstance().getSliceAngle(getTotalAngle(), dataModuleList.get(0).getPieModulePercent()) / 2;
        }
        return offsetAngle;
    }

    /**
     * 画圆弧中的内容
     */
    private void DrawArcContent(Canvas canvas, ArrayList<ModuleDataInfo> list, boolean defaultChooseFlag) {
        if (defaultChooseFlag) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getSelected()) {
                    drawContent(i, list, canvas);
                }
            }
        } else {
            //默认都是没有选中情况
            drawContent(0, list, canvas);
        }

    }

    /**
     * 判断三个模块中是否有被选中的
     */
    private boolean checkAllModuleSelectedStatus(ArrayList<ModuleDataInfo> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getSelected()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 画圆弧中的内容
     *
     * @param moduleId 被选中的模块  从 0 开始
     */
    private void drawContent(int moduleId, ArrayList<ModuleDataInfo> list, Canvas canvas) {

        //获取内容中的第一 二 部分
        String contentPartOne = list.get(moduleId).getPieModuleContentPartOne();
        String contentPartTwo = list.get(moduleId).getPieModuleContentPartTwo();
        //获取画笔的size
        float contentPartOneSize = getArcContentPaintPartOne().measureText(contentPartOne);
        float contentPartTwoSize = getArcContentPaintPartTwo().measureText(contentPartTwo);

        ModulePosInfo ModulePosInfo = mModulePosList.get(moduleId);

        float offsetAngle = ModulePosInfo.getOffsetAngle();
        float currentAngle = ModulePosInfo.getCurrentAngle();

        float radius = ModulePosInfo.getRadius();
        //获取当前圆弧的中点位置（也就是角度一半  半径一半的位置）,内容画在圆弧中间
        //注意起点是偏移后的圆心！！！
        PointF point = MathHelper.getInstance().calcArcEndPointXY(ModulePosInfo.getCenterX(), ModulePosInfo.getCenterY(),
                MathHelper.getInstance().div(radius, 2),
                MathHelper.getInstance().add(offsetAngle, currentAngle / 2f));
        canvas.translate(point.x, point.y);
        //画个坐标系方便辨识位置
//                canvas.drawLine(0, 0, 0, 100, getArcContentPaintPartTwo());
//                canvas.drawLine(0, 0, 100, 0, getArcContentPaintPartTwo());
        canvas.drawText(contentPartOne, -contentPartOneSize / 2, Dp2Px(2), getArcContentPaintPartOne());
        canvas.drawText(contentPartTwo, -contentPartTwoSize / 2, Dp2Px(20), getArcContentPaintPartTwo());
    }

    public float getTotalAngle() {
        return mTotalAngle;
    }

    /**
     * 检查角度的合法性
     *
     * @param Angle 角度
     * @return 是否正常
     */
    protected boolean validateAngle(float Angle) {
        if (Float.compare(Angle, 0.0f) == 0
                || Float.compare(Angle, 0.0f) == -1) {
            //Log.i(TAG, "扇区圆心角小于等于0度. 当前圆心角为:"+Float.toString(Angle));
//            System.out.println("xcqw validateAngle");
            return false;
        }
        return true;
    }

    /**
     * 开放饼图扇区的画笔
     *
     * @return 画笔
     */
    public Paint geArcPaint() {
        if (null == modulePaint) //画笔初始化
        {
            modulePaint = new Paint();
            modulePaint.setAntiAlias(true);
        }
        return modulePaint;
    }

    /**
     * 开放饼图扇区的画笔
     *
     * @return 画笔
     */
    public Paint getPieChartBackgourdPaint() {
        if (null == pieChartBackgroudPaint) //画笔初始化
        {
            pieChartBackgroudPaint = new Paint();
            pieChartBackgroudPaint.setAntiAlias(true);
        }
        return pieChartBackgroudPaint;
    }

    /**
     * 开放饼图扇区的画笔  第二部分的画笔
     *
     * @return 画笔
     */
    public Paint getArcContentPaintPartTwo() {
        if (null == ArcContentPaintPartOne) //画笔初始化
        {
            ArcContentPaintPartOne = new Paint();
            ArcContentPaintPartOne.setColor(Color.parseColor("#ffffff"));
            ArcContentPaintPartOne.setAntiAlias(true);
            ArcContentPaintPartOne.setTextSize(Dp2Px(12));
        }
        return ArcContentPaintPartOne;
    }

    /**
     * 开放饼图扇区的画笔
     *
     * @return 画笔
     */
    public Paint getArcContentPaintPartOne() {
        if (null == ArcContentPaintPartTwo) //画笔初始化
        {
            ArcContentPaintPartTwo = new Paint();
            ArcContentPaintPartTwo.setColor(Color.parseColor("#ffffff"));
            ArcContentPaintPartTwo.setAntiAlias(true);
            ArcContentPaintPartTwo.setTextSize(Dp2Px(24));
        }
        return ArcContentPaintPartTwo;
    }

    /**
     * 控件各个模块的长短大小
     */
    protected void initRectF(int moduleId, float left, float top, float right, float bottom) {
        if (null == mRectF) {
            if (0 == moduleId) {
                mRectF = new RectF(left, top, right, bottom);
            } else if (1 == moduleId) {
                mRectF = new RectF(left + Dp2Px(9), top + Dp2Px(9), right - Dp2Px(9), bottom - Dp2Px(9));
            } else if (2 == moduleId) {
                mRectF = new RectF(left + Dp2Px(15), top + Dp2Px(15), right - Dp2Px(15), bottom - Dp2Px(15));
            } else {
                mRectF = new RectF(left, top, right, bottom);
            }

        } else {
            if (0 == moduleId) {
                mRectF.set(left, top, right, bottom);
            } else if (1 == moduleId) {
                mRectF.set(left + Dp2Px(9), top + Dp2Px(9), right - Dp2Px(9), bottom - Dp2Px(9));
            } else if (2 == moduleId) {
                mRectF.set(left + Dp2Px(15), top + Dp2Px(15), right - Dp2Px(15), bottom - Dp2Px(15));
            } else {
                mRectF.set(left, top, right, bottom);
            }
        }
    }

    /**
     * 返回事件处理状态
     *
     * @return 是否激活
     */
    public boolean getListenItemClickStatus() {
        return moduleClickEnable;
    }

    /**
     * 设置可点击
     *
     * @return 是否激活
     */
    public void setModuleClickEnable(boolean flag) {
        this.moduleClickEnable = flag;
    }

    /**
     * 保存各个板块的信息  第几个  圆心坐标  半径  偏移角度  自身角度
     */
    protected void saveArcRecord(int dataID, float centerX, float centerY,
                                 float radius, float offsetAngle, float Angle, float selectedOffset,
                                 float initialAngle) {
        if (!getListenItemClickStatus())
            return;
        if (null == allModulePositionInfoList)
            allModulePositionInfoList = new ArrayList<ModulePosRecord>();
//        System.out.println("S xcan  dataId-* " + dataID + " *-centerX-* " + centerX + " *-centerY-* " + centerY);
//        System.out.println("xcan  radius-* " + radius + " *-offsetAngle-* " + offsetAngle + " *-Angle-* " + Angle);

        System.out.println("xcqw  offsetAngle-* " + offsetAngle + " *-initialAngle-* " + initialAngle);
        ModulePosRecord pRecord = new ModulePosRecord();
        pRecord.savePlotDataID(dataID);  //也就是第几个板块
        pRecord.savePlotCirXY(centerX, centerY);  //保存圆心的坐标
        pRecord.saveAngle(radius, offsetAngle, Angle, selectedOffset);//保存半径  偏移角度  角度  还有点击之后偏移的大小
        pRecord.saveInitialAngle(initialAngle);  //下一个板块的的初始角度
        allModulePositionInfoList.add(pRecord);
    }

    /**
     * 返回图的当前偏移角度
     *
     * @return 偏移角度
     */
    public float getInitOffsetAngle() {
        return mInitOffsetAngle;
    }

    public ModulePosition getPositionRecord(float x, float y) {
        return getArcRecord(x, y);
    }

    /**
     * 返回对应的记录
     *
     * @param x 当前点击点X坐标
     * @param y 当前点击点Y坐标
     * @return 记录类
     */
    protected ModulePosition getArcRecord(float x, float y) {
        if (!getListenItemClickStatus()) {
            return null;
        }
        if (null == allModulePositionInfoList) {
            return null;
        }
        Iterator it = allModulePositionInfoList.iterator();

        while (it.hasNext()) {
            ModulePosRecord record = (ModulePosRecord) it.next();
            //这个会去遍历  从板块0  开始遍历 //直到找到自己属于哪个板块
            if (record.compareF(x, y, mInitOffsetAngle)) {
                saveSelected(record.getRecordID(), record.getDataID(),
                        record.getDataChildID());
                return record;
            }
        }
        clearSelected();
        return null;
    }

    private void saveSelected(int recordID, int dataID, int dataChildID) {
        mSelectID = recordID;
        mSelectDataID = dataID;
        mSelectDataChildID = dataChildID;
    }

    private void clearSelected() {
        mSelectID = -1;
        mSelectDataID = -1;
        mSelectDataChildID = -1;
    }

    //处理点击事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // TODO Auto-generated method stub
        super.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_UP) {
            //这个应该是判断是否在圆内
            triggerClick(event.getX(), event.getY());
        }
        return true;
    }

    /**
     * 点击事件处理
     */
    private void triggerClick(float x, float y) {

        if (!getListenItemClickStatus()) return;
        //根据这个点获取当前所在的模块
        //在saveArcRecord保存了当前所有板块的信息
        ModulePosition record = getPositionRecord(x, y);

        if (null == record) return;

        //用于处理点击时弹开，再点时弹回的效果
        //record.getDataID()这个就是获取点到的是哪个模块
//        System.out.println("xcqw 点到"+record.getDataID());
        ModuleDataInfo moduleDataInfo = dataModuleList.get(record.getDataID());
        if (record.getDataID() == mSelectedID) {
            boolean bStatus = dataModuleList.get(mSelectedID).getSelected();
            //再次点击设置缩回去
            dataModuleList.get(mSelectedID).setSelected(!bStatus);
        } else {
            if (mSelectedID >= 0) {
                //之前被点击的  就被  设成未点击的状态
                dataModuleList.get(mSelectedID).setSelected(false);
            }
            //之前没被点击  这时候被点了  就设置成点击状态
            moduleDataInfo.setSelected(true);
        }
        //记录当前点击的模块
        mSelectedID = record.getDataID();
        this.refreshChart();

    }

    /**
     * 刷新图表
     */
    public void refreshChart() {
        this.invalidate();
    }

    /**
     * 设置总圆心角度,默认360
     *
     * @param total 总角度
     */
    @Override
    public void setTotalAngle(float total) {
        mTotalAngle = total;
    }

    /**
     * dp转px
     *
     * @param dp
     * @return
     */
    public int Dp2Px(float dp) {
        return (int) (dp * context.getResources().getDisplayMetrics().density + 0.5f);
    }



    @Override
    public void run() {
        animation.startAnimation();
    }

    @Override
    public void setData(ArrayList<ModuleDataInfo> list) {
        this.dataModuleList = list;
    }
}
