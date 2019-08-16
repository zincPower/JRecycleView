package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author       : Jiang zinc
 * time         : 2019-04-12 12:29
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class SlideInTopAnimation extends IBaseAnimation {
    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "translationY",
                -view.getRootView().getHeight() >> 2, 0));
        addAnimTogether(ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f));
    }
}
