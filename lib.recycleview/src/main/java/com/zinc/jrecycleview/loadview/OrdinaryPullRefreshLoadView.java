package com.zinc.jrecycleview.loadview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zinc.jrecycleview.loadview.base.IBasePullRefreshLoadView;
import com.zinc.jrecycleview.loadview.bean.MoveInfo;
import com.zinc.jrecycleview.widget.BallSpinFadeLoader;
import com.zinc.librecycleview.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/3/17
 * @description 最为普通的下拉刷新头部
 */

public class OrdinaryPullRefreshLoadView extends IBasePullRefreshLoadView {

    private LinearLayout mLoadView;

    //视图控件
    private RelativeLayout mRelativeContainer;
    private LinearLayout mLinearHeader;
    private TextView mTvRefreshStatus;
    private LinearLayout mLinearUpdate;
    private TextView mLastRefreshTime;
    private ImageView mIvArrow;
    private BallSpinFadeLoader mBallLoader;

    //向上转动画
    private RotateAnimation mArrowToUpAnim;
    //向下转动画
    private RotateAnimation mArrowToDownAnim;
    //动画时长
    private final int animDuration = 180;

    public OrdinaryPullRefreshLoadView(Context context) {
        this(context, null, 0);
    }

    public OrdinaryPullRefreshLoadView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OrdinaryPullRefreshLoadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMoving(MoveInfo moveInfo) {
//        Log.i(OrdinaryPullRefreshLoadView.class.getSimpleName(), "onMoving: " + moveInfo.toString());
    }

    @Override
    protected View getLoadView() {
        return this.mLoadView;
    }

    @Override
    protected View initView(Context context) {
        this.mLoadView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.j_widget_ordinary_load_refresh_view, null, false);

        //将loadView视图添加进RecycleView，且将其高度设置为0
//        addView(mLoadView, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
//        setGravity(Gravity.BOTTOM);

        this.mRelativeContainer = (RelativeLayout) mLoadView.findViewById(R.id.relative_container);
        this.mLinearHeader = (LinearLayout) mLoadView.findViewById(R.id.linear_header);
        this.mTvRefreshStatus = (TextView) mLoadView.findViewById(R.id.tv_refresh_status);
        this.mLinearUpdate = (LinearLayout) mLoadView.findViewById(R.id.linear_update);
        this.mLastRefreshTime = (TextView) mLoadView.findViewById(R.id.last_refresh_time);
        this.mIvArrow = (ImageView) mLoadView.findViewById(R.id.iv_arrow);
        this.mBallLoader = (BallSpinFadeLoader) mLoadView.findViewById(R.id.ball_loader);

        //向上转，从0->180
        this.mArrowToUpAnim = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        //动画时长
        this.mArrowToUpAnim.setDuration(animDuration);
        //让动画停留在最后一帧
        this.mArrowToUpAnim.setFillAfter(true);

        //向下转，从180->0
        this.mArrowToDownAnim = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        this.mArrowToDownAnim.setDuration(animDuration);
        this.mArrowToDownAnim.setFillAfter(true);

//        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        return mLoadView;
    }

    @Override
    protected void onPullToAction() {
        this.mBallLoader.setVisibility(GONE);
        this.mIvArrow.setVisibility(VISIBLE);
        this.mTvRefreshStatus.setText(getContext().getString(R.string.jrecycle_pull_to_refresh));

        if (this.mCurState == STATE_RELEASE_TO_ACTION) {//释放刷新->下拉刷新：把箭头往回转
            this.mIvArrow.startAnimation(this.mArrowToDownAnim);
        } else if (this.mCurState == STATE_EXECUTING) {//刷新ing->下拉刷新
            this.mIvArrow.clearAnimation();
        }
    }

    @Override
    protected void onReleaseToAction() {
        this.mBallLoader.setVisibility(GONE);
        this.mIvArrow.setVisibility(VISIBLE);
        this.mTvRefreshStatus.setText(getContext().getString(R.string.jrecycle_release_to_refresh));
        //需要先清空动画
        this.mIvArrow.clearAnimation();
        this.mIvArrow.startAnimation(this.mArrowToUpAnim);
    }

    @Override
    protected void onExecuting() {
        this.mBallLoader.setVisibility(VISIBLE);
//        //如果loading没有开启，则开启动画
//        if (!mBallLoader.isLoading()) {
//            mBallLoader.startAnimator();
//        }

        //需要先清空箭头状态，后在进行视图隐藏，否则会有问题
        this.mIvArrow.clearAnimation();
        this.mIvArrow.setVisibility(GONE);

        this.mTvRefreshStatus.setText(getContext().getString(R.string.jrecycle_refreshing));
    }

    @Override
    protected void onDone() {
        this.mIvArrow.clearAnimation();
        this.mIvArrow.setVisibility(GONE);
        this.mBallLoader.setVisibility(GONE);
        this.mTvRefreshStatus.setText(getContext().getString(R.string.jrecycle_refreshed));
//        this.mBallLoader.stopAnimator();
    }

}
