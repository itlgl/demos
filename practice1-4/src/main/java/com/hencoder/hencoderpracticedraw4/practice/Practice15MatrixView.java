package com.hencoder.hencoderpracticedraw4.practice;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.hencoder.hencoderpracticedraw4.R;

/**
 * Created by ligl01 on 2018/3/16.
 */

public class Practice15MatrixView extends View {
    Matrix matrix = new Matrix();
    Bitmap bitmap = null;
    Paint paint = new Paint();

    public Practice15MatrixView(Context context) {
        super(context);
    }

    public Practice15MatrixView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice15MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Practice15MatrixView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    {
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                matrix.reset();
                Log.e("test", "初始矩阵=\n" + formatMatrix(matrix));
                matrix.preTranslate(3, 3);
                Log.e("test", "pre平移(2,2)=\n" + formatMatrix(matrix));
                matrix.postTranslate(2, 2);
                Log.e("test", "post平移(2,2)=\n" + formatMatrix(matrix));
                matrix.reset();

                matrix.reset();
                Log.e("test", "初始矩阵=\n" + formatMatrix(matrix));
                matrix.preScale(3, 3);
                Log.e("test", "preScale(3, 3)=\n" + formatMatrix(matrix));
                matrix.reset();

                matrix.reset();
                Log.e("test", "初始矩阵=\n" + formatMatrix(matrix));
                matrix.preRotate(90);
                Log.e("test", "preRotate(90)=\n" + formatMatrix(matrix));
                matrix.reset();

                matrix.reset();
                Log.e("test", "初始矩阵=\n" + formatMatrix(matrix));
                matrix.postRotate(45);
                Log.e("test", "postRotate(45)=\n" + formatMatrix(matrix));
                matrix.postRotate(45);
                Log.e("test", "postRotate(45)=\n" + formatMatrix(matrix));
                matrix.reset();
            }
        });
    }

    String formatMatrix(Matrix matrix) {
        String str = matrix.toShortString();
        return str.replace("]", "]\n");
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        float[] src = {bw / 2, bh / 2, bw, 0};
        float[] dst = {bw / 2, bh / 2, bw / 2 + bh / 2, bh / 2 + bw / 2};
        matrix.setPolyToPoly(src, 0, dst, 0, 2);
        // matrix.setPolyToPoly(new float[]{0, bh, 0, 0}, 0, new float[]{0, 0, bh, 0}, 0, 2);
        // matrix.preTranslate()
        canvas.drawBitmap(bitmap, matrix, paint);
    }
}
