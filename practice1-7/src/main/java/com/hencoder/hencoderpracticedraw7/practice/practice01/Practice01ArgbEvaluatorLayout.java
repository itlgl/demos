package com.hencoder.hencoderpracticedraw7.practice.practice01;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice01ArgbEvaluatorLayout extends RelativeLayout {
    Practice01ArgbEvaluatorView view;
    Button animateBt;

    public Practice01ArgbEvaluatorLayout(Context context) {
        super(context);
    }

    public Practice01ArgbEvaluatorLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice01ArgbEvaluatorLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "使用 ObjectAnimator.setEvaluator() 来设置 ArgbEvaluator，修复闪烁问题\n" +
                        "\n" +
                        "view = (Practice01ArgbEvaluatorView) findViewById(R.id.objectAnimatorView);\n" +
                        "animateBt = (Button) findViewById(R.id.animateBt);\n" +
                        "\n" +
                        "animateBt.setOnClickListener(new OnClickListener() {\n" +
                        "    @Override\n" +
                        "    public void onClick(View v) {\n" +
                        "        ObjectAnimator animator = ObjectAnimator.ofInt(view, \"color\", 0xffff0000, 0xff00ff00);\n" +
                        "        // 在这里使用 ObjectAnimator.setEvaluator() 来设置 ArgbEvaluator，修复闪烁问题\n" +
                        "        animator.setEvaluator(new ArgbEvaluator());\n" +
                        "        animator.setInterpolator(new LinearInterpolator());\n" +
                        "        animator.setDuration(2000);\n" +
                        "        animator.start();\n" +
                        "    }\n" +
                        "});";
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

        view = (Practice01ArgbEvaluatorView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofInt(view, "color", 0xffff0000, 0xff00ff00);
                // 在这里使用 ObjectAnimator.setEvaluator() 来设置 ArgbEvaluator，修复闪烁问题
                animator.setEvaluator(new ArgbEvaluator());
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(2000);
                animator.start();
            }
        });
    }
}
