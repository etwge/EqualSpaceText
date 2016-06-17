package etwge.hawaii.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import etwge.hawaii.R;

/**
 * Author: lhy
 * Date: 2016/4/2
 */
public class EqualSpaceTextView extends View {

    public static final String TAG = "EqualSpaceTextView";

    private String mText = "";
    private int mColor;
    private int mTextSize;
    private Rect mBound;
    private Rect charBound;

    Paint.FontMetricsInt fontMetricsInt;

    Paint mPaint;

    public EqualSpaceTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EqualSpaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EqualSpaceTextView);
        mText = array.getString(R.styleable.EqualSpaceTextView_es_text);
        mColor = array.getColor(R.styleable.EqualSpaceTextView_es_textColor, Color.BLACK);
        mTextSize = array.getDimensionPixelSize(R.styleable.EqualSpaceTextView_es_textSize,
                (int) TypedValue
                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16, context.getResources().getDisplayMetrics()));
        array.recycle();

        /**
         * get target text width and height
         */
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setTextSize(mTextSize);
         mPaint.setColor(mColor);
        mBound = new Rect();
        fontMetricsInt = mPaint.getFontMetricsInt();
        mPaint.getTextBounds(mText, 0, mText.length(), mBound);
//        Log.i(TAG, "mBound:" + mBound.toString());
//        Log.i(TAG, "fontMetricsInt:" + fontMetricsInt.toString());
        charBound = new Rect();
    }

    //    * wrap_parent -> MeasureSpec.AT_MOST
    //    * match_parent -> MeasureSpec.EXACTLY
    //    * detail -> MeasureSpec.EXACTLY
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = 0;
        int widthSize = 0;
        widthMode = MeasureSpec.getMode(widthMeasureSpec);
        switch (widthMode) {
            case MeasureSpec.EXACTLY:
                widthSize = MeasureSpec.getSize(widthMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                widthSize = mBound.width();
        }

        int heightMode = 0;
        int heightSize = 0;
        heightMode = MeasureSpec.getMode(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                heightSize = fontMetricsInt.bottom - fontMetricsInt.top;
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float textSpace = 0;
        float curIndex = 0;
        int charWidthSum = 0;
        char[] textArray = mText.toCharArray();

        for (int i = 0; i < textArray.length; i++) {
            String charText = String.valueOf(textArray[i]);
            mPaint.getTextBounds(charText, 0,charText.length(), charBound);
            int charWidth = charBound.width();
            charWidthSum +=charWidth;
        }

        if(getWidth() - charWidthSum > 0){
            textSpace = (getWidth() - charWidthSum) * 1.00f/ (mText.length() - 1);
        }
        Log.i(TAG, "textSpace:" +  textSpace);
        Log.i(TAG, "charWidthSum1:" +  charWidthSum);
        charWidthSum = 0;
        for (int i = 0; i < textArray.length; i++) {
            String charText = String.valueOf(textArray[i]);
            mPaint.getTextBounds(charText, 0,charText.length(), charBound);
            int charWidth = charBound.width();
            if(i == textArray.length - 1) {
                curIndex -= 1;
            }
            canvas.drawText(charText, curIndex, -mBound.top + (getHeight() - mBound.height())/2, mPaint);
            Log.i(TAG, "charText:" + charText + "|" + charWidth);
            charWidthSum +=charWidth;
            curIndex = curIndex + textSpace + charWidth;
        }
        Log.i(TAG, "charWidthSum2:" +  charWidthSum);
    }
}
