package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-12 12:25
 * email        : 56002982@qq.com
 * desc         : 缩放动画
 * version      : 1.0.0
 */

public class ScaleAnimation extends IBaseAnimation {

    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1f));
        addAnimTogether(ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1f));
    }

}
