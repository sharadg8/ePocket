package com.sharad.epocket.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * Created by Sharad on 23-Jun-16.
 */

public class ZigZagCardView extends FrameLayout {
    private Paint paint;
    private Path path;
    private final int DESIRED_DUTY_CYCLE = 10;
    private float zigzagWidth;
    private float zigzagHeight;

    public ZigZagCardView(Context context) {
        this(context, null, 0);
    }

    public ZigZagCardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZigZagCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setWillNotDraw(false);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //paint.setColor(ContextCompat.getColor(context, R.color.transaction_transfer));
        paint.setColor(Color.WHITE);
        path = new Path();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        int count = w / DESIRED_DUTY_CYCLE;
        zigzagWidth = w / (count + 0.5f);
        count += 3;
        path.reset();
        path.moveTo(0, zigzagHeight);
        boolean isZig = true;
        float x = -zigzagWidth;
        float y = 0;
        zigzagHeight = zigzagWidth;
        for(int i=0; i<count; i++) {
            y = isZig ? 0 : zigzagHeight;
            y += getPaddingTop();
            path.lineTo(x, y);
            x += zigzagWidth;
            isZig = !isZig;
        }
        isZig = !isZig;
        for(int i=0; i<count; i++) {
            y = isZig ? h : h - zigzagHeight;
            y -= getPaddingBottom();
            path.lineTo(x, y);
            x -= zigzagWidth;
            isZig = !isZig;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }
}
