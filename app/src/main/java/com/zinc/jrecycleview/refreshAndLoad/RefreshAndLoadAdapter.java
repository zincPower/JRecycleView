package com.zinc.jrecycleview.refreshAndLoad;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zinc.jrecycleview.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : Jiang zinc
 * time         : 2018-03-17 22:15
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class RefreshAndLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mData;

    private final LayoutInflater mLayoutInflater;

    private final WeakReference<Context> mContext;

    RefreshAndLoadAdapter(Context context, List<String> data) {
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        return new TestHolder(mLayoutInflater
                .inflate(R.layout.refresh_and_load_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        final String content = mData.get(position);
        TestHolder testHolder = (TestHolder) holder;
        testHolder.mTvContent.setText(content);
        testHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("zincPower", "onClick: " + content);
                Toast.makeText(mContext.get(), content, Toast.LENGTH_LONG).show();
            }
        });
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
