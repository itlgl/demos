package com.itlgl.demo.jike;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;

public class LikeCountView extends View {
    private ColorStateList mTextColor;
    private int mLikeCount;
    private final TextPaint mTextPaint;
    private int mCurTextColor;

    /**
     * 当前显示的likeCount字符串
     */
    private String mCurrText;
    /**
     * 之前的likeCount字符串
     */
    private String mOldText;
    /**
     * mCurrTextChars 辅助计算和绘制
     */
    private char[] mCurrTextChars;
    /**
     * mOldTextChars 辅助计算和绘制
     */
    private char[] mOldTextChars;

    /**
     * 单个数字的宽度，因为数字高度都是一样的，所以获取一个数字"0"的高度即可
     * 这个值是数字带上边界的宽度，不能用getTextBounds获取，这样获取到的文字留白就丧失了
     */
    private float mSigleCharWidth;
    /**
     * 单个数字的高度，因为数字高度都是一样的，所以获取一个数字"0"的高度即可
     * 这个值是数字带上边界的高度，不能用getTextBounds获取，这样获取到的文字留白就丧失了
     */
    private float mSigleCharHeight;

    /**
     * 当前动画执行的进度，范围从-1~1
     * 如果动画是从-1~0，那么是上顶替换，底下的数字逐渐上升到中间
     * 如果是从1~0，那么是下顶替换，上面的数字逐渐下降到中间
     * 0就用来表示已经完成
     * 注意，因为还需要直接设置count，所以应该在setLikeCount方法中将mAnimProgress设置为0
     */
    private float mAnimProgress;

    public enum AnimationType {
        /** 表示下顶动画 */
        DOWN,
        /** 上顶动画 */
        UP,
        /** 无动画 */
        NONE;
    }

    public LikeCountView(Context context) {
        this(context, null);
    }

    public LikeCountView(Context context, AttributeSet attrs) {
        super(context, attrs);

        mLikeCount = 0;
        mCurrText = "";
        mOldText = "";

        final Resources res = getResources();
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.density = res.getDisplayMetrics().density;
        mTextPaint.setTextAlign(Paint.Align.CENTER);// 居中绘制文字

        int likeCount = 0;
        ColorStateList textColor = null;
        int textSize = 15;

        /*
         如何使用Android系统已有的属性
         在attrs文件中声明属性时，这样声明： <attr name="android:textColor"/>
         在代码中调用： ColorStateList color = ta.getColorStateList(R.styleable.LikeTextView_android_textColor)
         这个调用方式需要跟随系统的了，因为android:textColor的格式是在系统定义的，所以获取属性要获取指定的格式
         */
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LikeCountView);
        textColor = ta.getColorStateList(R.styleable.LikeCountView_android_textColor);
        likeCount = ta.getInteger(R.styleable.LikeCountView_likeCount, likeCount);
        textSize = ta.getDimensionPixelSize(R.styleable.LikeCountView_android_textSize, 15);
        ta.recycle();

        setRawTextSize(textSize);
        setLikeCount(likeCount);
        setTextColor(textColor != null ? textColor : ColorStateList.valueOf(0xFF000000));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float centerX = getWidth() / 2.0f;
        float centerY = getHeight() / 2.0f;

