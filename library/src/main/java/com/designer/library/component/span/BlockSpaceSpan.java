/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.span;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.style.ReplacementSpan;

import androidx.annotation.NonNull;

/**
 * 提供一个整行的空白的Span，可用来用于制作段间距
 *
 * @author cginechen
 * @date 2016-02-17
 */
public class BlockSpaceSpan extends ReplacementSpan {

  private int mHeight;

  public BlockSpaceSpan(int height) {
    mHeight = height;
  }

  @Override
  public int getSize(@NonNull Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
    if (fm != null) {
      //return后宽度为0，因此实际空隙和段落开始在同一行，需要加上一行的高度
      fm.ascent = fm.top = -mHeight - paint.getFontMetricsInt(fm);
      fm.descent = fm.bottom = 0;
    }
    return 0;
  }

  @Override
  public void draw(@NonNull Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, @NonNull Paint paint) {
  }
}
