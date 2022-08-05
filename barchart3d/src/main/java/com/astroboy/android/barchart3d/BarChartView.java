package com.astroboy.android.barchart3d;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 黄兴伟 (xwdz9989@gmail.com)
 * @since 2017/11/12
 */

public class BarChartView extends View {


    private static final String TAG = "BarChartView";

    private static final int DEFAULT_SHOW_RECT_COUNT = 3;

    /**
     * 矩形
     */
    private Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 矩形上面文字
     */
    private Paint mRectTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 线条
     */
    private Paint mGridLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * 外边框
     */
    private Paint mMarginLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    /**
     * Y轴
     */
    private Paint mLabelPaint = new Paint(Paint.ANTI_ALIAS_FLAG);


    /**
     * 动画进度
     */
    private float mCurAnimationValue = 0;
    private ValueAnimator mValueAnimator;

    private List<BarChartEntry> mData;

    private float mVerticalLineHeight;
    private float mVerticalLineWidth;

    private int mMaxValue;
    private int mMinValue;

    private float mMaxPaddingTop;
    private float mMaxPaddingBottom;
    private float mMaxPaddingRight;
    private float mMaxPaddingLeft;

    private OnFormatLabelListener mOnFormatLabelListener;

    private EntryConfig mConfig = new EntryConfig();


    public BarChartView(Context context) {
        this(context, null);
    }

