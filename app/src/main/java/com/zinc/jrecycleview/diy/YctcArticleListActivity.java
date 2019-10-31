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

                // 这里要使用 llSubCategory.getLayoutParams().height;
                // 因为 llSubCategory.getHeight() 的值为可见值，操作过快会有可能和预期不同
                int height = llSubCategory.getLayoutParams().height;

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

                int curHeight = llSubCategory.getLayoutParams().height;

                Log.i(TAG, "activity onUp: " + curHeight);

                if (curHeight >= mSubHeight || curHeight <= 0) {
                    return false;
                }

                int halfHeight = mSubHeight >> 1;

                if (mAnim.isRunning()) {
                    mAnim.cancel();
                }

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
        mAnim = new ValueAnimator();
        mAnim.setDuration(DURATION);
        mAnim.addUpdateListener(animation -> {
            int value = (int) animation.getAnimatedValue();
            setSubCateHeight(value);
        });
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

    private void initData() {
        mData.clear();
        mData.add(new YctcData(YctcArticleAdapter.BANNER,
                "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner" +
                        "BannerBannerBannerBannerBanner"));
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
