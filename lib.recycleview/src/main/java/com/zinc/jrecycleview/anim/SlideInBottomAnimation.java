package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description 从下往上动画
 */

public class SlideInBottomAnimation extends IBaseAnimation {
    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "translationY", view.getRootView().getHeight() / 4, 0));
        addAnimTogether(ObjectAnimator.ofFloat(view, "alpha", 0.5f, 1f));
    }
}
