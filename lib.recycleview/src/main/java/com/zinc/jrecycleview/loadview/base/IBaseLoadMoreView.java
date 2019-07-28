package com.zinc.jrecycleview.loadview.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;

import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/3/19
 * @description 加载更多，抽象类
 */

public abstract class IBaseLoadMoreView extends IBaseWrapperView {

    //加载出错
    public final static int STATE_ERROR = 1;

    //没有更多
    public final static int STATE_NO_MORE = 20;

    protected JRefreshAndLoadMoreAdapter.OnLoadMoreListener mOnLoadMoreListener;

    public IBaseLoadMoreView(Context context) {
        super(context);
    }

    public IBaseLoadMoreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public IBaseLoadMoreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnLoadMoreListener(JRefreshAndLoadMoreAdapter.OnLoadMoreListener onLoadMoreListener) {
        this.mOnLoadMoreListener = onLoadMoreListener;
    }

    public JRefreshAndLoadMoreAdapter.OnLoadMoreListener getOnLoadMoreListener() {
        return mOnLoadMoreListener;
    }

    /**
     * @return 返回是否正在刷新
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

        //如果释放的时候，大于刷新视图的高度值且未进入刷新状态，则需要进入刷新状态
        if (height > this.mHeight && this.mCurState < STATE_EXECUTING) {
            setState(STATE_EXECUTING);
            isOnRefresh = true;
        }

        smoothScrollTo(this.mHeight);

        return isOnRefresh;
    }

    public void loadComplete() {
        setState(STATE_DONE);
        reset(super.mHeight);
    }

    public void reset(){
        super.reset(super.mHeight);
    }

    public void loadError(){
        setState(STATE_ERROR);
    }

    public void noMore(){
        setState(STATE_NO_MORE);
        smoothScrollTo(super.mHeight);
    }

    @Override
    protected void onOther(int state) {
       switch (state){
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
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description
     * @version
     */
    public void onMove(float delta) {
        //需要符合：1、可见高度大于控件高度；2、拉动距离要大于0
        if (getVisibleHeight() >= super.mHeight || delta >0) {
            setVisibleHeight((int) (getVisibleHeight() + delta));

//            Log.i(IBaseLoadMoreView.class.getSimpleName(), "visiHeight:" + getVisibleHeight() + ";height:" + mHeight);

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
     *
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 没有更多状态
     * @version
     *
     */
    protected abstract void onNoMore();

    /**
     *
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 加载出错
     * @version
     *
     */
    protected abstract void onError();

}
