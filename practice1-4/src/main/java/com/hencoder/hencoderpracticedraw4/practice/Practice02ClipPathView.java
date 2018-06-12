package com.hencoder.hencoderpracticedraw4.practice;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice02ClipPathView extends View {
    Paint paint = new Paint();
    Bitmap bitmap;
    Point point1 = new Point(200, 200);
    Point point2 = new Point(600, 200);
    Path path1, path2;

    public Practice02ClipPathView(Context context) {
        super(context);
    }

    public Practice02ClipPathView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice02ClipPathView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        path1 = new Path();
        path1.addCircle(point1.x + bitmap.getWidth() / 2, point1.y + bitmap.getHeight() / 2, bitmap.getWidth() / 2, Path.Direction.CW);

        // 当path2的Direction设置为Direction.CW，FillType为FillType.INVERSE_WINDING，clipPath是反转绘制
        path2 = new Path();
        path2.addCircle(point2.x + bitmap.getWidth() / 2, point2.y + bitmap.getHeight() / 2, bitmap.getWidth() / 2, Path.Direction.CW);
        path2.setFillType(Path.FillType.INVERSE_WINDING);



        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tipMsg = "使用的方法是canvas.clipPath()，关键是path的设置\n" +
                        "普通情况下:\n" +
                        "\tpath1 = new Path();\n" +
                        "\tpath1.addCircle(point1.x + bitmap.getWidth() / 2, point1.y + bitmap.getHeight() / 2, bitmap.getWidth() / 2, Path.Direction.CW);\n" +
                        "其实就是给path添加了一个圆形\n\n" +
                        "如果想绘制path以外的区域，path的Direction参数不改动，另外设置FillType为Path.FillType.INVERSE_WINDING即可";
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
        canvas.clipPath(path1);
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.restore();

        canvas.save();
        canvas.clipPath(path2);
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
        canvas.restore();
    }
}
