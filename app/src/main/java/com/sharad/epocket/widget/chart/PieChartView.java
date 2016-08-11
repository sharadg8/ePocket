package com.sharad.epocket.widget.chart;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sharad.epocket.R;
import com.sharad.epocket.utils.Utils;
import com.sharad.epocket.widget.FlowLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by Sharad on 13-Sep-15.
 */
public class PieChartView extends LinearLayout {
    private final int MIN_SWEEP_ANGLE = 12;
    private final int MIN_SWEEP_ANGLE_TO_DRAW = 8;
    private Paint paint;
    private TextView valueText;
    private ArrayList<PieSector> sectors = new ArrayList<>();
    private RectF rectPie;
    private int pieHeight;
    private String isoCurrency;

    public PieChartView(Context context, AttributeSet attrs) {
        super(context, attrs);

        setOrientation(VERTICAL);

        pieHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                192, getResources().getDisplayMetrics());

        int[] set = {
                android.R.attr.minHeight, // idx 0
        };
        TypedArray a = context.obtainStyledAttributes(attrs, set);
        pieHeight = a.getDimensionPixelSize(0, pieHeight);
        a.recycle();

        valueText = new TextView(context);
        valueText.setTextAppearance(context, android.R.style.TextAppearance_Material_Headline);
        valueText.setGravity(Gravity.CENTER);
        valueText.setTextColor(Color.LTGRAY);
        valueText.setMinimumHeight(pieHeight);
        LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        llp.setMargins(0, 0, 0, pieHeight/10);
        valueText.setLayoutParams(llp);

        isoCurrency = Currency.getInstance(Locale.getDefault()).getCurrencyCode();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(22);

        setWillNotDraw(false);
    }

    public void setValues(String isoCurrency, ArrayList<PieSector> sectors) {
        this.isoCurrency = isoCurrency;
        this.sectors.clear();
        removeAllViews();
        float total = 0;
        float max = Float.MIN_VALUE;
        PieSector others = new PieSector(-1, 0);
        for (PieSector sector : sectors) {
            total += sector.value;
            max = Math.max(sector.value, max);
        }

        for (PieSector sector : sectors) {
            float sweepAngle = 360 * (sector.value / total);
            if(sweepAngle > MIN_SWEEP_ANGLE) {
                PieSector add = new PieSector(sector.id, sector.value);
                add.color = sector.color;
                add.resourceId = sector.resourceId;
                add.title = sector.title;
                add.sweepAngle = sweepAngle;
                add.percentage = Math.round((sector.value / total) * 100);
                add.factor = sector.value / max;
                this.sectors.add(add);
            } else {
                others.value += sector.value;
                others.resourceId.add(sector.resourceId.get(0));
            }
        }

        if(others.value > 0.01f) {
            others.isGroup = true;
            others.color = Color.BLACK;
            others.sweepAngle = 360 * (others.value / total);
            others.percentage = Math.round((others.value / total) * 100);
            others.factor = others.value / max;
            this.sectors.add(others);
        }

        Collections.sort(this.sectors, new PieSector.iComparator());

        if(total > 0.01) {
            valueText.setText(Utils.formatCurrencyDec(isoCurrency, total));
        } else {
            valueText.setText("Not Enough Data!");
        }
        addView(valueText);
        for (PieSector sector : this.sectors) {
            addView(new SectorViewGroup(getContext(), sector));
        }

        requestLayout();
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Rect rect = new Rect();
        this.getDrawingRect(rect);
        rectPie = new RectF(rect);
        float radius = pieHeight / 2;
        radius -= 12;
        rectPie.left = rect.centerX() - radius;
        rectPie.top = (pieHeight / 2) - radius;
        rectPie.right = rect.centerX() + radius;
        rectPie.bottom = (pieHeight / 2) + radius;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float temp = 0;
        if(sectors.size() > 0) {
            temp = 360 + 90 - (sectors.get(0).sweepAngle - MIN_SWEEP_ANGLE_TO_DRAW)/2;
        }
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
        public String title;
        public ArrayList<Integer> resourceId;
        public int color;
        public int percentage;
        public float sweepAngle;
        public float factor;
        public boolean isGroup = false;

        public PieSector(long id, float value) {
            this.id = id;
            this.value = value;
            this.resourceId = new ArrayList<>();
        }

        public static class iComparator implements Comparator<PieSector> {
            @Override
            public int compare(PieSector o, PieSector o1) {
                return (o.value == o1.value)
                        ? 0
                        : ((o.value < o1.value) ? 1 : -1);
            }
        }
    }

    public class SectorViewGroup extends LinearLayout {
        Paint paint;
        PieSector sector;
        public SectorViewGroup(Context context, PieSector sector) {
            super(context);

            this.sector = sector;

            setOrientation(HORIZONTAL);
            setGravity(Gravity.CENTER_VERTICAL);
            setWeightSum(1.0f);

            View view = LayoutInflater.from(context).inflate(R.layout.view_pie_sector_bar, this);

            ImageView sectorIcon = (ImageView) view.findViewById(R.id.icon);
            TextView titleText = (TextView) view.findViewById(R.id.title);
            FlowLayout otherLayout = (FlowLayout) view.findViewById(R.id.others);
            TextView percentageText = (TextView) view.findViewById(R.id.percentage);
            TextView valueText = (TextView) view.findViewById(R.id.value);

            SpannableString percentageSpan = new SpannableString(""+sector.percentage+"%");
            int perPos = percentageSpan.toString().lastIndexOf("%");
            percentageSpan.setSpan(new RelativeSizeSpan(0.6f), perPos, perPos+1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            percentageText.setText(percentageSpan);
            valueText.setText(Utils.formatCurrencyDec(isoCurrency, sector.value));

            if(sector.isGroup) {
                otherLayout.setVisibility(VISIBLE);
                titleText.setVisibility(GONE);
                sectorIcon.setVisibility(GONE);
                for(Integer resource : sector.resourceId) {
                    ImageView icon = new ImageView(context);
                    icon.setImageResource(resource);
                    icon.setColorFilter(Color.WHITE);
                    otherLayout.addView(icon);
                }
            } else {
                titleText.setVisibility(VISIBLE);
                sectorIcon.setVisibility(VISIBLE);
                otherLayout.setVisibility(GONE);
                titleText.setText(sector.title);
                sectorIcon.setImageResource(sector.resourceId.get(0));
            }

            paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setColor(sector.color);

            int color = (Integer) new ArgbEvaluator().evaluate(0.6f, sector.color, Color.WHITE);
            setBackgroundColor(color);
            setWillNotDraw(false);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            canvas.drawRect(0, 0, getWidth()*sector.factor, getHeight(), paint);
        }
    }
}
