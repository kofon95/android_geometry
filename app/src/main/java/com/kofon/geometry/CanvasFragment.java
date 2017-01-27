package com.kofon.geometry;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kofon.geometry.drawable.circle.BackgroundDrawableView;
import com.kofon.geometry.drawable.circle.CurvesDrawableView;

public class CanvasFragment extends Fragment {

    private CurvesDrawableView mLines;
    private BackgroundDrawableView mBackground;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_canvas, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBackground = (BackgroundDrawableView)view.findViewById(R.id.background_canvas);
        mLines = (CurvesDrawableView) view.findViewById(R.id.simple_canvas);
        mBackground.radiusOffset = mLines.radiusOffset = -10;
        mBackground.init(view);
        mLines.init(view);
    }

    public void setStrokeStep(int step){
        if (step < 1) throw new IllegalArgumentException("Stroke-step cannot be less than 1");
        mBackground.strokeOnRingStep = step;
        mBackground.invalidate();
    }
    public int getStrokeStep(){
        return mBackground.strokeOnRingStep;
    }

    public void setResultListener(CurvesDrawableView.IResult result){
        mLines.setPostDrawer(result);
    }

    public double getRoundAngle() {
        return mLines.roundAngle;
    }
    public void setRoundAngle(double roundAngle) {
        mLines.roundAngle = roundAngle;
    }

    public boolean roundAngle() {
        return mLines.doRound;
    }
    public void roundAngle(boolean round) {
        mLines.doRound = round;
    }

    public void resetAll() {
        mLines.reset();
    }
}
