package com.hencoder.hencoderpracticedraw7.practice;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.AlertDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.hencoder.hencoderpracticedraw7.R;

public class Practice04PropertyValuesHolderLayout extends RelativeLayout {
    View view;
    Button animateBt;

    public Practice04PropertyValuesHolderLayout(Context context) {
        super(context);
    }

    public Practice04PropertyValuesHolderLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice04PropertyValuesHolderLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "// 使用 PropertyValuesHolder.ofFloat() 来创建不同属性的动画值方案\n" +
                        "// 第一个： scaleX 从 0 到 1\n" +
                        "// 第二个： scaleY 从 0 到 1\n" +
                        "// 第三个： alpha 从 0 到 1\n" +
                        "PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat(\"scaleX\", 0, 1);\n" +
                        "PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat(\"scaleY\", 0, 1);\n" +
                        "PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat(\"alpha\", 0, 1);\n" +
                        "// 然后，用 ObjectAnimator.ofPropertyValuesHolder() 把三个属性合并，创建 Animator 然后执行\n" +
                        "ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2, pvh3).start();";
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
                // 使用 PropertyValuesHolder.ofFloat() 来创建不同属性的动画值方案
                // 第一个： scaleX 从 0 到 1
                // 第二个： scaleY 从 0 到 1
                // 第三个： alpha 从 0 到 1
                PropertyValuesHolder pvh1 = PropertyValuesHolder.ofFloat("scaleX", 0, 1);
                PropertyValuesHolder pvh2 = PropertyValuesHolder.ofFloat("scaleY", 0, 1);
                PropertyValuesHolder pvh3 = PropertyValuesHolder.ofFloat("alpha", 0, 1);
                // 然后，用 ObjectAnimator.ofPropertyValuesHolder() 把三个属性合并，创建 Animator 然后执行
                ObjectAnimator.ofPropertyValuesHolder(view, pvh1, pvh2, pvh3).start();
            }
        });
    }
}
