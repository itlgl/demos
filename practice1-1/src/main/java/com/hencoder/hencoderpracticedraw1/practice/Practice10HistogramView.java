package com.hencoder.hencoderpracticedraw1.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice10HistogramView extends View {

    Paint textPaint = new Paint();
    Paint rectPaint = new Paint();
    Paint linePaint = new Paint();

    {
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(35);
        rectPaint.setColor(Color.GREEN);
        linePaint.setColor(Color.WHITE);
    }

    public Practice10HistogramView(Context context) {
        super(context);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice10HistogramView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

//        综合练习
//        练习内容：使用各种 Canvas.drawXXX() 方法画直方图
        final int x = 100;
        final int y = 600;
        final int rectWidth = 80;
        final int rectSpace = 40;
        final String[] names = new String[]{"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};
        final float[] datas = new float[]{1, 10, 10, 200, 350, 400, 200};

        // 画坐标系
        canvas.drawLine(x, y, x, 100, linePaint);
        canvas.drawLine(x, y, x + 900, y, linePaint);
        // 画直方图
        for (int i = 0, len = datas.length; i < len; i++) {
            float left = x + rectSpace + i * (rectWidth + rectSpace);
            float top = y - datas[i];
            float right = left + rectWidth;
            float bottom = y;
            canvas.drawRect(left, top, right, bottom, rectPaint);
        }
        // 画文字
        for (int i = 0, len = names.length; i < len; i++) {
            float textSize = textPaint.measureText(names[i]);
            float temx = x + rectSpace + i * (rectWidth + rectSpace) + rectWidth / 2 - textSize / 2;
            float temy = y + 40;
            canvas.drawText(names[i], temx, temy, textPaint);
        }
    }
}
