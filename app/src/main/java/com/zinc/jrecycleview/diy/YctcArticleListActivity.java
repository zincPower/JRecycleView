package com.zinc.jrecycleview.diy;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.listener.JRecycleListener;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019-08-19 11:51
 * desc         :
 * version      :
 */
public class YctcArticleListActivity extends AppCompatActivity {

    private static final String TAG = "YctcArticleListActivity";

    private static final int OFFSET = 0;
    private static final int DURATION = 200;

    private int mSubHeight;

    private static final int PAGE_SIZE = 20;

    private LinearLayout llTopCategory;
    private LinearLayout llSubCategory;
    private JRecycleView jRecycleView;
    private TextView tvTab;
    private TextView tvSort;

    private YctcArticleAdapter mAdapter;
    private final List<YctcData> mData = new ArrayList<>();

    private ValueAnimator mAnim;

    private Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yctc_article_list);

        llTopCategory = findViewById(R.id.ll_top_category);
        llSubCategory = findViewById(R.id.ll_sub_category);
        jRecycleView = findViewById(R.id.j_recycle_view);
        tvTab = findViewById(R.id.tv_tab);
        tvSort = findViewById(R.id.tv_sort);

        mSubHeight = UIUtils.dip2px(this, 45);

        initAnim();

        tvSort.setVisibility(View.GONE);

        jRecycleView.setListener(new JRecycleListener() {
            @Override
            public boolean onTouch(MotionEvent event, float deltaY) {

                int height = llSubCategory.getHeight();

                Log.i(TAG, "onTouch: [height:" + height + "; deltaY:" + deltaY + "]");

                if (height <= 0 && deltaY < 0) {
                    return false;
                } else if (height >= mSubHeight && deltaY > 0) {
                    return false;
                } else {
                    setSubCateHeight((int) (height + deltaY));
                    return true;
                }
            }

            @Override
            public boolean onUp(MotionEvent event) {

                int curHeight = llSubCategory.getHeight();

                if (curHeight >= mSubHeight || curHeight <= 0) {
                    return false;
                }

                int halfHeight = mSubHeight >> 1;

                if (mAnim.isRunning()) {
                    mAnim.cancel();
                }

                mAnim.setIntValues();

                if (curHeight > halfHeight) {
                    mAnim.setIntValues(curHeight, mSubHeight);
                } else {
                    mAnim.setIntValues(curHeight, 0);
                }
                mAnim.start();

                return true;
            }
        });

        jRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                View firstView = recyclerView.getLayoutManager().getChildAt(0);
                if (firstView == null) {
                    return;
                }

                RecyclerView.ViewHolder childViewHolder = recyclerView.getChildViewHolder(firstView);

                if (childViewHolder == null) {
                    return;
                }

                if (childViewHolder instanceof YctcArticleAdapter.SortViewHolder
                        || childViewHolder instanceof YctcArticleAdapter.ContentViewHolder) {
                    tvSort.setVisibility(View.VISIBLE);
                } else {
                    tvSort.setVisibility(View.GONE);
                }
            }
        });

        initData();

        YctcArticleAdapter yctcArticleAdapter = new YctcArticleAdapter(this, mData);
        JRefreshAndLoadMoreAdapter adapter
                = new JRefreshAndLoadMoreAdapter(this, yctcArticleAdapter);

        jRecycleView.setAdapter(adapter);
        jRecycleView.setLayoutManager(new LinearLayoutManager(this));

        adapter.setOnLoadMoreListener(() -> {
            mHandler.postDelayed(() -> {

                int size = mData.size();

                addData();
                adapter.setLoadComplete();
                adapter.setRefreshComplete();
                adapter.notifyItemRangeInserted(size, PAGE_SIZE);

            }, 2000);
        });

        adapter.setOnRefreshListener(() -> {
            mHandler.postDelayed(() -> {

                initData();
                adapter.setLoadComplete();
                adapter.setRefreshComplete();
                adapter.notifyDataSetChanged();

            }, 2000);
        });


        llSubCategory.post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG, "run: " + llSubCategory.getY());
            }
        });
    }

    private void initAnim() {
        mAnim = ValueAnimator.ofInt(0, mSubHeight);
        mAnim.setDuration(DURATION);
        mAnim.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            setSubCateHeight(value);
        });

//        mHideAnim = ValueAnimator.ofInt(mSubHeight, 0);
//        mHideAnim.setDuration(DURATION);
//        mHideAnim.addUpdateListener(animation -> {
//            int value = (int) animation.getAnimatedValue();
//            setSubCateHeight(value);
//        });
    }

    private void setSubCateHeight(int height) {
        Log.i(TAG, "setSubCateHeight: [height: " + height + "]");

        if (height > mSubHeight) {
            height = mSubHeight;
        } else if (height < 0) {
            height = 0;
        }

        ViewGroup.LayoutParams layoutParams = llSubCategory.getLayoutParams();
        layoutParams.height = height;
        llSubCategory.setLayoutParams(layoutParams);
    }

//    /**
//     * 处理滚动
//     *
//     * @param dy 滚动量
//     */
//    private void handleScroll(int dy) {
//
//        if (dy > 0) {   // 向上滑动
//            handleScrollToTop(dy);
//        } else {        // 向下滑动
//            handleScrollToDown(dy);
//        }
//
//    }
//
//    private void handleScrollToDown(int dy) {
//        if (mHideAnim.isRunning()) {
//            mHideAnim.cancel();
//        }
//
//        if (mShowAnim.isRunning()) {
//            return;
//        }
//
//        if (llSubCategory.getHeight() >= mSubHeight) {
//            Log.i(TAG, "handleScrollToDown: [height: " + llSubCategory.getHeight() + "; "
//                    + "subHeight: " + mSubHeight + "]");
//            return;
//        }
//
//        Log.i(TAG, "handleScrollToDown");
//        mShowAnim.start();
//    }
//
//    private void handleScrollToTop(int dy) {
//
//        if (mShowAnim.isRunning()) {
//            mShowAnim.cancel();
//        }
//
//        if (mHideAnim.isRunning()) {
//            return;
//        }
//
//        if (llSubCategory.getHeight() <= 0) {
//            return;
//        }
//
//        Log.i(TAG, "handleScrollToTop");
//        mHideAnim.start();
//    }

    private void initData() {
        mData.clear();
        mData.add(new YctcData(YctcArticleAdapter.BANNER, "Banner"));
        mData.add(new YctcData(YctcArticleAdapter.SORT, "Sort"));
        for (int i = 1; i <= PAGE_SIZE; ++i) {
            mData.add(new YctcData(YctcArticleAdapter.CONTENT, "Content " + i));
        }
    }

    private void addData() {
        int size = mData.size();
        for (int i = 1; i < PAGE_SIZE; ++i) {
            mData.add(new YctcData(YctcArticleAdapter.CONTENT, "Content " + (size + i)));
        }
    }

}
