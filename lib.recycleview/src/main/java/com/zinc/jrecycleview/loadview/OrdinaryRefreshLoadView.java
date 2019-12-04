package com.zinc.jrecycleview.loadview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.zinc.jrecycleview.loadview.base.IBaseRefreshLoadView;
import com.zinc.jrecycleview.loadview.bean.MoveInfo;
import com.zinc.jrecycleview.utils.LogUtils;
import com.zinc.jrecycleview.widget.BallSpinFadeLoader;
import com.zinc.librecycleview.R;

/**
 * author       : Jiang Pengyong
 * time         : 2018-03-17 14:35
 * email        : 56002982@qq.com
 * desc         : 最为普通的下拉刷新头部
 * <p>
 * 自定义流程
 * 1、只需要继承 {@link IBaseRefreshLoadView}，实现相应的方法。
 * 2、全局设置，可以通过
 * {@link com.zinc.jrecycleview.config.JRecycleViewManager#setRefreshLoadView(IBaseRefreshLoadView)}
 * 3、单个设置，可以通过
 * {@link com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter#setRefreshLoadView(IBaseRefreshLoadView)}
 * <p>
 * version      : 1.0.0
 */

public class OrdinaryRefreshLoadView extends IBaseRefreshLoadView {

    private View mLoadView;

    // 动画时长
    private static final int ANIM_DURATION = 180;

    //视图控件
    private TextView mTvRefreshStatus;
    private ImageView mIvArrow;
    private BallSpinFadeLoader mBallLoader;

    //向上转动画
    private RotateAnimation mArrowToUpAnim;
    //向下转动画
    private RotateAnimation mArrowToDownAnim;

    public OrdinaryRefreshLoadView(Context context) {
        this(context, null, 0);
    }

    public OrdinaryRefreshLoadView(Context context,
                                   @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrdinaryRefreshLoadView(Context context,
                                   @Nullable AttributeSet attrs,
                                   int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMoving(MoveInfo moveInfo) {
        LogUtils.i(TAG, "onMoving: " + moveInfo.toString());
    }

    @Override
    protected View getLoadView() {
        return mLoadView;
    }

    @Override
    protected View initView(Context context) {
        mLoadView = LayoutInflater
                .from(context)
                .inflate(R.layout.j_widget_ordinary_load_refresh_view, this, false);

        mTvRefreshStatus = mLoadView.findViewById(R.id.tv_refresh_status);
        mIvArrow = mLoadView.findViewById(R.id.iv_arrow);
        mBallLoader = mLoadView.findViewById(R.id.ball_loader);

        //向上转，从0->180
        mArrowToUpAnim = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //动画时长
        mArrowToUpAnim.setDuration(ANIM_DURATION);
        //让动画停留在最后一帧
        mArrowToUpAnim.setFillAfter(true);

        //向下转，从180->0
        mArrowToDownAnim = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mArrowToDownAnim.setDuration(ANIM_DURATION);
        mArrowToDownAnim.setFillAfter(true);

        return mLoadView;
    }

    @Override
    protected void onPullToAction() {
        mBallLoader.setVisibility(GONE);
        mIvArrow.setVisibility(VISIBLE);
        mTvRefreshStatus.setText(getContext().getString(R.string.j_recycle_pull_to_refresh));

        if (mCurState == STATE_RELEASE_TO_ACTION) {//释放刷新->下拉刷新：把箭头往回转
            mIvArrow.startAnimation(mArrowToDownAnim);
        } else if (mCurState == STATE_EXECUTING) {//刷新ing->下拉刷新
            mIvArrow.clearAnimation();
        }
    }

    @Override
    protected void onReleaseToAction() {
        mBallLoader.setVisibility(GONE);
        mIvArrow.setVisibility(VISIBLE);
        mTvRefreshStatus.setText(getContext().getString(R.string.j_recycle_release_to_refresh));
        //需要先清空动画
        mIvArrow.clearAnimation();
        mIvArrow.startAnimation(mArrowToUpAnim);
    }

    @Override
    protected void onExecuting() {
        mBallLoader.setVisibility(VISIBLE);

        //需要先清空箭头状态，后在进行视图隐藏，否则会有问题
        mIvArrow.clearAnimation();
        mIvArrow.setVisibility(GONE);

        mTvRefreshStatus.setText(getContext().getString(R.string.j_recycle_refreshing));
    }

    @Override
    protected void onDone() {
        mIvArrow.clearAnimation();
        mIvArrow.setVisibility(GONE);
        mBallLoader.setVisibility(GONE);
        mTvRefreshStatus.setText(getContext().getString(R.string.j_recycle_refreshed));
    }

}
