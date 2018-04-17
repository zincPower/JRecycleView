package com.zinc.jrecycleview.swipe;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zinc.jrecycleview.swipe.JSwipeItemLayout;
import com.zinc.librecycleview.R;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/8
 * @description 侧滑的ViewHolder
 */

public abstract class JSwipeViewHolder extends RecyclerView.ViewHolder {

    protected FrameLayout flLeftMenu;
    protected FrameLayout flContent;
    protected FrameLayout flRightMenu;

    public JSwipeItemLayout swipeItemLayout;

    public JSwipeViewHolder(View itemView) {
        super(itemView);

        swipeItemLayout = itemView.findViewById(R.id.swipe_item_layout);
        flLeftMenu = itemView.findViewById(R.id.fl_left_menu);
        flContent = itemView.findViewById(R.id.fl_content);
        flRightMenu = itemView.findViewById(R.id.fl_right_menu);

        flLeftMenu.removeAllViews();
        flRightMenu.removeAllViews();
        flContent.removeAllViews();

        LayoutInflater.from(itemView.getContext()).inflate(getLeftMenuLayout(), flLeftMenu, true);//null, false);
        LayoutInflater.from(itemView.getContext()).inflate(getContentLayout(), flContent, true);
        LayoutInflater.from(itemView.getContext()).inflate(getRightMenuLayout(), flRightMenu, true);

        initLeftMenuItem(flLeftMenu);
        initContentMenuItem(flContent);
        initRightMenuItem(flRightMenu);

    }

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 获取左菜单布局
     * @version
     *
     */
    public abstract int getLeftMenuLayout();

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 获取右菜单布局
     * @version
     *
     */
    public abstract int getRightMenuLayout();

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 获取内容布局
     * @version
     *
     */
    public abstract int getContentLayout();

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 初始化左菜单项
     * @version
     *
     */
    public abstract void initLeftMenuItem(FrameLayout flLeftMenu);

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 初始化右菜单项
     * @version
     *
     */
    public abstract void initRightMenuItem(FrameLayout flRightMenu);

    /**
     *
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 初始化内容项
     * @version
     *
     */
    public abstract void initContentMenuItem(FrameLayout flContent);

}
