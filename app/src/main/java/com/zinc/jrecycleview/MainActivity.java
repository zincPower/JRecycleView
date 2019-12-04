package com.zinc.jrecycleview;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.zinc.jrecycleview.anim.dialog.AnimDialog;
import com.zinc.jrecycleview.diy.DiyArticleListActivity;
import com.zinc.jrecycleview.mix.dialog.MixDialog;
import com.zinc.jrecycleview.refreshAndLoad.RefreshAndLoadActivity;
import com.zinc.jrecycleview.stick.content.StickContentActivity;
import com.zinc.jrecycleview.stick.header.StickHeaderActivity;
import com.zinc.jrecycleview.swipe.SwipeActivity;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-16 22:20
 * email        : 56002982@qq.com
 * desc         : 主视图
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
        findViewById(R.id.tv_diy_refresh).setOnClickListener(this);
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
                Intent intent1 = new Intent(this, RefreshAndLoadActivity.class);
                intent1.putExtra(RefreshAndLoadActivity.IS_DIY, false);
                startActivity(intent1);
                break;
            case R.id.tv_diy_refresh:
                Intent intent2 = new Intent(this, RefreshAndLoadActivity.class);
                intent2.putExtra(RefreshAndLoadActivity.IS_DIY, true);
                startActivity(intent2);
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
                go(DiyArticleListActivity.class);
                break;
        }
    }

    private void go(Class to) {
        Intent intent = new Intent(this, to);
        startActivity(intent);
    }
}
