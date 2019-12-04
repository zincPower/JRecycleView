package com.zinc.jrecycleview.anim;

import android.animation.ObjectAnimator;
import android.view.View;

public class ScaleToMissAnimation extends IBaseAnimation {

    @Override
    protected void init(View view) {
        addAnimTogether(ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f));
        addAnimTogether(ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f));
    }

}