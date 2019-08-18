package com.zinc.jrecycleview;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.jrecycleview.adapter.JRefreshAndLoadMoreAdapter;
import com.zinc.jrecycleview.loadview.base.IBaseWrapperView;
import com.zinc.jrecycleview.loadview.base.IBaseLoadMoreView;
import com.zinc.jrecycleview.loadview.base.IBaseRefreshLoadView;
import com.zinc.jrecycleview.stick.IStick;
import com.zinc.jrecycleview.swipe.JSwipeItemLayout;
import com.zinc.jrecycleview.utils.LogUtils;

/**
 * author       : Jiang zinc
 * time         : 2018-03-17
 * email        : 56002982@qq.com
 * desc         : 丰富的RecycleView:
 * 1、带下拉刷新和上拉加载更多；
 * 2、侧滑
 * 3、带粘性（需要粘性的 item 的 ViewHolder 需要实现 {@link IStick}）
 * version      : 1.0.0
 */
public class JRecycleView extends RecyclerView {

    private static final String TAG = JRecycleView.class.getSimpleName();
    // 最后拖动Y的坐标
    private float mLastY = -1;

    private static final float DRAG_FACTOR = 2f;

    // 刷新视图的下标
    private int mRefreshViewPos = 0;

    // 用于获取 item 范围
    private final Rect mFrame;
    // 是否正在触碰
    private boolean mIsTouching = false;
    // 是否正在滚动
    private boolean isScrolling = false;

    public JRecycleView(Context context) {
        this(context, null, 0);
    }

