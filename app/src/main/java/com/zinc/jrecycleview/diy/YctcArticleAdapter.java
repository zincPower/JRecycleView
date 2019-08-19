package com.zinc.jrecycleview.diy;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.stick.IStick;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : zinc
 * time         : 2019-08-19 12:05
 * desc         :
 * version      :
 */
public class YctcArticleAdapter extends RecyclerView.Adapter {

    public static final int BANNER = 1;
    public static final int SORT = 2;
    public static final int CONTENT = 3;

    private final List<YctcData> mData;
    private final WeakReference<Context> mContext;
    private final LayoutInflater mInflater;

    public YctcArticleAdapter(Context context, List<YctcData> data) {
        mData = data;
        mContext = new WeakReference<>(context);
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == BANNER) {
            return new BannerViewHolder(
                    mInflater.inflate(R.layout.item_article_banner, parent, false)
            );
        } else if (viewType == SORT) {
            return new SortViewHolder(
                    mInflater.inflate(R.layout.item_article_sort, parent, false)
            );
        } else if (viewType == CONTENT) {
            return new ContentViewHolder(
                    mInflater.inflate(R.layout.item_article_content, parent, false)
            );
        }

        return null;

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        YctcData yctcData = mData.get(position);
        if (holder instanceof BannerViewHolder) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            bannerViewHolder.tvBanner.setText(yctcData.getContent());
        } else if (holder instanceof SortViewHolder) {
            SortViewHolder sortViewHolder = (SortViewHolder) holder;
            sortViewHolder.tvSort.setText(yctcData.getContent());
        } else if (holder instanceof ContentViewHolder) {
            ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.tvContent.setText(yctcData.getContent());
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getType();
    }

    public static class BannerViewHolder extends RecyclerView.ViewHolder implements IStick {

        private TextView tvBanner;

        BannerViewHolder(View itemView) {
            super(itemView);
            tvBanner = itemView.findViewById(R.id.tv_banner);
        }

    }

    public static class SortViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSort;

        SortViewHolder(View itemView) {
            super(itemView);
            tvSort = itemView.findViewById(R.id.tv_sort);
        }

    }

    public static class ContentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        ContentViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }

    }

}
