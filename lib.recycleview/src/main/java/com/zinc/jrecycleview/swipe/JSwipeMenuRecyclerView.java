package com.zinc.jrecycleview.swipe;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

public class JSwipeMenuRecyclerView extends RecyclerView {

    public JSwipeMenuRecyclerView(Context context) {
        super(context);
    }

    public JSwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public JSwipeMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        // 手指按下的时候，如果有开启的菜单，只要手指不是落在该Item上，则关闭菜单, 并且不分发事件。
        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            View openItem = findOpenItem();
            if (openItem != null && openItem != getTouchItem(x, y)) {
                JSwipeItemLayout JSwipeItemLayout = findSwipeItemLayout(openItem);
                if (JSwipeItemLayout != null) {
                    JSwipeItemLayout.close();
                    return false;
                }
            }
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            //禁止多点触控
            return false;
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获取按下位置的Item
     */
    @Nullable
    private View getTouchItem(int x, int y) {
        Rect frame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == VISIBLE) {
                child.getHitRect(frame);
                if (frame.contains(x, y)) {
                    return child;
                }
            }
        }
        return null;
    }

    //查找
    @Nullable
    private View findOpenItem() {
        //获取
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            JSwipeItemLayout jswipeItemLayout = findSwipeItemLayout(getChildAt(i));
            if (jswipeItemLayout != null && jswipeItemLayout.isOpen()) {
                return getChildAt(i);
            }
        }
        return null;
    }

    /**
     * 获取该View
     */
    @Nullable
    private JSwipeItemLayout findSwipeItemLayout(View view) {
        if (view instanceof JSwipeItemLayout) {
            return (JSwipeItemLayout) view;
        } else if (view instanceof ViewGroup) {
            ViewGroup group = (ViewGroup) view;
            int count = group.getChildCount();
            for (int i = 0; i < count; i++) {
                JSwipeItemLayout swipeLayout = findSwipeItemLayout(group.getChildAt(i));
                if (swipeLayout != null) {
                    return swipeLayout;
                }
            }
        }
        return null;
    }

}