        canvas.drawColor(Color.GRAY);
        if(mAnimProgress == 0) {// 如果进度为0，只绘制当前的文字
            mTextPaint.setAlpha(255);
            canvas.drawText(mCurrText, centerX, centerY, mTextPaint);
        } else {
            // 当绘制的时候，就以新值计算居中位置，这样绘制完成就不会出现数字瞬移的现象
            int maxTextSize = mCurrText.length();
            float startX = centerX - maxTextSize / 2 * mSigleCharWidth - (maxTextSize % 2 == 0 ? 0 : mSigleCharWidth / 2);
            float startCenterX = startX + mSigleCharWidth / 2;

            float currY, oldY;
            int oldAlpha, currAlpha;
            float x;
            for (int i = 0; i < maxTextSize; i++) {
                x = startCenterX + i * mSigleCharWidth;
                // 如果两个char相同
                if(i < mCurrTextChars.length && i< mOldTextChars.length && mCurrTextChars[i] == mOldTextChars[i]) {
                    mTextPaint.setAlpha(255);
                    canvas.drawText(mCurrTextChars, i, 1, x, centerY, mTextPaint);
                } else {// 否则的话分别绘制
                    if(mAnimProgress < 0) { // UP动画
                        oldY = centerY - (1 + mAnimProgress) * mSigleCharHeight;
                        currY = oldY + mSigleCharHeight;
                        oldAlpha = (int) (255 * -mAnimProgress);
                        currAlpha = (int) (255 * (1 + mAnimProgress));
                    } else { // DOWN动画
                        oldY = centerY + (1 - mAnimProgress) * mSigleCharHeight;
                        currY = oldY - mSigleCharHeight;
                        oldAlpha = (int) (255 * mAnimProgress);
                        currAlpha = (int) (255 * (1 - mAnimProgress));
                    }
                    if(i < mOldTextChars.length) {
                        mTextPaint.setAlpha(oldAlpha);
                        canvas.drawText(mOldTextChars, i, 1, x, oldY, mTextPaint);
                    }
                    if(i < mCurrTextChars.length) {
                        mTextPaint.setAlpha(currAlpha);
                        canvas.drawText(mCurrTextChars, i, 1, x, currY, mTextPaint);
                    }
                }
            }
        }
    }

    /**
     * 重写测量宽高的方法
     * 1. 如果长度/宽度确定，那么就用确定的，不考虑不够显示的情况
     * 2. 如果不确定，那就自己算一下，宽度为文字的宽度，高度为文字的高度*3
     *
     * 高度的判断标准
     * 有上下的动画，数字都可以显示全，共三个数字高度，mSigleCharHeight * 3
     * 总共3个单个字高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        Paint.FontMetrics fm = mTextPaint.getFontMetrics();
        // 单个字体的高度
        mSigleCharHeight = fm.descent - fm.ascent;
        // 单个字体的宽度
        mSigleCharWidth = mTextPaint.measureText("0");

        int maxTextSize = Math.max(mCurrText.length(), mOldText.length());

        if(widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = (int) (maxTextSize * mSigleCharWidth);
        }

        if(heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            // 高度的判断标准
            // 有上下的动画，数字都可以显示全，共三个数字高度，mSigleCharHeight * 3
            // 而且数字之间有间距，间距定位数字高度的1/4，两个间距即为mSigleCharHeight * 0.5f
            // 总共3.5f个单个字高度
            height = (int) (mSigleCharHeight * 3.0f);
        }

        System.out.println("onMeasure width=" + width + ",height=" + height);
        setMeasuredDimension(width, height);
    }

    public float getAnimProgress() {
        return mAnimProgress;
    }

    public void setAnimProgress(float progress) {
        this.mAnimProgress = progress;
        invalidate();
    }

    public float getTextSize() {
        return mTextPaint.getTextSize();
    }

    /**
     * Set the default text size to the given value, interpreted as "scaled
     * pixel" units.  This size is adjusted based on the current density and
     * user font size preference.
     *
     * @param size The scaled pixel size.
     *
     * @attr ref com.itlgl.demo.jike.R.styleable#LikeTextView_android_textSize
     */
    public void setTextSize(float size) {
        setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
    }

    /**
     * Set the default text size to a given unit and value.  See {@link
     * TypedValue} for the possible dimension units.
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     *
     * @attr ref com.itlgl.demo.jike.R.styleable#LikeTextView_android_textSize
     */
    public void setTextSize(int unit, float size) {
        Context c = getContext();
        Resources r;

        if (c == null) {
            r = Resources.getSystem();
        } else {
            r = c.getResources();
        }

        setRawTextSize(TypedValue.applyDimension(unit, size, r.getDisplayMetrics()));
    }

    private void setRawTextSize(float size) {
        if (size != mTextPaint.getTextSize()) {
            mTextPaint.setTextSize(size);

            // calculateParameterBeforeDraw();

            requestLayout();
            invalidate();
        }
    }

    public int getLikeCount() {
        return mLikeCount;
    }

    /**
     * 直接设置likeCount的方法，这个方法没有动画效果
     * @param likeCount
     */
    public void setLikeCount(int likeCount) {
        setLikeCount(likeCount, AnimationType.NONE);
    }

    /**
     * 设置likeCount数量，同时根据设置数量的大小判断是上移动画还是下移动画
     * @param likeCount
     */
    public void setLikeCountWithAnim(int likeCount) {
        if(likeCount > getLikeCount()) {
            setLikeCount(likeCount, AnimationType.UP);
        } else if(likeCount < getLikeCount()) {
            setLikeCount(likeCount, AnimationType.DOWN);
        }
    }

    /**
     * 让数量自增1的操作，有动画
     */
    public void addLikeCountOne() {
        int count = getLikeCount();
        setLikeCount(count + 1, AnimationType.UP);
    }

    /**
     * 让数量自减1的操作，有动画
     */
    public void reduceLikeCountOne() {
        int count = getLikeCount();
        setLikeCount(count - 1, AnimationType.DOWN);
    }

    public void setLikeCount(int likeCount, AnimationType animationType) {
        if(likeCount < 0) {
            throw new IllegalArgumentException("likeCount不能小于0");
        }

        if(likeCount == mLikeCount) {
            return;
        }

        this.mLikeCount = likeCount;
        updateText();
        switch(animationType) {
            case NONE:
                // 直接设置进度为0，表示动画结束
                setAnimProgress(0);
                break;
            case UP:
                ObjectAnimator animatorUp = ObjectAnimator.ofFloat(this, "animProgress", -1, 0);
                animatorUp.start();
                break;
            case DOWN:
                ObjectAnimator animatorDown = ObjectAnimator.ofFloat(this, "animProgress", 1, 0);
                animatorDown.start();
                break;
        }
    }

    // 更新文字，格式化count的字符串
    private void updateText() {
        // 将之前的chars保存下来
        mOldText = mCurrText == null ? "" : mCurrText;

        // 格式化文字
        if(mLikeCount > 9999) {
            mCurrText = "9999+";
        } else {
            mCurrText = String.valueOf(mLikeCount);
        }

        mCurrTextChars = mCurrText.toCharArray();
        mOldTextChars = mOldText.toCharArray();

        // calculateParameterBeforeDraw();

        // 判断是否需要更新view大小
        if(mOldText.length() != mCurrText.length()) {
            requestLayout();
        }
    }

