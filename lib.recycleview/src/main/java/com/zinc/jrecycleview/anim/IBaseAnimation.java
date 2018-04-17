package com.zinc.jrecycleview.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.zinc.jrecycleview.config.JRecycleConfig;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description 动画接口
 */

public abstract class IBaseAnimation {

    AnimatorSet set;

    /**
     * @param view 需要加载动画的视图，一般为ViewHolder中的itemView{@link android.support.v7.widget.RecyclerView.ViewHolder#itemView}
     * @return 动画
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 获取动画
     * @version
     */
    public AnimatorSet getAnimators(View view){

        set = new AnimatorSet();

        initAnimatorSet();
        init(view);

        return set;
    }

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 初始化动画集合
     * @version
     *
     */
    public void initAnimatorSet(){
        set.setDuration(JRecycleConfig.ANIM_DURATION);
        set.setInterpolator(new LinearInterpolator());
    }

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 增加同步动画
     * @version
     *
     */
    protected void addAnimTogether(Animator animator){
        set.playTogether(animator);
    }

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 初始化方法
     * @version
     *
     */
    protected abstract void init(View view);

}
