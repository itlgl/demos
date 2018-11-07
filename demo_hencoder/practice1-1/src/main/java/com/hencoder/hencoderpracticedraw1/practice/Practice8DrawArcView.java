package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice8DrawArcView extends View {

    Paint textPaint = new Paint();
    Paint paint1 = new Paint();
    Paint paint2 = new Paint();
    Paint paint3 = new Paint();
    {
        paint3.setStyle(Paint.Style.STROKE);
        textPaint.setTextSize(50);
    }

    public Practice8DrawArcView(Context context) {
        super(context);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice8DrawArcView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        练习内容：使用 canvas.drawArc() 方法画弧形和扇形
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawArc(200, 100, 800, 500, -110, 100, true, paint1); // 绘制扇形
            canvas.drawArc(200, 100, 800, 500, 20, 140, false, paint2); // 绘制弧形
            canvas.drawArc(200, 100, 800, 500, 180, 60, false, paint3); // 绘制不封口的弧形
        } else {
            canvas.drawText("device api < 21,can't use drawArc api.", 100, 100, textPaint);
        }
    }
}
