package com.zinc.jrecycleview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.zinc.jrecycleview.anim.AnimActivity;
import com.zinc.jrecycleview.refreshAndLoad.RefreshAndLoadActivity;
import com.zinc.jrecycleview.swipe.SwipeActivity;
import com.zinc.jrecycleview.swipe.SwipeAdapter;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/16
 * @description
 */

public class MainActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_load_and_refresh).setOnClickListener(this);
//        findViewById(R.id.tv_all).setOnClickListener(this);
        findViewById(R.id.tv_anim).setOnClickListener(this);
        findViewById(R.id.tv_swipe).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_load_and_refresh:
                go(RefreshAndLoadActivity.class);
                break;
            case R.id.tv_swipe:
                go(SwipeActivity.class);
                break;
            case R.id.tv_anim:
                go(AnimActivity.class);
                break;
//            case R.id.all:
//
//                break;
        }
    }

    private void go(Class to){
        Intent intent = new Intent(this, to);
        startActivity(intent);
    }
}
