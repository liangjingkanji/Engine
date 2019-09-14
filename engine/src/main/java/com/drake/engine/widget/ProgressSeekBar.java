/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 不支持划动的SeekBar
 */
public class ProgressSeekBar extends androidx.appcompat.widget.AppCompatSeekBar {

  public ProgressSeekBar(Context context) {
    super(context);
  }

  public ProgressSeekBar(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ProgressSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return false;
  }
}
