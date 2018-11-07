package com.hencoder.hencoderpracticedraw7.practice.practice06;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice06KeyframeLayout extends RelativeLayout {
    Practice06KeyframeView view;
    Button animateBt;

    public Practice06KeyframeLayout(Context context) {
        super(context);
    }

    public Practice06KeyframeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice06KeyframeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "// 使用 Keyframe.ofFloat() 来为 view 的 progress 属性创建关键帧\n" +
                        "// 初始帧：progress 为 0\n" +
                        "// 时间进行到一般：progress 为 100\n" +
                        "// 结束帧：progress 回落到 80\n" +
                        "// 使用 PropertyValuesHolder.ofKeyframe() 来把关键帧拼接成一个完整的属性动画方案\n" +
                        "// 使用 ObjectAnimator.ofPropertyValuesHolder() 来创建动画\n" +
                        "Keyframe kf1 = Keyframe.ofFloat(0, 0);\n" +
                        "Keyframe kf2 = Keyframe.ofFloat(0.5f, 100);\n" +
                        "Keyframe kf3 = Keyframe.ofFloat(1, 80);\n" +
                        "PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe(\"progress\", kf1, kf2, kf3);\n" +
                        "ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, pvh);\n" +
                        "animator.setDuration(2000);\n" +
                        "animator.start();";
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

        view = (Practice06KeyframeView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用 Keyframe.ofFloat() 来为 view 的 progress 属性创建关键帧
                // 初始帧：progress 为 0
                // 时间进行到一般：progress 为 100
                // 结束帧：progress 回落到 80
                // 使用 PropertyValuesHolder.ofKeyframe() 来把关键帧拼接成一个完整的属性动画方案
                // 使用 ObjectAnimator.ofPropertyValuesHolder() 来创建动画
                Keyframe kf1 = Keyframe.ofFloat(0, 0);
                Keyframe kf2 = Keyframe.ofFloat(0.5f, 100);
                Keyframe kf3 = Keyframe.ofFloat(1, 80);
                PropertyValuesHolder pvh = PropertyValuesHolder.ofKeyframe("progress", kf1, kf2, kf3);
                ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, pvh);
                animator.setDuration(2000);
                animator.start();
            }
        });
    }
}
