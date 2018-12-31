/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget;

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
