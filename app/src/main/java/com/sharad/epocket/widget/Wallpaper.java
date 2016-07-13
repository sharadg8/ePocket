package com.sharad.epocket.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

import com.sharad.epocket.R;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Sharad on 05-Jun-16.
 */
public class Wallpaper extends View {
    private ArrayList<CircleObject> mList;
    private Paint mMaskWindowPaint;

    public Wallpaper(Context context) {
        super(context);
        init(context, null);
    }

    public Wallpaper(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public Wallpaper(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mList = new ArrayList<CircleObject>();

        mMaskWindowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mMaskWindowPaint.setStyle(Paint.Style.FILL);
        mMaskWindowPaint.setColor(getResources().getColor(R.color.primary));

        if(attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Wallpaper);
            final int id = array.getResourceId(R.styleable.Wallpaper_color_array, 0);

            if (id != 0) {
                final int[] values = getResources().getIntArray(id);
                for(int i=0; i<values.length; i++){
                    mList.add(new CircleObject(values[i], values.length, i));
                }
            }
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        /*for(int i=0; i<mList.size(); i++){
            mList.get(i).onSizeChanged(w, h);
        }*/
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        /*
        for(int i=0; i<mList.size(); i++){
            mList.get(i).onDraw(canvas);
        }
        canvas.drawRect(new RectF(0, 0, getRight(), getBottom()), mMaskWindowPaint);
        */
        Path mPathToday = new Path();
        mPathToday.moveTo(getLeft(), getBottom());
        mPathToday.lineTo(getLeft(), 3*getBottom()/4);
        mPathToday.lineTo(getRight()/3, getBottom());
        canvas.drawPath(mPathToday, mMaskWindowPaint);
    }

    public class CircleObject {
        private Paint mPaint;
        private Point mCenter;
        private int mRadius;
        private int mIndex;
        private int mCount;

        public CircleObject(int color, int count, int index) {
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(color);

            int min = 220;
            Random r = new Random();
            int alpha = r.nextInt(256 - min) + min;
            mPaint.setAlpha(alpha);

            mCenter = new Point(0, 0);
            mRadius = 0;
            mCount = (int)Math.sqrt((double)count);
            mCount = (mCount > 0) ? mCount : 1;
            mIndex = index;
        }

        public void onSizeChanged(int w, int h) {
            /* Set radius between 20% to 40% of width */
            int size = (w > h) ? h : w;
            int minSize = (int)(size * 0.2);
            int maxSize = (int)(size * 0.4);
            Random r = new Random();
            mRadius = r.nextInt(maxSize - minSize) + minSize;

            /* Set center point */
            int posMinX = (((mIndex % mCount) + 0) * w / mCount);
            int posMaxX = (((mIndex % mCount) + 1) * w / mCount);
            int posMinY = (((mIndex / mCount) + 0) * h / mCount);
            int posMaxY = (((mIndex / mCount) + 1) * h / mCount);
            int posX = r.nextInt(posMaxX - posMinX) + posMinX;
            int posY = r.nextInt(posMaxY - posMinY) + posMinY;
            mCenter.set(posX, posY);
        }

        public void onDraw(Canvas canvas) {
            canvas.drawCircle(mCenter.x, mCenter.y, mRadius, mPaint);
        }
    }
}
