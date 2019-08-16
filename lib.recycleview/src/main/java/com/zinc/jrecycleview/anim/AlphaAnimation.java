package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author       : Jiang zinc
 * time         : 2018-04-12 12:15
 * email        : 56002982@qq.com
 * desc         : 透明度动画
 * version      : 1.0.0
 */

public class AlphaAnimation extends IBaseAnimation {

    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "alpha", 0f, 1f));
    }

}
