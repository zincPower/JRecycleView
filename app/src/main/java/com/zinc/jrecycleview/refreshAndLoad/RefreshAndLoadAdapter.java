package com.zinc.jrecycleview.refreshAndLoad;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.zinc.jrecycleview.R;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/3/17
 * @description
 */

public class RefreshAndLoadAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mLayoutInflater;

    private Context context;

    public RefreshAndLoadAdapter(Context context, List<String> data) {
        this.mData = data;
        mLayoutInflater = LayoutInflater.from(context);
        this.context = context;
    }

    public void setData(List<String> mData) {
        this.mData = mData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestHolder(mLayoutInflater.inflate(R.layout.refresh_and_load_view_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
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

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class TestHolder extends RecyclerView.ViewHolder {

        private TextView mTvContent;

        public TestHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_content);
        }
    }

}
