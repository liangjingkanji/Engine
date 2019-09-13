/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget;

import android.content.Context;
import android.util.AttributeSet;

public class FixedCheckBox extends androidx.appcompat.widget.AppCompatCheckBox {

  public FixedCheckBox(Context context) {
    super(context);
  }

  public FixedCheckBox(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public FixedCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  public void toggle() {
  }
}
