package com.zinc.jrecycleview.anim;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description
 */

public class ScaleAnimation extends IBaseAnimation {

    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f));
        addAnimTogether(ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f));
    }

}
