package com.sharad.epocket.accounts;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sharad.epocket.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Sharad on 03-Aug-16.
 */

public class BlockGraph extends View {
    private final int MAX_NUM_DAYS = 31;
    private Paint mPaintBlock;
    private Paint mPaintShadow;
    private Rect  mBounds;
    private int mMaxIndex;
    private float mMaxValue;
    private float mMinValue;
    private int mChartHeight;
    private int mStrokeWidth;

    private ArrayList<DataPoint> mDataPoints;
    private ArrayList<ITransaction> iTransactionArrayList;

    public BlockGraph(Context context) {
        this(context, null);
    }

    public BlockGraph(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BlockGraph(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mDataPoints = new ArrayList<>();
        iTransactionArrayList = new ArrayList<>();

        mPaintBlock = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintBlock.setStyle(Paint.Style.STROKE);
        mPaintBlock.setColor(Color.WHITE);
        mPaintBlock.setAlpha(200);

        mPaintShadow = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintShadow.setStyle(Paint.Style.STROKE);
        mPaintShadow.setColor(Color.BLACK);
        mPaintShadow.setAlpha(20);

        mBounds = new Rect();

        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBounds.set(getPaddingLeft(), getPaddingTop(), w - getPaddingRight(), h - getPaddingBottom());

        mChartHeight = mBounds.centerY() - mBounds.top;

        mStrokeWidth = (int)(0.65 * mBounds.width() / MAX_NUM_DAYS);
        mPaintBlock.setStrokeWidth(mStrokeWidth);
        mPaintBlock.setPathEffect(new DashPathEffect(new float[] {mStrokeWidth, 0.4f*mStrokeWidth}, 0));
        mPaintShadow.setStrokeWidth(mStrokeWidth);
        mPaintShadow.setPathEffect(new DashPathEffect(new float[] {mStrokeWidth, 0.4f*mStrokeWidth}, 0));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for(int i=0; i<mMaxIndex; i++) {
            DataPoint data = getDataPoint(i+1);

            if(data != null) {
                RectF block = new RectF();
                int x = getPosX(i);
                block.set(x, (mBounds.centerY() - getHeight(data.value)), x+mStrokeWidth, mBounds.centerY());
                canvas.drawLine(block.centerX(), block.bottom, block.centerX(), block.top, mPaintBlock);

                int shadow = (int)(getHeight(data.value) * 0.7);
                canvas.drawLine(block.centerX(), block.bottom, block.centerX(), block.bottom + shadow, mPaintShadow);
            }
        }
    }

    private int getPosX(float value) {
        // scale it to the view size
        value = (value / MAX_NUM_DAYS) * mBounds.width();

        // offset it to adjust for padding
        value += getPaddingLeft();

        return (int)value;
    }

    private float getHeight(float value) {
        return mChartHeight * ((value - mMinValue) / (mMaxValue - mMinValue));
    }

    public void setValues(ArrayList<ITransaction> iTransactionArrayList) {
        mDataPoints.clear();
        if(iTransactionArrayList == null) {
            return;
        }

        Collections.reverse(iTransactionArrayList);
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        float amount = 0;
        Calendar cal = Calendar.getInstance();
        int today = cal.get(Calendar.DAY_OF_MONTH);
        for(ITransaction iTransaction : iTransactionArrayList) {
            cal.setTimeInMillis(iTransaction.getDate());
            int day = cal.get(Calendar.DAY_OF_MONTH);
            if(iTransaction.getType() == ITransaction.TRANSACTION_TYPE_ACCOUNT_INCOME) {
                amount += iTransaction.getAmount();
            } else if((iTransaction.getType() == ITransaction.TRANSACTION_TYPE_ACCOUNT_EXPENSE)
                    || (iTransaction.getType() == ITransaction.TRANSACTION_TYPE_ACCOUNT_TRANSFER)) {
                amount -= iTransaction.getAmount();
            }
            mDataPoints.add(new BlockGraph.DataPoint(day, amount));

            min = (amount < min) ? amount : min;
            max = (amount > max) ? amount : max;
        }

        mMaxValue = max;
        mMinValue = 0.8f * min;
        mMaxIndex = Utils.isThisMonth(cal.getTimeInMillis()) ? today : MAX_NUM_DAYS;

        invalidate();
    }

    private DataPoint getDataPoint(int i) {
        DataPoint data = null;
        for(DataPoint dataPoint : mDataPoints) {
            if(dataPoint.index > i) {
                break;
            }
            data = dataPoint;
        }
        return data;
    }

    public static class DataPoint {
        public int index;
        public float value;

        public DataPoint(int index, float value) {
            this.index = index;
            this.value = value;
        }

        public static class iComparator implements Comparator<DataPoint> {
            @Override
            public int compare(DataPoint o, DataPoint o1) {
                return (o.index == o1.index)
                        ? 0
                        : ((o.index < o1.index) ? -1 : 1);
            }
        }
    }
}
