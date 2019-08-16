package com.zinc.jrecycleview.loadview.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.utils.LogUtils;

/**
 * author       : Jiang zinc
 * time         : 2018-03-19 12:34
 * email        : 56002982@qq.com
 * desc         : 加载更多，抽象类
 * version      : 1.0.0
 */

public abstract class IBaseLoadMoreView extends IBaseWrapperView {

    protected static final String TAG = "IBaseLoadMoreView";

    // 加载出错
    public final static int STATE_ERROR = INDEX;

    // 没有更多
    public final static int STATE_NO_MORE = INDEX << 6;

    protected JRefreshAndLoadMoreAdapter.OnLoadMoreListener mOnLoadMoreListener;

    public IBaseLoadMoreView(Context context) {
        super(context);
    }

    public IBaseLoadMoreView(Context context,
                             @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IBaseLoadMoreView(Context context,
                             @Nullable AttributeSet attrs,
                             int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnLoadMoreListener(JRefreshAndLoadMoreAdapter.OnLoadMoreListener listener) {
        this.mOnLoadMoreListener = listener;
    }

    public JRefreshAndLoadMoreAdapter.OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    /**
     * 释放动作，会进入两种状态：1、等待刷新；2、正在刷新；
     *
     * @return 返回是否正在刷新
     */
    public boolean releaseAction() {
        //是否正在刷新
        boolean isOnRefresh = false;
        //可见高度
        int height = getVisibleHeight();

        //如果释放的时候，大于刷新视图的高度值且未进入刷新状态，则需要进入刷新状态
        if (height > this.mHeight && this.mCurState < STATE_EXECUTING) {
            setState(STATE_EXECUTING);
            isOnRefresh = true;
        }

        smoothScrollTo(this.mHeight);

        return isOnRefresh;
    }

    /**
     * 加载完毕
     */
    public void loadComplete() {
        setState(STATE_DONE);
        reset(super.mHeight);
    }

    /**
     * 重置
     */
    public void reset() {
        super.reset(super.mHeight);
    }

    /**
     * 加载错误
     */
    public void loadError() {
        setState(STATE_ERROR);
    }

    /**
     * 没有更多
     */
    public void noMore() {
        setState(STATE_NO_MORE);
        smoothScrollTo(super.mHeight);
    }

    @Override
    protected void onOther(int state) {
        switch (state) {
            case STATE_NO_MORE:
                onNoMore();
                break;
            case STATE_ERROR:
                onError();
                break;
        }
    }

    /**
     * @param delta 垂直增量
     */
    public void onMove(float delta) {
        //需要符合：1、可见高度大于控件高度；2、拉动距离要大于0
        if (getVisibleHeight() >= super.mHeight || delta > 0) {
            setVisibleHeight((int) (getVisibleHeight() + delta));

            LogUtils.i(TAG, "visibleHeight:" + getVisibleHeight() + ";height:" + mHeight);

            //当前状态为1、下拉刷新；2、释放刷新
            if (this.mCurState <= STATE_RELEASE_TO_ACTION) {

                //小于loadView高度
                if (getVisibleHeight() <= this.mHeight) {
                    setState(STATE_PULL_TO_ACTION);
                } else {
                    setState(STATE_RELEASE_TO_ACTION);
                }

            }

        }
    }

    /**
     * 没有更多状态
     */
    protected abstract void onNoMore();

    /**
     * 加载出错
     */
    protected abstract void onError();

}
