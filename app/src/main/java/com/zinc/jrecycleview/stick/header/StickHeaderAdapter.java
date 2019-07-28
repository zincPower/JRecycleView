package com.zinc.jrecycleview.stick.header;

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
import com.zinc.jrecycleview.stick.IStick;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/3/17
 * @description
 */

public class StickHeaderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mLayoutInflater;

    private Context context;

    public StickHeaderAdapter(Context context, List<String> data) {
        this.mData = data;
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<String> mData) {
        this.mData = mData;
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
                    Toast.makeText(context, content, Toast.LENGTH_LONG).show();
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
