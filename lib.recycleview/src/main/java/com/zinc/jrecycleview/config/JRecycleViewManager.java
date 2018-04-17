package com.zinc.jrecycleview.config;

import com.zinc.jrecycleview.anim.IBaseAnimation;
import com.zinc.jrecycleview.anim.SlideInBottomAnimation;
import com.zinc.jrecycleview.loadview.OrdinaryLoadMoreView;
import com.zinc.jrecycleview.loadview.base.IBaseLoadMoreView;
import com.zinc.jrecycleview.loadview.base.IBasePullRefreshLoadView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/17
 * @description
 */

public class JRecycleViewManager {

    private static final JRecycleViewManager ourInstance = new JRecycleViewManager();

    //下拉刷新视图
    private IBasePullRefreshLoadView basePullRefreshLoadView;
    //上拉更多视图
    private IBaseLoadMoreView baseLoadMoreView;

    //动画
    private IBaseAnimation[] itemAnimations;

    public static JRecycleViewManager getInstance() {
        return ourInstance;
    }

    private JRecycleViewManager() {
        itemAnimations = new IBaseAnimation[]{new SlideInBottomAnimation()};
    }

    public IBasePullRefreshLoadView getBasePullRefreshLoadView() {
        return basePullRefreshLoadView;
    }

    public void setBasePullRefreshLoadView(IBasePullRefreshLoadView basePullRefreshLoadView) {
        this.basePullRefreshLoadView = basePullRefreshLoadView;
    }

    public IBaseLoadMoreView getBaseLoadMoreView() {
        return baseLoadMoreView;
    }

    public void setBaseLoadMoreView(IBaseLoadMoreView baseLoadMoreView) {
        this.baseLoadMoreView = baseLoadMoreView;
    }

    public IBaseAnimation[] getItemAnimations() {
        return itemAnimations;
    }

    public void setItemAnimations(IBaseAnimation[] itemAnimations) {
        this.itemAnimations = itemAnimations;
    }
}
