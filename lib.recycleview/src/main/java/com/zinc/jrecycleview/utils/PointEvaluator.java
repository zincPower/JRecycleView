package com.zinc.jrecycleview.utils;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * author       : Jiang zinc
 * time         : 2019-08-16 10:25
 * email        : 56002982@qq.com
 * desc         : 点的估值器
 * version      : 0.1.0
 */
public class PointEvaluator implements TypeEvaluator<PointF> {
    @Override
    public PointF evaluate(float fraction,
                           PointF startValue,
                           PointF endValue) {
        float x = startValue.x + fraction * (endValue.x - startValue.x);
        float y = startValue.y + fraction * (endValue.y - startValue.y);
        return new PointF(x, y);
    }
}
