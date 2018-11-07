package com.hencoder.hencoderpracticedraw2.practice;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class Practice12PathEffectView extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Path path = new Path();

    PathEffect cornerPathEffect = new CornerPathEffect(20);
    PathEffect discretePathEffect = new DiscretePathEffect(20, 5);
    PathEffect dashPathEffect = new DashPathEffect(new float[]{20, 10, 5, 10}, 0);
    PathEffect pathDashPathEffect;
    PathEffect sumPathEffect = new SumPathEffect(dashPathEffect, discretePathEffect);
    PathEffect composePathEffect = new ComposePathEffect(dashPathEffect, discretePathEffect);

    public Practice12PathEffectView(Context context) {
        super(context);
    }

    public Practice12PathEffectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public Practice12PathEffectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    {
        paint.setStyle(Paint.Style.STROKE);

        path.moveTo(50, 100);
        path.rLineTo(50, 100);
        path.rLineTo(80, -150);
        path.rLineTo(100, 100);
        path.rLineTo(70, -120);
        path.rLineTo(150, 80);

        Path dashPath = new Path();
        dashPath.lineTo(20, -30);
        dashPath.lineTo(40, 0);
        dashPath.close();
        pathDashPathEffect = new PathDashPathEffect(dashPath, 50, 0, PathDashPathEffect.Style.MORPH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 使用 Paint.setPathEffect() 来设置不同的 PathEffect

        // 第一处：CornerPathEffect
        paint.setPathEffect(new CornerPathEffect(20));
        canvas.drawPath(path, paint);

        canvas.save();
        canvas.translate(500, 0);
        // 第二处：DiscretePathEffect
        paint.setPathEffect(new DiscretePathEffect(20, 5));
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 200);
        // 第三处：DashPathEffect
        paint.setPathEffect(new DashPathEffect(new float[]{20, 10, 15, 5}, 10));
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(500, 200);
        // 第四处：PathDashPathEffect
        Path dashPath = new Path();
        dashPath.setFillType(Path.FillType.EVEN_ODD);
        dashPath.lineTo(20, -40);
        dashPath.lineTo(40, 0);
        dashPath.close();
        paint.setPathEffect(new PathDashPathEffect(dashPath, 50, 0, PathDashPathEffect.Style.TRANSLATE));
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(0, 400);
        // 第五处：SumPathEffect
        paint.setPathEffect(new SumPathEffect(new DiscretePathEffect(20, 5), new DashPathEffect(new float[]{20, 10, 15, 5}, 10)));
        canvas.drawPath(path, paint);
        canvas.restore();

        canvas.save();
        canvas.translate(500, 400);
        // 第六处：ComposePathEffect
        // public ComposePathEffect(PathEffect outerpe, PathEffect innerpe)
        // innerpe是先应用的，outerpe是后应用的
        // 所以先应用的是拐角变圆角，然后再用线段的方式绘制出来
        // 但是返回来是没效果的，看到的就是线段效果
        // 所以innerpe可以理解为线段路径样式，比如圆角或者随机偏离，而outerpe是线段的绘制画笔样式，比如用虚线还是三角?
        paint.setPathEffect(new ComposePathEffect(new DashPathEffect(new float[]{20, 10, 15, 5}, 10),
                new CornerPathEffect(20)));// 圆角+虚线
//        paint.setPathEffect(new ComposePathEffect(new PathDashPathEffect(dashPath, 50, 0, PathDashPathEffect.Style.TRANSLATE),
//                new CornerPathEffect(20)));// 圆角+三角
//        paint.setPathEffect(new ComposePathEffect(new CornerPathEffect(20),
//                new DashPathEffect(new float[]{20, 10, 15, 5}, 10)));// 仅显示虚线
        canvas.drawPath(path, paint);
        canvas.restore();
    }
}
