package com.zinc.jrecycleview.loadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zinc.jrecycleview.loadview.base.IBaseLoadMoreView;
import com.zinc.jrecycleview.widget.BallSpinFadeLoader;
import com.zinc.librecycleview.R;

/**
 * author       : Jiang Pengyong
 * time         : 2018-03-19 16:09
 * email        : 56002982@qq.com
 * desc         : 普通的加载更多
 * <p>
 * 自定义流程
 * 1、只需要继承 {@link IBaseLoadMoreView}，实现相应的方法。
 * 2、全局设置，可以通过
 * {@link com.zinc.jrecycleview.config.JRecycleViewManager#setLoadMoreView(IBaseLoadMoreView)}
 * 3、单个设置，可以通过
 * {@link com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter#setLoadMoreView(IBaseLoadMoreView)}
 * <p>
 * version      : 1.0.0
 */

public class OrdinaryLoadMoreView extends IBaseLoadMoreView {

    private View mLoadMoreView;
    private BallSpinFadeLoader mProgressBar;
    private TextView mTvTip;
    private ImageView mIvReload;

    public OrdinaryLoadMoreView(Context context) {
        this(context, null, 0);
    }

    public OrdinaryLoadMoreView(Context context,
                                @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrdinaryLoadMoreView(Context context,
                                @Nullable AttributeSet attrs,
                                int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getLoadView() {
        return this.mLoadMoreView;
    }

    @Override
    protected void onNoMore() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(getContext().getString(R.string.j_recycle_no_more));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    @Override
    protected void onError() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(getContext().getString(R.string.j_recycle_reload));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(VISIBLE);
    }

    @Override
    protected void onPullToAction() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(getContext().getString(R.string.j_recycle_pull_to_load));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    @Override
    protected void onReleaseToAction() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(getContext().getString(R.string.j_recycle_release_to_load));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    @Override
    protected void onExecuting() {
        this.mProgressBar.setVisibility(VISIBLE);
        this.mTvTip.setText(getContext().getString(R.string.j_recycle_loading));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    @Override
    protected void onDone() {
        this.mProgressBar.setVisibility(GONE);
        this.mTvTip.setText(getContext().getString(R.string.j_recycle_loaded));
        this.mTvTip.setVisibility(VISIBLE);
        this.mIvReload.setVisibility(GONE);
    }

    @Override
    protected View initView(Context context) {
        this.mLoadMoreView = LayoutInflater.from(context)
                .inflate(R.layout.j_widget_ordinary_load_more_view,
                        this,
                        false);
        this.mProgressBar = this.mLoadMoreView.findViewById(R.id.ball_loader);
        this.mTvTip = this.mLoadMoreView.findViewById(R.id.tv_tip);
        this.mIvReload = this.mLoadMoreView.findViewById(R.id.iv_reload);

        return this.mLoadMoreView;
    }

}
