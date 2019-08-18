package com.zinc.jrecycleview.swipe;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.data.SwipeData;
import com.zinc.jrecycleview.util.ToastUtil;

import java.util.List;

/**
 * author       : Jiang zinc
 * time         : 2018-04-08 14:39
 * email        : 56002982@qq.com
 * desc         :
 * version      : 1.0.0
 */

public class SwipeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int SWIPE_TYPE = 0x10001;
    public static final int SWIPE_TYPE_ONLY_RIGHT = 0x10002;
    public static final int SWIPE_TYPE_ONLY_LEFT = 0x10003;
    private List<SwipeData> swipeData;

    private LayoutInflater mLayoutInflater;

    public SwipeAdapter(Context context, List<SwipeData> swipeData) {
        this.swipeData = swipeData;
        this.mLayoutInflater = LayoutInflater.from(context);
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
        if (holder instanceof MyContentViewHolder) {
            final MyContentViewHolder myContentViewHolder = (MyContentViewHolder) holder;
            myContentViewHolder.tvRightMenu.setText("test Right");
            myContentViewHolder.tvRightMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Right menu");
                    //关闭菜单
                    myContentViewHolder.getSwipeItemLayout().close();
                }
            });

            myContentViewHolder.tvRightMenuTwo.setText("test Right 2");
            myContentViewHolder.tvRightMenuTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Right 2 menu");
                    //关闭菜单
                    myContentViewHolder.getSwipeItemLayout().close();
                }
            });

            myContentViewHolder.tvLeftMenu.setText("test Left");
            myContentViewHolder.tvLeftMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Left menu");
                    //关闭菜单
                    myContentViewHolder.getSwipeItemLayout().close();
                }
            });

            myContentViewHolder
                    .tvContent
                    .setText(swipeData.get(position).getContent());
            myContentViewHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Content");
                }
            });

            //防QQ小红点
//            if (swipeData != null) {
//                myContentViewHolder.point.setVisibility(swipeData.get(position).getReadNum() != 0 ? View.VISIBLE : View.INVISIBLE);
//                myContentViewHolder.point.setText(String.valueOf((swipeData.get(position).getReadNum())));
//                myContentViewHolder.point.setOnDragListener(new QQBezierView.onDragStatusListener() {
//                    @Override
//                    public void onDrag() {
//                    }
//
//                    @Override
//                    public void onMove() {
//
//                    }
//
//                    @Override
//                    public void onRestore() {
//
//                    }
//
//                    @Override
//                    public void onDismiss() {
//                        swipeData.get(position).setReadNum(0);
//                    }
//                });
//            }
        } else if (holder instanceof OnlyLeftViewHolder) {

            final OnlyLeftViewHolder onlyLeftViewHolder = (OnlyLeftViewHolder) holder;

            onlyLeftViewHolder.tvContent.setText(swipeData.get(position).getContent());

            onlyLeftViewHolder.tvLeftMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("点击了左菜单");
                    //关闭菜单
                    onlyLeftViewHolder.getSwipeItemLayout().close();
                }
            });

        } else if (holder instanceof OnlyRightViewHolder) {

            final OnlyRightViewHolder onlyRightViewHolder = (OnlyRightViewHolder) holder;

            final int fPos = position;

            onlyRightViewHolder.getSwipeItemLayout().setSwipeEnable(false);

            onlyRightViewHolder.tvContent.setText(swipeData.get(position).getContent());

            onlyRightViewHolder.tvRightMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("点击了右菜单");
                    //关闭菜单
                    swipeData.remove(fPos);
                    notifyDataSetChanged();
                    onlyRightViewHolder.getSwipeItemLayout().close();
                }
            });

            onlyRightViewHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Content");
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return swipeData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return swipeData.get(position).getType();
    }

    class MyContentViewHolder extends JSwipeViewHolder {

        private TextView tvLeftMenu;
        private TextView tvRightMenu;
        private TextView tvContent;
        private TextView tvRightMenuTwo;

//        private QQBezierView point;

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
