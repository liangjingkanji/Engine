/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.text.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;

/**
 * 支持调整字体大小的 span。{@link android.text.style.AbsoluteSizeSpan} 可以调整字体大小，但在中英文混排下由于 decent 的不同，
 * 无法根据具体需求进行底部对齐或者顶部对齐。而 TextSizeSpan 则可以多传一个参数，让你可以根据具体情况来决定偏移值。
 *
 * @author cginechen
 * @date 2016-12-02
 */

public class TextSizeSpan extends ReplacementSpan {
    private int mTextSize;
    private int mVerticalOffset;
    private Paint mPaint;

    public TextSizeSpan(int textSize, int verticalOffset) {
        mTextSize = textSize;
        mVerticalOffset = verticalOffset;
    }

    @Override
    public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        mPaint = new Paint(paint);
        mPaint.setTextSize(mTextSize);
        if (mTextSize > paint.getTextSize() && fm != null) {
            Paint.FontMetricsInt newFm = mPaint.getFontMetricsInt();
            fm.descent = newFm.descent;
            fm.ascent = newFm.ascent;
            fm.top = newFm.top;
            fm.bottom = newFm.bottom;
        }
        return (int) mPaint.measureText(text, start, end);
    }

    @Override
    public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top,
                     int y, int bottom, @NonNull Paint paint) {
        int baseline = y + mVerticalOffset;
        canvas.drawText(text, start, end, x, baseline, mPaint);
    }
}
