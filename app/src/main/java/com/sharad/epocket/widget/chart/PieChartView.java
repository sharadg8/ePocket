package com.sharad.epocket.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.sharad.epocket.accounts.CategoryImageList;

import java.util.ArrayList;

/**
 * Created by Sharad on 13-Sep-15.
 */
public class PieChartView extends ViewGroup {
    private final int MIN_SWEEP_ANGLE = 12;
    private final int MIN_SWEEP_ANGLE_TO_DRAW = 8;
    private Paint paint;
    private ArrayList<PieSector> sectors = new ArrayList<>();
    RectF rectPie;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(22);

        setWillNotDraw(false);
    }

    public void setValues(ArrayList<PieSector> sectors) {
        this.sectors.clear();
        float total = 0;
        PieSector others = new PieSector(-1, 0);
        for (PieSector sector : sectors) {
            total += sector.value;
        }

        for (PieSector sector : sectors) {
            float sweepAngle = 360 * (sector.value / total);
            if(sweepAngle > MIN_SWEEP_ANGLE) {
                PieSector add = new PieSector(sector.id, sector.value);
                add.color = sector.color;
                add.resourceId = sector.resourceId;
                add.name = sector.name;
                add.sweepAngle = sweepAngle;
                add.percentage = Math.round((sector.value / total) * 100);
                this.sectors.add(add);
            } else {
                others.value += sector.value;
            }
        }

        if(others.value > 0.01f) {
            others.name = "Others";
            others.color = Color.BLACK;
            others.resourceId = CategoryImageList.RESOURCE_UNKNOWN;
            others.sweepAngle = 360 * (others.value / total);
            others.percentage = Math.round((others.value / total) * 100);
            this.sectors.add(others);
        }

        invalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Rect rect = new Rect();
        this.getDrawingRect(rect);
        rectPie = new RectF(rect);
        float radius = Math.min(rect.width(), rect.height()) / 2;
        radius -= 12;
        rectPie.left = rect.centerX() - radius;
        rectPie.top = rect.centerY() - radius;
        rectPie.right = rect.centerX() + radius;
        rectPie.bottom = rect.centerY() + radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float temp = 0;
        for (PieSector sector : sectors) {
            if(sector.sweepAngle > MIN_SWEEP_ANGLE_TO_DRAW) {
                paint.setColor(sector.color);
                Path path = new Path();
                path.arcTo(rectPie, temp, sector.sweepAngle - MIN_SWEEP_ANGLE_TO_DRAW, true);
                canvas.drawPath(path, paint);
                temp += sector.sweepAngle;
            }
        }
    }

    public static class PieSector {
        public long id;
        public float value;
        public String name;
        public int resourceId;
        public int color;
        public int percentage;
        public float sweepAngle;

        public PieSector(long id, float value) {
            this.id = id;
            this.value = value;
        }
    }
}
