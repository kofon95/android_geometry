package com.kofon.geometry.drawable.circle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.kofon.geometry.drawable.IDrawable;

/**
 * Created by kofon on 27.01.17.
 */
public class CurvesDrawableView extends View implements IDrawable {
    public int radiusOffset;
    private TextView mTextViewDegree;
    private TextView mTextViewSin;
    private TextView mTextViewCos;
    private Point mWrapperSize;
    private int mCenterX;
    private int mCenterY;
    private boolean mDoDraw;
    private int mRadius;

    public void setPostDrawer(IResult result) {
        mPostDrawer = result;
    }

    IResult mPostDrawer;

    // Paints
    private final Paint mCircle;
    private final Paint mCoordinateSystem;
    private final Paint mCenterPoint;
    private final Paint mBackground;
    private final Paint mLineFromCenterToRing;
    private final Paint mLineFromOxToRing;
    private final Paint mLineFromOyToRing;
    private final Paint mPointOnCircle;
    private float mTouchPosX;
    private float mTouchPosY;
    public double roundAngle;
    public boolean doRound;

    public CurvesDrawableView(Context context, AttributeSet attrs) {
        super(context, attrs);

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

        mCoordinateSystem = new Paint();
        mCoordinateSystem.setStyle(Paint.Style.FILL);
        mCoordinateSystem.setColor(Color.BLACK);
        mCoordinateSystem.setStrokeWidth(2);

        mLineFromCenterToRing = new Paint();
        mLineFromCenterToRing.setStyle(Paint.Style.FILL);
        mLineFromCenterToRing.setColor(Color.MAGENTA);
        mLineFromCenterToRing.setStrokeWidth(3);

        mLineFromOxToRing = new Paint();
        mLineFromOxToRing.setStyle(Paint.Style.FILL);
        mLineFromOxToRing.setColor(Color.YELLOW);
        mLineFromOxToRing.setStrokeWidth(3);

        mLineFromOyToRing = new Paint();
        mLineFromOyToRing.setStyle(Paint.Style.FILL);
        mLineFromOyToRing.setColor(Color.GREEN);
        mLineFromOyToRing.setStrokeWidth(3);

        mPointOnCircle = new Paint();
        mPointOnCircle.setStyle(Paint.Style.FILL);
        mPointOnCircle.setColor(Color.MAGENTA);
        mPointOnCircle.setStrokeWidth(6);
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
    public boolean onTouchEvent(MotionEvent event) {
        mDoDraw = true;
        mTouchPosX = event.getX();
        mTouchPosY = event.getY();
//        mTouchMotionState = event.getAction() & MotionEvent.ACTION_MASK;

        invalidate();
        return true;
    }

    @Override
    public void onDraw(Canvas canvas) {
        if (isInEditMode()) return;

        if (!mDoDraw){
            if (mPostDrawer != null) {
                mPostDrawer.result(Double.NaN, Double.NaN, Double.NaN);
            }
            return;
        }

        final int cx = mCenterX;
        final int cy = mCenterY;
        final int r = mRadius;

        double x = (double) mTouchPosX - cx;
        double y = (double) cy - mTouchPosY;
        double c = Math.sqrt(x * x + y * y);
        double u = Math.asin(y / c);
        if (x < 0) u = Math.PI-u;
        else if (x >= 0 && y < 0) u = 2*Math.PI + u;
        double degree = u * 180 / Math.PI;

        if (doRound){
            // calc rounded degree
            final double rd = roundAngle;    // rounded value
            degree = Math.round(degree / rd) * rd;
            u = degree / (180 / Math.PI);

            mTouchPosX = (float) (c * Math.cos(u));
            mTouchPosY = (float) (c * Math.sin(u));
            mTouchPosX = mTouchPosX + cx;
            mTouchPosY = cy - mTouchPosY;

            y = (double) mTouchPosX - cx;
            x = (double) cy - mTouchPosY;
        }

        float rx = r*(float)Math.cos(u);
        float ry = (float)Math.sqrt(r*r - rx*rx);
        if (u > Math.PI) ry = -ry;

        float rDeltaX = cx + rx;
        float rDeltaY = cy - ry;

        // ring to OX
        canvas.drawLine(rDeltaX, cy, rDeltaX, rDeltaY, mLineFromOxToRing);
        // ring to OY
        canvas.drawLine(cx, rDeltaY, rDeltaX, rDeltaY, mLineFromOyToRing);
        // line from center to ring
        canvas.drawLine(cx, cy, rDeltaX, rDeltaY, mLineFromCenterToRing);

        if (mPostDrawer != null)
            mPostDrawer.result(degree, y/c, x/c);
    }

    public void reset(){
        mDoDraw = false;
        invalidate();
    }

    public interface IResult {
        void result(double degree, double sin, double cos);
    }
}
