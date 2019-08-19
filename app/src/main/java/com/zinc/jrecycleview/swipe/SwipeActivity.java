package com.zinc.jrecycleview.swipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.data.SwipeData;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang zinc
 * time         : 2018-04-08 22:27
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class SwipeActivity extends Activity {

    private static final int PAGE_SIZE = 20;

    private RecyclerView mJRecycleView;
    private final List<SwipeData> mData = new ArrayList<>();
    private SwipeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mJRecycleView = findViewById(R.id.j_recycle_view);

        getInitData();

        mAdapter = new SwipeAdapter(this, mData);
        mJRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mJRecycleView.setAdapter(this.mAdapter);

    }

    public void getInitData() {
        mData.clear();
        for (int i = 1; i <= PAGE_SIZE; ++i) {
            if (i % 4 == 1) {
                mData.add(new SwipeData(SwipeAdapter.SWIPE_TYPE_ONLY_RIGHT, "只有右菜单" + i, i));
            } else if (i % 4 == 2) {
                mData.add(new SwipeData(SwipeAdapter.SWIPE_TYPE_ONLY_LEFT, "只有左菜单" + i, i));
            } else {
                mData.add(new SwipeData(SwipeAdapter.SWIPE_TYPE, "两个菜单" + i, i));
            }
        }
    }

}
