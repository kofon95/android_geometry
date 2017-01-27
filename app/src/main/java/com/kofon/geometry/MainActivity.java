package com.kofon.geometry;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.kofon.geometry.drawable.circle.CurvesDrawableView;

public class MainActivity extends AppCompatActivity {
    private TextView mTextViewDegree;
    private TextView mTextViewSin;
    private TextView mTextViewCos;
    private CanvasFragment mCanvasFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CheckBox checkBox = (CheckBox) findViewById(R.id.round_checkBox);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCanvasFragment.roundAngle(b);
            }
        });

        mTextViewDegree = (TextView) findViewById(R.id.degree_text);
        mTextViewSin = (TextView) findViewById(R.id.sin_text);
        mTextViewCos = (TextView) findViewById(R.id.cos_text);

        mCanvasFragment = ((CanvasFragment) getSupportFragmentManager().findFragmentById(R.id.canvas_fragment));
        mCanvasFragment.setResultListener(new CurvesDrawableView.IResult() {
            @Override
            public void result(double degree, double sin, double cos) {
                if (Double.isNaN(degree)){
                    mTextViewDegree.setText("NaN");
                    mTextViewSin.setText("NaN");
                    mTextViewCos.setText("NaN");
                } else {
                    mTextViewDegree.setText(String.valueOf(Math.floor(degree*10)/10));
                    mTextViewSin.setText(String.valueOf(Math.round(sin*10)/10f));
                    mTextViewCos.setText(""+Math.round(cos*10)/10f);
                }
            }
        });

        EditText edt = (EditText) findViewById(R.id.stroke_step_editText);
        edt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                setStrokeAndRoundAngle(s);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        setStrokeAndRoundAngle(edt.getText());
    }

    private void setStrokeAndRoundAngle(CharSequence s) {
        if (s.length() <= 0) return;
        int step = Integer.valueOf(s.toString());
        if (step > 0) {
            mCanvasFragment.setStrokeStep(step);
            mCanvasFragment.setRoundAngle(step);
        }
    }

    public void resetDrawable(View view) {
        mCanvasFragment.resetAll();
    }
}
