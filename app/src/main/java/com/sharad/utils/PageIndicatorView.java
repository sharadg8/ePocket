package com.sharad.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;


/**
 * Created by Sharad on 11-Jun-16.
 */

public class PageIndicatorView extends View {
    private int mMaxPages = 0;
    private int mActivePage = 0;
    private final int mRadius = 6;
    private Paint mPaintActive;
    private Paint mPaintInactive;

    public PageIndicatorView(Context context) {
        super(context);
        init(context);
    }

    public PageIndicatorView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PageIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public void init(Context context) {
        mPaintActive = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintActive.setStyle(Paint.Style.FILL);
        mPaintActive.setColor(Color.WHITE);

        mPaintInactive = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintInactive.setStyle(Paint.Style.FILL);
        mPaintInactive.setColor(Color.WHITE);
        mPaintInactive.setAlpha(120);
    }

    public void setActivePage(int mActivePage) {
        this.mActivePage = mActivePage;
    }

    public void setMaxPages(int mMaxPages) {
        this.mMaxPages = mMaxPages;
    }

    public Point getCenter(int index) {
        Point point = new Point();
        int width = mMaxPages * 24;
        int posX = (getWidth() - width - 12) / 2;
        point.set(posX + index*24, (int)(getBottom()*0.9));
        return point;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mMaxPages > 0) {
            for (int i = 0; i < mMaxPages; i++) {
                Point p = getCenter(i);
                canvas.drawCircle(p.x, p.y, mRadius, (i == mActivePage) ? mPaintActive : mPaintInactive);
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
