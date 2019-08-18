package com.zinc.jrecycleview.loadview.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.zinc.jrecycleview.utils.LogUtils;

/**
 * author       : Jiang zinc
 * time         : 2018-04-16 14:28
 * email        : 56002982@qq.com
 * desc         : 上拉和下拉基类
 * version      : 1.0.0
 */

public abstract class IBaseWrapperView extends LinearLayout {

    protected String TAG = this.getClass().getSimpleName();

    protected final static int INDEX = 1;

    private static final int SCROLL_DURATION = 300;

    // 加载出错
    public final static int STATE_ERROR = INDEX;
    // 下拉刷新或上拉更多状态：
    // 1、还没操作；
    // 2、下拉的高度未超过显示的高度；
    public final static int STATE_PULL_TO_ACTION = INDEX << 1;
    // 释放执行
    public final static int STATE_RELEASE_TO_ACTION = INDEX << 2;
    // 执行中
    public final static int STATE_EXECUTING = INDEX << 3;
    // 执行完毕
    public final static int STATE_DONE = INDEX << 4;
    // 没有更多
    public final static int STATE_NO_MORE = INDEX << 5;

    // 当前状态
    protected int mCurState;

    // 本视图高度
    protected int mHeight;

    public IBaseWrapperView(Context context) {
        this(context, null, 0);
    }

    public IBaseWrapperView(Context context,
                            @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBaseWrapperView(Context context,
                            @Nullable AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init(context);
    }

    private void init(Context context) {
        this.mCurState = STATE_PULL_TO_ACTION;

        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 0);
        setLayoutParams(layoutParams);
        setPadding(0, 0, 0, 0);

        View view = initView(context);
        wrapper(context, view);
    }

    protected abstract void wrapper(Context context, View view);

    public int getViewHeight() {
        return this.mHeight;
    }

    public void setHeight(int height) {
        this.mHeight = height;
    }

    /**
     * 获取当前状态
     */
    public int getCurState() {
        return mCurState;
    }

    /**
     * 获取刷新的根视图
     */
    protected abstract View getLoadView();

    /**
     * 获取加载视图的高度
     *
     * @return 可见高度
     */
    public int getVisibleHeight() {
        LinearLayout.LayoutParams layoutParams
                = (LinearLayout.LayoutParams) this.getLoadView().getLayoutParams();
        return layoutParams.height;
    }

    /**
     * 设置加载View的高度
     *
     * @param height 设置可见高度
     */
    protected void setVisibleHeight(int height) {
        if (height <= 0) {
            height = 0;
        }
        LinearLayout.LayoutParams layoutParams
                = (LinearLayout.LayoutParams) this.getLoadView().getLayoutParams();
        layoutParams.height = height;
        this.getLoadView().setLayoutParams(layoutParams);
    }

    /**
     * 设置当前状态
     *
     * @param state 当前状态
     */
    public void setState(int state) {
        //如果与当前状态相同，不做任何处理
        if (state == this.mCurState) {
            return;
        }

        LogUtils.i(TAG, "state: " + state);

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
     * 重置状态
     *
     * @param destHeight 目标高度
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
     * 平滑滚动至某个高度
     *
     * @param destHeight 目标高度
     */
    protected void smoothScrollTo(int destHeight) {
        smoothScrollTo(destHeight, SCROLL_DURATION);
    }

    /**
     * 平滑滚动至某个高度
     *
     * @param destHeight 目标高度
     * @param durTime    时长
     */
    protected void smoothScrollTo(int destHeight, int durTime) {
        // 设置从可见高度->目标高度
        ValueAnimator valueAnimator = ValueAnimator.ofInt(getVisibleHeight(), destHeight);
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
     * 等待上拉 或 等待下拉的状态 视图表现
     */
    protected abstract void onPullToAction();

    /**
     * 释放执行（释放刷新 或 释放加载更多）视图表现
     */
    protected abstract void onReleaseToAction();

    /**
     * 执行中 视图表现
     */
    protected abstract void onExecuting();

    /**
     * 执行完视图表现
     */
    protected abstract void onDone();

    /**
     * 扩展方法，主要用于后面扩展一写细节的状态，
     * 现用于LoadMore增加{@link IBaseLoadMoreView#STATE_NO_MORE}状态
     *
     * @param state 当前状态
     */
    protected abstract void onOther(int state);

    /**
     * 初始化视图，用于加载自己的视图
     *
     * @param context 上下文
     * @return 视图
     */
    protected abstract View initView(Context context);

}
