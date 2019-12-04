package com.zinc.jrecycleview.swipe;

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
import com.zinc.jrecycleview.data.SwipeData;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-08 14:39
 * email        : 56002982@qq.com
 * desc         : 测滑适配器
 * version      : 1.0.0
 */

public class SwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    static final int SWIPE_TYPE = 0x10001;
    static final int SWIPE_TYPE_ONLY_RIGHT = 0x10002;
    static final int SWIPE_TYPE_ONLY_LEFT = 0x10003;

    private final List<SwipeData> mSwipeData;

    private final LayoutInflater mLayoutInflater;

    private final WeakReference<Context> mContext;

    SwipeAdapter(Context context, List<SwipeData> data) {
        mSwipeData = data;
        mLayoutInflater = LayoutInflater.from(context);
        mContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                                      int viewType) {
        switch (viewType) {
            case SWIPE_TYPE:
                return new MyContentViewHolder(mLayoutInflater
                        .inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false));
            case SWIPE_TYPE_ONLY_RIGHT:
                return new OnlyRightViewHolder(mLayoutInflater
                        .inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false));
            case SWIPE_TYPE_ONLY_LEFT:
                return new OnlyLeftViewHolder(mLayoutInflater
                        .inflate(JRecycleConfig.SWIPE_LAYOUT, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        final SwipeData swipeData = this.mSwipeData.get(position);

        if (holder instanceof MyContentViewHolder) {

            final MyContentViewHolder myContentViewHolder = (MyContentViewHolder) holder;
            myContentViewHolder.tvRightMenu.setText("test Right");
            myContentViewHolder.tvRightMenu.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), "Right menu", Toast.LENGTH_SHORT).show();
                //关闭菜单
                myContentViewHolder.getSwipeItemLayout().close();
            });

            myContentViewHolder.tvRightMenuTwo.setText("test Right 2");
            myContentViewHolder.tvRightMenuTwo.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), "Right 2 menu", Toast.LENGTH_SHORT).show();
                //关闭菜单
                myContentViewHolder.getSwipeItemLayout().close();
            });

            myContentViewHolder.tvLeftMenu.setText("test Left");
            myContentViewHolder.tvLeftMenu.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), "Left menu", Toast.LENGTH_SHORT).show();
                //关闭菜单
                myContentViewHolder.getSwipeItemLayout().close();
            });

            myContentViewHolder.tvLeftMenuTwo.setText("test Left 2");
            myContentViewHolder.tvLeftMenuTwo.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), "Left 2 menu", Toast.LENGTH_SHORT).show();
                //关闭菜单
                myContentViewHolder.getSwipeItemLayout().close();
            });

            myContentViewHolder
                    .tvContent
                    .setText(this.mSwipeData.get(position).getContent());
            myContentViewHolder.tvContent.setOnClickListener(v ->
                    Toast.makeText(mContext.get(), swipeData.getContent(), Toast.LENGTH_SHORT).show());

        } else if (holder instanceof OnlyLeftViewHolder) {

            final OnlyLeftViewHolder onlyLeftViewHolder = (OnlyLeftViewHolder) holder;

            onlyLeftViewHolder.tvContent.setText(this.mSwipeData.get(position).getContent());

            onlyLeftViewHolder.tvLeftMenu.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), "点击了左菜单", Toast.LENGTH_SHORT).show();
                //关闭菜单
                onlyLeftViewHolder.getSwipeItemLayout().close();
            });

            onlyLeftViewHolder.tvContent.setOnClickListener(v ->
                    Toast.makeText(mContext.get(), swipeData.getContent(), Toast.LENGTH_SHORT).show());

        } else if (holder instanceof OnlyRightViewHolder) {

            final OnlyRightViewHolder onlyRightViewHolder = (OnlyRightViewHolder) holder;

            final int fPos = position;

            // 关闭侧滑
//            onlyRightViewHolder.getSwipeItemLayout().setSwipeEnable(false);

            onlyRightViewHolder.tvContent.setText(this.mSwipeData.get(position).getContent());

            onlyRightViewHolder.tvRightMenu.setOnClickListener(v -> {
                Toast.makeText(mContext.get(), "点击了右菜单，删除", Toast.LENGTH_SHORT).show();
                //关闭菜单
                this.mSwipeData.remove(fPos);
                onlyRightViewHolder.getSwipeItemLayout().close();
                notifyItemRemoved(fPos);
            });

            onlyRightViewHolder.tvContent.setOnClickListener(v ->
                    Toast.makeText(mContext.get(), swipeData.getContent(), Toast.LENGTH_SHORT).show());

        }
    }

    @Override
    public int getItemCount() {
        return mSwipeData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mSwipeData.get(position).getType();
    }

    class MyContentViewHolder extends JSwipeViewHolder {

        private TextView tvLeftMenu;
        private TextView tvRightMenu;
        private TextView tvContent;
        private TextView tvRightMenuTwo;
        private TextView tvLeftMenuTwo;

        MyContentViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getLeftMenuLayout() {
            return R.layout.swipe_left_menu;
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.swipe_right_menu;
        }

        @Override
        public int getContentLayout() {
            return R.layout.swipe_content;
        }

        @Override
        public void initLeftMenuItem(FrameLayout flLeftMenu) {
            tvLeftMenu = flLeftMenu.findViewById(R.id.tv_left_menu);
            tvLeftMenuTwo = flLeftMenu.findViewById(R.id.tv_left_menu_two);
        }

        @Override
        public void initRightMenuItem(FrameLayout flRightMenu) {
            tvRightMenu = flRightMenu.findViewById(R.id.tv_right_menu);
            tvRightMenuTwo = flRightMenu.findViewById(R.id.tv_right_menu_two);
        }

        @Override
        public void initContentItem(FrameLayout flContent) {
            tvContent = flContent.findViewById(R.id.tv_content);
        }

        @Override
        public void initItem(FrameLayout frameLayout) {

        }
    }

    class OnlyLeftViewHolder extends JSwipeViewHolder {

        private TextView tvLeftMenu;
        private TextView tvContent;

        OnlyLeftViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getLeftMenuLayout() {
            return R.layout.swipe_only_left;
        }

        @Override
        public int getContentLayout() {
            return R.layout.swipe_content;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            tvLeftMenu = frameLayout.findViewById(R.id.tv_left_menu);
            tvContent = frameLayout.findViewById(R.id.tv_content);
        }
    }

    class OnlyRightViewHolder extends JSwipeViewHolder {

        private TextView tvRightMenu;
        private TextView tvContent;

        OnlyRightViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public int getRightMenuLayout() {
            return R.layout.swipe_only_right;
        }

        @Override
        public int getContentLayout() {
            return R.layout.swipe_content;
        }

        @Override
        public void initItem(FrameLayout frameLayout) {
            tvRightMenu = frameLayout.findViewById(R.id.tv_right_menu);
            tvContent = frameLayout.findViewById(R.id.tv_content);
        }
    }
}
