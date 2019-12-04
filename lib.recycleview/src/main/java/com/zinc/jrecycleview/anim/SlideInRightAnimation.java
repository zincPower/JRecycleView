package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-12 12:28
 * email        : 56002982@qq.com
 * desc         : 从右进
 * version      : 1.0.0
 */

public class SlideInRightAnimation extends IBaseAnimation {
    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "translationX",
                view.getRootView().getWidth(), 0));
    }
}
