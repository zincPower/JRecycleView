package com.zinc.jrecycleview.diy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019-08-19 11:51
 * desc         :
 * version      :
 */
public class YctcArticleListActivity extends AppCompatActivity {

    private static final int PAGE_SIZE = 20;

    private TextView tvTopCategory;
    private TextView tvSubCategory;
    private JRecycleView jRecycleView;
    private TextView tvTab;

    private YctcArticleAdapter mAdapter;
    private final List<YctcData> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yctc_article_list);

        tvTopCategory = findViewById(R.id.tv_top_category);
        tvSubCategory = findViewById(R.id.tv_sub_category);
        jRecycleView = findViewById(R.id.j_recycle_view);
        tvTab = findViewById(R.id.tv_tab);

        initData();

        YctcArticleAdapter yctcArticleAdapter = new YctcArticleAdapter(this, mData);
        JRefreshAndLoadMoreAdapter adapter
                = new JRefreshAndLoadMoreAdapter(this, yctcArticleAdapter);

        jRecycleView.setAdapter(adapter);
        jRecycleView.setLayoutManager(new LinearLayoutManager(this));

    }

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
