package com.zinc.jrecycleview.mix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.stick.IStick;
import com.zinc.jrecycleview.swipe.JSwipeViewHolder;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2018-03-17 22:15
 * email        : 56002982@qq.com
 * desc         : 混合适配器
 * version      : 1.0.0
 */

public class MixAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<MixData> mData;

    private final LayoutInflater mLayoutInflater;

    private final WeakReference<Context> mContext;

    MixAdapter(Context context, List<MixData> data) {
        mData = data;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        if (viewType == Constant.DATA_TYPE.NORMAL) {
            return new NormalHolder(mLayoutInflater
                    .inflate(R.layout.refresh_and_load_view_item, parent, false));
        } else if (viewType == Constant.DATA_TYPE.SWIPE) {
            return new SwipeHolder(mLayoutInflater
                    .inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false));
        } else if (viewType == Constant.DATA_TYPE.STICK) {
            return new StickHolder(mLayoutInflater
                    .inflate(R.layout.stick_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        MixData mixData = mData.get(position);
        final int pos = position;

        if (holder instanceof NormalHolder) {
            NormalHolder normalHolder = (NormalHolder) holder;
            normalHolder.tvContent.setText(mixData.getName());
            normalHolder.tvContent.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), mixData.getName() + pos, Toast.LENGTH_SHORT).show();
            });
        } else if (holder instanceof StickHolder) {
            StickHolder stickHolder = (StickHolder) holder;
            stickHolder.tvStick.setText(mixData.getName());
            stickHolder.itemView.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), mixData.getName() + pos, Toast.LENGTH_SHORT).show();
            });
        } else if (holder instanceof SwipeHolder) {
            SwipeHolder swipeHolder = (SwipeHolder) holder;
            swipeHolder.tvContent.setText(mixData.getName());
            swipeHolder.tvRightMenu.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), "右" + pos, Toast.LENGTH_SHORT).show();
                swipeHolder.getSwipeItemLayout().close();
            });

            swipeHolder.tvContent.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), mixData.getName() + pos, Toast.LENGTH_SHORT).show();
            });
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

    static class NormalHolder extends RecyclerView.ViewHolder {

        private TextView tvContent;

        NormalHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

    static class StickHolder extends RecyclerView.ViewHolder implements IStick {

        private TextView tvStick;

        StickHolder(View itemView) {
            super(itemView);

            tvStick = itemView.findViewById(R.id.tv_stick);
        }
    }

    static class SwipeHolder extends JSwipeViewHolder {

        private TextView tvRightMenu;
        private TextView tvContent;

        SwipeHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getContentLayout() {
            return R.layout.swipe_content;
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.swipe_only_right;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {

            tvRightMenu = frameLayout.findViewById(R.id.tv_right_menu);
            tvContent = frameLayout.findViewById(R.id.tv_content);

        }
    }

}
