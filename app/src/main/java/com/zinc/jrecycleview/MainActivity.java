package com.zinc.jrecycleview;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zinc.jrecycleview.anim.AnimActivity;
import com.zinc.jrecycleview.anim.dialog.AnimDialog;
import com.zinc.jrecycleview.diy.YctcArticleListActivity;
import com.zinc.jrecycleview.mix.dialog.MixDialog;
import com.zinc.jrecycleview.refreshAndLoad.RefreshAndLoadActivity;
import com.zinc.jrecycleview.stick.content.StickContentActivity;
import com.zinc.jrecycleview.stick.header.StickHeaderActivity;
import com.zinc.jrecycleview.swipe.SwipeActivity;

/**
 * author       : Jiang zinc
 * time         : 2018-04-16 22:20
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private AnimDialog mAnimDialog;
    private MixDialog mMixDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAnimDialog = AnimDialog.newInstance();
        mMixDialog = MixDialog.newInstance();

        findViewById(R.id.tv_mix).setOnClickListener(this);
        findViewById(R.id.tv_load_and_refresh).setOnClickListener(this);
        findViewById(R.id.tv_anim).setOnClickListener(this);
        findViewById(R.id.tv_swipe).setOnClickListener(this);
        findViewById(R.id.tv_stick_header).setOnClickListener(this);
        findViewById(R.id.tv_stick_content).setOnClickListener(this);
        findViewById(R.id.tv_diy).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_mix:
                mMixDialog.show(getSupportFragmentManager(), "Mix");
                break;
            case R.id.tv_load_and_refresh:
                go(RefreshAndLoadActivity.class);
                break;
            case R.id.tv_swipe:
                go(SwipeActivity.class);
                break;
            case R.id.tv_anim:
                mAnimDialog.show(getSupportFragmentManager(), "Anim");
                break;
            case R.id.tv_stick_header:
                go(StickHeaderActivity.class);
                break;
            case R.id.tv_stick_content:
                go(StickContentActivity.class);
                break;
            case R.id.tv_diy:
                go(YctcArticleListActivity.class);
                break;
        }
    }

    private void go(Class to) {
        Intent intent = new Intent(this, to);
        startActivity(intent);
    }
}
