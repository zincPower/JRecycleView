package com.zinc.jrecycleview.listener;

import android.view.MotionEvent;

/**
 * author       : zinc
 * time         : 2019-08-19 15:34
 * desc         : JRecycleListener
 * version      : 1.0.0
 */
public interface JRecycleListener {

    boolean onTouch(MotionEvent event, float deltaY);

    boolean onUp(MotionEvent event);

}