    public JRecycleView(Context context,
                        @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JRecycleView(Context context,
                        @Nullable AttributeSet attrs,
                        int defStyle) {
        super(context, attrs, defStyle);

        addOnScrollListener(new ScrollerListener());
        setOverScrollMode(OVER_SCROLL_NEVER);

        mFrame = new Rect();
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

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (mLastY == -1) {
            mLastY = e.getRawY();
        }

        switch (e.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                mIsTouching = true;

                if (getRefreshLoadView() != null && isScrolledTop()) {
                    float deltaY = e.getRawY() - mLastY;

                    LogUtils.i(TAG, "refreshLoadView: [rawY: " + e.getRawY() + "; " +
                            "lastY: " + mLastY + "; " +
                            "deltaY: " + deltaY + "]");

                    int visibleHeight = getRefreshVisibleHeight();
                    if (visibleHeight != -1) {
                        getRefreshLoadView().onMove(visibleHeight, deltaY / DRAG_FACTOR);
                    }

                    mLastY = e.getRawY();

                    //当refresh视图出现 且 当前状态为"下拉刷新"或"释放刷新"时，
                    // 需要RecycleView不捕获该事件，否则会有问题
                    if (visibleHeight > 0 &&
                            getRefreshLoadView().getCurState() < IBaseWrapperView.STATE_EXECUTING) {
                        return false;
                    }
                }

                if (getLoadMoreView() != null && isScrolledBottom()) {
                    float deltaY = mLastY - e.getRawY();

                    int visibleHeight = getLoadMoreVisibleHeight();
                    if (visibleHeight != -1) {
                        if (deltaY > 0) {   //向上滑动
                            getLoadMoreView().onMove(visibleHeight, deltaY / DRAG_FACTOR);
                        } else {            //向下滑动
                            getLoadMoreView().onMove(visibleHeight, deltaY / DRAG_FACTOR);
                        }
                    }

                    mLastY = e.getRawY();

                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mLastY = -1;

                handleRefreshLoad();
                handleLoadMore();

                mIsTouching = false;

                if (isScrolling) {
                    if (isScrollStick()) {
                        return true;
                    }
                }

                break;
        }

        return super.onTouchEvent(e);
    }

    /**
     * 处理下拉刷新的逻辑
     *
     * @return true 说明进行处理；false 不进行处理
     */
    private boolean handleRefreshLoad() {

        if (getRefreshLoadView() == null) {
            return false;
        }

        int visibleHeight = getRefreshVisibleHeight();

        if (visibleHeight == -1) {
            return false;
        }

        if (getRefreshLoadView().releaseAction(visibleHeight)) {
            if (getRefreshLoadView().getOnRefreshListener() != null) {
                getRefreshLoadView().getOnRefreshListener().onRefreshing();
            }

            return true;
        }

        return false;
    }

    /**
     * 处理加载更多的逻辑
     *
     * @return true 说明进行处理；false 不进行处理
     */
    private boolean handleLoadMore() {

        if (getLoadMoreView() == null) {
            return false;
        }

        int visibleHeight = getLoadMoreVisibleHeight();

        if (visibleHeight == -1) {
            return false;
        }

        if (getLoadMoreView().releaseAction(visibleHeight)) {
            if (getLoadMoreView().getOnLoadMoreListener() != null) {
                getLoadMoreView().getOnLoadMoreListener().onLoading();
            }

            return true;
        }

        return false;
    }

    /**
     * 获取下拉刷新视图 高度
     *
     * @return -1 说明有问题
     */
    private int getRefreshVisibleHeight() {
        if (getRefreshLoadView() == null) {
            return -1;
        }

        int childCount = getLayoutManager().getChildCount();
        if (childCount <= 0) {
            return -1;
        }

        View view = getLayoutManager().getChildAt(mRefreshViewPos);
        if (view == null) {
            return -1;
        }

        if (view != getRefreshLoadView()) {
            return -1;
        }

        int top = view.getTop();
        int bottom = view.getBottom();

        return bottom - top;
    }

    /**
     * 获取加载更多视图 高度
     *
     * @return -1 说明有问题
     */
    private int getLoadMoreVisibleHeight() {
        if (getLoadMoreView() == null) {
            return -1;
        }

        int childCount = getLayoutManager().getChildCount();
        if (childCount <= 0) {
            return -1;
        }

        View view = getLayoutManager().getChildAt(childCount - 1);
        if (view == null) {
            return -1;
        }

        if (view != getLoadMoreView()) {
            return -1;
        }

        int layoutHeight = getLayoutManager().getHeight();
        int top = view.getTop();

        return layoutHeight - top;
    }

    /**
     * 是否进行粘性滚动
     *
     * @return true：进行粘性滚动；false：不进行粘性滚动
     */
    private boolean isScrollStick() {

        View theFirstView = getChildAt(0);

        if (theFirstView == null) {
            return false;
        }

        ViewHolder childViewHolder = getChildViewHolder(theFirstView);
        if (childViewHolder == null) {
            return false;
        }

        if (!(childViewHolder instanceof IStick)) {
            return false;
        }

        boolean isInAction = false;

        if (handleRefreshLoad()) {
            isInAction = true;
        }

        if (handleLoadMore()) {
            isInAction = true;
        }

        // 没有处于 刷新 或 加载更多 的状态
        if (isInAction) {
            return false;
        }

        float y = theFirstView.getY();
        int height = theFirstView.getHeight();

        boolean isShowAll = Math.abs(y) > (height / 2);
        float offset = isShowAll ? height + y : y;

        smoothScrollBy(0, (int) offset);

        return true;

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

        /**
         * 1、子视图数量大于一
         * 2、第一个子视图 是 {@link IBaseRefreshLoadView}
         * 3、第一个可见视图 <= 1
         * 4、第二个视图 y 坐标要大于等于 0
         */
        if (getChildCount() > 1 &&
                getChildAt(0) instanceof IBaseRefreshLoadView &&
                ((LinearLayoutManager) getLayoutManager()).findFirstVisibleItemPosition() <= 1 &&
                getChildAt(1).getY() >= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取下拉刷新视图
     */
    private IBaseRefreshLoadView getRefreshLoadView() {
        if (getAdapter() instanceof JRefreshAndLoadMoreAdapter) {
            JRefreshAndLoadMoreAdapter jAdapter = (JRefreshAndLoadMoreAdapter) getAdapter();
            return jAdapter.getRefreshLoadView();
        }
        return null;
    }

    //========================下拉刷新更多 end  ==============================

    //========================上拉加载更多 start==============================

    /**
     * 是否是最后一个view
     *
     * @return true：最后一个view；false：不是最后一个
     */
    private boolean isScrolledBottom() {

        int itemTotal;
        // 若正在下拉刷新状态则 item个数-2
        // 若不是正在下拉刷新状态则 item个数-3
        if (getRefreshLoadView() == null
                || getRefreshLoadView().getCurState() != IBaseWrapperView.STATE_EXECUTING) {
            itemTotal = getAdapter().getItemCount() - 3;
        } else {
            itemTotal = getAdapter().getItemCount() - 2;
        }

        if (getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) getLayoutManager())
                        .findLastCompletelyVisibleItemPosition() >= itemTotal) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取加载更多视图
     */
    private IBaseLoadMoreView getLoadMoreView() {
        if (getAdapter() instanceof JRefreshAndLoadMoreAdapter) {
            JRefreshAndLoadMoreAdapter jAdapter = (JRefreshAndLoadMoreAdapter) getAdapter();
            return jAdapter.getLoadMoreView();
        }
        return null;
    }

    //========================上拉加载更多 end  ==============================

    //========================侧滑效果分割线 start==============================

    /**
     * 查找已经开启侧滑的项
     */
    @Nullable
    private View findOpenItem() {
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
     * 获取view中的JSwipeItemLayout
     * 有可能view就是JSwipeItemLayout，也有可能是view下的子view
     *
     * @param view 根View
     * @return JSwipeItemLayout
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

    //获取当前点击的坐标的item
    @Nullable
    private View getTouchItem(int x, int y) {
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            //是可见的
            if (child.getVisibility() == VISIBLE) {
                //获取范围
                child.getHitRect(mFrame);
                //判断点击点是否已经在其范围内
                if (mFrame.contains(x, y)) {
                    return child;
                }
            }
        }
        return null;
    }

    //========================侧滑效果分割线 end================================

    private class ScrollerListener extends OnScrollListener {

        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            switch (newState) {

                case SCROLL_STATE_IDLE: // 停止滚动
                    isScrolling = false;
                    break;

                case SCROLL_STATE_DRAGGING: // 正在被外部拖拽,一般为用户正在用手指滚动
                    isScrolling = true;
                    break;

                case SCROLL_STATE_SETTLING: // 自动滚动开始
                    isScrolling = true;
                    break;
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            if (mIsTouching) {
                return;
            }

            isScrollStick();
        }

    }

}
