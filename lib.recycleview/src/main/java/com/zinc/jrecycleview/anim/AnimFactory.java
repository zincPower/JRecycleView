package com.zinc.jrecycleview.anim;

import android.animation.AnimatorSet;
import android.view.View;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description 动画工厂
 */

public class AnimFactory {

    public static final int SLIDE_TOP = 1;
    public static final int SLIDE_RIGHT = 2;
    public static final int SLIDE_BOTTOM = 3;
    public static final int SLIDE_LEFT = 4;
    public static final int SCALE = 5;
    public static final int ALPHA = 6;

    public static IBaseAnimation[] getAnimSet(int type) {
        IBaseAnimation[] set = null;

        switch (type) {
            case SLIDE_TOP:
                set = new IBaseAnimation[]{new SlideInTopAnimation()};
                break;
            case SLIDE_RIGHT:
                set = new IBaseAnimation[]{new SlideInRightAnimation()};
                break;
            case SLIDE_BOTTOM:
                set = new IBaseAnimation[]{new SlideInBottomAnimation()};
                break;
            case SLIDE_LEFT:
                set = new IBaseAnimation[]{new SlideInLeftAnimation()};
                break;
            case SCALE:
                set = new IBaseAnimation[]{new ScaleAnimation()};
                break;
            case ALPHA:
                set = new IBaseAnimation[]{new AlphaAnimation()};
                break;
        }

        return set;
    }

}
