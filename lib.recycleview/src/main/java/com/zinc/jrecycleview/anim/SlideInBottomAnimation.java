package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-12 12:26
 * email        : 56002982@qq.com
 * desc         : 从下往上动画
 * version      : 1.0.0
 */

public class SlideInBottomAnimation extends IBaseAnimation {
    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "translationY",
                view.getRootView().getHeight() >> 2, 0));
        addAnimTogether(ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f));
    }
}

