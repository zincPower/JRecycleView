package com.zinc.jrecycleview.refreshAndLoad;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.anim.AnimFactory;
import com.zinc.jrecycleview.anim.IBaseAnimation;
import com.zinc.jrecycleview.loadview.base.IBasePullRefreshLoadView;
import com.zinc.jrecycleview.loadview.bean.MoveInfo;
import com.zinc.jrecycleview.widget.BallSpinFadeLoader;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/17
 * @description
 */

public class MyRefreshView extends IBasePullRefreshLoadView {

    private RelativeLayout mLoadView;
    private View mIvMeituan;

    public MyRefreshView(Context context) {
        super(context);
    }

    public MyRefreshView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRefreshView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getLoadView() {
        return mLoadView;
    }

    @Override
    protected void onPullToAction() {

    }

    @Override
    protected void onReleaseToAction() {

    }

    @Override
    protected void onExecuting() {
        this.mIvMeituan.clearAnimation();
        this.mIvMeituan.setBackgroundResource(R.drawable.meituan_refreshing_anim);
        AnimationDrawable anim = (AnimationDrawable) this.mIvMeituan.getBackground();
        anim.start();
    }

    @Override
    protected void onDone() {
        this.mIvMeituan.clearAnimation();
        this.mIvMeituan.setBackgroundResource(R.drawable.meituan_pull_image);
    }

    @Override
    protected View initView(Context context) {
        this.mLoadView = (RelativeLayout) LayoutInflater.from(context).inflate(R.layout.view_my_refresh, null, false);

        //将loadView视图添加进RecycleView，且将其高度设置为0
//        addView(mLoadView, new LayoutParams(LayoutParams.MATCH_PARENT, 0));
//        setGravity(Gravity.BOTTOM);

        this.mIvMeituan = this.mLoadView.findViewById(R.id.iv_meituan);
        this.mIvMeituan.setBackgroundResource(R.drawable.meituan_pull_image);

        return mLoadView;
    }

    @Override
    protected void onMoving(MoveInfo moveInfo) {
        this.mIvMeituan.clearAnimation();

        if (moveInfo.getPercent() < 100) {
            ViewGroup.LayoutParams layoutParams = this.mIvMeituan.getLayoutParams();
            int height = moveInfo.getViewHeight() * moveInfo.getPercent() / 100;
            layoutParams.height = height;
            layoutParams.width = height;

            this.mIvMeituan.setBackgroundResource(R.drawable.meituan_pull_image);
        } else {
            this.mIvMeituan.clearAnimation();
            this.mIvMeituan.setBackgroundResource(R.drawable.meituan_pull_to_refresh_anim);
            AnimationDrawable anim = (AnimationDrawable) this.mIvMeituan.getBackground();
            anim.start();
        }

    }
}
