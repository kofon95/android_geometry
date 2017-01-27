package com.kofon.geometry.drawable.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;

import com.kofon.geometry.drawable.IDrawable;

import java.util.Locale;

/**
 * Created by kofon on 27.01.17.
 */
public class BackgroundDrawableView extends View implements IDrawable {
    public int radiusOffset;
    private Point mWrapperSize;
    private int mCenterX;
    private int mCenterY;

    // Paints
    private Paint mCircle;
    private Paint mCoordinateSystem;
    private Paint mCenterPoint;
    private Paint mBackground;
    private Paint mLineFromCenterToRing;
    private Paint mLineFromOxToRing;
    private Paint mLineFromOyToRing;
    private Paint mStrokeOnRing;
    private Paint mText;
    private int mRadius;
    public int strokeOnRingStep = 1;

    public BackgroundDrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) return;
        mBackground = new Paint();
        mBackground.setStyle(Paint.Style.FILL);
        mBackground.setColor(Color.WHITE);

        mCircle = new Paint();
        mCircle.setStyle(Paint.Style.STROKE);
        mCircle.setColor(Color.BLACK);
        mCircle.setStrokeWidth(3);

        mCenterPoint = new Paint();
        mCenterPoint.setStyle(Paint.Style.FILL);
        mCenterPoint.setColor(Color.BLACK);
        mCenterPoint.setStrokeWidth(7);
        mCenterPoint.setStrokeCap(Paint.Cap.ROUND);

        mCoordinateSystem = new Paint();
        mCoordinateSystem.setColor(Color.GRAY);
        mCoordinateSystem.setStrokeWidth(2);

        mLineFromCenterToRing = new Paint();
        mLineFromCenterToRing.setColor(Color.MAGENTA);
        mLineFromCenterToRing.setStrokeWidth(3);

        mLineFromOxToRing = new Paint();
        mLineFromOxToRing.setColor(Color.YELLOW);
        mLineFromOxToRing.setStrokeWidth(3);

        mLineFromOyToRing = new Paint();
        mLineFromOyToRing.setColor(Color.GREEN);
        mLineFromOyToRing.setStrokeWidth(3);

        mStrokeOnRing = new Paint();
        mStrokeOnRing.setColor(Color.rgb(0x2B, 0x91, 0xAF));
        mStrokeOnRing.setStyle(Paint.Style.STROKE);
        mStrokeOnRing.setStrokeWidth(2);

        mText = new Paint();
        mText.setColor(Color.rgb(0x2B, 0x91, 0xAF));
        mText.setStyle(Paint.Style.STROKE);
//        mText.setStrokeWidth(2);
        mText.setTextSize(50f);
    }


    public void init(final View container) {
        if (isInEditMode()) return;
        // set fragment size (mWrapperSize)
        container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWrapperSize = new Point(container.getWidth(), container.getMeasuredHeightAndState());
                mCenterX = mWrapperSize.x / 2;
                mCenterY = mWrapperSize.y / 2;
                mRadius = Math.min(mCenterX, mCenterY) + radiusOffset;
            }
        });
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) return;

        canvas.drawColor(mBackground.getColor());
        final int cx = mCenterX;
        final int cy = mCenterY;
        final int r = mRadius;

        // center point
        canvas.drawPoint(cx, cy, mCenterPoint);

        // circle
        canvas.drawCircle(cx, cy, r, mCircle);

        // coordinate system
        canvas.drawLine(cx, 0, cx, mWrapperSize.y, mCoordinateSystem);
        canvas.drawLine(0, cy, mWrapperSize.x, cy, mCoordinateSystem);

        // make strokes
        Path path = new Path();
        if (90 % strokeOnRingStep == 0) {
            for (int i = 0; i < 90; i += strokeOnRingStep) {
                double j = i / (180 / Math.PI);
                final int l = 10;  // length of each line (stroke)
                float x1 = (r - l) * (float) Math.cos(j);
                float y1 = (r - l) * (float) Math.sin(j);
                float x2 = (r) * (float) Math.cos(j);
                float y2 = (r) * (float) Math.sin(j);

                // quarters

                path.moveTo(cx + x1, cy - y1);
                path.lineTo(cx + x2, cy - y2);

                path.moveTo(cx + y1, cy + x1);
                path.lineTo(cx + y2, cy + x2);

                path.moveTo(cx - x1, cy + y1);
                path.lineTo(cx - x2, cy + y2);

                path.moveTo(cx - y1, cy - x1);
                path.lineTo(cx - y2, cy - x2);
            }
        } else {
            for (int i = 0; i < 360; i += strokeOnRingStep) {
                double j = i / (180 / Math.PI);
                final int l = 10;  // length of each line (stroke)
                float x1 = (r - l) * (float) Math.cos(j);
                float y1 = (r - l) * (float) Math.sin(j);
                float x2 = (r) * (float) Math.cos(j);
                float y2 = (r) * (float) Math.sin(j);

                // quarters

                path.moveTo(cx + x1, cy - y1);
                path.lineTo(cx + x2, cy - y2);
            }
        }
        // draw strokes on ring
        canvas.drawPath(path, mStrokeOnRing);


        canvas.drawText("x", cx+r-60, cy, mText);
        canvas.drawText("y", cx, cy-r+50, mText);
    }
}
