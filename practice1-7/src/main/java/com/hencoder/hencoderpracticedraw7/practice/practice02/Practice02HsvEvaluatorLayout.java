package com.hencoder.hencoderpracticedraw7.practice.practice02;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice02HsvEvaluatorLayout extends RelativeLayout {
    Practice02HsvEvaluatorView view;
    Button animateBt;

    public Practice02HsvEvaluatorLayout(Context context) {
        super(context);
    }

    public Practice02HsvEvaluatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice02HsvEvaluatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "使用自定义的 HsvEvaluator,重写 evaluate() 方法，让颜色按照 HSV 来变化\n" +
                        "\n" +
                        "animateBt.setOnClickListener(new OnClickListener() {\n" +
                        "    @Override\n" +
                        "    public void onClick(View v) {\n" +
                        "        ObjectAnimator animator = ObjectAnimator.ofInt(view, \"color\", 0xffff0000, 0xff00ff00);\n" +
                        "        animator.setEvaluator(new HsvEvaluator()); // 使用自定义的 HsvEvaluator\n" +
                        "        animator.setInterpolator(new LinearInterpolator());\n" +
                        "        animator.setDuration(2000);\n" +
                        "        animator.start();\n" +
                        "    }\n" +
                        "});\n" +
                        "\n" +
                        "private class HsvEvaluator implements TypeEvaluator<Integer> {\n" +
                        "\n" +
                        "   float[] hsvStart = new float[3];\n" +
                        "   float[] hsvEnd = new float[3];\n" +
                        "   float[] hsvCurr = new float[3];\n" +
                        "\n" +
                        "   // 重写 evaluate() 方法，让颜色按照 HSV 来变化\n" +
                        "   @Override\n" +
                        "   public Integer evaluate(float fraction, Integer startValue, Integer endValue) {\n" +
                        "       Color.colorToHSV(startValue, hsvStart);\n" +
                        "       Color.colorToHSV(endValue, hsvEnd);\n" +
                        "\n" +
                        "        for (int i = 0; i < 3; i++) {\n" +
                        "            hsvCurr[i] = hsvStart[i] + (hsvEnd[i] - hsvStart[i]) * fraction;\n" +
                        "        }\n" +
                        "        return Color.HSVToColor(hsvCurr);\n" +
                        "    }\n" +
                        "}";
                new AlertDialog.Builder(getContext())
                        .setTitle("tip")
                        .setMessage(tipMsg)
                        .setNegativeButton("ok", null)
                        .show();
            }
        });
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        view = (Practice02HsvEvaluatorView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
                animator.setEvaluator(new HsvEvaluator()); // 使用自定义的 HsvEvaluator
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }

    private class HsvEvaluator implements TypeEvaluator<Integer> {

        float[] hsvStart = new float[3];
        float[] hsvEnd = new float[3];
        float[] hsvCurr = new float[3];

        // 重写 evaluate() 方法，让颜色按照 HSV 来变化
        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            Color.colorToHSV(startValue, hsvStart);
            Color.colorToHSV(endValue, hsvEnd);

            for (int i = 0; i < 3; i++) {
                hsvCurr[i] = hsvStart[i] + (hsvEnd[i] - hsvStart[i]) * fraction;
            }
            return Color.HSVToColor(hsvCurr);
        }
    }
}
