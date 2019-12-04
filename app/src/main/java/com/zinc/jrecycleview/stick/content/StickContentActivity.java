package com.zinc.jrecycleview.stick.content;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2019-07-26 14:09
 * desc         : 粘性效果视图
 * version      : 1.0.0
 */
public class StickContentActivity extends AppCompatActivity {

    private JRecycleView mJRecycleView;

    private JRefreshAndLoadMoreAdapter mAdapter;
    private final List<String> mData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_and_load);

        mJRecycleView = findViewById(R.id.j_recycle_view);

        getInitData();

        RecyclerView.Adapter adapter = new StickContentAdapter(this, mData);
        mAdapter = new JRefreshAndLoadMoreAdapter(this, adapter);

        mAdapter.setIsOpenLoadMore(false);
        mAdapter.setIsOpenRefresh(false);

        mJRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mJRecycleView.setAdapter(mAdapter);

    }

    public void getInitData() {
        mData.clear();
        for (int i = 1; i <= 20; ++i) {
            mData.add("zinc Power" + i);
        }
    }

}
