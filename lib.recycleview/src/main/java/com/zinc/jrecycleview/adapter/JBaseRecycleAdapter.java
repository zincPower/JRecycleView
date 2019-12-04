package com.zinc.jrecycleview.adapter;

import android.animation.AnimatorSet;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zinc.jrecycleview.anim.IBaseAnimation;
import com.zinc.jrecycleview.config.JRecycleConfig;
import com.zinc.jrecycleview.config.JRecycleViewManager;

/**
 * author       : Jiang Pengyong
 * time         : 2018-04-12 10:28
 * email        : 56002982@qq.com
 * desc         : JBaseRecycleAdapter
 * version      : 1.0.0
 */
public abstract class JBaseRecycleAdapter<T extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<T> {

    //是否开启动画
    private boolean mOpenAnim = false;
    //是否第一次使用动画
    private boolean mOnlyFirstAnimEnable = true;

    //最后显示的item位置
    private int mLastPosition = -1;

    //动画集合
    private IBaseAnimation[] mAnimations = JRecycleViewManager.getInstance().getItemAnimations();

    @Override
    public void onViewAttachedToWindow(@NonNull T holder) {
        super.onViewAttachedToWindow(holder);

        addAnimForItem(holder);
    }

    public void setOpenAnim(boolean openAnim) {
        this.mOpenAnim = openAnim;
    }

    public void setOnlyFirstAnimEnable(boolean onlyFirstAnimEnable) {
        this.mOnlyFirstAnimEnable = onlyFirstAnimEnable;
    }

    public void setAnimations(IBaseAnimation[] animations) {
        this.mAnimations = animations;
        this.mOpenAnim = true;
    }

    /**
     * 给每个item添加动画
     */
    protected void addAnimForItem(T holder) {

        //是否开启动画
        if (!this.mOpenAnim) {
            return;
        }

        //是否有动画
        if (this.mAnimations == null || this.mAnimations.length <= 0) {
            return;
        }

        int itemType = holder.getItemViewType();
        if (itemType == JRecycleConfig.FOOT || itemType == JRecycleConfig.HEAD) {
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
