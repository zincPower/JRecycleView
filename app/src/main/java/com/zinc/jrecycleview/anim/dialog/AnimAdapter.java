package com.zinc.jrecycleview.anim.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zinc.jrecycleview.R;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019-08-24 22:03
 * desc         :
 * version      :
 */
public class AnimAdapter extends RecyclerView.Adapter<AnimAdapter.AnimViewHolder> {

    private final WeakReference<Context> mContext;
    private final LayoutInflater mInflater;
    private final List<AnimData> mData;
    private final AnimListener mListener;

    public AnimAdapter(Context context,
                       List<AnimData> data,
                       AnimListener listener) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public AnimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimViewHolder(
                mInflater.inflate(R.layout.item_anim, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimViewHolder holder, int position) {
        AnimData animData = mData.get(position);
        holder.tvContent.setText(animData.getName());
        holder.tvContent.setOnClickListener(v -> {
            mListener.onItemClick(animData.getType());
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class AnimViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;

        AnimViewHolder(View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

    interface AnimListener {
        void onItemClick(int type);
    }

}
