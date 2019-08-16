package com.zinc.jrecycleview.loadview.bean;

/**
 * author       : Jiang zinc
 * time         : 2018-04-17 16:07
 * email        : 56002982@qq.com
 * desc         : 移动信息
 * version      : 1.0.0
 */

public class MoveInfo {

    // 视图的高度
    private int viewHeight;
    // 下拉高度
    private int dragHeight;
    // 百分比(最大100，最小0)
    private int percent;

    public MoveInfo() {
    }

    public int getViewHeight() {
        return viewHeight;
    }

    public void setViewHeight(int viewHeight) {
        this.viewHeight = viewHeight;
    }

    public int getDragHeight() {
        return dragHeight;
    }

    public void setDragHeight(int dragHeight) {
        this.dragHeight = dragHeight;
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        this.percent = percent;
    }

    @Override
    public String toString() {
        return "MoveInfo{" +
                "viewHeight=" + viewHeight +
                ", dragHeight=" + dragHeight +
                ", percent=" + percent +
                '}';
    }
}
