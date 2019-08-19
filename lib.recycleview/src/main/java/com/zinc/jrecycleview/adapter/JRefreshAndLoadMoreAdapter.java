package com.zinc.jrecycleview.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.config.JRecycleViewManager;
import com.zinc.jrecycleview.loadview.base.IBaseLoadMoreView;
import com.zinc.jrecycleview.loadview.OrdinaryLoadMoreView;
import com.zinc.jrecycleview.loadview.OrdinaryRefreshLoadView;
import com.zinc.jrecycleview.loadview.base.IBaseRefreshLoadView;

/**
 * author       : Jiang zinc
 * time         : 2018-03-18 10:29
 * email        : 56002982@qq.com
 * desc         : 刷新和下拉加载更多包装
 * version      : 1.0.0
 */

public class JRefreshAndLoadMoreAdapter extends JBaseRecycleAdapter<RecyclerView.ViewHolder> {

    private boolean mIsOpenRefresh = true;
    private boolean mIsOpenLoadMore = true;

    private IBaseRefreshLoadView mRefreshLoadView;
    private IBaseLoadMoreView mLoadMoreView;

    private RecyclerView.Adapter mRealAdapter;

    private OnLoadMoreListener mOnLoadMoreListener;
    private OnRefreshListener mOnRefreshListener;

    public JRefreshAndLoadMoreAdapter(Context context,
                                      RecyclerView.Adapter adapter) {
        mRealAdapter = adapter;

        if (mRefreshLoadView == null) {
            if (JRecycleViewManager.getInstance().getRefreshLoadView() == null) {
                mRefreshLoadView = new OrdinaryRefreshLoadView(context);
            } else {
                mRefreshLoadView = JRecycleViewManager.getInstance().getRefreshLoadView();
            }
        }

        if (mLoadMoreView == null) {
            if (JRecycleViewManager.getInstance().getLoadMoreView() == null) {
                mLoadMoreView = new OrdinaryLoadMoreView(context);
            } else {
                mLoadMoreView = JRecycleViewManager.getInstance().getLoadMoreView();
            }
        }

    }

    /**
     * 是否开启下拉刷新
     *
     * @param isOpenRefresh true：开启下拉刷新
     *                      false：关闭下拉刷新
     */
    public void setIsOpenRefresh(boolean isOpenRefresh) {
        mIsOpenRefresh = isOpenRefresh;
    }

    /**
     * 是否开启上拉加载更多
     *
     * @param isOpenLoadMore true：开启上拉加载
     *                       false：关闭上拉加载
     */
    public void setIsOpenLoadMore(boolean isOpenLoadMore) {
        mIsOpenLoadMore = isOpenLoadMore;
    }

    /**
     * 添加上拉加载更多侦听器
     */
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        mOnLoadMoreListener = onLoadMoreListener;
    }

    /**
     * 添加下拉刷新侦听器
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mOnRefreshListener = onRefreshListener;
    }

    /**
     * 获取下拉刷新视图
     */
    public IBaseRefreshLoadView getRefreshLoadView() {
        return mRefreshLoadView;
    }

    /**
     * 获取上拉加载更多视图
     */
    public IBaseLoadMoreView getLoadMoreView() {
        return mLoadMoreView;
    }

    /**
     * 设置刷新视图
     */
    public void setRefreshLoadView(IBaseRefreshLoadView refreshLoadView) {
        mRefreshLoadView = refreshLoadView;
    }

    /**
     * 设置加载视图
     */
    public void setLoadMoreView(IBaseLoadMoreView loadMoreView) {
        mLoadMoreView = loadMoreView;
    }

    @Override
    public void onAttachedToRecyclerView(@NonNull RecyclerView recyclerView) {

        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();

        if (manager instanceof GridLayoutManager) {

            final GridLayoutManager gridManager = ((GridLayoutManager) manager);

            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    // 根据格子布局的列数，给其分配所占列数达到占一行
                    return (getItemViewType(position) == JRecycleConfig.FOOT
                            || getItemViewType(position) == JRecycleConfig.HEAD) ?
                            gridManager.getSpanCount() : 1;
                }
            });

        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == JRecycleConfig.HEAD) {

            if (mOnRefreshListener != null) {
                mRefreshLoadView.setOnRefreshListener(mOnRefreshListener);
            }
            return new JRefreshViewHolder(mRefreshLoadView);

        } else if (viewType == JRecycleConfig.FOOT) {

            if (mOnLoadMoreListener != null) {
                mLoadMoreView.setOnLoadMoreListener(mOnLoadMoreListener);
            }
            return new JLoadMoreViewHolder(mLoadMoreView);

        } else {
            return mRealAdapter.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder,
                                 int position) {
        if (holder instanceof JRefreshViewHolder) {

        } else if (holder instanceof JLoadMoreViewHolder) {

        } else {
            mRealAdapter.onBindViewHolder(holder, _getRealPosition(position));
        }
    }

    @Override
    public int getItemCount() {
        int count = mRealAdapter.getItemCount();
        if (mIsOpenRefresh) {
            ++count;
        }
        if (mIsOpenLoadMore) {
            ++count;
        }
        return count;
    }

    /**
     * 获取真正数据的下标
     */
    private int _getRealPosition(int position) {
        return mIsOpenRefresh ? position - 1 : position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mIsOpenRefresh) {
            return JRecycleConfig.HEAD;
        } else if (position == getItemCount() - 1 && mIsOpenLoadMore) {
            return JRecycleConfig.FOOT;
        } else {
            return mRealAdapter.getItemViewType(_getRealPosition(position));
        }
    }

    public int getRealPosition(int position) {
        return mIsOpenRefresh ? position + 1 : position;
    }

    //================================设置刷新完成的状态 start========================================

