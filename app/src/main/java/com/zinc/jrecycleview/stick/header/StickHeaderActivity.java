package com.zinc.jrecycleview.stick.header;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.JRecycleView;
import com.zinc.jrecycleview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2019-07-26 14:09
 * email        : 56002982@qq.com
 * desc         : 粘性头
 * version      : 1.0.0
 */
public class StickHeaderActivity extends AppCompatActivity {

    private static final float OFFSET = 600;
    private static final int PAGE_SIZE = 20;

    private final List<String> mData = new ArrayList<>();

    private JRecycleView mJRecycleView;
    private TextView mTextView;

    private float mRvOffset = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stick);

        mJRecycleView = findViewById(R.id.j_recycle_view);
        mTextView = findViewById(R.id.tv_header);

        mTextView.setAlpha(0);
        mTextView.setText("Zinc");

        getInitData();

        RecyclerView.Adapter adapter = new StickHeaderAdapter(this, mData);

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
        mJRecycleView.setAdapter(adapter);

    }

    public void getInitData() {
        this.mData.clear();
        for (int i = 1; i <= PAGE_SIZE; ++i) {
            mData.add("zinc Power" + i);
        }
    }

}
