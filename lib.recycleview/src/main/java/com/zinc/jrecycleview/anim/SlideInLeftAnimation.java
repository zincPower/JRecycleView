package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description
 */

public class SlideInLeftAnimation extends IBaseAnimation {
    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "translationX", -view.getRootView().getWidth(), 0));
    }
}
