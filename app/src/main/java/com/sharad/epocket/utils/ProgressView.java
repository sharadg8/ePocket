package com.sharad.epocket.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sharad on 04-Jun-16.
 */
public class ProgressView extends View {
    private Paint mPaintProgressBG;
    private Paint mPaintProgress;
    private Rect mBounds;

    private int mColor = Color.GRAY;
    private int mValue = 0;

    public ProgressView(Context context){
        super(context);
        init(context, null);
    }

    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBounds = new Rect(0, 0, 0, 0);
        mPaintProgressBG = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressBG.setStyle(Paint.Style.FILL);
        mPaintProgressBG.setColor(Color.BLACK);
        mPaintProgressBG.setStrokeWidth(2);
        mPaintProgressBG.setAlpha(60);

        mPaintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgress.setStyle(Paint.Style.FILL);
        mPaintProgress.setColor(mColor);
        mPaintProgress.setStrokeWidth(4);
    }

    public void setValue(int mValue) { this.mValue = mValue; }
    public void setColor(int mColor) {
        this.mColor = mColor;
        mPaintProgress.setColor(mColor);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds.set(getPaddingLeft(), getPaddingTop(),
                w - (getPaddingLeft() + getPaddingRight()), h - (getPaddingTop()+getPaddingBottom()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawLine(getLeft(), mBounds.centerY(), getRight(), mBounds.centerY(), mPaintProgressBG);

        int progressWidth = (int) ((getRight() * mValue) / 100);
        canvas.drawLine(getLeft(), mBounds.centerY(), progressWidth, mBounds.centerY(), mPaintProgress);
        canvas.drawCircle(progressWidth, mBounds.centerY(), 6, mPaintProgress);

        mPaintProgress.setAlpha(60);
        canvas.drawCircle(progressWidth, mBounds.centerY(), 14, mPaintProgress);
        mPaintProgress.setAlpha(255);

        int warningWidth = (int) (getRight() * 0.9);
        mPaintProgressBG.setAlpha(120);
        canvas.drawLine(warningWidth, mBounds.centerY() - 6, warningWidth, mBounds.centerY() + 6, mPaintProgressBG);
        mPaintProgressBG.setAlpha(60);
    }
}
