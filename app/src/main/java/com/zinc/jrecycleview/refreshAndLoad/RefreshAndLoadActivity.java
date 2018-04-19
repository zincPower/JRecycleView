package com.zinc.jrecycleview.refreshAndLoad;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.config.JRecycleViewManager;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class RefreshAndLoadActivity extends AppCompatActivity {

    private JRecycleView mJRecycleView;

    private JRefreshAndLoadMoreAdapter mAdapter;
    private List<String> data = new ArrayList<>();

    private Handler mHandler = new MyHandler(this);

    private int count = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_and_load);

        mJRecycleView = findViewById(R.id.j_recycle_view);

        data = getInitData();

        RecyclerView.Adapter adapter = new RefreshAndLoadAdapter(this, data);
        this.mAdapter = new JRefreshAndLoadMoreAdapter(this, adapter);

//        this.mAdapter.setIsOpenLoadMore(false);
//        this.mAdapter.setIsOpenRefresh(false);

        this.mAdapter.setOnLoadMoreListener(new JRefreshAndLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoading() {
                Toast.makeText(RefreshAndLoadActivity.this, "触发加载更多", Toast.LENGTH_SHORT).show();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (count > 40) {
                            mAdapter.setLoadError();
                        } else {
                            addData();
                            mAdapter.setLoadComplete();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });
        this.mAdapter.setOnRefreshListener(new JRefreshAndLoadMoreAdapter.OnRefreshListener() {
            @Override
            public void onRefreshing() {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getInitData();
                        mAdapter.resetLoadMore();
                        mAdapter.setRefreshComplete(true);
                    }
                }, 2000);
            }
        });

//        this.mAdapter.setOpenAnim(true);

        mJRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mJRecycleView.setAdapter(mAdapter);

    }

    public List<String> getInitData() {
        this.data.clear();
        count = 20;
        for (int i = 1; i <= count; ++i) {
            data.add("zinc Power" + i);
        }
        ++count;
        return data;
    }

    public void addData() {
        for (int i = 1; i <= 15; ++i) {
            data.add("zinc Power" + count);
            ++count;
        }
    }

    private static class MyHandler extends Handler {

        private WeakReference<RefreshAndLoadActivity> weakReference;

        public MyHandler(RefreshAndLoadActivity activity) {
            weakReference = new WeakReference<RefreshAndLoadActivity>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            RefreshAndLoadActivity refreshAndLoadActivity = weakReference.get();
        }
    }

}
