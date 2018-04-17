package com.zinc.jrecycleview.config;

import com.zinc.librecycleview.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description JRecycle的一些配置
 */

public class JRecycleConfig {

    //侧滑的layout
    public static int SWIPE_LAYOUT = R.layout.j_swipe_wrapper;

    //刷新头部
    public static final int JHEAD = 0xABC101;
    //加载更多
    public static final int JFOOT = 0xABC103;
    //侧滑的type
    public static final int SWIPE_TYPE = 0xABC201;

    public static final int ANIM_DURATION = 500;

//    private static final JRecycleConfig ourInstance = new JRecycleConfig();
//
//    public static JRecycleConfig getInstance() {
//        return ourInstance;
//    }
//
//    private JRecycleConfig() {
//    }
//
//    public int getAnimDuration() {
//        return ANIM_DURATION;
//    }
}
