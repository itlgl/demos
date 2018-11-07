package com.hencoder.hencoderpracticedraw7.practice.practice03;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice03OfObjectLayout extends RelativeLayout {
    Practice03OfObjectView view;
    Button animateBt;

    public Practice03OfObjectLayout(Context context) {
        super(context);
    }

    public Practice03OfObjectLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice03OfObjectLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "使用自定义的 PointFEvaluator,重写 evaluate() 方法，让 PointF 可以作为属性来做动画\n" +
                        "\n" +
                        "animateBt.setOnClickListener(new OnClickListener() {\n" +
                        "    @Override\n" +
                        "    public void onClick(View v) {\n" +
                        "        ObjectAnimator animator = ObjectAnimator.ofObject(view, \"position\",\n" +
                        "                new PointFEvaluator(), new PointF(0, 0), new PointF(1, 1));\n" +
                        "        animator.setInterpolator(new LinearInterpolator());\n" +
                        "        animator.setDuration(1000);\n" +
                        "        animator.start();\n" +
                        "    }\n" +
                        "});\n" +
                        "\n" +
                        "private class PointFEvaluator implements TypeEvaluator<PointF> {\n" +
                        "\n" +
                        "    PointF point = new PointF();\n" +
                        "\n" +
                        "    // 重写 evaluate() 方法，让 PointF 可以作为属性来做动画\n" +
                        "    @Override\n" +
                        "    public PointF evaluate(float fraction, PointF startValue, PointF endValue) {\n" +
                        "        point.x = startValue.x + (endValue.x - startValue.x) * fraction;\n" +
                        "        point.y = startValue.y + (endValue.y - startValue.y) * fraction;\n" +
                        "        return point;\n" +
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

        view = (Practice03OfObjectView) findViewById(R.id.objectAnimatorView);
        animateBt = (Button) findViewById(R.id.animateBt);

        animateBt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ObjectAnimator animator = ObjectAnimator.ofObject(view, "position",
                        new PointFEvaluator(), new PointF(0, 0), new PointF(1, 1));
                animator.setInterpolator(new LinearInterpolator());
                animator.setDuration(1000);
                animator.start();
            }
        });
    }

    private class PointFEvaluator implements TypeEvaluator<PointF> {

        PointF point = new PointF();

        // 重写 evaluate() 方法，让 PointF 可以作为属性来做动画
        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {
            point.x = startValue.x + (endValue.x - startValue.x) * fraction;
            point.y = startValue.y + (endValue.y - startValue.y) * fraction;
            return point;
        }
    }
}
