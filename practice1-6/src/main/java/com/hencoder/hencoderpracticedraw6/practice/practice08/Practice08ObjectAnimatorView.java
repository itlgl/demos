package com.hencoder.hencoderpracticedraw6.practice.practice08;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import static com.hencoder.hencoderpracticedraw6.Utils.dpToPixel;

public class Practice08ObjectAnimatorView extends View {
    final float radius = dpToPixel(80);

    RectF arcRectF = new RectF();
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // TODO 为 progress 添加 getter 和 setter 方法（setter 方法记得加 invalidate()）
    float progress = 0;

    public Practice08ObjectAnimatorView(Context context) {
        super(context);
    }

    public Practice08ObjectAnimatorView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice08ObjectAnimatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setTextSize(dpToPixel(40));
        paint.setTextAlign(Paint.Align.CENTER);// 使用这个设置，绘制的文字就是左右居中的

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "这个View是如何绘制的？\n" +
                        "1、 使用canvas.drawArc方法绘制一个弧形\n" +
                        "绘制弧形的时候设置一下paint的宽度，颜色，线头的形状等，可以画出比较漂亮的弧形形状\n" +
                        "2、 使用canvas.drawText绘制文字\n" +
                        "paint.setTextAlign(Paint.Align.CENTER)\n" +
                        "这段代码可以设置canvas绘制的text居中对齐，这样就不用知道文字的长度，文字总是居中对齐的\n" +
                        "代码：\n" +
                        "RectF arcRectF = new RectF();\n" +
                        "Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);\n" +
                        "\n" +
                        "public void onDraw(Canvas canvas) {\n" +
                        "    super.onDraw(canvas);\n" +
                        "\n" +
                        "    float centerX = getWidth() / 2;\n" +
                        "    float centerY = getHeight() / 2;\n" +
                        "\n" +
                        "    paint.setColor(Color.parseColor(\"#E91E63\"));\n" +
                        "    paint.setStyle(Paint.Style.STROKE);\n" +
                        "    paint.setStrokeCap(Paint.Cap.ROUND);\n" +
                        "    paint.setStrokeWidth(dpToPixel(15));\n" +
                        "    arcRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);\n" +
                        "    canvas.drawArc(arcRectF, 135, progress * 2.7f, false, paint);\n" +
                        "\n" +
                        "    paint.setColor(Color.WHITE);\n" +
                        "    paint.setStyle(Paint.Style.FILL);\n" +
                        "    canvas.drawText((int) progress + \"%\", centerX, centerY - (paint.ascent() + paint.descent()) / 2, paint);\n" +
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
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2;
        float centerY = getHeight() / 2;

        paint.setColor(Color.parseColor("#E91E63"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(dpToPixel(15));
        arcRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(arcRectF, 135, progress * 2.7f, false, paint);

        paint.setColor(Color.WHITE);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawText((int) progress + "%", centerX, centerY - (paint.ascent() + paint.descent()) / 2, paint);
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
        invalidate();
    }
}
