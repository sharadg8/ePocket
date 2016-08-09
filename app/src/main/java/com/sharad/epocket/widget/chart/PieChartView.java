package com.sharad.epocket.widget.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Sharad on 13-Sep-15.
 */
public class PieChartView extends View {
    private Paint paint;
    private ArrayList<PieSector> sectors = new ArrayList<>();
    RectF rectPie;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(24);
    }

    public void setValues(ArrayList<PieSector> sectors) {
        this.sectors.clear();
        float total = 0;
        for (PieSector sector : sectors) {
            total += sector.value;
            PieSector add = new PieSector(sector.id, sector.value);
            add.color = sector.color;
            add.resourceId = sector.resourceId;
            add.name = sector.name;
            this.sectors.add(add);
        }

        for (PieSector sector : this.sectors) {
            sector.sweepAngle = 360 * (sector.value / total);
            sector.percentage = Math.round((sector.value / total) * 100);
        }

        invalidate();
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
            if(sector.sweepAngle > 8) {
                paint.setColor(sector.color);
                Path path = new Path();
                path.arcTo(rectPie, temp, sector.sweepAngle - 8, true);
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
