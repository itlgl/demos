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
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.hencoder.hencoderpracticedraw4.R;

public class Practice14FlipboardView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Bitmap bitmap;
    Camera camera = new Camera();
    int degree;
    ObjectAnimator animator = ObjectAnimator.ofInt(this, "degree", 0, 180);
    Matrix matrix = new Matrix();

    public Practice14FlipboardView(Context context) {
        super(context);
    }

    public Practice14FlipboardView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice14FlipboardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.maps);

        animator.setDuration(2500);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.REVERSE);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String tipMsg = "// 绘制上半部分\n" +
                        "canvas.save();\n" +
                        "canvas.clipRect(x, y, x + bitmapWidth, y + bitmapHeight / 2);\n" +
                        "canvas.drawBitmap(bitmap, x, y, paint);\n" +
                        "canvas.restore();\n" +
                        "\n" +
                        "// 绘制下半部分\n" +
                        "canvas.save();\n" +
                        "// 通过camera.rotateX(degree)这个方法来让canvas绘制的图像旋转\n" +
                        "// 如果degree <= 90，这时只绘制bitmap的下半部分\n" +
                        "// 如果degree > 90，那么只绘制bitmap的上半部分（因为这时bitmap已经翻转了，上半部分还是图片尾部）\n" +
                        "// 另外，图片翻转过程中，长宽会比原来大一点，所以裁切的部分要大一点才能把图片显示全\n" +
                        "if(degree <= 90) {// 只显示bitmap的下半部分\n" +
                        "    int left = x;\n" +
                        "    int top = y + bitmapHeight / 2;\n" +
                        "    int right = x + bitmapWidth;\n" +
                        "    int bottom = y + bitmapHeight;\n" +
                        "    // 把裁切的宽度和下面长度扩大一点\n" +
                        "    canvas.clipRect(left - bitmapWidth / 2, top, right + bitmapWidth / 2, bottom + bitmapHeight / 2);\n" +
                        "} else {// 只显示bitmap上半部分\n" +
                        "    int left = x;\n" +
                        "    int top = y;\n" +
                        "    int right = x + bitmapWidth;\n" +
                        "    int bottom = y + bitmapHeight / 2;\n" +
                        "    canvas.clipRect(left - bitmapWidth / 2, top - bitmapHeight / 2, right + bitmapWidth / 2, bottom);\n" +
                        "}\n" +
                        "camera.save();\n" +
                        "camera.rotateX(degree);\n" +
                        "camera.getMatrix(matrix);\n" +
                        "camera.restore();\n" +
                        "matrix.postTranslate(x + bitmapWidth / 2, y + bitmapHeight / 2);\n" +
                        "matrix.preTranslate(-(x + bitmapWidth / 2), -(y + bitmapHeight / 2));\n" +
                        "canvas.concat(matrix);\n" +
                        "canvas.drawBitmap(bitmap, x, y, paint);\n" +
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
        int centerX = getWidth() / 2;
        int centerY = getHeight() / 2;
        int x = centerX - bitmapWidth / 2;
        int y = centerY - bitmapHeight / 2;

        // 绘制上半部分
        canvas.save();
        canvas.clipRect(x, y, x + bitmapWidth, y + bitmapHeight / 2);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        // 绘制下半部分
        canvas.save();
        // 通过camera.rotateX(degree)这个方法来让canvas绘制的图像旋转
        // 如果degree <= 90，这时只绘制bitmap的下半部分
        // 如果degree > 90，那么只绘制bitmap的上半部分（因为这时bitmap已经翻转了，上半部分还是图片尾部）
        // 另外，图片翻转过程中，长宽会比原来大一点，所以裁切的部分要大一点才能把图片显示全
        if(degree <= 90) {// 只显示bitmap的下半部分
            int left = x;
            int top = y + bitmapHeight / 2;
            int right = x + bitmapWidth;
            int bottom = y + bitmapHeight;
            // 把裁切的宽度和下面长度扩大一点
            canvas.clipRect(left - bitmapWidth / 2, top, right + bitmapWidth / 2, bottom + bitmapHeight / 2);
        } else {// 只显示bitmap上半部分
            int left = x;
            int top = y;
            int right = x + bitmapWidth;
            int bottom = y + bitmapHeight / 2;
            canvas.clipRect(left - bitmapWidth / 2, top - bitmapHeight / 2, right + bitmapWidth / 2, bottom);
        }
        camera.save();
        camera.rotateX(degree);
        camera.getMatrix(matrix);
        camera.restore();
        matrix.postTranslate(x + bitmapWidth / 2, y + bitmapHeight / 2);
        matrix.preTranslate(-(x + bitmapWidth / 2), -(y + bitmapHeight / 2));
        canvas.concat(matrix);
        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.restore();

        /*canvas.save();

        camera.save();
        camera.rotateX(degree);
        canvas.translate(centerX, centerY);
        camera.applyToCanvas(canvas);
        canvas.translate(-centerX, -centerY);
        camera.restore();

        canvas.drawBitmap(bitmap, x, y, paint);
        canvas.drawBitmap(bitmap, srcRect, dstRect, paint);
        canvas.restore();*/
    }
}
