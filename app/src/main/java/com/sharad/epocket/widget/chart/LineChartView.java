package com.sharad.epocket.widget.chart;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.OvershootInterpolator;

/**
 * Created by Sharad on 08-Aug-16.
 */

public class LineChartView extends View {

    private static final int MIN_LINES = 3;
    private static final int MAX_LINES = 6;
    private static final int[] DISTANCES = { 1, 2, 5 };
    private static final float GRAPH_SMOOTHNES = 0.02f;
    private static final float RATIO = 4f / 4f;

    private Dynamics[] datapoints;
    private int numPoints = 0;
    private Paint paint = new Paint();
    private ValueAnimator valueAnimator = null;

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * Sets the y data points of the line chart. The data points are assumed to
     * be positive and equally spaced on the x-axis. The line chart will be
     * scaled so that the entire height of the view is used.
     *
     * @param newDatapoints     y values of the line chart
     * @param numPoints         number of points to draw
     */
    public void setChartData(float[] newDatapoints, int numPoints) {
        setChartData(newDatapoints);
        this.numPoints = (numPoints < this.numPoints) ? numPoints : this.numPoints;
    }

    /**
     * Sets the y data points of the line chart. The data points are assumed to
     * be positive and equally spaced on the x-axis. The line chart will be
     * scaled so that the entire height of the view is used.
     *
     * @param newDatapoints     y values of the line chart
     */
    public void setChartData(float[] newDatapoints) {
        numPoints = newDatapoints.length;
        if (datapoints == null || datapoints.length != newDatapoints.length) {
            datapoints = new Dynamics[newDatapoints.length];
            for (int i = 0; i < newDatapoints.length; i++) {
                datapoints[i] = new Dynamics();
                datapoints[i].setTargetPosition(newDatapoints[i]);
            }
        } else {
            for (int i = 0; i < newDatapoints.length; i++) {
                datapoints[i].setTargetPosition(newDatapoints[i]);
            }
        }

        /**
         * Animate the values
         */
        if(valueAnimator != null) {
            valueAnimator.cancel();
        }
        valueAnimator = ValueAnimator.ofFloat(0.0f, 1.0f);
        valueAnimator.setDuration(1500);
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                for (Dynamics dynamics : datapoints) {
                    dynamics.update(valueAnimator.getAnimatedFraction());
                }
                invalidate();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float maxValue = getMax(datapoints);
        drawBackground(canvas, maxValue);
        drawLineChart(canvas, maxValue);
    }

    private void drawBackground(Canvas canvas, float maxValue) {
        int range = getLineDistance(maxValue);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.GRAY);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(16);
        paint.setStrokeWidth(1);
        for (int y = 0; y < maxValue; y += range) {
            final int yPos = (int) getYPos(y, maxValue);

            // turn off anti alias for lines, they get crisper then
            paint.setAntiAlias(false);
            canvas.drawLine(0, yPos, getWidth(), yPos, paint);

            // turn on anti alias again for the text
            paint.setAntiAlias(true);
            canvas.drawText(String.valueOf(y), 0, yPos - 2, paint);
        }
    }

    private int getLineDistance(float maxValue) {
        long distance;
        int distanceIndex = 0;
        long distanceMultiplier = 1;
        int numberOfLines;
        if(maxValue < 1.0) {
            return 1;
        }
        do {
            distance = DISTANCES[distanceIndex] * distanceMultiplier;
            numberOfLines = (int) Math.ceil(maxValue / distance);

            distanceIndex++;
            if (distanceIndex == DISTANCES.length) {
                distanceIndex = 0;
                distanceMultiplier *= 10;

            }
        } while (numberOfLines < MIN_LINES || numberOfLines > MAX_LINES);

        return (int) distance;
    }

    private void drawLineChart(Canvas canvas, float maxValue) {
        Path path = createSmoothPath(maxValue);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(4);
        paint.setColor(0xFF33B5E5);
        paint.setAntiAlias(true);
        paint.setShadowLayer(4, 2, 2, 0x81000000);
        canvas.drawPath(path, paint);
        paint.setShadowLayer(0, 0, 0, 0);
    }

    private Path createSmoothPath(float maxValue) {

        Path path = new Path();
        path.moveTo(getXPos(0), getYPos(datapoints[0].getPosition(), maxValue));
        for (int i = 0; i < numPoints - 1; i++) {
            float thisPointX = getXPos(i);
            float thisPointY = getYPos(datapoints[i].getPosition(), maxValue);
            float nextPointX = getXPos(i + 1);
            float nextPointY = getYPos(datapoints[si(i + 1)].getPosition(), maxValue);

            float startdiffX = (nextPointX - getXPos(si(i - 1)));
            float startdiffY = (nextPointY - getYPos(datapoints[si(i - 1)].getPosition(), maxValue));
            float endDiffX = (getXPos(si(i + 2)) - thisPointX);
            float endDiffY = (getYPos(datapoints[si(i + 2)].getPosition(), maxValue) - thisPointY);

            float firstControlX = thisPointX + (GRAPH_SMOOTHNES * startdiffX);
            float firstControlY = thisPointY + (GRAPH_SMOOTHNES * startdiffY);
            float secondControlX = nextPointX - (GRAPH_SMOOTHNES * endDiffX);
            float secondControlY = nextPointY - (GRAPH_SMOOTHNES * endDiffY);

            path.cubicTo(firstControlX, firstControlY, secondControlX, secondControlY, nextPointX,
                    nextPointY);
        }
        return path;
    }

    /**
     * Given an index in datapoints, it will make sure the the returned index is
     * within the array
     *
     * @param i
     * @return
     */
    private int si(int i) {
        if (i > datapoints.length - 1) {
            return datapoints.length - 1;
        } else if (i < 0) {
            return 0;
        }
        return i;
    }

    private float getMax(Dynamics[] array) {
        float max = array[0].getMax();
        for (int i = 1; i < array.length; i++) {
            if (array[i].getMax() > max) {
                max = array[i].getMax();
            }
        }
        return max;
    }

    private float getYPos(float value, float maxValue) {
        float height = getHeight() - getPaddingTop() - getPaddingBottom();

        // scale it to the view size
        value = (value / maxValue) * height;

        // invert it so that higher values have lower y
        value = height - value;

        // offset it to adjust for padding
        value += getPaddingTop();

        return value;
    }

    private float getXPos(float value) {
        float width = getWidth() - getPaddingLeft() - getPaddingRight();
        float maxValue = datapoints.length - 1;

        // scale it to the view size
        value = (value / maxValue) * width;

        // offset it to adjust for padding
        value += getPaddingLeft();

        return value;
    }




    public class Dynamics {
        /** The position the dynamics should to be at */
        private float targetPosition = 0;

        /** The current position of the dynamics */
        private float position = 0;

        /** The origin of the dynamics */
        private float origin = 0;

        public void setTargetPosition(float targetPosition) {
            this.origin = this.position;
            this.targetPosition = targetPosition;
        }

        public void update(float factor) {
            position = origin + (targetPosition - origin) * factor;
        }

        public float getPosition() {
            return position;
        }

        public float getMax() {
            return Math.max(targetPosition, position);
        }
    }
}