//    // 当likeCount变更或者字体大小变更以后，需要重新更新一下绘制文字需要的辅助变量
//    private void calculateParameterBeforeDraw() {
//        if(mCurrText == null) {
//            mCurrText = "";
//        }
//        if(mOldText == null) {
//            mOldText = "";
//        }
//
//        // 需要计算哪些数字需要更新
//        int i;
//        for (i = 0; i < mCurrText.length() && i < mOldText.length(); i++) {
//            if(mCurrText.charAt(i) != mOldText.charAt(i)) {
//                break;
//            }
//        }
//        int sameCharLength = i;
//        if(mCurrText.length() != mOldText.length()) {
//            sameCharLength = 0;
//        }
//        if(sameCharLength == 0) {
//            mUnchangeText = "";
//            mCurrChangeText = mCurrText;
//            mOldChangeText = mOldText;
//        } else {
//            mUnchangeText = mCurrText.substring(0, sameCharLength);
//            mCurrChangeText = mCurrText.substring(sameCharLength, mCurrText.length());
//            mOldChangeText = mOldText.substring(sameCharLength, mOldText.length());
//        }
//
//        // 计算一下绘制需要的变量
//        Rect rect = new Rect();
//        mTextPaint.getTextBounds(mCurrText, 0, mCurrText.length(), rect);
//        mCurrTextWidth = rect.right - rect.left;
//        mSigleCharHeight = rect.bottom - rect.top;
//
//        mTextPaint.getTextBounds(mOldText, 0, mOldText.length(), rect);
//        mOldTextWidth = rect.right - rect.left;
//
//        mTextPaint.getTextBounds(mUnchangeText, 0, mUnchangeText.length(), rect);
//        mUnchangeTextWidth = rect.right - rect.left;
//
//        mTextPaint.getTextBounds(mCurrChangeText, 0, mCurrChangeText.length(), rect);
//        mCurrChangeTextWidth = rect.right - rect.left;
//
//        mTextPaint.getTextBounds(mOldChangeText, 0, mOldChangeText.length(), rect);
//        mOldChangeTextWidth = rect.right - rect.left;
//    }

    public ColorStateList getTextColors() {
        return mTextColor;
    }

    public void setTextColor(int color) {
        mTextColor = ColorStateList.valueOf(color);
        updateTextColors();
    }

    /**
     * Sets the text color.
     *
     * @see #setTextColor(int)
     * @see #getTextColors()
     *
     * @attr ref android.R.styleable#TextView_textColor
     */
    public void setTextColor(ColorStateList colors) {
        if (colors == null) {
            throw new NullPointerException();
        }

        mTextColor = colors;
        updateTextColors();
    }

    private void updateTextColors() {
        boolean inval = false;
        int color = mTextColor.getColorForState(getDrawableState(), 0);
        if (color != mCurTextColor) {
            mCurTextColor = color;
            mTextPaint.setColor(mCurTextColor);
            inval = true;
        }
        if (inval) {
            // Text needs to be redrawn with the new color
            invalidate();
        }
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();

        if (mTextColor != null && mTextColor.isStateful()) {
            updateTextColors();
        }
    }
}
