package com.itlgl.demo.jike;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

public class LikeImageView extends View {

    //Drawable

    public LikeImageView(Context context) {
        this(context, null);
    }

    public LikeImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

//        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LikeImageView);
//        int imageType = ta.getInt(R.styleable.LikeImageView_imageType, -1);
//        ta.recycle();
    }

    public void setImageType(int imageType) {
        //mImageType = imageType;
    }
}
