package com.zinc.jrecycleview.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.jrecycleview.config.JRecycleConfig;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-12 12:17
 * email        : 56002982@qq.com
 * desc         : 动画接口
 * version      : 1.0.0
 */

public abstract class IBaseAnimation {

    protected AnimatorSet mAnimSet;

    /**
     * 需要加载动画的视图，一般为ViewHolder中的itemView
     * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder#itemView}
     */
    public AnimatorSet getAnimators(View view) {

        mAnimSet = new AnimatorSet();

        initAnimatorSet();
        init(view);

        return mAnimSet;
    }

    /**
     * 初始化动画集合
     */
    private void initAnimatorSet() {
        mAnimSet.setDuration(JRecycleConfig.ANIM_DURATION);
        mAnimSet.setInterpolator(new LinearInterpolator());
    }

    /**
     * 增加同步动画
     */
    protected void addAnimTogether(Animator animator) {
        mAnimSet.playTogether(animator);
    }

    /**
     * 初始化方法
     */
    protected abstract void init(View view);

}
