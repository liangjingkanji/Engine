/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget;

import android.content.Context;
import android.text.TextUtils.TruncateAt;
import android.util.AttributeSet;

public class MarqueeTextView extends androidx.appcompat.widget.AppCompatTextView {

  public MarqueeTextView(Context context) {
    super(context);
  }

  public MarqueeTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public MarqueeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    setEllipsize(TruncateAt.MARQUEE);
    setSingleLine(true);
  }

  @Override
  public boolean isFocused() {
    return true;
  }


}
