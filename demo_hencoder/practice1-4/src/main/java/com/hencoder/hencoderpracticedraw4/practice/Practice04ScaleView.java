package com.hencoder.hencoderpracticedraw4.practice;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice04ScaleView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point1 = new Point(200, 200);
    Point point2 = new Point(600, 200);

    public Practice04ScaleView(Context context) {
        super(context);
    }

    public Practice04ScaleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice04ScaleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tipMsg = "canvas的缩放：\n" +
                        "Canvas.scale(float sx, float sy, float px, float py)\n" +
                        "参数里的 sx sy 是横向和纵向的放缩倍数； px py 是放缩的轴心。\n" +
                        "示例代码：\n" +
                        "canvas.save();\n" +
                        "canvas.scale(1.3f, 1.3f, point1.x + bitmap.getWidth() / 2, point1.y + bitmap.getHeight() / 2);\n" +
                        "canvas.drawBitmap(bitmap, point1.x, point1.y, paint);\n" +
                        "canvas.restore()\n\n" +
                        "Canvas.scale(float sx, float sy, float px, float py)函数的内部实现如下：\n" +
                        "translate(px, py);\n" +
                        "scale(sx, sy);\n" +
                        "translate(-px, -py);";
                new AlertDialog.Builder(getContext())
                        .setTitle("tip")
                        .setMessage(tipMsg)
                        .setNegativeButton("ok", null)
                        .show();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.scale(1.3f, 1.3f, point1.x + bitmap.getWidth() / 2, point1.y + bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.restore();

        canvas.save();
        canvas.scale(0.7f, 1.3f, point2.x + bitmap.getWidth() / 2, point2.y + bitmap.getHeight() / 2);
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
        canvas.restore();
    }
}
