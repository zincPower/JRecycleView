package com.zinc.jrecycleview.anim;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zinc.jrecycleview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang zinc
 * time         : 2018-04-17 22:23
 * email        : 56002982@qq.com
 * desc         : 动画
 * version      : 1.0.0
 */

public class AnimActivity extends AppCompatActivity {

    private final static int PAGE_SIZE = 100;
    private RecyclerView mRecycleView;

    private AnimAdapter mAdapter;
    private final List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        mRecycleView = findViewById(R.id.recycle_view);

        getInitData();

        mAdapter = new AnimAdapter(this, data);
        //加入视图动画
//        mAdapter.setAnimations(AnimFactory.getAnimSet(AnimFactory.SLIDE_BOTTOM));
        mAdapter.setOpenAnim(true);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(mAdapter);

    }

    public void getInitData() {
        data.clear();
        for (int i = 1; i <= PAGE_SIZE; ++i) {
            data.add("zinc Power" + i);
        }
    }

}