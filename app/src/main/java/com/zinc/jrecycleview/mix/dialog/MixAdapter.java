package com.zinc.jrecycleview.mix.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
public class MixAdapter extends RecyclerView.Adapter<MixAdapter.AnimViewHolder> {

    private final WeakReference<Context> mContext;
    private final LayoutInflater mInflater;
    private final List<MixShowInfoData> mData;

    public MixAdapter(Context context,
                      List<MixShowInfoData> data) {
        this.mContext = new WeakReference<>(context);
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    @NonNull
    @Override
    public AnimViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AnimViewHolder(
                mInflater.inflate(R.layout.item_mix, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AnimViewHolder holder, int position) {
        MixShowInfoData mixData = mData.get(position);
        holder.tvContent.setText(mixData.getName());
        holder.ivCheck.setVisibility(mixData.isSelect() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            mixData.setSelect(!mixData.isSelect());
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    static class AnimViewHolder extends RecyclerView.ViewHolder {
        private TextView tvContent;
        private ImageView ivCheck;

        AnimViewHolder(View itemView) {
            super(itemView);

            tvContent = itemView.findViewById(R.id.tv_content);
            ivCheck = itemView.findViewById(R.id.iv_check);

        }
    }

}
