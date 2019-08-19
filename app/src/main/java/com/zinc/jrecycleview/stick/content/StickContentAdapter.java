package com.zinc.jrecycleview.stick.content;

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
 * author       : Jiang zinc
 * time         : 2018-03-17 23:45
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class StickContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<String> mData;
    private final LayoutInflater mLayoutInflater;

    private Context context;

    StickContentAdapter(Context context, List<String> data) {
        this.mData = data;
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        if (viewType == 1) {
            return new StickHolder(mLayoutInflater
                    .inflate(R.layout.stick_item, parent, false));
        } else {
            return new TestHolder(mLayoutInflater
                    .inflate(R.layout.refresh_and_load_view_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {

        final String content = mData.get(position);

        if (holder instanceof TestHolder) {

            TestHolder testHolder = (TestHolder) holder;
            testHolder.tvContent.setText(content);
            testHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("zincPower", "onClick: " + content);
                    Toast.makeText(context, content, Toast.LENGTH_LONG).show();
                }
            });

        } else if (holder instanceof StickHolder) {

            StickHolder stickHolder = (StickHolder) holder;
            stickHolder.tvStick.setText(content);

        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    class TestHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        TestHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

    class StickHolder extends RecyclerView.ViewHolder implements IStick {

        private TextView tvStick;

        StickHolder(View itemView) {
            super(itemView);
            tvStick = itemView.findViewById(R.id.tv_stick);
        }
    }

}
