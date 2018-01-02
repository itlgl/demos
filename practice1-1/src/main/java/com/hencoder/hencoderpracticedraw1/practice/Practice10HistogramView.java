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
        int x = 100;
        int y = 600;
        int rectWidth = 80;
        final String[] names = new String[]{"Froyo", "GB", "ICS", "JB", "KitKat", "L", "M"};
        final float[] datas = new float[]{1, 10, 10, 200, 350, 400, 200};

        canvas.drawLine(x, y, x, 100, linePaint);
        canvas.drawLine(x, y, x + 800, y, linePaint);
        for(int i=0,len=names.length;i<len;i++) {
            // canvas.drawRect();
        }
    }
}
