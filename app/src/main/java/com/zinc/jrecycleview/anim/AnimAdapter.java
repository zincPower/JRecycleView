package com.zinc.jrecycleview.anim;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JBaseRecycleAdapter;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/8
 * @description
 */

public class AnimAdapter extends JBaseRecycleAdapter<RecyclerView.ViewHolder> {//RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<String> mData;
    private LayoutInflater mLayoutInflater;

    public AnimAdapter(Context context, List<String> data) {
        this.mData = data;
        this.mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
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

        public TestHolder(View itemView) {
            super(itemView);
            mTvContent = itemView.findViewById(R.id.tv_content);
        }
    }
}
