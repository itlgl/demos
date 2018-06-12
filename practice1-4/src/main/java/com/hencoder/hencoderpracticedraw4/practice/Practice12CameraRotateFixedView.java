package com.hencoder.hencoderpracticedraw4.practice;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice12CameraRotateFixedView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point1 = new Point(200, 200);
    Point point2 = new Point(600, 200);
    Camera camera = new Camera();
    Matrix matrix = new Matrix();

    public Practice12CameraRotateFixedView(Context context) {
        super(context);
    }

    public Practice12CameraRotateFixedView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice12CameraRotateFixedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tipMsg = "camera.save();\n" +
                        "// camera.translate(point1.x + bitmap.getWidth() / 2, -(point1.y + bitmap.getHeight() / 2), 0);\n" +
                        "camera.rotateX(30);\n" +
                        "// camera.translate(-(point1.x + bitmap.getWidth() / 2), point1.y + bitmap.getHeight() / 2, 0);\n" +
                        "camera.getMatrix(matrix);\n" +
                        "camera.restore();\n" +
                        "canvas.save();\n" +
                        "canvas.translate(point1.x + bitmap.getWidth() / 2, point1.y + bitmap.getHeight() / 2);\n" +
                        "canvas.concat(matrix);\n" +
                        "canvas.translate(-(point1.x + bitmap.getWidth() / 2), -(point1.y + bitmap.getHeight() / 2));\n" +
                        "canvas.drawBitmap(bitmap, point1.x, point1.y, paint);\n" +
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

        camera.save();
        // camera.translate(point1.x + bitmap.getWidth() / 2, -(point1.y + bitmap.getHeight() / 2), 0);
        camera.rotateX(30);
        // camera.translate(-(point1.x + bitmap.getWidth() / 2), point1.y + bitmap.getHeight() / 2, 0);
        camera.getMatrix(matrix);
        camera.restore();
        canvas.save();
        canvas.translate(point1.x + bitmap.getWidth() / 2, point1.y + bitmap.getHeight() / 2);
        canvas.concat(matrix);
        canvas.translate(-(point1.x + bitmap.getWidth() / 2), -(point1.y + bitmap.getHeight() / 2));
        canvas.drawBitmap(bitmap, point1.x, point1.y, paint);
        canvas.restore();

        camera.save();
        // camera.translate(point2.x + bitmap.getWidth() / 2, -(point2.y + bitmap.getHeight() / 2), 0);
        camera.rotateY(30);
        // camera.translate(-(point2.x + bitmap.getWidth() / 2), point2.y + bitmap.getHeight() / 2, 0);
        camera.getMatrix(matrix);
        camera.restore();
        canvas.save();
        canvas.translate(point2.x + bitmap.getWidth() / 2, point2.y + bitmap.getHeight() / 2);
        canvas.concat(matrix);
        canvas.translate(-(point2.x + bitmap.getWidth() / 2), -(point2.y + bitmap.getHeight() / 2));
        canvas.drawBitmap(bitmap, point2.x, point2.y, paint);
        canvas.restore();
    }
}
