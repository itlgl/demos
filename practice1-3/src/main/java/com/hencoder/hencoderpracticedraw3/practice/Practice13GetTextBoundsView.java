package com.hencoder.hencoderpracticedraw3.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice13GetTextBoundsView extends View {
    Paint paint1 = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint paint2 = new Paint(Paint.ANTI_ALIAS_FLAG);
    String[] texts = new String[]{"A", "a", "J", "j", "Â", "â"};
    int[] yOffsets = {0, 0, 0, 0, 0, 0};
    int top = 200;
    int bottom = 400;

    public Practice13GetTextBoundsView(Context context) {
        super(context);
    }

    public Practice13GetTextBoundsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice13GetTextBoundsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint1.setStyle(Paint.Style.STROKE);
        paint1.setStrokeWidth(20);
        paint1.setColor(Color.parseColor("#E91E63"));
        paint2.setTextSize(160);

        Rect textBounds = new Rect();
        for(int i = 0;i < texts.length;i++) {
            paint2.getTextBounds(texts[i], 0, 1, textBounds);
            yOffsets[i] = - (textBounds.top + textBounds.bottom) / 2;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 Paint.getTextBounds() 计算出文字的显示区域
        // 然后计算出文字的绘制位置，从而让文字上下居中
        // 这种居中算法的优点是，可以让文字精准地居中，分毫不差
        canvas.drawRect(50, top, getWidth() - 50, bottom, paint1);

        int middle = (top + bottom) / 2;
        canvas.drawText(texts[0], 100, middle + yOffsets[0], paint2);
        canvas.drawText(texts[1], 200, middle + yOffsets[1], paint2);
        canvas.drawText(texts[2], 300, middle + yOffsets[2], paint2);
        canvas.drawText(texts[3], 400, middle + yOffsets[3], paint2);
        canvas.drawText(texts[4], 500, middle + yOffsets[4], paint2);
        canvas.drawText(texts[5], 600, middle + yOffsets[5], paint2);
    }
}