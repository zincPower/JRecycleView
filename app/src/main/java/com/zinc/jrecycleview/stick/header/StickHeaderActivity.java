package com.zinc.jrecycleview.stick.header;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019-07-26 14:09
 * desc         : 粘性头
 * version      : 1.0.0
 */
public class StickHeaderActivity extends AppCompatActivity {

    private JRecycleView mJRecycleView;
    private TextView mTextView;

    private JRefreshAndLoadMoreAdapter mAdapter;
    private List<String> data = new ArrayList<>();

    private Handler mHandler = new Handler(Looper.getMainLooper());

    private int count = 20;

    private static final float OFFSET = 600;
    private float mRvOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refresh_and_load);

        mJRecycleView = findViewById(R.id.j_recycle_view);
        mTextView = findViewById(R.id.tv_header);

        mTextView.setAlpha(0);
        mTextView.setText("Zinc");

        data = getInitData();

        RecyclerView.Adapter adapter = new StickHeaderAdapter(this, data);
        this.mAdapter = new JRefreshAndLoadMoreAdapter(this, adapter);

        this.mAdapter.setIsOpenLoadMore(false);
        this.mAdapter.setIsOpenRefresh(true);

        this.mAdapter.setOnLoadMoreListener(new JRefreshAndLoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoading() {
                Toast.makeText(StickHeaderActivity.this, "触发加载更多", Toast.LENGTH_SHORT).show();
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
                        mAdapter.setIsOpenLoadMore(true);
                        mAdapter.resetLoadMore();
                        mAdapter.setRefreshComplete(true);
                    }
                }, 2000);
            }
        });

        mJRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                mRvOffset += dy;
                float percent = mRvOffset / OFFSET;

                if (percent >= 1) {
                    percent = 1;
                }

                Log.i("test", "onScrolled: [x: " + dx + ";" +
                        " y:" + dy + ";" +
                        " mRvOffset:" + mRvOffset + ";" +
                        " percent:" + percent + "]");

                mTextView.setAlpha(percent);
            }
        });

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

}
