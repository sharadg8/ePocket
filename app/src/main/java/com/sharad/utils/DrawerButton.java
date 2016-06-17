package com.sharad.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by Sharad on 16-Jun-16.
 */

public class DrawerButton extends View implements Checkable {
    private Paint mBackground;
    private Paint mForeground;
    private Paint mLine;
    private boolean mChecked = false;
    private String mTitle;
    private int mImageResource;
    private int mCenterX;
    private int mCenterY;
    private int mRadius;

    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    public DrawerButton(Context context){
        super(context);
        init(context, null);
    }

    public DrawerButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DrawerButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackground.setStyle(Paint.Style.FILL);
        mBackground.setColor(Color.parseColor("#0D47A1"));

        mForeground = new Paint(Paint.ANTI_ALIAS_FLAG);
        mForeground.setStyle(Paint.Style.FILL);
        mForeground.setColor(Color.DKGRAY);
        mForeground.setTextSize(32f);

        mLine = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLine.setStyle(Paint.Style.FILL);
        mLine.setColor(Color.WHITE);
        mLine.setAlpha(80);
        mLine.setStrokeWidth(5f);
        mLine.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if(mChecked != checked) {
            mChecked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    public void setText(String title) {
        mTitle = title;
    }

    public void setImageResource(int icon) {
        mImageResource = icon;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int size = Math.max(width, height);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w / 2;
        mCenterY = h / 3;
        mRadius = (int)((Math.min(w, h) / 2) * 0.54);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(mCenterX, mCenterY, mRadius, mBackground);

        int left = (int)((canvas.getWidth() - mForeground.measureText(mTitle, 0, mTitle.length())) / 2.0f);
        canvas.drawText(mTitle, left, mCenterY*2-mForeground.ascent(), mForeground);

        Drawable icon = getResources().getDrawable(mImageResource);
        icon.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        icon.setBounds(mCenterX-icon.getIntrinsicWidth()/2, mCenterY-(int)(icon.getIntrinsicHeight()*0.67),
                mCenterX+icon.getIntrinsicWidth()/2, mCenterY+(int)(icon.getIntrinsicHeight()*0.33));
        icon.draw(canvas);

        canvas.drawLine(mCenterX-(int)(mRadius/2.8), mCenterY+mRadius/2, mCenterX+(int)(mRadius/2.8), mCenterY+mRadius/2, mLine);
        super.onDraw(canvas);
    }
}
