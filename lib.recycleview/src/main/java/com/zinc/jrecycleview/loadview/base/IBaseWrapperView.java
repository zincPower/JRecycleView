package com.zinc.jrecycleview.loadview.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/16
 * @description 上拉和下拉基类
 */

public abstract class IBaseWrapperView extends LinearLayout {

    //下拉刷新或上拉更多状态：1、还没操作；2、下拉的高度未超过显示的高度；
    public final static int STATE_PULL_TO_ACTION = 0;
    //释放执行
    public final static int STATE_RELEASE_TO_ACTION = 4;
    //执行中
    public final static int STATE_EXECUTING = 8;
    //执行完毕
    public final static int STATE_DONE = 16;

    //当前状态
    protected int mCurState;

    //本视图高度
    protected int mHeight;

    public IBaseWrapperView(Context context) {
        this(context, null, 0);
    }

    public IBaseWrapperView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBaseWrapperView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.mCurState = STATE_PULL_TO_ACTION;

        //初始化本视图
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(layoutParams);
        setPadding(0, 0, 0, 0);

        this.initView(context);

        measure(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.mHeight = getMeasuredHeight();
    }

    public int getMeasureHeight() {
        return this.mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getCurState() {
        return mCurState;
    }

    /**
     * @date 创建时间 2018/3/20
     * @author Jiang zinc
     * @Description 获取刷新的根视图
     * @version
     */
    protected abstract View getLoadView();

    /**
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 获取加载视图的高度
     * @version
     */
    public int getVisibleHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLoadView().getLayoutParams();
        return layoutParams.height;
    }

    /**
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 设置加载View的高度
     * @version
     */
    protected void setVisibleHeight(int height) {
        if (height <= 0) {
            height = 0;
        }
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.getLoadView().getLayoutParams();
        layoutParams.height = height;
        this.getLoadView().setLayoutParams(layoutParams);
    }

    /**
     * @date 创建时间 2018/3/17
     * @author Jiang zinc
     * @Description 设置当前状态
     * @version
     */
    public void setState(int state) {
        //如果与当前状态相同，不做任何处理
        if (state == this.mCurState) {
            return;
        }

        switch (state) {
            //下拉执行
            case STATE_PULL_TO_ACTION:
                onPullToAction();
                break;

            //释放执行
            case STATE_RELEASE_TO_ACTION:
                onReleaseToAction();
                break;

            //执行中
            case STATE_EXECUTING:
                onExecuting();
                break;

            //执行完毕
            case STATE_DONE:
                onDone();
                break;
            default:
                onOther(state);
        }

        //保存为当前状态
        this.mCurState = state;
    }

    /**
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 重置状态
     * @version
     */
    protected void reset(int destHeight) {
        smoothScrollTo(destHeight);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_PULL_TO_ACTION);
            }
        }, 200);
    }

    /**
     * @param destheight 目标高度
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 平滑滚动至某个高度
     * @version
     */
    protected void smoothScrollTo(int destheight) {
        smoothScrollTo(destheight, 300);
    }

    protected void smoothScrollTo(int destheight, int durTime) {

        //设置从可见高度->目标高度
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getVisibleHeight(), destheight);
        valueAnimator.setDuration(durTime);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setVisibleHeight((Integer) animation.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 等待上拉 或 等待下拉的状态 视图表现
     * @version
     */
    protected abstract void onPullToAction();

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 释放执行（释放刷新 或 释放加载更多）视图表现
     * @version
     */
    protected abstract void onReleaseToAction();

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 执行中 视图表现
     * @version
     */
    protected abstract void onExecuting();

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 执行完视图表现
     * @version
     */
    protected abstract void onDone();

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 扩展方法，主要用于后面扩展一写细节的状态，现用于LoadMore增加{@link IBaseLoadMoreView#STATE_NO_MORE}状态
     * @version
     */
    protected abstract void onOther(int state);

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 初始化视图，用于加载自己的视图
     * @version
     */
    protected abstract void initView(Context context);

}
