package com.hencoder.hencoderpracticedraw7.practice;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice05AnimatorSetLayout extends RelativeLayout {
    View view;
    Button animateBt;

    public Practice05AnimatorSetLayout(Context context) {
        super(context);
    }

    public Practice05AnimatorSetLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice05AnimatorSetLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "view.setTranslationX(-200f);\n" +
                        "\n" +
                        "ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, \"alpha\", 0, 1);\n" +
                        "ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, \"translationX\", -200, 200);\n" +
                        "ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, \"rotation\", 0, 1080);\n" +
                        "animator1.setDuration(3000);\n" +
                        "animator3.setDuration(1000);\n" +
                        "\n" +
                        "AnimatorSet animatorSet = new AnimatorSet();\n" +
                        "// 用 AnimatorSet 的方法来让三个动画协作执行\n" +
                        "// 要求 1： animator1 先执行，animator2 在 animator1 完成后立即开始\n" +
                        "// 要求 2： animator2 和 animator3 同时开始\n" +
                        "animatorSet.play(animator1).before(animator2).before(animator3);\n" +
                        "animatorSet.start();";
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

        view = findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setTranslationX(-200f);

                ObjectAnimator animator1 = ObjectAnimator.ofFloat(view, "alpha", 0, 1);
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(view, "translationX", -200, 200);
                ObjectAnimator animator3 = ObjectAnimator.ofFloat(view, "rotation", 0, 1080);
                animator1.setDuration(3000);
                animator3.setDuration(1000);

                AnimatorSet animatorSet = new AnimatorSet();
                // 用 AnimatorSet 的方法来让三个动画协作执行
                // 要求 1： animator1 先执行，animator2 在 animator1 完成后立即开始
                // 要求 2： animator2 和 animator3 同时开始
                animatorSet.play(animator1).before(animator2).before(animator3);
                animatorSet.start();
            }
        });
    }
}
