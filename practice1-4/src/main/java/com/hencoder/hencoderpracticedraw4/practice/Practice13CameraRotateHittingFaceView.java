package com.hencoder.hencoderpracticedraw4.practice;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
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
import android.view.animation.LinearInterpolator;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice13CameraRotateHittingFaceView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Point point = new Point(200, 50);
    Camera camera = new Camera();
    Matrix matrix = new Matrix();
    int degree;
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, 360);

    public Practice13CameraRotateHittingFaceView(Context context) {
        super(context);
    }

    public Practice13CameraRotateHittingFaceView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice13CameraRotateHittingFaceView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() * 2, bitmap.getHeight() * 2, true);
        bitmap.recycle();
        bitmap = scaledBitmap;

        animator.setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);

        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String tipMsg = "通过camera.setLocation(0, 0, -20)让摄像机远离\n" +
                        "通过matrix.postTranslate(centerX, centerY);\n" +
                        "   matrix.preTranslate(-centerX, -centerY);\n" +
                        "来完成旋转的居中（Matrix先在开头post右乘位移到bitmap中心，最后pre左乘位移移回原点）\n" +
                        "\n问题：为何setLocation设置-10或者-20效果基本一致？？？\n\n" +
                        "代码：\n" +
                        "int bitmapWidth = bitmap.getWidth();\n" +
                        "int bitmapHeight = bitmap.getHeight();\n" +
                        "int centerX = point.x + bitmapWidth / 2;\n" +
                        "int centerY = point.y + bitmapHeight / 2;\n" +
                        "\n" +
                        "camera.save();\n" +
                        "matrix.reset();\n" +
                        "camera.rotateX(degree);\n" +
                        "camera.setLocation(0, 0, -20);\n" +
                        "camera.getMatrix(matrix);\n" +
                        "camera.restore();\n" +
                        "matrix.postTranslate(centerX, centerY);\n" +
                        "matrix.preTranslate(-centerX, -centerY);\n" +
                        "canvas.save();\n" +
                        "canvas.concat(matrix);\n" +
                        "canvas.drawBitmap(bitmap, point.x, point.y, paint);\n" +
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
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        animator.start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        animator.end();
    }

    @SuppressWarnings("unused")
    public void setDegree(int degree) {
        this.degree = degree;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int centerX = point.x + bitmapWidth / 2;
        int centerY = point.y + bitmapHeight / 2;

        camera.save();
        matrix.reset();
        camera.rotateX(degree);
        camera.setLocation(0, 0, -20);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.postTranslate(centerX, centerY);
        matrix.preTranslate(-centerX, -centerY);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, point.x, point.y, paint);
        canvas.restore();
    }
}