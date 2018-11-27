package test.kezy.com.customcornertextview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import test.kezy.com.customcornertextview.R;


/**
 * @author: Kezy
 * @create_time: 2018.11.27
 * @description:
 */
public class CustomCornerTextView extends View {


    private String mContent; // 输入内容
    private float mTextSize; // 输入内容
    private int mBgColor; // 背景色
    private int mBorderColor; //  外框 线的颜色
    private int mTextColor; // 文字颜色

    private float mCornerRadius; // 四个角的圆角值

    private float mTopLeftRadius; // 左上
    private float mTopRightRadius; // 右上
    private float mBottomLeftRadius; // 左下
    private float mBottomRightRadius; // 右下

    private float mBorderWidth; // 边框线 粗细


    private Paint mPaint; // 画笔

    private Rect mRect; //


    public CustomCornerTextView(Context context) {
        this(context, null);
    }

    public CustomCornerTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public CustomCornerTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.CustomCornerTextView);

        mContent = array.getString(R.styleable.CustomCornerTextView_text);
        mTextSize = array.getDimension(R.styleable.CustomCornerTextView_text_size, 48f); // 默认 14sp
        mBgColor = array.getColor(R.styleable.CustomCornerTextView_bg_color, Color.parseColor("#00000000"));  // 默认白色
        mBorderColor = array.getColor(R.styleable.CustomCornerTextView_border_color, Color.parseColor("#00000000"));  // 默认无色
        mTextColor = array.getColor(R.styleable.CustomCornerTextView_text_color, Color.BLACK);  // 默认白色


        mCornerRadius = array.getDimension(R.styleable.CustomCornerTextView_corner_radius, 0); // 默认无圆角
        mTopLeftRadius = array.getDimension(R.styleable.CustomCornerTextView_radius_top_left, 0); // 默认无圆角
        mTopRightRadius = array.getDimension(R.styleable.CustomCornerTextView_radius_top_right, 0); // 默认无圆角
        mBottomLeftRadius = array.getDimension(R.styleable.CustomCornerTextView_radius_bottom_left, 0); // 默认无圆角
        mBottomRightRadius = array.getDimension(R.styleable.CustomCornerTextView_radius_bottom_right, 0); // 默认无圆角

        mBorderWidth = array.getDimension(R.styleable.CustomCornerTextView_radius_border_width, 0); // 默认粗细


        array.recycle();

        if (TextUtils.isEmpty(mContent)) {
            mContent = "";
        }

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
        mPaint.setColor(mTextColor);
        mRect = new Rect();
        mPaint.getTextBounds(mContent, 0, mContent.length(), mRect);
        if (mCornerRadius != 0) {
            setCornerRadius(mCornerRadius);
        } else {
            setCornerRadius(mTopLeftRadius, mTopRightRadius, mBottomLeftRadius, mBottomRightRadius);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);


        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int drawWidth = getDrawWidth(widthMode, widthSize);
        int drawHeight = getDrawHeight(heightMode, heightSize);

        setMeasuredDimension(drawWidth, drawHeight);

    }

    private int getDrawHeight(int heightMode, int heightSize) {

        int drawHeight = 0;

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                drawHeight = heightSize;
                break;
            case MeasureSpec.AT_MOST:
                drawHeight = getPaddingTop() + mRect.height() + getPaddingBottom() + (int) (2 * mBorderWidth);
                break;
            case MeasureSpec.UNSPECIFIED:
                drawHeight = getPaddingTop() + mRect.height() + getPaddingBottom() + (int) (2 * mBorderWidth);
                break;
        }

        Log.v("====msg  getDrawHeight", "getPaddingTop: " + getPaddingTop() + " , getPaddingBottom : " + getPaddingBottom());
        Log.d("====msg  getDrawHeight", "mRect.height(): " + mRect.height() + " , drawHeight : " + drawHeight);
        return drawHeight;
    }

    private int getDrawWidth(int widthMode, int widthSize) {
        int drawWidth = 0;

        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                drawWidth = widthSize;
                break;
            case MeasureSpec.AT_MOST:
                drawWidth = getPaddingLeft() + mRect.width() + getPaddingRight() + (int) (2 * mBorderWidth) + dp2px(3);
                break;
            case MeasureSpec.UNSPECIFIED:
                drawWidth = getPaddingLeft() + mRect.width() + getPaddingRight() + (int) (2 * mBorderWidth) + dp2px(3);
                break;
        }
        Log.d("====msg  getDrawWidth", "getPaddingLeft: " + getPaddingLeft() + " ,getPaddingRight : " + getPaddingRight());
        return drawWidth;

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 计算基线
        Paint.FontMetricsInt fontMetricsInt = mPaint.getFontMetricsInt();
        Log.d("====msg  onDraw", "fontMetricsInt.bottom : " + fontMetricsInt.bottom + " , fontMetricsInt.top: " + fontMetricsInt.top);
        float dy = (fontMetricsInt.bottom - fontMetricsInt.top + getPaddingTop() - getPaddingBottom()) / 2 - fontMetricsInt.bottom;
        float startY = getHeight() / 2 + dy;

        float startX = getPaddingLeft() + mBorderWidth;

        Log.d("====msg  onDraw", "startX : " + startX + " , startY: " + startY);

        canvas.drawText(mContent, startX, startY, mPaint);
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
    }

    public void setCornerRadius(float radius) {
        this.setCornerRadius(radius, radius, radius, radius);
    }

    /**
     * @param topLeft     左上角圆角角度
     * @param topRight    右上角圆角
     * @param bottomLeft  左下角
     * @param bottomRight 右下角
     */
    public void setCornerRadius(float topLeft, float topRight, float bottomLeft, float bottomRight) {
        GradientDrawable drawable = new GradientDrawable();

        /**
         * topLeftRadius, topLeftRadius,
         topRightRadius, topRightRadius,
         bottomRightRadius, bottomRightRadius,
         bottomLeftRadius, bottomLeftRadius
         */
        drawable.setCornerRadii(new float[]{topLeft, topLeft, topRight, topRight,
                bottomRight, bottomRight, bottomLeft, bottomLeft});
        drawable.setStroke((int) mBorderWidth, mBorderColor);
        drawable.setColor(mBgColor);
        this.setBackgroundDrawable(drawable);
    }

    /**
     * @param value
     * @return
     */
    private int dp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().density;
        return (int) (v * value + 0.5f);
    }

    /**
     * @param value
     * @return
     */
    private int sp2px(int value) {
        float v = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (v * value + 0.5f);
    }
}
