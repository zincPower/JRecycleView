package com.zinc.jrecycleview;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;
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

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);

        ToastUtil.init(this);

        JRecycleViewManager.getInstance().setItemAnimations(AnimFactory.getAnimSet(AnimFactory.SLIDE_RIGHT));
//        JRecycleViewManager.getInstance().setBasePullRefreshLoadView(new MyRefreshView(getBaseContext()));
    }
}
