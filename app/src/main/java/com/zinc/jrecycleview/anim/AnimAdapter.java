package com.zinc.jrecycleview.anim;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JBaseRecycleAdapter;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : Jiang zinc
 * time         : 2018-04-08 22:25
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class AnimAdapter extends JBaseRecycleAdapter<RecyclerView.ViewHolder> {

    private final List<String> mData;
    private final LayoutInflater mLayoutInflater;

    AnimAdapter(Context context,
                List<String> data) {
        mLayoutInflater = LayoutInflater.from(context);
        mData = data;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        return new TestHolder(
                mLayoutInflater
                        .inflate(R.layout.refresh_and_load_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String content = mData.get(position);
        TestHolder testHolder = (TestHolder) holder;
        testHolder.mTvContent.setText(content);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TestHolder extends RecyclerView.ViewHolder {

        private TextView mTvContent;

        TestHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_content);
        }
    }

}
