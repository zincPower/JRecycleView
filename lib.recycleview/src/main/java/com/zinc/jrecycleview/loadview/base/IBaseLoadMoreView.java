package com.zinc.jrecycleview.loadview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.utils.LogUtils;

/**
 * author       : Jiang Pengyong
 * time         : 2018-03-19 12:34
 * email        : 56002982@qq.com
 * desc         : 加载更多，抽象类
 * version      : 1.0.0
 */

public abstract class IBaseLoadMoreView extends IBaseWrapperView {

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

    @Override
    protected void wrapper(Context context, View view) {

        addView(view);

        measure(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        this.mHeight = getMeasuredHeight();
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
     * @param visible 可见高度
     * @return 返回是否正在刷新
     */
    public boolean releaseAction(int visible) {
        //是否正在刷新
        boolean isOnRefresh = false;

        LogUtils.i(TAG, "visible: " + visible + "; height: " + mHeight);

        //如果释放的时候，大于刷新视图的高度值且未进入刷新状态，则需要进入刷新状态
        if (visible > this.mHeight && this.mCurState < STATE_EXECUTING) {
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
        reset(mHeight);
    }

    /**
     * 重置
     */
    public void reset() {
        setState(STATE_PULL_TO_ACTION);
        reset(mHeight);
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
        smoothScrollTo(mHeight);
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
     * @param visibleHeight 可视高度
     * @param delta         垂直增量
     */
    public void onMove(int visibleHeight, float delta) {
        //需要符合：1、可见高度大于控件高度；2、拉动距离要大于0
        float viewHeight = visibleHeight + delta;
        if (viewHeight < mHeight) {
            viewHeight = mHeight;
        }
        setVisibleHeight((int) viewHeight);

        LogUtils.i(TAG,
                "visibleHeight: " + visibleHeight + "; " +
                        "height: " + mHeight + "; " +
                        "viewHeight: " + viewHeight);

        //当前状态为: 1、上拉刷新; 2、释放刷新
        if (this.mCurState <= STATE_RELEASE_TO_ACTION) {
            //小于loadView高度
            if (visibleHeight <= mHeight) {
                if (this.mCurState == STATE_ERROR) {
                    setState(STATE_ERROR);
                } else {
                    setState(STATE_PULL_TO_ACTION);
                }
            } else {
                setState(STATE_RELEASE_TO_ACTION);
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
