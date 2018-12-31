/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.designer.library.R;

import androidx.appcompat.widget.AppCompatImageView;

public class RectangleImageView extends AppCompatImageView {

  private float mRatio;

  public RectangleImageView(Context context) {
    this(context, null);
  }

  public RectangleImageView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RectangleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  /**
   * 初始化
   *
   * @param context 上下文
   * @param attrs   属性
   */
  private void init(Context context, AttributeSet attrs) {
    TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RectangleImageView);
    mRatio = typedArray.getFloat(R.styleable.RectangleImageView_ratio, 0);
    typedArray.recycle();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 宽模式
    int widthMode = View.MeasureSpec.getMode(widthMeasureSpec);
    // 宽大小
    int widthSize = View.MeasureSpec.getSize(widthMeasureSpec);
    // 高大小
    int heightSize;
    // 只有宽的值是精确的才对高做精确的比例校对
    if (widthMode == View.MeasureSpec.EXACTLY && mRatio > 0) {
      heightSize = (int) (widthSize / mRatio + 0.5f);
      heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(heightSize,
              View.MeasureSpec.EXACTLY);
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
