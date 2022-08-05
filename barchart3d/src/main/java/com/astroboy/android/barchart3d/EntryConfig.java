package com.astroboy.android.barchart3d;

import android.view.Gravity;

/**
 * @author 黄兴伟 (huangxingwei@parkingwang.com)
 * @since 17-11-24
 */
public class EntryConfig {


    private static final float DEFAULT_SPACE_WIDTH = 20f;
    private static final float DEFAULT_COLUMN_WIDTH = 90f;
    private static final long DEFAULT_ANIMATION_TIME = 1000;
    private static final int DEFAULT_LABEL_COUNT = 4;

    /**
     * 条型图上面文字颜色
     */
    public int rectTextColor;
    /**
     * 条型图上面文字大小
     */
    public float rectTextSize;
    /**
     * y轴绘制个数
     */
    public int labelCount = DEFAULT_LABEL_COUNT;
    /**
     * 网格颜色
     */
    public int gridLineColor;
    /**
     * 右边label字体颜色
     */
    public int labelTextColor;
    /**
     * 是否显示动画
     */
    public boolean showAnimation = true;

    /**
     * Y轴label偏移量
     */
    public float yAxisOffsetRight;

    /**
     * 矩形右边偏移量
     */
    public float rectOffsetRight;
    /**
     * 矩形左边偏移量
     */
    public float rectOffsetLeft;
    /**
     * 直方图间隔
     */
    public float spaceWidth = DEFAULT_SPACE_WIDTH;
    /**
     * 直方图宽度
     */
    public float columnWidget = DEFAULT_COLUMN_WIDTH;
    /**
     * 网格线条宽度
     */
    public float gridLineStrokeWidth;
    /**
     * 矩形上面文字是否显示
     */
    public boolean showRectTopText = false;
    /**
     * 动画时间
     */
    public long durationTime = DEFAULT_ANIMATION_TIME;
    /**
     * 柱状图距离网格偏移量
     */
    public int rectOffsetTop;
    public int rectOffsetBottom;

    /**
     * 是否绘制右边label
     */
    public boolean isDrawLabel = true;

    /**
     * 对齐方式, 目前仅支持start|left , center
     */
    public int gravity = Gravity.CENTER;


    public EntryConfig setGravity(int gravity) {
        this.gravity = gravity;
        return this;
    }


    public EntryConfig setRectTextColor(int color) {
        rectTextColor = color;
        return this;
    }

    public EntryConfig setLabelCount(int count) {
        labelCount = count;
        return this;
    }

    public EntryConfig setGridLineColor(int color) {
        gridLineColor = color;
        return this;
    }

    public EntryConfig setLabelTextColor(int color) {
        labelTextColor = color;
        return this;
    }

    public EntryConfig setSpaceWidth(float spaceWidth) {
        this.spaceWidth = spaceWidth;
        return this;
    }

    public EntryConfig setColumnWidget(float columnWidget) {
        this.columnWidget = columnWidget;
        return this;
    }

    public EntryConfig setRectOffsetRight(float rectOffsetRight) {
        this.rectOffsetRight = rectOffsetRight;
        return this;
    }

    public EntryConfig setAnimation(boolean showAnimation) {
        this.showAnimation = showAnimation;
        return this;
    }

    public EntryConfig setGridLineStrokeWidth(float width) {
        gridLineStrokeWidth = width;
        return this;
    }

    public EntryConfig setShowRectTopText(boolean showRectTopText) {
        this.showRectTopText = showRectTopText;
        return this;
    }

    public EntryConfig setAnimatorTime(long duration) {
        this.durationTime = duration;
        return this;
    }

    public EntryConfig setRectTextSize(float size) {
        this.rectTextSize = size;
        return this;
    }

    public EntryConfig setRectOffsetLeft(float rectoffsetLeft) {
        this.rectOffsetLeft = rectoffsetLeft;
        return this;
    }

    public EntryConfig setDrawLabel(boolean drawLabel) {
        this.isDrawLabel = drawLabel;
        return this;
    }

    public EntryConfig setYAxisOffsetRight(int yAxisOffsetRight) {
        this.yAxisOffsetRight = yAxisOffsetRight;
        return this;
    }
}
