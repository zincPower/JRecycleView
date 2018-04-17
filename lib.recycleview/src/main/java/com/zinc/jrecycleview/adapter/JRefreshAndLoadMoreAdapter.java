package com.zinc.jrecycleview.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.config.JRecycleViewManager;
import com.zinc.jrecycleview.loadview.base.IBaseLoadMoreView;
import com.zinc.jrecycleview.loadview.OrdinaryLoadMoreView;
import com.zinc.jrecycleview.loadview.OrdinaryPullRefreshLoadView;
import com.zinc.jrecycleview.loadview.base.IBasePullRefreshLoadView;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/3/18
 * @description 刷新和下拉加载更多包装
 */

public class JRefreshAndLoadMoreAdapter extends JBaseRecycleAdapter<RecyclerView.ViewHolder>{//RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private boolean mIsOpenRefresh = true;
    private boolean mIsOpenLoadMore = true;

    private IBasePullRefreshLoadView mRefreshLoadView;
    private IBaseLoadMoreView mLoadMoreView;

    private RecyclerView.Adapter mRealAdapter;

    private OnLoadMoreListener mOnLoadMoreListener;
    private OnRefreshListener mOnRefreshListener;

    public JRefreshAndLoadMoreAdapter(Context context, RecyclerView.Adapter adapter) {
        this.mRealAdapter = adapter;

        if (this.mRefreshLoadView == null) {
            if (JRecycleViewManager.getInstance().getBasePullRefreshLoadView() == null) {
                this.mRefreshLoadView = new OrdinaryPullRefreshLoadView(context);
            } else {
                this.mRefreshLoadView = JRecycleViewManager.getInstance().getBasePullRefreshLoadView();
            }
        }

        if (this.mLoadMoreView == null) {
            if (JRecycleViewManager.getInstance().getBaseLoadMoreView() == null) {
                this.mLoadMoreView = new OrdinaryLoadMoreView(context);
            } else {
                this.mLoadMoreView = JRecycleViewManager.getInstance().getBaseLoadMoreView();
            }
        }

    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 是否开启下拉刷新
     * @version
     */
    public void setIsOpenRefresh(boolean mIsOpenRefresh) {
        this.mIsOpenRefresh = mIsOpenRefresh;
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 是否开启上拉加载更多
     * @version
     */
    public void setIsOpenLoadMore(boolean mIsOpenLoadMore) {
        this.mIsOpenLoadMore = mIsOpenLoadMore;
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 添加上拉加载更多侦听器
     * @version
     */
    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.mOnLoadMoreListener = mOnLoadMoreListener;
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 添加下拉刷新侦听器
     * @version
     */
    public void setOnRefreshListener(OnRefreshListener mOnRefreshListener) {
        this.mOnRefreshListener = mOnRefreshListener;
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 获取下拉刷新视图
     * @version
     */
    public IBasePullRefreshLoadView getRefreshLoadView() {
        return mRefreshLoadView;
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 获取上拉加载更多视图
     * @version
     */
    public IBaseLoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    /**
     *
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description
     * @version
     *
     */
    public void setRefreshLoadView(IBasePullRefreshLoadView mRefreshLoadView) {
        this.mRefreshLoadView = mRefreshLoadView;
    }

    /**
     *
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description
     * @version
     *
     */
    public void setLoadMoreView(IBaseLoadMoreView mLoadMoreView) {
        this.mLoadMoreView = mLoadMoreView;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 根据格子布局的列数，给其分配所占列数达到占一行
                    return (getItemViewType(position) == JRecycleConfig.JFOOT || getItemViewType(position) == JRecycleConfig.JHEAD) ? gridManager.getSpanCount() : 1;
                }
            });
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == JRecycleConfig.JHEAD) {

            if (this.mOnRefreshListener != null) {
                this.mRefreshLoadView.setOnRefreshListener(mOnRefreshListener);
            }
            return new JRefreshViewHolder(this.mRefreshLoadView);

        } else if (viewType == JRecycleConfig.JFOOT) {

            if (this.mOnLoadMoreListener != null) {
                this.mLoadMoreView.setOnLoadMoreListener(mOnLoadMoreListener);
            }
            return new JLoadMoreViewHolder(this.mLoadMoreView);

        } else {
            return mRealAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof JRefreshViewHolder) {

        } else if (holder instanceof JLoadMoreViewHolder) {

        } else {
            this.mRealAdapter.onBindViewHolder(holder, getRealPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        int count = this.mRealAdapter.getItemCount();
        if (this.mIsOpenRefresh) {
            ++count;
        }
        if (this.mIsOpenLoadMore) {
            ++count;
        }
        return count;
    }

    /**
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 获取真正数据的下标
     * @version
     */
    public int getRealPosition(int position) {
        return this.mIsOpenRefresh ? position - 1 : position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && this.mIsOpenRefresh) {
            return JRecycleConfig.JHEAD;
        } else if (position == getItemCount() - 1 && this.mIsOpenLoadMore) {
            return JRecycleConfig.JFOOT;
        } else {
            return mRealAdapter.getItemViewType(getRealPosition(position));
        }
    }

    /**
     * @date 创建时间 2018/3/18
     * @author Jiang zinc
     * @Description 刷新结束，不刷新【下拉刷新】
     * @version
     */
    public void setRefreshComplete() {
        setRefreshComplete(false);
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 重载方法，如果不想让其自动刷新，参数传false【下拉刷新】
     * @version
     */
    public void setRefreshComplete(boolean isRefreshRightNow) {
        if (this.getRefreshLoadView() != null) {
            this.getRefreshLoadView().refreshComplete();

            if (isRefreshRightNow) {
                notifyDataSetChanged();
            }

        }
    }

    /**
     * @date 创建时间 2018/3/19
     * @author Jiang zinc
     * @Description 加载完，但还没加载全部【加载更多】
     * @version
     */
    public void setLoadComplete() {
        if (mIsOpenLoadMore) {
            this.mLoadMoreView.loadComplete();
        }
    }

    public void setLoadError() {
        if (mIsOpenLoadMore) {
            this.mLoadMoreView.loadError();
        }
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 重置加载更多状态【加载更多】
     * @version
     */
    public void resetLoadMore() {
        if (mIsOpenLoadMore) {
            this.mLoadMoreView.reset();
        }
    }

    /**
     * @date 创建时间 2018/4/17
     * @author Jiang zinc
     * @Description 没有更多数据【加载更多】
     * @version
     */
    public void setNoMore() {
        if (mIsOpenLoadMore) {
            this.mLoadMoreView.noMore();
        }
    }

    class JRefreshViewHolder extends RecyclerView.ViewHolder {
        public JRefreshViewHolder(View itemView) {
            super(itemView);
        }
    }

    class JLoadMoreViewHolder extends RecyclerView.ViewHolder {
        public JLoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * @author Jiang zinc
     * @date 创建时间：2018/3/18
     * @description 刷新回调接口
     */
    public static interface OnRefreshListener {
        /**
         * @date 创建时间 2018/4/17
         * @author Jiang zinc
         * @Description 刷新中回调，刷新完需要调用{@link JRefreshAndLoadMoreAdapter#setRefreshComplete()}
         * 如果想刷新可以使用{@link JRefreshAndLoadMoreAdapter#setRefreshComplete(boolean isRefreshRightNow)}，传递true）
         * @version
         */
        public void onRefreshing();
    }

    /**
     * @author Jiang zinc
     * @date 创建时间：2018/3/19
     * @description 加载更多接口
     */
    public static interface OnLoadMoreListener {
        /**
         * @date 创建时间 2018/4/17
         * @author Jiang zinc
         * @Description 加载更多数据中回调，加载完成后，可调用一下方法：
         * 1、还有更多数据{@link JRefreshAndLoadMoreAdapter#setLoadComplete()}
         * 2、没有更多数据{@link JRefreshAndLoadMoreAdapter#setNoMore()}
         * 3、数据异常{@link JRefreshAndLoadMoreAdapter#setLoadError()}
         * @version
         */
        public void onLoading();
    }

}
