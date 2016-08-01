package com.sharad.epocket.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by Sharad on 01-Aug-16.
 */

public class CircularProgress extends View {
    private int ANIMATION_DURATION = 2500;
    private int mProgress = 0;
    private int mProgressToDraw = 0;
    private Paint mPaintContour;
    private Paint mPaintProgress;
    private Paint mPaintPercentage;
    private RectF mBounds = new RectF();
    private final Rect mTextBounds = new Rect();
    private int mCenterY;
    private int mCenterX;
    private int mStartAngle = 140;
    private int mMaxAngle = 260;
    private int mSweepAngle = 180;
    private int mStrokeWidth = 12;
    private ValueAnimator mAnimator = null;

    public CircularProgress(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaintContour = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintContour.setStyle(Paint.Style.STROKE);
        mPaintContour.setStrokeCap(Paint.Cap.ROUND);
        mPaintContour.setStrokeWidth(mStrokeWidth);
        mPaintContour.setColor(Color.BLACK);
        mPaintContour.setAlpha(30);

        mPaintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);
        mPaintProgress.setStrokeWidth(mStrokeWidth);
        mPaintProgress.setColor(Color.WHITE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int size = Math.min(w - (getPaddingLeft() + getPaddingRight()),
                h - (getPaddingTop() + getPaddingBottom()));
        size -= mStrokeWidth;
        int radius = size / 2;
        int l = (int)(Math.sin(Math.toRadians(mStartAngle-90)) * radius);
        int s = (int)(radius - Math.sqrt(radius*radius - l*l));
        mCenterX = getPaddingLeft() + (w / 2);
        mCenterY = getPaddingTop() + ((h + s) / 2);
        mBounds.set(mCenterX - radius, mCenterY - radius, mCenterX + radius, mCenterY + radius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mBounds, mStartAngle, mMaxAngle, false, mPaintContour);

        mSweepAngle = mMaxAngle * mProgressToDraw / 100;
        canvas.drawArc(mBounds, mStartAngle, mSweepAngle, false, mPaintProgress);

        //drawTextCentred(canvas);
    }
/*
    public void drawTextCentred(Canvas canvas) {

        textPaint.getTextBounds(text, 0, text.length(), textBounds);

        canvas.drawText(text, centerX - textBounds.exactCenterX(), centerY - textBounds.exactCenterY(), textPaint);
    }
*/
    /**
     * Sets the value of the bar.  If the passed in value exceeds the maximum, the value
     * will be set to the maximum.
     *
     * @param newValue
     */
    public void setProgress(int newValue) {
        int previousValue = mProgress;
        if(newValue < 0) {
            mProgress = 0;
        } else if (newValue > 100) {
            mProgress = 100;
        } else {
            mProgress = newValue;
        }

        if(mAnimator != null) {
            mAnimator.cancel();
        }

        mAnimator = ValueAnimator.ofInt(previousValue, mProgress);
        int changeInValue = Math.abs(mProgress - previousValue);
        long durationToUse = (long) (ANIMATION_DURATION * ((float) changeInValue / (float) 100));
        mAnimator.setDuration(durationToUse);
        mAnimator.setInterpolator(new AccelerateInterpolator());

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mProgressToDraw = (int) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        mAnimator.start();

        invalidate();
    }
}
