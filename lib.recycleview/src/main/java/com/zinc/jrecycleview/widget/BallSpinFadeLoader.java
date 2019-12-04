package com.zinc.jrecycleview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import com.zinc.librecycleview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author       : Jiang Pengyong
 * time         : 2018-03-17 15:35
 * email        : 56002982@qq.com
 * desc         : loading
 * version      : 1.0.0
 */

public class BallSpinFadeLoader extends View {

    private Paint mPaint;

    private float mCenterX;
    private float mCenterY;
    private float mRadius;

    private volatile boolean mAnimatorEnable;

    float[] scaleFloats = new float[]{SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE,
            SCALE};

    int[] alphas = new int[]{ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA,
            ALPHA};


    public static final float SCALE = 1.0f;
    public static final int ALPHA = 255;

    private final List<ValueAnimator> scaleAnimList = new ArrayList<>();
    private final List<ValueAnimator> alphaAnimList = new ArrayList<>();

    public BallSpinFadeLoader(Context context) {
        this(context, null);
    }

    public BallSpinFadeLoader(Context context,
                              AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BallSpinFadeLoader(Context context,
                              AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(dip2px(1));
        mPaint.setColor(ContextCompat.getColor(context, R.color.j_recycle_balling_color));

        int[] delays = {0, 120, 240, 360, 480, 600, 720, 780, 840};
        for (int i = 0; i < 8; i++) {

            ValueAnimator scaleAnim = ValueAnimator.ofFloat(1, 0.4f, 1);
            scaleAnim.setDuration(1000);
            scaleAnim.setRepeatMode(ValueAnimator.RESTART);
            scaleAnim.setRepeatCount(ValueAnimator.INFINITE);
            scaleAnim.setStartDelay(delays[i]);
            scaleAnimList.add(scaleAnim);

            ValueAnimator alphaAnim = ValueAnimator.ofInt(255, 77, 255);
            alphaAnim.setDuration(1000);
            alphaAnim.setRepeatMode(ValueAnimator.RESTART);
            alphaAnim.setRepeatCount(ValueAnimator.INFINITE);
            alphaAnim.setStartDelay(delays[i]);
            alphaAnimList.add(alphaAnim);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);

        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);

        //处理 wrap_content问题
        int defaultDimension = dip2px(30);

        if (widthSpecMode == MeasureSpec.AT_MOST && heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, defaultDimension);
        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultDimension, heightSpecSize);
        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthSpecSize, defaultDimension);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRadius = w / 10;
        mCenterX = w >> 1;
        mCenterY = h >> 1;
    }


    public int dip2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < alphas.length; i++) {
            canvas.save();
            canvas.translate(mCenterX + ((getWidth() >> 1) - mRadius) * (float) Math.cos(Math.toRadians(i * 45)),
                    mCenterY + ((getWidth() >> 1) - mRadius) * (float) Math.sin(Math.toRadians(i * 45)));
            canvas.scale(scaleFloats[i], scaleFloats[i]);
            mPaint.setAlpha(alphas[i]);
            canvas.drawCircle(0, 0, mRadius, mPaint);
            canvas.restore();
        }
    }

    public void startAnimator() {

        for (int i = 0; i < scaleAnimList.size(); i++) {
            ValueAnimator valueAnimator = scaleAnimList.get(i);

            final int index = i;

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    scaleFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            valueAnimator.start();
        }

        for (int i = 0; i < alphaAnimList.size(); i++) {
            ValueAnimator valueAnimator = alphaAnimList.get(i);

            final int index = i;

            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    alphas[index] = (int) animation.getAnimatedValue();
                    postInvalidate();
                }
            });

            valueAnimator.start();
        }

        mAnimatorEnable = true;

    }

    public boolean isLoading() {
        return mAnimatorEnable;
    }

    public void stopAnimator() {
        if (isLoading()) {
            for (ValueAnimator valueAnimator : scaleAnimList) {
                valueAnimator.removeAllUpdateListeners();
                valueAnimator.cancel();
            }

            for (ValueAnimator valueAnimator : alphaAnimList) {
                valueAnimator.removeAllUpdateListeners();
                valueAnimator.cancel();
            }
        }

        mAnimatorEnable = false;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimator();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimator();
    }
}