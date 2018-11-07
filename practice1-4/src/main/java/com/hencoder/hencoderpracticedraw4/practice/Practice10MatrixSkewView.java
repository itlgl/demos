package com.hencoder.hencoderpracticedraw4.practice;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice10MatrixSkewView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point1 = new Point(200, 200);
    Point point2 = new Point(600, 200);
    Matrix matrix = new Matrix();

    public Practice10MatrixSkewView(Context context) {
        super(context);
    }

    public Practice10MatrixSkewView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice10MatrixSkewView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tipMsg = "canvas.save();\n" +
                        "matrix.reset();\n" +
                        "matrix.postSkew(0, 0.5f);\n" +
                        "canvas.concat(matrix);\n" +
                        "canvas.drawBitmap(bitmap, point1.x, point1.y, paint);\n" +
                        "canvas.restore();\n" +
                        "\n" +
                        "canvas.save();\n" +
                        "matrix.reset();\n" +
                        "matrix.postSkew(-0.5f, 0);\n" +
                        "canvas.concat(matrix);\n" +
                        "canvas.drawBitmap(bitmap, point2.x, point2.y, paint);\n" +
                        "canvas.restore();";
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
        matrix.reset();
        matrix.postSkew(0, 0.5f);
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.restore();

        canvas.save();
        matrix.reset();
        matrix.postSkew(-0.5f, 0);
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
        canvas.restore();
    }
}
