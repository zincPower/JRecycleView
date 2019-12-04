package com.zinc.jrecycleview.mix;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * author       : Jiang Pengyong
 * time         : 2019-08-24 22:49
 * email        : 56002982@qq.com
 * desc         : 混合视图
 * version      : 1.0.0
 */
public class MixActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 20;

    private static final String REFRESH = "REFRESH";
    private static final String LOAD_MORE = "LOAD_MORE";
    private static final String SWIPE = "SWIPE";
    private static final String ANIM = "ANIM";
    private static final String STICK = "STICK";

    private boolean refresh;
    private boolean loadMore;
    private boolean swipe;
    private boolean anim;
    private boolean stick;

    private JRecycleView recycleView;

    private Random mRandom;

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    private final List<MixData> mValidType = new ArrayList<>();

    private JRefreshAndLoadMoreAdapter mAdapter;

    public static void startActivity(Context context,
                                     boolean isRefresh,
                                     boolean isLoadMore,
                                     boolean isSwipe,
                                     boolean isAnim,
                                     boolean isStick) {
        Intent intent = new Intent(context, MixActivity.class);

        intent.putExtra(REFRESH, isRefresh);
        intent.putExtra(LOAD_MORE, isLoadMore);
        intent.putExtra(SWIPE, isSwipe);
        intent.putExtra(ANIM, isAnim);
        intent.putExtra(STICK, isStick);

        context.startActivity(intent);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mix);

        mRandom = new Random();

        initIntent(getIntent());
        initType();
        initData();

        mAdapter = new JRefreshAndLoadMoreAdapter(this, new MixAdapter(this, mData));

        mAdapter.setIsOpenRefresh(refresh);
        mAdapter.setIsOpenLoadMore(loadMore);
        mAdapter.setOpenAnim(anim);

        mAdapter.setOnRefreshListener(() -> {
            mHandler.postDelayed(() -> {

                initData();
                // 将 "上拉加载更多" 重置
                mAdapter.resetLoadMore();
                // 将 "下拉刷新"
                mAdapter.setRefreshComplete();
                // 刷新数据
                mAdapter.notifyDataSetChanged();

            }, 2000);

        });

        mAdapter.setOnLoadMoreListener(() -> {
            mHandler.postDelayed(() -> {

                if (mData.size() > 2 * PAGE_SIZE) {
                    // 错误
                    mAdapter.setLoadError();
                } else {
                    int size = mData.size();

                    addData();
                    // 加载成功
                    mAdapter.setLoadComplete();
                    // 刷新
                    mAdapter.notifyItemRangeInserted(mAdapter.getRealPosition(size), PAGE_SIZE);
                }

            }, 2000);
        });

        recycleView = findViewById(R.id.recycle_view);
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.setAdapter(mAdapter);

    }

    private void initType() {

        mValidType.add(new MixData(Constant.DATA_TYPE.NORMAL, "普通类型"));

        if (stick) {
            mValidType.add(new MixData(Constant.DATA_TYPE.STICK, "粘性类型"));
        }

        if (swipe) {
            mValidType.add(new MixData(Constant.DATA_TYPE.SWIPE, "侧滑类型"));
        }

    }

    private void initIntent(Intent intent) {

        refresh = intent.getBooleanExtra(REFRESH, false);
        loadMore = intent.getBooleanExtra(LOAD_MORE, false);
        swipe = intent.getBooleanExtra(SWIPE, false);
        anim = intent.getBooleanExtra(ANIM, false);
        stick = intent.getBooleanExtra(STICK, false);

    }

    private List<MixData> mData = new ArrayList<>();

    private void initData() {
        mData.clear();

        int typeSize = mValidType.size();

        for (int i = 0; i < PAGE_SIZE; ++i) {

            int type = mRandom.nextInt(typeSize);

            if (type == typeSize) {
                type = 0;
            }

            MixData validData = mValidType.get(type);

            MixData mixData = new MixData(validData.getType(), validData.getName());
            mData.add(mixData);
        }
    }

    private void addData() {
        int typeSize = mValidType.size();

        for (int i = 0; i < PAGE_SIZE; ++i) {

            int type = mRandom.nextInt(typeSize);

            if (type == typeSize) {
                type = 0;
            }

            MixData validData = mValidType.get(type);

            MixData mixData = new MixData(validData.getType(), validData.getName());
            mData.add(mixData);
        }
    }

}
