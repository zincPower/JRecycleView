package com.zinc.jrecycleview.loadview.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.loadview.bean.MoveInfo;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/3/17
 * @description
 */

public abstract class IBasePullRefreshLoadView extends IBaseWrapperView {

    private JRefreshAndLoadMoreAdapter.OnRefreshListener mOnRefreshListener;

    public IBasePullRefreshLoadView(Context context) {
        this(context, null, 0);
    }

    public IBasePullRefreshLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IBasePullRefreshLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnRefreshListener(JRefreshAndLoadMoreAdapter.OnRefreshListener onRefreshListener) {
        this.mOnRefreshListener = onRefreshListener;
    }

    public JRefreshAndLoadMoreAdapter.OnRefreshListener getOnRefreshListener() {
        return this.mOnRefreshListener;
    }

    /**
     * @return 是否正在刷新
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 释放动作，会进入两种状态：1、等待刷新；2、正在刷新；
     * @version
     */
    public boolean releaseAction() {
        //是否正在刷新
        boolean isOnRefresh = false;
        //可见高度
        int height = getVisibleHeight();
        //此次释放后，需要进入的目标高度
        int destHeight = 0;

        //当前已经正在刷新，则让视图回到加载视图的高度(正在刷新，不用返回true，否则会再触发)
        if (this.mCurState == STATE_EXECUTING) {
            destHeight = this.mHeight;
        }

        //如果释放的时候，大于刷新视图的高度值且未进入刷新状态，则需要进入刷新状态
        if (height > this.mHeight && this.mCurState < STATE_EXECUTING) {
            setState(STATE_EXECUTING);
            destHeight = this.mHeight;
            isOnRefresh = true;
        }

        smoothScrollTo(destHeight);

        return isOnRefresh;
    }

    /**
     * @param delta 垂直增量
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description
     * @version
     */
    public void onMove(float delta) {
        //需要符合：1、可见高度大于0，即用户已有向下拉动；2、拉动距离要大于0
        if (getVisibleHeight() > 0 || delta > 0) {
            setVisibleHeight((int) (getVisibleHeight() + delta));

            //当前状态为1、下拉刷新；2、释放刷新
            if (this.mCurState <= STATE_RELEASE_TO_ACTION) {

                //小于loadView高度
                if (getVisibleHeight() <= super.mHeight) {
                    setState(STATE_PULL_TO_ACTION);
                } else {
                    setState(STATE_RELEASE_TO_ACTION);
                }

            }

            int height;
            if (getVisibleHeight() >= super.mHeight) {
                height = super.mHeight;
            } else {
                height = getVisibleHeight();
            }

            onMoving(new MoveInfo(super.mHeight, getVisibleHeight(), height * 100 / super.mHeight));

        }
    }

    public void refreshComplete() {
        setState(STATE_DONE);
        reset(0);
    }

    @Override
    protected void onOther(int state) {
        //目前空实现，需扩展，可子类进行重写
    }

    /**
     *
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 下拉过程中的回调，可以更加细微的处理动画
     * @version
     *
     */
    protected abstract void onMoving(MoveInfo moveInfo);

}
