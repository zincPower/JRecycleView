package com.zinc.jrecycleview;

import android.app.Application;

import com.zinc.jrecycleview.anim.AnimFactory;
import com.zinc.jrecycleview.config.JRecycleViewManager;
import com.zinc.jrecycleview.refreshAndLoad.MyRefreshView;
import com.zinc.jrecycleview.util.ToastUtil;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/8
 * @description
 */

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ToastUtil.init(this);

        JRecycleViewManager.getInstance().setItemAnimations(AnimFactory.getAnimSet(AnimFactory.SLIDE_RIGHT));
//        JRecycleViewManager.getInstance().setBasePullRefreshLoadView(new MyRefreshView(getBaseContext()));
    }
}
