package com.zinc.jrecycleview.mix;

/**
 * author       : Jiang Pengyong
 * time         : 2019-08-24 23:34
 * email        : 56002982@qq.com
 * desc         : 可选择的类型
 * version      : 1.0.0
 */
public interface Constant {

    int REFRESH = 1;
    int LOAD_MORE = 2;
    int SWIPE = 3;
    int ANIM = 4;
    int STICK = 5;

    interface DATA_TYPE {
        int STICK = 0;
        int NORMAL = 1;
        int SWIPE = 2;
    }

}
