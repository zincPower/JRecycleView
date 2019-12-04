package com.zinc.jrecycleview.diy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.stick.IStick;

import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2019-08-19 12:05
 * email        : 56002982@qq.com
 * desc         : 自定义联动适配器
 * version      : 1.0.0
 */
public class DiyArticleAdapter extends RecyclerView.Adapter {

    static final int BANNER = 1;
    static final int SORT = 2;
    static final int CONTENT = 3;

    private final List<DiyData> mData;
    private final LayoutInflater mInflater;

    DiyArticleAdapter(Context context, List<DiyData> data) {
        mData = data;
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

        DiyData yctcData = mData.get(position);
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

    static class SortViewHolder extends RecyclerView.ViewHolder {

        private TextView tvSort;

        SortViewHolder(View itemView) {
            super(itemView);
            tvSort = itemView.findViewById(R.id.tv_sort);
        }

    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        ContentViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }

    }

}
