package com.zinc.jrecycleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.loadview.base.IBaseWrapperView;
import com.zinc.jrecycleview.loadview.base.IBaseLoadMoreView;
import com.zinc.jrecycleview.loadview.base.IBasePullRefreshLoadView;
import com.zinc.jrecycleview.stick.IStick;
import com.zinc.jrecycleview.swipe.JSwipeItemLayout;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/3/17
 * @description 丰富的RecycleView:1、带下拉刷新和上拉加载更多；2、侧滑
 */

public class JRecycleView extends RecyclerView {

    private static final String TAG = JRecycleView.class.getSimpleName();
    //最后拖动Y的坐标
    private float mLastY = -1;

    private static final int DRAG_FACTOR = 2;

    public JRecycleView(Context context) {
        this(context, null, 0);
    }

    public JRecycleView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JRecycleView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        addOnScrollListener(new ScrollerListener());

        setOverScrollMode(OVER_SCROLL_NEVER);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {

        int action = e.getActionMasked();

        if (action == MotionEvent.ACTION_DOWN) {
            int x = (int) e.getX();
            int y = (int) e.getY();

            //获取已经侧滑的item
            View openItemView = findOpenItem();

            //判断是否有侧滑 且 当前点击区域不在该侧滑的项中
            if (openItemView != null && openItemView != getTouchItem(x, y)) {
                JSwipeItemLayout swipeItemLayout = findSwipeItemLayout(openItemView);
                if (swipeItemLayout != null) {
                    //关闭侧滑
                    swipeItemLayout.close();
                    //不拦截此次事件，此次事件只帮我们关闭菜单
                    return false;
                }
            }
        } else if (action == MotionEvent.ACTION_POINTER_DOWN) {
            return false;
        }

        return super.dispatchTouchEvent(e);
    }

    private boolean mIsTouching = false;
    private boolean mIsAction = false;

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (getRefreshLoadView() == null) {
            return super.onTouchEvent(e);
        }

        if (this.mLastY == -1) {
            this.mLastY = e.getRawY();
        }

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                this.mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                this.mIsTouching = true;

                if (isScrolledTop()) {
                    float deltaY = e.getRawY() - mLastY;

                    this.getRefreshLoadView().onMove(deltaY / DRAG_FACTOR);

                    mLastY = e.getRawY();

                    //当refresh视图出现 且 当前状态为"下拉刷新"或"释放刷新"时，
                    // 需要RecycleView不捕获该事件，否则会有问题
                    if (this.getRefreshLoadView().getVisibleHeight() > 0 &&
                            this.getRefreshLoadView().getCurState() < IBaseWrapperView.STATE_EXECUTING) {
                        return false;
                    }
                }

                if (isScrolledBottom()) {
                    float deltaY = mLastY - e.getRawY();

                    if (deltaY > 0) {   //向上滑动
                        this.getLoadMoreView().onMove(deltaY / DRAG_FACTOR);
                    } else {            //向下滑动
                        this.getLoadMoreView().onMove(deltaY);
                    }
                    mLastY = e.getRawY();

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                this.mLastY = -1;

                if (this.getRefreshLoadView() != null
                        && this.getRefreshLoadView().releaseAction()) {
//                    isInAction = true;
                    if (this.getRefreshLoadView().getOnRefreshListener() != null) {
                        this.getRefreshLoadView().getOnRefreshListener().onRefreshing();
                    }
                }

                if (this.getLoadMoreView() != null
                        && this.getLoadMoreView().releaseAction()) {
//                    isInAction = true;
                    if (this.getLoadMoreView().getOnLoadMoreListener() != null) {
                        this.getLoadMoreView().getOnLoadMoreListener().onLoading();
                    }
                }

                Log.i(TAG, "onTouchEvent: up");
                this.mIsTouching = false;

                if (isScrolling) {

                    boolean isInAction = false;

                    if (this.getRefreshLoadView() != null
                            && this.getRefreshLoadView().releaseAction()) {
                        isInAction = true;
                    }

                    if (this.getLoadMoreView() != null
                            && this.getLoadMoreView().releaseAction()) {
                        isInAction = true;
                    }
                    // 没有处于 刷新 或 加载更多 的状态
                    if (isInAction) {
                        return false;
                    }

                    View theFirstView = getChildAt(0);

                    ViewHolder childViewHolder = getChildViewHolder(theFirstView);

                    if (childViewHolder instanceof IStick) {

                        float y = theFirstView.getY();
                        int height = theFirstView.getHeight();
//                        Log.i(TAG, "theFirstView [offset: " + y + "; height: " + height + "]");

                        boolean isShowAll = Math.abs(y) > (height / 2);
                        float offset = isShowAll ? height + y : y;

                        smoothScrollBy(0, (int) offset);

                        return true;
                    }
                }

                break;
        }

