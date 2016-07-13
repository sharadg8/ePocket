package com.sharad.epocket.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.CompoundButton;

import com.sharad.epocket.R;

/**
 * Created by Sharad on 09-Jul-16.
 */

public class SwitchButton extends CompoundButton {
    private Paint mPaintTrack;
    private Paint mPaintThumb;

    private int mTrackWidth;
    private int mTrackHeight;
    private int mThumbRadius;

    private Drawable mIconLeft;
    private Drawable mIconRight;

    public SwitchButton(Context context) {
        this(context, null);
    }

    public SwitchButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton);
            mIconLeft = a.getDrawable(R.styleable.SwitchButton_sb_src_left);
            mIconRight = a.getDrawable(R.styleable.SwitchButton_sb_src_right);
            a.recycle();
        }

        mPaintTrack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintTrack.setColor(ContextCompat.getColor(context, R.color.primary_light));
        mPaintTrack.setStrokeCap(Paint.Cap.ROUND);

        mPaintThumb = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintThumb.setColor(ContextCompat.getColor(context, R.color.primary_dark));
        mPaintThumb.setShadowLayer(mThumbRadius, 0, 8, ContextCompat.getColor(context, R.color.primary_light));

        setFocusable(true);
        setClickable(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mThumbRadius = (h - getPaddingTop() - getPaddingBottom()) / 2;
        mTrackHeight = (h - getPaddingTop() - getPaddingBottom()) / 2;
        mTrackWidth = 2 * mThumbRadius;

        mPaintTrack.setStrokeWidth(mTrackHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int trackStartX = getPaddingLeft() + (getWidth() - getPaddingLeft() - getPaddingRight() - mTrackWidth) / 2;
        int trackY = canvas.getClipBounds().centerY();
        canvas.drawLine(trackStartX, trackY, trackStartX + mTrackWidth, trackY, mPaintTrack);

        int thumbCx = trackStartX + (isChecked() ? (mTrackWidth - (mThumbRadius / 4)) : (mThumbRadius / 4));
        int thumbCy = trackY;
        canvas.drawCircle(thumbCx, thumbCy, mThumbRadius, mPaintThumb);

        if(isChecked()) {
            if (mIconRight != null) {
                int halfSize = (Math.max(mIconRight.getIntrinsicHeight(), mIconRight.getIntrinsicWidth())) / 2;
                mIconRight.setTint(Color.WHITE);
                mIconRight.setBounds((thumbCx - halfSize), (thumbCy - halfSize), (thumbCx + halfSize), (thumbCy + halfSize));
                mIconRight.draw(canvas);
            }
        } else {
            if (mIconLeft != null) {
                int halfSize = (Math.max(mIconLeft.getIntrinsicHeight(), mIconLeft.getIntrinsicWidth())) / 2;
                mIconLeft.setTint(Color.WHITE);
                mIconLeft.setBounds((thumbCx - halfSize), (thumbCy - halfSize), (thumbCx + halfSize), (thumbCy + halfSize));
                mIconLeft.draw(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);
    }
}
