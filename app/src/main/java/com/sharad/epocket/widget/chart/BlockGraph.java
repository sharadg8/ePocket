package com.sharad.epocket.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Sharad on 03-Aug-16.
 */

public class BlockGraph extends View {
    private Paint mPaintBlock;
    private Paint mPaintShadow;
    private Rect  mBounds;

    public BlockGraph(Context context) {
        this(context, null);
    }

    public BlockGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaintBlock = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBlock.setColor(Color.WHITE);
        mPaintBlock.setStyle(Paint.Style.FILL);

        mPaintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintShadow.setColor(Color.BLACK);
        mPaintShadow.setAlpha(30);
        mPaintShadow.setStyle(Paint.Style.FILL);

        mBounds = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(mBounds.left, mBounds.top, mBounds.right, mBounds.centerY(), mPaintBlock);
        canvas.drawRect(mBounds.left, mBounds.centerY(), mBounds.right, mBounds.bottom, mPaintShadow);
    }

    private class DataPoint {

    }
}
