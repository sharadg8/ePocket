package com.sharad.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.Size;
import android.view.View;

import com.sharad.epocket.R;

import java.util.ArrayList;

/**
 * Created by Sharad on 04-Jun-16.
 */
public class ProgressGraph extends View {
    private ArrayList<ProgressItem> mList;
    private Rect mBounds;
    private int mItemHeight = 120;

    private Paint mPaintProgressBG;
    private Paint mPaintProgress;
    private Paint mPaintToday;
    private Path mPathToday;

    public ProgressGraph(Context context){
        super(context);
        mList = new ArrayList<ProgressItem>();
        mBounds = new Rect(0, 0, 0, 0);

        mPaintProgressBG = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgressBG.setStyle(Paint.Style.FILL);
        mPaintProgressBG.setColor(getResources().getColor(R.color.divider));
        mPaintProgressBG.setStrokeWidth(22);
        mPaintProgressBG.setStrokeCap(Paint.Cap.ROUND);

        mPaintProgress = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintProgress.setStyle(Paint.Style.FILL);
        mPaintProgress.setColor(getResources().getColor(R.color.divider));
        mPaintProgress.setStrokeWidth(22);
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);

        mPaintToday = new Paint();
        mPaintToday.setStyle(Paint.Style.FILL);
        mPaintToday.setColor(Color.GRAY);

        mPathToday = new Path();
        mPathToday.lineTo(1, 1);
        mPathToday.lineTo(25, 1);
        mPathToday.lineTo(16, 10);
        mPathToday.lineTo(16, 36);
        mPathToday.lineTo(25, 45);
        mPathToday.lineTo(1, 45);
        mPathToday.lineTo(11, 36);
        mPathToday.lineTo(11, 10);
        mPathToday.close();
    }

    public int getGraphHeight() {
        return ((mList.size() * mItemHeight) + getPaddingTop() + getPaddingBottom());
    }
    public void addProgressItem(String title, int icon, int color, float max, float current) {
        mList.add(new ProgressItem(title, icon, color, max, current));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBounds.set(getPaddingLeft(), getPaddingTop(),
                w-(getPaddingLeft()+getPaddingRight()), h-(getPaddingTop()+getPaddingBottom()));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPathToday.offset(3*36, 0);
        for(int i=0; i <mList.size(); i++) {
            ProgressItem thisItem = mList.get(i);
            Rect bounds = new Rect(mBounds.left, i * mItemHeight + getPaddingTop(), mBounds.right, (i + 1) * mItemHeight - getPaddingBottom());

            Drawable d = getResources().getDrawable(thisItem.Icon());
            d.setColorFilter(thisItem.Color(), PorterDuff.Mode.SRC_ATOP);
            d.setBounds(
                    bounds.left,
                    bounds.centerY() - thisItem.IconOffset,
                    bounds.left + 2 * thisItem.IconOffset,
                    bounds.centerY() + thisItem.IconOffset);
            d.draw(canvas);

            int lineWidth = bounds.right - (bounds.left + (3 * thisItem.IconOffset));
            canvas.drawLine(
                    bounds.left + 3 * thisItem.IconOffset,
                    bounds.centerY(),
                    bounds.right,
                    bounds.centerY(),
                    mPaintProgressBG);

            int progressWidth = (int) (lineWidth * (thisItem.mCurrent / thisItem.mMax));
            mPaintProgress.setColor(thisItem.Color());
            canvas.drawLine(
                    bounds.left + 3 * thisItem.IconOffset,
                    bounds.centerY(),
                    bounds.left + (3 * thisItem.IconOffset) + progressWidth,
                    bounds.centerY(),
                    mPaintProgress);

            canvas.drawPath(mPathToday, mPaintToday);
            mPathToday.offset(0, mItemHeight);
        }
        mPathToday.offset(0, -(mList.size()*mItemHeight));
    }

    public class ProgressItem {
        private int mIcon, mColor;
        private float mMax, mCurrent;
        private String mTitle;

        public int IconOffset = 36;

        public ProgressItem(String title, int icon, int color, float max, float current) {
            mTitle = title; mIcon = icon; mColor = color; mMax = max; mCurrent = current;
        }

        public float Current() { return mCurrent; }
        public float Max() { return mMax; }
        public int Color() { return mColor; }
        public int Icon() { return mIcon; }
        public String Title() { return mTitle; }
    }
}
