package com.zinc.jrecycleview.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AnimationSet;

import com.zinc.jrecycleview.config.JRecycleConfig;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description 透明度动画
 */

public class AlphaAnimation extends IBaseAnimation {

    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "alpha", 0f, 1f));
    }

}
