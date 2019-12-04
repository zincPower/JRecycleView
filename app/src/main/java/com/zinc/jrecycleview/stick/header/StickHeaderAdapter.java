package com.zinc.jrecycleview.stick.header;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.stick.IStick;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2018-03-17 21:20
 * email        : 56002982@qq.com
 * desc         : 粘性头部适配器
 * version      : 1.0.0
 */

public class StickHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mData;
    private final LayoutInflater mLayoutInflater;

    private final WeakReference<Context> context;

    public StickHeaderAdapter(Context context, List<String> data) {
        this.mData = data;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.context = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new StickHolder(mLayoutInflater
                    .inflate(R.layout.stick_item, parent, false));
        } else {
            return new TestHolder(mLayoutInflater
                    .inflate(R.layout.refresh_and_load_view_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TestHolder) {

            final String content = mData.get(position);
            TestHolder testHolder = (TestHolder) holder;
            testHolder.mTvContent.setText(content);
            testHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("zincPower", "onClick: " + content);
                    Toast.makeText(context.get(), content, Toast.LENGTH_LONG).show();
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    class TestHolder extends RecyclerView.ViewHolder {

        private TextView mTvContent;

        TestHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_content);
        }
    }

    class StickHolder extends RecyclerView.ViewHolder implements IStick {

        StickHolder(View itemView) {
            super(itemView);
        }
    }

}