    public BarChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BarChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mRectTextPaint.setTextSize(24);
        mGridLinePaint.setColor(Color.parseColor("#DADADA"));
        mGridLinePaint.setStyle(Paint.Style.STROKE);
        mGridLinePaint.setStrokeWidth(2f);
        mMarginLinePaint.setColor(Color.parseColor("#000000"));
        mMarginLinePaint.setStyle(Paint.Style.STROKE);
        mMarginLinePaint.setStrokeWidth(2f);
        mLabelPaint.setColor(Color.parseColor("#999999"));
        mLabelPaint.setStyle(Paint.Style.STROKE);
        mLabelPaint.setTextSize(28);
        mValueAnimator = ValueAnimator.ofFloat(0f, 1f);
        mValueAnimator.setDuration(mConfig.durationTime);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mCurAnimationValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureItem(widthMeasureSpec), measureItem(heightMeasureSpec));
    }

    public int measureItem(int spec) {
        int modeWidth = MeasureSpec.getMode(spec);
        int sizeWidth = MeasureSpec.getSize(spec);

        int resultWidth;
        if (modeWidth == MeasureSpec.EXACTLY) {
            resultWidth = sizeWidth;
        } else {
            resultWidth = 200;
            if (modeWidth == MeasureSpec.AT_MOST) {
                resultWidth = Math.min(resultWidth, sizeWidth);
            }
        }
        return resultWidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mData == null || mData.isEmpty()) {
            return;
        }
        mMaxPaddingRight = Math.max(mConfig.rectOffsetRight, getPaddingRight());
        mMaxPaddingLeft = Math.max(mConfig.rectOffsetLeft, getPaddingLeft());

        mMaxPaddingTop = Math.max(mConfig.rectOffsetTop, getPaddingTop());
        mMaxPaddingBottom = Math.max(mConfig.rectOffsetBottom, getPaddingBottom());

        mVerticalLineHeight = getHeight() - mMaxPaddingTop - mMaxPaddingBottom;
        mVerticalLineWidth = getWidth() - mMaxPaddingLeft - mMaxPaddingRight;
        final int size = mData.size();
        final int count = mData.size() + 1;
        float childWidth;
        if (size > DEFAULT_SHOW_RECT_COUNT) {
            childWidth = mVerticalLineWidth / size;
        } else {
            childWidth = mVerticalLineWidth / DEFAULT_SHOW_RECT_COUNT;
        }
        cVW = mVerticalLineWidth / count;
        mDeep = childWidth / 15;
        countWidth = mVerticalLineWidth * size / count + 4 * mDeep;
        drawBaseline(canvas, mMaxPaddingLeft, mMaxPaddingTop, getWidth() - mMaxPaddingLeft - mMaxPaddingRight, getHeight() - mMaxPaddingBottom);
        drawGridAndLabel(canvas, mMaxPaddingLeft, mMaxPaddingTop + 100, getWidth() - mMaxPaddingLeft - mMaxPaddingRight, getHeight() - mMaxPaddingBottom);
        for (int i = mData.size() - 1; i >= 0; i--) {
            BarChartEntry columnEntry = mData.get(i);
            drawBarChart(canvas, columnEntry, mMaxPaddingLeft,mMaxPaddingTop + 100,getHeight() - mMaxPaddingBottom);
        }
    }

    private float countWidth;
    private float mDeep = 30;
    private float cVW = 0;

    /**
     * 绘制柱形条
     */
    private void drawBarChart(Canvas canvas, BarChartEntry columnEntry, float pLeft, float pTop, float pBottom) {
        mRectPaint.setColor(columnEntry.colorId);
        final int gravity = mConfig.gravity;
        int dValue = mMaxValue - mMinValue;
        float h = pBottom - pTop;
        float startY = (dValue - (columnEntry.value - mMinValue) * mCurAnimationValue) * h / dValue + pTop;
        if (gravity == Gravity.CENTER) {
            float left = countWidth - 5 * mDeep;
            float right = left + 4 * mDeep;
            //如果超过了paddingLeft 则不绘制
            if (left < pLeft) {
                return;
            }
            drawRect(canvas, startY, left, right, columnEntry.colorId);
            drawBarChartRectSide(canvas, startY, right, columnEntry.colorId);
            drawBarChartTopRect(canvas, startY, left, right, columnEntry.colorId);
            drawBarChartLable(canvas, pBottom, left, right, columnEntry.lable);
            countWidth -= cVW;
        }
    }

    private Shader getBarChartRectPositiveBg(int color, float w, float h) {
        int b = Color.blue(color);
        int g = Color.green(color);
        int r = Color.red(color);
        int statColor = Color.rgb(r, g, b);
        int centerColor = Color.rgb(r, g, b);
        int endColor = Color.rgb(r, g, b);
        Shader shader = new LinearGradient(0, 0, w, h, new int[]{statColor, centerColor, endColor}, null, Shader.TileMode.CLAMP);
        return shader;
    }

    private Shader getBarChartRectTopBg(int color, float w, float h) {
        int b = Color.blue(color);
        int g = Color.green(color);
        int r = Color.red(color);
        int statColor = Color.rgb(r, g, b);
        int centerColor = Color.rgb(r, g, b);
        int endColor = Color.rgb(r - 20, g - 20, b - 20);
        Shader shader = new LinearGradient(0, 0, 0, h, new int[]{statColor, centerColor, endColor}, null, Shader.TileMode.CLAMP);
        return shader;
    }

    private Shader getBarChartRectSideBg(int color, float w, float h) {
        int b = Color.blue(color);
        int g = Color.green(color);
        int r = Color.red(color);
        int statColor = Color.rgb(r, g, b);
        int centerColor = Color.rgb(r, g, b);
        int endColor = Color.rgb(r - 40, g - 40, b - 40);
        Shader shader = new LinearGradient(0, 0, w, 0, new int[]{statColor, centerColor, endColor}, null, Shader.TileMode.CLAMP);
        return shader;
    }

    private void drawBarChartRectSide(Canvas canvas, float startY, float left, int color) {
        Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.parseColor("#666666"));
        float w = mDeep;
        float h = mVerticalLineHeight + mMaxPaddingTop - startY;
        rectPaint.setShader(getBarChartRectSideBg(color, w, h));
        Path path = new Path();
        path.moveTo(left, mVerticalLineHeight + mMaxPaddingTop);
        path.lineTo(left + mDeep, mVerticalLineHeight + mMaxPaddingTop - mDeep);
        path.lineTo(left + mDeep, startY - mDeep);
        path.lineTo(left, startY);
        canvas.drawPath(path, rectPaint);
    }

    private void drawBarChartTopRect(Canvas canvas, float startY, float left, float right, int color) {
        Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float w = right - left;
        float h = mDeep;
        rectPaint.setShader(getBarChartRectTopBg(color, w, h));
        Path path = new Path();
        path.moveTo(left, startY);
        path.lineTo(right, startY);
        path.lineTo(right + mDeep, startY - mDeep);
        path.lineTo(left + mDeep, startY - mDeep);
        canvas.drawPath(path, rectPaint);
    }

    private void drawBarChartLable(Canvas pCanvas, float startY, float left, float right, String lable) {
        Paint rectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        rectPaint.setColor(Color.parseColor("#000000"));
        rectPaint.setTextSize(28);
        float w = 36;
        float h = 36;
        float rectLeft = left;
        float rectRight = rectLeft + w;
        float rectTop = startY + 30;
        float rectbBottom = rectTop + h;
        pCanvas.drawRect(rectLeft, rectTop, rectRight, rectbBottom, mRectPaint);
        pCanvas.drawText(lable, rectRight + 10, rectTop + 30, rectPaint);
    }

    private void drawRect(Canvas canvas, float startY, float left, float right, int color) {
        float w = right - left;
        float h = mVerticalLineHeight + mMaxPaddingTop - startY;
        mRectPaint.setShader(getBarChartRectPositiveBg(color, w, h));
        if (mConfig.showAnimation) {
            canvas.drawRect(left,
                    startY,
                    right,
                    mVerticalLineHeight + mMaxPaddingTop, mRectPaint);
        } else {
            canvas.drawRect(left + 14, startY,
                    right,
                    mVerticalLineHeight + mMaxPaddingTop, mRectPaint);
        }
    }

    private void drawRectTopText(Canvas canvas, float offsetLeft, float columnEntry) {
        if (mOnFormatLabelListener != null) {
            canvas.drawText(mOnFormatLabelListener.onFormatValueTextListener(columnEntry),
                    offsetLeft,
                    columnEntry - 10,
                    mRectTextPaint);
        } else {
            canvas.drawText(formatValueText(columnEntry),
                    offsetLeft,
                    columnEntry - 10,
                    mRectTextPaint);
        }
    }


    private String formatValueText(float y) {
        return (int) y + "";
    }

    private void drawBaseline(Canvas canvas, float left, float top, float right, float bottom) {
        //绘制底部外框线
        canvas.drawLine(left, bottom, right, bottom, mMarginLinePaint);
        //绘制底部内框线
        canvas.drawLine(left + mDeep, bottom - mDeep, right, bottom - mDeep, mGridLinePaint);
        //绘制底部斜线
        canvas.drawLine(left, bottom, left + mDeep, bottom - mDeep, mGridLinePaint);
        //绘制左边外框线
        canvas.drawLine(left, top, left, bottom, mMarginLinePaint);
        //绘制左边内框线
        canvas.drawLine(left + mDeep, top, left + mDeep, bottom - mDeep, mGridLinePaint);
    }

    private void drawGridAndLabel(Canvas canvas, float left, float top, float right, float bottom) {
        int dValue =  mMaxValue- mMinValue;
        int interval = dValue / (mConfig.labelCount - 1);
        float yValue = mMinValue;
        float h = bottom - top;
        for (int i = 0; i < mConfig.labelCount; i++) {
            float startY = (top + (mMaxValue - yValue) * h / dValue);
            String text = mOnFormatLabelListener.onFormatLabelListener(yValue);
            if (TextUtils.isEmpty(text)) {
                text = String.valueOf(yValue);
            }
            float textWidth = mLabelPaint.measureText(text);
            final float textX = left - textWidth - 10;
            if (i != 0) {
                if (mConfig.isDrawLabel) {
                    canvas.drawText(text,
                            textX, startY + 10, mLabelPaint);
                }
                canvas.drawLine(left + mDeep, startY - mDeep, right, startY - mDeep, mGridLinePaint);
                canvas.drawLine(left, startY, left + mDeep, startY - mDeep, mGridLinePaint);
            } else {
                if (mConfig.isDrawLabel) {
                    canvas.drawText(text, textX, startY + 10, mLabelPaint);
                }

            }
            yValue += interval;
        }

    }

    public void reset() {
        resetAnimation();
    }

    private void resetAnimation() {
        mValueAnimator.cancel();
        mCurAnimationValue = 0;
        mValueAnimator.start();
    }

    public void setData(List<BarChartEntry> data) {
        mData = data == null ? new ArrayList<BarChartEntry>() : data;
        compareList(mData);
        reset();
    }

    private void compareList(List<BarChartEntry> data) {
        int count=mConfig.labelCount - 1;
        float max = 0;
        float min = 0;
        boolean b=false;
        for (BarChartEntry columnEntry : data) {
            int y = (int) columnEntry.value;
            if (y > max) {
                max = y;
            }
            if (y < min || min == 0) {
                min = y;
            }
        }
        if(max>1000){
            min = min / 1000;
            max = max / 1000;
            b=true;
        }
        int ceilMax= (int) Math.ceil(max);

        if(ceilMax==0){
            mMaxValue=count;
            mMinValue=0;
        }else if(ceilMax<=3&&ceilMax>0){
            max=max*10;
            min=min*10;
            if(max==min||min<=0.4*max){
                int gapY= (int) Math.ceil(max/count);
                mMaxValue=gapY*count/10;
                if(mMaxValue<3){
                    mMaxValue=3;
                }
                mMinValue=0;
            }else{
                int minus= (int) Math.floor((min-0.4*max)/0.6);
                int gapY= (int) (Math.ceil((max-minus)/count));
                mMinValue=minus/10;
                mMaxValue=(minus+gapY*count)/10;
                if(mMaxValue<3){
                    mMaxValue=3;
                }
            }
        }else if(ceilMax>3){
            if(max==min||min<=0.4*max){
                int gapY= (int) Math.ceil(max/count);
                mMaxValue=gapY*count;
                if(mMaxValue<3){
                    mMaxValue=3;
                }
                mMinValue=0;
            }else{
                int minus= (int) Math.floor((min-0.4*max)/0.6);
                int gapY= (int) Math.ceil((max-minus)/count);
                mMinValue=minus;
                mMaxValue=(minus+gapY*count);
                if(mMaxValue<3){
                    mMaxValue=3;
                }
            }
        }
        if(b){
            mMinValue=mMinValue*1000;
            mMaxValue=mMaxValue*1000;
        }
//        float d = max - min;
//        if (d == 0) {
//            min = 0;
//        } else if (d >= 1000) {
//            d = (d - d % 1000);
//            max = max + d + 1000 - max % 1000;
//            min = min - d - min % 1000;
//        } else if (d < 1000 && d >= 100) {
//            d = (d - d % 100);
//            max = max + d + 100 - max % 100;
//            min = min - d - min % 100;
//        } else if (d < 100 && d >= 10) {
//            d = (d - d % 10);
//            max = max + d + 10 - max % 10;
//            min = min - d - min % 10;
//        } else if (d < 10 && d >= 3) {
//            d = (d - d % 3);
//            max = max + d + 3 - max % 3;
//            min = min - d - min % 4;
//        } else {
//            d = 2 / 2;
//            max = max + d;
//            min = min - d;
//        }
//        if (max < 3) {
//            max = 3;
//        }
//        if (min < 0) {
//            min = 0;
//        }
//        mMaxValue = (int) max;
//
//        mMinValue = (int) (min);
    }

    public void setOnFormatLabelListener(OnFormatLabelListener onFormatLabelListener) {
        mOnFormatLabelListener = onFormatLabelListener;
    }


    public void setConfig(EntryConfig config) {
        if (config == null) {
            return;
        }
        mConfig = config;
        mValueAnimator.setDuration(config.durationTime);
        mRectTextPaint.setColor(config.rectTextColor);
        mRectTextPaint.setTextSize(config.rectTextSize);
    }
}
