package com.zinc.jrecycleview.adapter;

import android.animation.AnimatorSet;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zinc.jrecycleview.anim.IBaseAnimation;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.config.JRecycleViewManager;

/**
 * @author Jiang zinc
 * @date 创建时间：2018/4/12
 * @description
 */

public abstract class JBaseRecycleAdapter<T extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<T> {

    private static final String TAG = "Test";
    //是否开启动画
    public boolean mOpenAnim = false;
    //是否第一次使用动画
    public boolean mOnlyFirstAnimEnable = true;

    //最后显示的item位置
    private int mLastPosition = -1;

    //动画集合
    private IBaseAnimation[] mAnimations = JRecycleViewManager.getInstance().getItemAnimations();

    @Override
    public void onViewAttachedToWindow(T holder) {
        super.onViewAttachedToWindow(holder);

        addAnimForItem(holder);

    }

    public JBaseRecycleAdapter setOpenAnim(boolean mOpenAnim) {
        this.mOpenAnim = mOpenAnim;
        return this;
    }

    public JBaseRecycleAdapter setOnlyFirstAnimEnable(boolean mOnlyFirstAnimEnable) {
        this.mOnlyFirstAnimEnable = mOnlyFirstAnimEnable;
        return this;
    }

    public JBaseRecycleAdapter setAnimations(IBaseAnimation[] mAnimations) {
        this.mAnimations = mAnimations;
        this.mOpenAnim = true;
        return this;
    }

    /**
     * @date 创建时间 2018/4/12
     * @author Jiang zinc
     * @Description 给每个item添加动画
     * @version
     */
    protected void addAnimForItem(T holder) {

        //是否开启动画
        if (!this.mOpenAnim) {
            return;
        }

//        //是否有动画
//        if (this.mAnimations.length <= 0) {
//            return;
//        }

        int itemType = holder.getItemViewType();
        if (itemType == JRecycleConfig.JFOOT || itemType == JRecycleConfig.JHEAD) {
            return;
        }

        //是否只有第一次起作用 或是 第一次进入页面
        if (!this.mOnlyFirstAnimEnable || holder.getLayoutPosition() > mLastPosition) {
            this.mLastPosition = holder.getLayoutPosition();
            int animLength = this.mAnimations.length;
            int curAnimPosition = holder.getLayoutPosition() % animLength;
            AnimatorSet animators = this.mAnimations[curAnimPosition].getAnimators(holder.itemView);
            animators.start();
        }

    }

}
