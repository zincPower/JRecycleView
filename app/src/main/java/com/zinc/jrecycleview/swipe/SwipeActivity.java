package com.zinc.jrecycleview.swipe;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.anim.AnimFactory;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.data.SwipeData;
import com.zinc.jrecycleview.refreshAndLoad.RefreshAndLoadAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/8
 * @description
 */

public class SwipeActivity extends Activity {

    private RecyclerView mJRecycleView;
    private List<SwipeData> data = new ArrayList<>();
    private SwipeAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe);

        mJRecycleView = findViewById(R.id.j_recycle_view);

        data = getInitData();

        this.mAdapter = new SwipeAdapter(this, data);
        this.mJRecycleView.setLayoutManager(new LinearLayoutManager(this));
        this.mJRecycleView.setAdapter(this.mAdapter);

    }

    public List<SwipeData> getInitData() {
        this.data.clear();
        int count = 20;
        for (int i = 1; i <= count; ++i) {
            if (i % 4 == 1) {
                data.add(new SwipeData(SwipeAdapter.SWIPE_TYPE_ONLY_RIGHT, "只有右菜单" + i, i));
            } else if (i % 4 == 2) {
                data.add(new SwipeData(SwipeAdapter.SWIPE_TYPE_ONLY_LEFT, "只有左菜单" + i, i));
            } else {
                data.add(new SwipeData(SwipeAdapter.SWIPE_TYPE, "两个菜单" + i, i));
            }
        }
        ++count;
        return data;
    }

}
