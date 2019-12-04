package com.zinc.jrecycleview.swipe;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.customview.widget.ViewDragHelper;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-07 13:57
 * email        : 56002982@qq.com
 * desc         : 侧滑效果
 * version      : 1.0.0
 */

public class JSwipeItemLayout extends FrameLayout {

    public String TAG = this.getClass().getSimpleName();

    private ViewDragHelper mDragHelper;

    private int mTouchSlop;
    private int mVelocity;

    // 按下的横竖坐标
    private float mDownX;
    private float mDownY;

    // 是否已经拖拽
    private boolean mIsDragged;
    // 是否可以侧滑
    private boolean mSwipeEnable = true;
    // 触碰范围
    private final Rect mTouchRect;

    /**
     * 通过判断手势进行赋值 {@link #checkCanDragged(MotionEvent)}
     */
    private View mCurrentMenu;

    /**
     * 某些情况下，不能通过mIsOpen判断当前菜单是否开启或是关闭。
     * 因为在调用 {@link #open()} 或者 {@link #close()} 的时候，mIsOpen的值已经被改变，但是
     * 此时ContentView还没有到达应该的位置。亦或者ContentView已经到拖拽达指定位置，但是此时并没有
     * 松开手指，mIsOpen并不会重新赋值。
     */
    private boolean mIsOpen;

    /**
     * Menu的集合
     * 以 {@link android.view.Gravity#LEFT} 和 {@link android.view.Gravity#LEFT} 作为key，
     * 菜单View作为value保存。
     */
    private LinkedHashMap<Integer, View> mMenus = new LinkedHashMap<>();

    private List<SwipeListener> mListeners;

    public JSwipeItemLayout(Context context) {
        this(context, null);
    }