        return super.onTouchEvent(e);
    }

    private boolean isScrollStick() {

        boolean isInAction = false;

        if (this.getRefreshLoadView() != null
                && this.getRefreshLoadView().releaseAction()) {
            isInAction = true;
        }

        if (this.getLoadMoreView() != null
                && this.getLoadMoreView().releaseAction()) {
            isInAction = true;
        }

        // 没有处于 刷新 或 加载更多 的状态
        if (isInAction) {
            return false;
        }

        View theFirstView = getChildAt(0);

        ViewHolder childViewHolder = getChildViewHolder(theFirstView);

        if (childViewHolder instanceof IStick) {

            float y = theFirstView.getY();
            int height = theFirstView.getHeight();
//            Log.i(TAG, "theFirstView [offset: " + y + "; height: " + height + "]");

            boolean isShowAll = Math.abs(y) > (height / 2);
            float offset = isShowAll ? height + y : y;

            smoothScrollBy(0, (int) offset);

            return true;
        }

        return false;
    }

    //========================下拉刷新更多 start==============================

    /**
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 是否滚至最顶端
     * @version
     */
    private boolean isScrolledTop() {
        // 20181126 修复
        // 修复原因如下：findFirstCompletelyVisibleItemPosition方法只有当整个view显示时才会返回该item的下标，
        // 当item过大时，无法在一个界面显示时，会返回-1，具体可看：
        // https://stackoverflow.com/questions/37363901/layoutmanager-findfirstcompletelyvisibleitemposition-always-returns-1
//        if (getLayoutManager() instanceof LinearLayoutManager &&
//                ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() <= 1) {

        if (getChildCount() > 1 &&
                getChildAt(0) instanceof IBasePullRefreshLoadView &&
                ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() <= 1 &&
                getChildAt(1).getY() >= 0) {
            return true;
        } else {
            return false;
        }
    }

    private IBasePullRefreshLoadView getRefreshLoadView() {
        if (getAdapter() instanceof JRefreshAndLoadMoreAdapter) {
            JRefreshAndLoadMoreAdapter jAdapter = (JRefreshAndLoadMoreAdapter) getAdapter();
            return jAdapter.getRefreshLoadView();
        }
        return null;
    }

    //========================下拉刷新更多 end  ==============================

    //========================上拉加载更多 start==============================

    /**
     * @date 创建时间 2018/4/16
     * @author Jiang zinc
     * @Description 是否是最后一个view
     * @version
     */
    private boolean isScrolledBottom() {

        //若正在下拉刷新状态则 item个数-2
        //若不是正在下拉刷新状态则 item个数-3
        int itemTotal = getRefreshLoadView().getCurState() == IBaseWrapperView.STATE_EXECUTING ? getAdapter().getItemCount() - 2 : getAdapter().getItemCount() - 3;

        if (getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) getLayoutManager()).findLastCompletelyVisibleItemPosition() >= itemTotal) {
            return true;
        } else {
            return false;
        }
    }

    private IBaseLoadMoreView getLoadMoreView() {
        if (getAdapter() instanceof JRefreshAndLoadMoreAdapter) {
            JRefreshAndLoadMoreAdapter jAdapter = (JRefreshAndLoadMoreAdapter) getAdapter();
            return jAdapter.getLoadMoreView();
        }
        return null;
    }

    /**
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 刷新结束
     * @version
     */
//    public void setLoadMoreComplete() {
//        if (this.getLoadMoreView() != null) {
//            this.getLoadMoreView().refreshComplete();
//        }
//    }
    //========================上拉加载更多 end  ==============================

    //========================侧滑效果分割线 start==============================

    //查找已经开启侧滑的项
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

    //获取view中的JSwipeItemLayout
    //有可能view就是JSwipeItemLayout，也有可能是view下的子view
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

    //获取当前点击的坐标的item
    @Nullable
    private View getTouchItem(int x, int y) {
        Rect frame = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //是可见的
            if (child.getVisibility() == VISIBLE) {
                //获取范围
                child.getHitRect(frame);
                //判断点击点是否已经在其范围内
                if (frame.contains(x, y)) {
                    return child;
                }
            }
        }
        return null;
    }

    //========================侧滑效果分割线 end================================

    private boolean isScrolling = false;

    private class ScrollerListener extends OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {
                case SCROLL_STATE_IDLE:
                    isScrolling = false;
                    break;
                case SCROLL_STATE_DRAGGING:
                    isScrolling = true;
                    break;
                case SCROLL_STATE_SETTLING:
                    isScrolling = true;
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mIsTouching) {
                return;
            }
            Log.i(TAG, "onScrolled: " + mIsTouching);
            isScrollStick();
        }

    }

}
