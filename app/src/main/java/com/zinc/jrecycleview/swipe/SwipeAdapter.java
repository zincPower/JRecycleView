package com.zinc.jrecycleview.swipe;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zinc.jrecycleview.R;
import com.zinc.jrecycleview.adapter.JBaseRecycleAdapter;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.data.SwipeData;
import com.zinc.jrecycleview.util.ToastUtil;
import com.zinc.jrecycleview.widget.QQBezierView;

import java.util.List;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/8
 * @description
 */

public class SwipeAdapter extends JBaseRecycleAdapter<RecyclerView.ViewHolder> {//RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int OTHER_TYPE = 0x10002;
    private List<SwipeData> swipeData;

    private LayoutInflater mLayoutInflater;

    public SwipeAdapter(Context context, List<SwipeData> swipeData) {
        this.swipeData = swipeData;
        this.mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case JRecycleConfig.SWIPE_TYPE:
                return new MyContentViewHolder(mLayoutInflater.inflate(JRecycleConfig.SWIPE_LAYOUT, parent,false));
            case OTHER_TYPE:
                return null;
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof MyContentViewHolder){
            final MyContentViewHolder myContentViewHolder = (MyContentViewHolder) holder;
            myContentViewHolder.tvRightMenu.setText("test Right");
            myContentViewHolder.tvRightMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Right menu");
                    myContentViewHolder.swipeItemLayout.close();
                }
            });

            myContentViewHolder.tvRightMenuTwo.setText("test Right 2");
            myContentViewHolder.tvRightMenuTwo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Right 2 menu");
                    myContentViewHolder.swipeItemLayout.close();
                }
            });

            myContentViewHolder.tvLeftMenu.setText("test Left");
            myContentViewHolder.tvLeftMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Left menu");
                    myContentViewHolder.swipeItemLayout.close();
                }
            });

            myContentViewHolder.tvContent.setText(swipeData.get(position).getContent());
            myContentViewHolder.tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.show("Content");
                }
            });

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

        public MyContentViewHolder(View itemView) {
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
        public void initContentMenuItem(FrameLayout flContent) {
            tvContent = flContent.findViewById(R.id.tv_content);
//            point = flContent.findViewById(R.id.qq_point);
        }
    }
}