    public JSwipeItemLayout(Context context,
                            AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JSwipeItemLayout(Context context,
                            AttributeSet attrs,
                            int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // getScaledTouchSlop是一个距离，表示滑动的时候，手的移动要大于这个距离才开始移动控件。
        // 如果小于这个距离就不触发移动控件
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        // 获得允许执行一个fling手势动作的最小速度值
        mVelocity = ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        mDragHelper = ViewDragHelper.create(this, new DragCallBack());
        mTouchRect = new Rect();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateMenu();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            // 关闭菜单过程中禁止接收down事件
            if (isCloseAnimating()) {
                return false;
            }

            // 菜单打开的时候，按下Content关闭菜单
            if (mIsOpen && isTouchContent(((int) ev.getX()), ((int) ev.getY()))) {
                close();
                return false;
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!mSwipeEnable) {
            return false;
        }

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsDragged = false;
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                checkCanDragged(ev);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsDragged) {
                    mDragHelper.processTouchEvent(ev);
                    mIsDragged = false;
                }
                break;
            default:
                if (mIsDragged) {
                    mDragHelper.processTouchEvent(ev);
                }
                break;
        }
        return mIsDragged || super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!mSwipeEnable) {
            return super.onTouchEvent(ev);
        }

        int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mIsDragged = false;
                mDownX = ev.getX();
                mDownY = ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                boolean beforeCheckDrag = mIsDragged;
                checkCanDragged(ev);
                if (mIsDragged) {
                    mDragHelper.processTouchEvent(ev);
                }

                // 开始拖动后，发送一个cancel事件用来取消点击效果，不然交互会比较差
                if (!beforeCheckDrag && mIsDragged) {
                    MotionEvent obtain = MotionEvent.obtain(ev);
                    obtain.setAction(MotionEvent.ACTION_CANCEL);
                    super.onTouchEvent(obtain);
                }

                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mIsDragged || mIsOpen) {
                    mDragHelper.processTouchEvent(ev);
                    // 拖拽后手指抬起，或者已经开启菜单，不应该响应到点击事件
                    ev.setAction(MotionEvent.ACTION_CANCEL);
                    mIsDragged = false;
                }
                break;
            default:
                if (mIsDragged) {
                    mDragHelper.processTouchEvent(ev);
                }
                break;
        }
        return mIsDragged || super.onTouchEvent(ev)
                // 当 ContentView 没有设置点击事件时，事件会给 RecycleView 响应导致无法划开菜单
                || (!isClickable() && mMenus.size() > 0);
    }

    /**
     * 侧滑的ViewHolder
     */
    private void checkCanDragged(MotionEvent ev) {
        // 确保第一次能进入
        if (mIsDragged) {
            return;
        }

        float dx = ev.getX() - mDownX;
        float dy = ev.getY() - mDownY;
        boolean isRightDrag = dx > mTouchSlop && Math.abs(dx) > Math.abs(dy);
        boolean isLeftDrag = dx < -mTouchSlop && Math.abs(dx) > Math.abs(dy);

        if (mIsOpen) {
            // 开启状态下，点击在content上就捕获事件，点击在菜单上则判断touchSlop
            int downX = (int) mDownX;
            int downY = (int) mDownY;
            if (isTouchContent(downX, downY)) {
                mIsDragged = true;
            } else if (isTouchMenu(downX, downY)) {
                mIsDragged = (isLeftMenu() && isLeftDrag) || (isRightMenu() && isRightDrag);
            }

        } else {
            // 关闭状态，获取当前即将要开启的菜单。
            if (isRightDrag) {
                mCurrentMenu = mMenus.get(JSwipeConstant.LEFT);
                mIsDragged = mCurrentMenu != null;
            } else if (isLeftDrag) {
                mCurrentMenu = mMenus.get(JSwipeConstant.RIGHT);
                mIsDragged = mCurrentMenu != null;
            }
        }

        if (mIsDragged) {
            // 开始拖动后，分发down事件给DragHelper
            MotionEvent obtain = MotionEvent.obtain(ev);
            obtain.setAction(MotionEvent.ACTION_DOWN);
            mDragHelper.processTouchEvent(obtain);
            if (getParent() != null) {
                // 解决和父控件的滑动冲突。
                getParent().requestDisallowInterceptTouchEvent(true);
            }
        }
    }

    public LinkedHashMap<Integer, View> getMenus() {
        return mMenus;
    }

    public void setSwipeEnable(boolean enable) {
        mSwipeEnable = enable;
    }

    public boolean isSwipeEnable() {
        return mSwipeEnable;
    }

    /**
     * 获取ContentView
     * 最后一个view，这个是规定（按照layout布局，具体看 {@link layout/j_swipe_wrapper.xml}）
     *
     * @return 内容的view
     */
    public View getContentView() {
        return getChildAt(getChildCount() - 1);
    }

    /**
     * 是否为左菜单
     */
    private boolean isLeftMenu() {
        return mCurrentMenu != null && mCurrentMenu == mMenus.get(JSwipeConstant.LEFT);
    }

    /**
     * 是否为右菜单
     */
    private boolean isRightMenu() {
        return mCurrentMenu != null && mCurrentMenu == mMenus.get(JSwipeConstant.RIGHT);
    }

    /**
     * 是否 触碰当前的 menu
     *
     * @param x 触碰的x坐标
     * @param y 触碰的y坐标
     * @return true：触碰的是当前的menu项；false：触碰的不是当前的menu项
     */
    public boolean isTouchMenu(int x, int y) {
        if (mCurrentMenu == null) {
            return false;
        }

        mCurrentMenu.getHitRect(mTouchRect);
        return mTouchRect.contains(x, y);
    }

    /**
     * 判断 down 是否点击在 Content 上
     *
     * @param x 触碰的x坐标
     * @param y 触碰的y坐标
     * @return true：触碰在content上
     */
    public boolean isTouchContent(int x, int y) {
        View contentView = getContentView();
        if (contentView == null) {
            return false;
        }
        contentView.getHitRect(mTouchRect);
        return mTouchRect.contains(x, y);
    }

    /**
     * 关闭菜单
     */
    public void close() {
        if (mCurrentMenu == null) {
            mIsOpen = false;
            return;
        }

        mDragHelper.smoothSlideViewTo(getContentView(), getPaddingLeft(), getPaddingTop());

        mIsOpen = false;
        if (mListeners != null) {
            int listenerCount = mListeners.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                mListeners.get(i).onSwipeClose(this);
            }
        }
        invalidate();
    }

    /**
     * 开启菜单
     */
    public void open() {
        if (mCurrentMenu == null) {
            mIsOpen = false;
            return;
        }

        if (isLeftMenu()) {
            mDragHelper.smoothSlideViewTo(getContentView(), mCurrentMenu.getWidth(), getPaddingTop());
        } else if (isRightMenu()) {
            mDragHelper.smoothSlideViewTo(getContentView(), -mCurrentMenu.getWidth(), getPaddingTop());
        }

        mIsOpen = true;

        if (mListeners != null) {
            int listenerCount = mListeners.size();
            for (int i = listenerCount - 1; i >= 0; i--) {
                mListeners.get(i).onSwipeOpen(this);
            }
        }

        invalidate();
    }

    /**
     * 菜单是否开始拖动
     */
    public boolean isOpen() {
        return mIsOpen;
    }

    /**
     * 是否正在做开启动画
     */
    private boolean isOpenAnimating() {
        if (mCurrentMenu != null) {
            int contentLeft = getContentView().getLeft();
            int menuWidth = mCurrentMenu.getWidth();
            if (mIsOpen && ((isLeftMenu() && contentLeft < menuWidth)
                    || (isRightMenu() && -contentLeft < menuWidth))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 是否正在做关闭动画
     */
    private boolean isCloseAnimating() {
        if (mCurrentMenu != null) {
            int contentLeft = getContentView().getLeft();
            //当开启 （左菜单或右菜单）且 未打开 ===》说明正在关闭动画【只要触发关闭，mIsOpen {@link #close} 】
            if (!mIsOpen && ((isLeftMenu() && contentLeft > 0) || (isRightMenu() && contentLeft < 0))) {
                return true;
            }
        }
        return false;
    }

    /**
     * 当菜单被ContentView遮住的时候，要设置菜单为Invisible，防止已隐藏的菜单接收到点击事件。
     */
    private void updateMenu() {

        View contentView = getContentView();

        if (contentView != null) {

            int contentLeft = contentView.getLeft();

            if (contentLeft == 0) { //当内容view占据主场时，隐藏菜单
                for (View view : mMenus.values()) {
                    view.setVisibility(INVISIBLE);
                }
            } else if (mCurrentMenu != null) {  //当有侧滑时，显示侧滑出来的view
                mCurrentMenu.setVisibility(VISIBLE);
            }

        }
    }

    /**
     * 添加一个监听器用于监听SwipeItemLayout的开启和关闭
     *
     * @param listener SwipeListener
     */
    public void addSwipeListener(SwipeListener listener) {
        if (listener == null) {
            return;
        }

        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(listener);
    }

    /**
     * 移除监听器
     */
    public void removeSwipeListener(SwipeListener listener) {
        if (listener == null) {
            return;
        }

        if (mListeners == null) {
            return;
        }

        mListeners.remove(listener);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (mDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    private class DragCallBack extends ViewDragHelper.Callback {

        /**
         * 需要捕获的视图
         */
        @Override
        public boolean tryCaptureView(@NonNull View child,
                                      int pointerId) {
            // menu和content都可以抓取，因为在menu的宽度为MatchParent的时候，是无法点击到content的
            return child == getContentView() || mMenus.containsValue(child);
        }

        //控制水平活动，默认是不可动
        @Override
        public int clampViewPositionHorizontal(@NonNull View child,
                                               int left,
                                               int dx) {

            // 如果child是内容， 那么可以左划或右划，开启或关闭菜单
            if (child == getContentView()) {
                if (isRightMenu()) {
                    return left > 0 ? 0 : left < -mCurrentMenu.getWidth() ? -mCurrentMenu.getWidth() : left;
                } else if (isLeftMenu()) {
                    return left > mCurrentMenu.getWidth() ? mCurrentMenu.getWidth() : left < 0 ? 0 : left;
                }
            }

            // 如果抓取到的child是菜单，那么不移动child，而是移动contentView
            else if (isRightMenu()) {
                View contentView = getContentView();
                int newLeft = contentView.getLeft() + dx;
                if (newLeft > 0) {
                    newLeft = 0;
                } else if (newLeft < -child.getWidth()) {
                    newLeft = -child.getWidth();
                }
                contentView.layout(newLeft, contentView.getTop(), newLeft + contentView.getWidth(),
                        contentView.getBottom());
                return child.getLeft();
            } else if (isLeftMenu()) {
                View contentView = getContentView();
                int newLeft = contentView.getLeft() + dx;
                if (newLeft < 0) {
                    newLeft = 0;
                } else if (newLeft > child.getWidth()) {
                    newLeft = child.getWidth();
                }
                contentView.layout(newLeft, contentView.getTop(), newLeft + contentView.getWidth(),
                        contentView.getBottom());
                return child.getLeft();
            }
            return 0;
        }

        @Override
        public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
            super.onViewPositionChanged(changedView, left, top, dx, dy);
            updateMenu();
        }

        /**
         * 手指释放的时候会触发
         */
        @Override
        public void onViewReleased(@NonNull View releasedChild,
                                   float xVel,
                                   float yVel) {
            if (isLeftMenu()) {
                if (xVel > mVelocity) {
                    open();
                } else if (xVel < -mVelocity) {
                    close();
                } else {
                    if (getContentView().getLeft() > mCurrentMenu.getWidth() / 3 * 2) {
                        open();
                    } else {
                        close();
                    }
                }
            } else if (isRightMenu()) { //右菜单
                if (xVel < -mVelocity) {
                    open();
                } else if (xVel > mVelocity) {
                    close();
                } else {
                    //如果小于最小速度，需要大于视图的2／3才能打开
                    if (getContentView().getLeft() < -mCurrentMenu.getWidth() / 3 * 2) {
                        open();
                    } else {
                        close();
                    }
                }
            }
        }

    }

    public interface SwipeListener {
        void onSwipeOpen(JSwipeItemLayout view);

        void onSwipeClose(JSwipeItemLayout view);
    }
}
