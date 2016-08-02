package com.sharad.epocket.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by Sharad on 01-Aug-16.
 */

public class CircularProgress extends View {
    private int ANIMATION_DURATION = 2000;
    private int mProgress = 0;
    private int mProgressToDraw = 0;
    private int mIndicator = 0;
    private Paint mPaintContour;
    private Paint mPaintProgress;
    private Paint mPaintIndicator;
    private Path mPathIndicator;
    private TextPaint mPaintNumber;
    private TextPaint mPaintPercentage;
    private RectF mBounds = new RectF();
    private int mCenterY;
    private int mCenterX;
    private int mRadius;
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

        mPaintNumber = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaintNumber.setColor(Color.WHITE);
        mPaintPercentage = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaintPercentage.setColor(Color.WHITE);

        mPaintIndicator = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintIndicator.setColor(Color.WHITE);
        CornerPathEffect corEffect = new CornerPathEffect(6);
        mPaintIndicator.setPathEffect(corEffect);
        mPathIndicator = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int size = Math.min(w - (getPaddingLeft() + getPaddingRight()),
                h - (getPaddingTop() + getPaddingBottom()));
        size -= mStrokeWidth;
        mRadius = size / 2;
        int l = (int)(Math.sin(Math.toRadians(mStartAngle-90)) * mRadius);
        int s = (int)(mRadius - Math.sqrt(mRadius*mRadius - l*l));
        mCenterX = getPaddingLeft() + (w / 2);
        mCenterY = getPaddingTop() + ((h + s) / 2);
        mBounds.set(mCenterX - mRadius, mCenterY - mRadius, mCenterX + mRadius, mCenterY + mRadius);

        setTextSizeForWidth((float)(mRadius*1.4), "100%");

        createIndicatorPath();
    }

    private void createIndicatorPath() {
        Point a = new Point();
        Point b = new Point();
        Point c = new Point();
        double cos120 = Math.cos(Math.toRadians(120));
        double sin120 = Math.sin(Math.toRadians(120));
        double cos240 = Math.cos(Math.toRadians(240));
        double sin240 = Math.sin(Math.toRadians(240));

        a.set((int)(mStrokeWidth * 1.2), 0);
        b.x = (int)(a.x * cos120 - a.y * sin120);
        b.y = (int)(a.x * sin120 + a.y * cos120);
        c.x = (int)(a.x * cos240 - a.y * sin240);
        c.y = (int)(a.x * sin240 + a.y * cos240);

        a.set(mCenterX + a.x, mCenterY - a.y);
        b.set(mCenterX + b.x, mCenterY - b.y);
        c.set(mCenterX + c.x, mCenterY - c.y);

        mPathIndicator.reset();
        mPathIndicator.moveTo(a.x, a.y);
        mPathIndicator.lineTo(b.x, b.y);
        mPathIndicator.lineTo(c.x, c.y);
        mPathIndicator.lineTo(a.x, a.y);
        mPathIndicator.close();

        mPathIndicator.offset(mRadius - (int)(1.5*mStrokeWidth), 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawArc(mBounds, mStartAngle, mMaxAngle, false, mPaintContour);

        mSweepAngle = mMaxAngle * mProgressToDraw / 100;
        canvas.drawArc(mBounds, mStartAngle, mSweepAngle, false, mPaintProgress);
        canvas.drawPath(mPathIndicator, mPaintIndicator);

        drawTextCentred(canvas);
    }

    private void drawTextCentred(Canvas canvas) {
        String text = ""+mProgressToDraw;
        Rect textBounds = new Rect();
        mPaintNumber.getTextBounds(text, 0, text.length(), textBounds);
        Rect percentageBounds = new Rect();
        mPaintPercentage.getTextBounds("%", 0, 1, percentageBounds);

        canvas.drawText(text, mCenterX - textBounds.exactCenterX() - percentageBounds.width() / 2,
                mCenterY - textBounds.exactCenterY(), mPaintNumber);

        canvas.drawText("%", mCenterX + textBounds.exactCenterX() - percentageBounds.width() / 2,
                mCenterY - textBounds.exactCenterY(), mPaintPercentage);
    }

    /**
     * Sets the text size for a Paint object so a given string of text will be a
     * given width.
     *
     * @param desiredWidth  the desired width
     * @param text          the text that should be that width
     */
    private void setTextSizeForWidth(float desiredWidth, String text) {
        final float testTextSize = 48f;

        // Get the bounds of the text, using our testTextSize.
        mPaintNumber.setTextSize(testTextSize);
        Rect bounds = new Rect();
        mPaintNumber.getTextBounds(text, 0, text.length(), bounds);

        // Calculate the desired size as a proportion of our testTextSize.
        float desiredTextSize = testTextSize * desiredWidth / bounds.width();

        // Set the paint for that size.
        mPaintNumber.setTextSize(desiredTextSize);
        mPaintPercentage.setTextSize((float)(0.6*desiredTextSize));
    }

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
        mAnimator.setInterpolator(new AccelerateDecelerateInterpolator());

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

    /**
     * Sets the value of the bar.  If the passed in value exceeds the maximum, the value
     * will be set to the maximum.
     *
     * @param newValue
     * @param maxValue
     */
    public void setIndicator(int newValue, int maxValue) {
        int sweepAngle = mStartAngle + mMaxAngle * newValue / maxValue;

        createIndicatorPath();

        Matrix matrix = new Matrix();
        matrix.postRotate(sweepAngle, mCenterX, mCenterY);
        mPathIndicator.transform(matrix);
        invalidate();
    }
}
