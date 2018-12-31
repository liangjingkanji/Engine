/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget;

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
