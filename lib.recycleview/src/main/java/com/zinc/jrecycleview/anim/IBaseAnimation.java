package com.zinc.jrecycleview.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.jrecycleview.config.JRecycleConfig;

/**
 * author       : Jiang zinc
 * time         : 2018-04-12 12:17
 * email        : 56002982@qq.com
 * desc         : 动画接口
 * version      : 1.0.0
 */

public abstract class IBaseAnimation {

    private AnimatorSet set;

    /**
     * 需要加载动画的视图，一般为ViewHolder中的itemView
     * {@link android.support.v7.widget.RecyclerView.ViewHolder#itemView}
     */
    public AnimatorSet getAnimators(View view) {

        set = new AnimatorSet();

        initAnimatorSet();
        init(view);

        return set;
    }

    /**
     * 初始化动画集合
     */
    private void initAnimatorSet() {
        set.setDuration(JRecycleConfig.ANIM_DURATION);
        set.setInterpolator(new LinearInterpolator());
    }

    /**
     * 增加同步动画
     */
    protected void addAnimTogether(Animator animator) {
        set.playTogether(animator);
    }

    /**
     * 初始化方法
     */
    protected abstract void init(View view);

}
