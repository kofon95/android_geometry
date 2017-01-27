package com.kofon.geometry;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements IDrawable {

    private final Paint mCircle;
    private final Paint mCoordinateSystem;
    private final Paint mCenterPoint;
    private final Paint mBackground;
    private final Paint mLineFromCenter;
    private final Paint mPointOnCircle;
    private Point mWrapperSize;
    private CanvasView mCanvasView;
    private boolean doDraw;
    private TextView mTextViewX;
    private TextView mTextViewY;
    private TextView mTextViewR;
    private TextView mTextViewDegree;
    private TextView mTextViewSin;
    private TextView mTextViewCos;
    private int mCenterX;
    private int mCenterY;
    private CheckBox mRoundCheckBox;

    public MainActivity(){
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

        mLineFromCenter = new Paint();
        mLineFromCenter.setStyle(Paint.Style.FILL);
        mLineFromCenter.setColor(Color.MAGENTA);
        mLineFromCenter.setStrokeWidth(3);

        mPointOnCircle = new Paint();
        mPointOnCircle.setStyle(Paint.Style.FILL);
        mPointOnCircle.setColor(Color.MAGENTA);
        mPointOnCircle.setStrokeWidth(6);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCanvasView = ((CanvasView) findViewById(R.id.simple_canvas));
        mRoundCheckBox = (CheckBox) findViewById(R.id.round_checkBox);

        // set fragment size (mWrapperSize)
        final View wrapper = findViewById(R.id.canvas_fragment);
        wrapper.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                mWrapperSize = new Point(wrapper.getWidth(), wrapper.getMeasuredHeightAndState());
                mCenterX = mWrapperSize.x / 2;
                mCenterY = mWrapperSize.y / 2;
//                Log.i("my_log", mWrapperSize.x + " x " + mWrapperSize.y);
            }
        });

        mTextViewX = (TextView) findViewById(R.id.x_text);
        mTextViewY = (TextView) findViewById(R.id.y_text);
        mTextViewR = (TextView) findViewById(R.id.r_text);
        mTextViewDegree = (TextView) findViewById(R.id.degree_text);
        mTextViewSin = (TextView) findViewById(R.id.sin_text);
        mTextViewCos = (TextView) findViewById(R.id.cos_text);
    }


    public void drawCircle(View view) {
        mTouchPosX = mCenterX;
        mTouchPosY = mCenterY;
        mCanvasView.invalidate();
    }

    @Override
    public boolean touchEvent(MotionEvent event) {
        doDraw = true;
        mTouchPosX = event.getX();
        mTouchPosY = event.getY();

        mTouchMotionState = event.getAction() & MotionEvent.ACTION_MASK;
        mCanvasView.invalidate();
        return true;
    }

    int mTouchMotionState = -1;
    float mTouchPosX;
    float mTouchPosY;

    @Override
    public void onDraw(Canvas canvas) {
        canvas.drawColor(mBackground.getColor());
        final int cx = mCenterX;
        final int cy = mCenterY;
        final int r = Math.min(cx, cy);

        // circle
        canvas.drawCircle(cx, cy, r, mCircle);
        // center point
        canvas.drawCircle(cx, cy, 3, mCenterPoint);
        // coordinate system
        canvas.drawLine(cx, 0, cx, mWrapperSize.y, mCoordinateSystem);
        canvas.drawLine(0, cy, mWrapperSize.x, cy, mCoordinateSystem);

        if (!doDraw){
            showValues(0, 0, 0, Double.NaN, 0, 0);
            return;
        }


        double x = (double) mTouchPosX - cx;
        double y = (double) cy - mTouchPosY;
        double c = Math.sqrt(x * x + y * y);
        double u = Math.asin(y / c);
        if (x < 0) u = Math.PI-u;
        else if (x >= 0 && y < 0) u = 2*Math.PI + u;
        double degree = u * 180 / Math.PI;

        if (mRoundCheckBox.isChecked()){
            // calc rounded degree
            final double rd = 15.0;    // rounded value
            degree = Math.round(degree / rd) * rd;
            u = degree / (180 / Math.PI);
            mTouchPosX = (float) (c * Math.cos(u));
            mTouchPosY = (float) (c * Math.sin(u));

            mTouchPosX = mTouchPosX + cx;
            mTouchPosY = cy - mTouchPosY;
            x = (double) mTouchPosX - cx;
            y = (double) cy - mTouchPosY;
        }

        float rx = r*(float)Math.cos(u);
        float ry = (float)Math.sqrt(r*r - rx*rx);
        if (u > Math.PI) ry = -ry;

        // point on circle's side
        canvas.drawPoint(cx+rx, cy-ry, mPointOnCircle);
        // line from center to touch position
        canvas.drawLine(cx, cy, mTouchPosX, mTouchPosY, mLineFromCenter);

        showValues(x, y, c, degree, y/c, x/c);

        switch (mTouchMotionState) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    private void showValues(double x, double y, double r, double degree, double sin, double cos) {
        mTextViewX.setText("" + Math.round(x));
        mTextViewY.setText(""+Math.round(y));
        mTextViewR.setText(""+Math.round(r));
        mTextViewDegree.setText(""+Math.floor(degree*10)/10);
        mTextViewSin.setText(""+Math.round(sin*10)/10f);
        mTextViewCos.setText(""+Math.round(cos*10)/10f);
    }
}
