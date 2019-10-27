
package com.drake.engine.text.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

/**
 * 支持设置图片左右间距的 ImageSpan
 */
public class MarginImageSpan extends AlignImageSpan {

    private int spanMarginLeft = 0;
    private int spanMarginRight = 0;
    private int offsetY = 0;

    public MarginImageSpan(Drawable d, int verticalAlignment, int marginLeft, int marginRight) {
        super(d, verticalAlignment);
        spanMarginLeft = marginLeft;
        spanMarginRight = marginRight;
    }

    public MarginImageSpan(Drawable d, int verticalAlignment, int marginLeft, int marginRight, int offsetY) {
        this(d, verticalAlignment, marginLeft, marginRight);
        this.offsetY = offsetY;
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        if (spanMarginLeft != 0 || spanMarginRight != 0) {
            super.getSize(paint, text, start, end, fm);
            Drawable d = getDrawable();
            return d.getIntrinsicWidth() + spanMarginLeft + spanMarginRight;
        } else {
            return super.getSize(paint, text, start, end, fm);
        }
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end,
                     float x, int top, int y, int bottom, Paint paint) {
        canvas.save();
        canvas.translate(0, offsetY);
        // marginRight不用专门处理，只靠getSize()中改变即可
        super.draw(canvas, text, start, end, x + spanMarginLeft, top, y, bottom, paint);
        canvas.restore();
    }
}
