package com.zinc.jrecycleview.swipe;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zinc.librecycleview.R;

/**
 * author       : Jiang zinc
 * time         : 2018-04-08 14:37
 * email        : 56002982@qq.com
 * desc         : 侧滑的ViewHolder
 * version      : 1.0.0
 */

public abstract class JSwipeViewHolder extends RecyclerView.ViewHolder {

    private static final int NONE = -1;

    private FrameLayout flLeftMenu;
    private FrameLayout flContent;
    private FrameLayout flRightMenu;

    private JSwipeItemLayout swipeItemLayout;

    public JSwipeViewHolder(View itemView) {
        super(itemView);

        swipeItemLayout = itemView.findViewById(R.id.swipe_item_layout);
        flLeftMenu = itemView.findViewById(R.id.fl_left_menu);
        flContent = itemView.findViewById(R.id.fl_content);
        flRightMenu = itemView.findViewById(R.id.fl_right_menu);

        flLeftMenu.removeAllViews();
        flRightMenu.removeAllViews();
        flContent.removeAllViews();

        if (getLeftMenuLayout() != NONE) {
            LayoutInflater
                    .from(itemView.getContext())
                    .inflate(getLeftMenuLayout(), flLeftMenu, true);

            initLeftMenuItem(flLeftMenu);
            swipeItemLayout
                    .getMenus()
                    .put(JSwipeConstant.LEFT, flLeftMenu);
        }

        if (getRightMenuLayout() != NONE) {
            LayoutInflater
                    .from(itemView.getContext())
                    .inflate(getRightMenuLayout(), flRightMenu, true);

            initRightMenuItem(flRightMenu);
            swipeItemLayout
                    .getMenus()
                    .put(JSwipeConstant.RIGHT, flRightMenu);
        }

        LayoutInflater.from(itemView.getContext())
                .inflate(getContentLayout(), flContent, true);
        initContentItem(flContent);

        initItem(swipeItemLayout);

    }

    public JSwipeItemLayout getSwipeItemLayout() {
        return swipeItemLayout;
    }

    /**
     * 获取左菜单布局
     *
     * @return 左菜单的 xml
     */
    public int getLeftMenuLayout() {
        return NONE;
    }

    /**
     * 获取右菜单布局
     *
     * @return 右菜单的 xml
     */
    public int getRightMenuLayout() {
        return NONE;
    }

    /**
     * 获取内容布局
     *
     * @return 内容的 xml
     */
    public abstract int getContentLayout();

    /**
     * 初始化左菜单项
     *
     * @param flLeftMenu 左菜单视图
     */
    public void initLeftMenuItem(FrameLayout flLeftMenu) {

    }

    /**
     * 初始化右菜单项
     *
     * @param flRightMenu 右菜单视图
     */
    public void initRightMenuItem(FrameLayout flRightMenu) {

    }

    /**
     * 初始化内容项
     *
     * @param flContent 内容视图
     */
    public void initContentItem(FrameLayout flContent) {
    }

    /**
     * 初始化视图
     *
     * @param frameLayout 包含[左菜单视图，内容视图，右菜单视图]
     */
    public abstract void initItem(FrameLayout frameLayout);

}