//    /**
//     * 刷新结束，不刷新
//     * 默认为【下拉刷新】
//     */
//    public void setRefreshComplete() {
//        setRefreshComplete(false);
//    }

    /**
     * 重载方法，如果不想让其自动刷新，参数传false
     * 默认为【下拉刷新】
     */
    public void setRefreshComplete() {
        if (getRefreshLoadView() != null) {
            getRefreshLoadView().refreshComplete();

//            if (isRefreshRightNow) {
            notifyDataSetChanged();
//            }
        }
    }

    //================================设置刷新完成的状态 end  ========================================

    //================================设置加载更多的状态 start========================================
    //==================================   1、加载更多   ============================================
    //==================================   2、加载出错   ============================================
    //==================================   3、加载更多   ============================================
    //==================================   4、加载完毕   ============================================

    /**
     * 加载完，但还没加载全部
     * 默认为【加载完毕】
     */
    public void setLoadComplete() {
        if (mIsOpenLoadMore) {
            mLoadMoreView.loadComplete();
        }
    }

    /**
     * 加载出错
     */
    public void setLoadError() {
        if (mIsOpenLoadMore) {
            mLoadMoreView.loadError();
        }
    }

    /**
     * 重置加载更多状态
     * 默认为【加载更多】
     */
    public void resetLoadMore() {
        if (mIsOpenLoadMore) {
            mLoadMoreView.reset();
        }
    }

    /**
     * 没有更多数据
     * 默认为【没有数据】
     */
    public void setNoMore() {
        if (mIsOpenLoadMore) {
            mLoadMoreView.noMore();
        }
    }
    //================================设置加载更多的状态 end==========================================

    /**
     * 头部刷新视图
     */
    static class JRefreshViewHolder extends RecyclerView.ViewHolder {
        JRefreshViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 底部刷新视图
     */
    static class JLoadMoreViewHolder extends RecyclerView.ViewHolder {
        JLoadMoreViewHolder(View itemView) {
            super(itemView);
        }
    }

    /**
     * 刷新回调接口
     */
    public interface OnRefreshListener {
        /**
         * 刷新中回调，刷新完需要调用{@link JRefreshAndLoadMoreAdapter#setRefreshComplete()}
         */
        void onRefreshing();
    }

    /**
     * 加载更多接口
     */
    public interface OnLoadMoreListener {
        /**
         * 加载更多数据中回调，加载完成后，可调用一下方法：
         * 1、还有更多数据{@link JRefreshAndLoadMoreAdapter#setLoadComplete()}
         * 2、没有更多数据{@link JRefreshAndLoadMoreAdapter#setNoMore()}
         * 3、数据异常{@link JRefreshAndLoadMoreAdapter#setLoadError()}
         */
        void onLoading();
    }

}
