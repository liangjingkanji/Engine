/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.tablayout;

import android.content.Context;
import android.content.res.TypedArray;

import com.designer.library.R;

class ThemeUtils {

  private static final int[] APPCOMPAT_CHECK_ATTRS = {
          R.attr.colorPrimary
  };

  static void checkAppCompatTheme(Context context) {
    TypedArray a = context.obtainStyledAttributes(APPCOMPAT_CHECK_ATTRS);
    final boolean failed = !a.hasValue(0);
    if (a != null) {
      a.recycle();
    }
    if (failed) {
      throw new IllegalArgumentException("You need to use a Theme.AppCompat theme "
              + "(or descendant) with the design library.");
    }
  }
}
