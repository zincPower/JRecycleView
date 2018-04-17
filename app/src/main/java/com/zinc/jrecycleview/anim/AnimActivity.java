package com.zinc.jrecycleview.anim;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.refreshAndLoad.RefreshAndLoadActivity;
import com.zinc.jrecycleview.refreshAndLoad.RefreshAndLoadAdapter;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/17
 * @description
 */

public class AnimActivity extends AppCompatActivity {

    private int count = 100;
    private RecyclerView mRecycleView;

    private AnimAdapter adapter;
    private List<String> data = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anim);

        mRecycleView = findViewById(R.id.recycle_view);

        data = getInitData();

        adapter = new AnimAdapter(this, data);
        //加入视图动画
//        adapter.setAnimations(AnimFactory.getAnimSet(AnimFactory.SLIDE_BOTTOM));
        adapter.setOpenAnim(true);

        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(adapter);

    }

    public List<String> getInitData() {
        this.data.clear();
        for (int i = 1; i <= count; ++i) {
            data.add("zinc Power" + i);
        }
        ++count;
        return data;
    }

}