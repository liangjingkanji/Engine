/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.tablayout;

import android.os.Build;

class ViewUtils {

  static final ValueAnimatorCompat.Creator DEFAULT_ANIMATOR_CREATOR
          = new ValueAnimatorCompat.Creator() {
    @Override
    public ValueAnimatorCompat createAnimator() {
      return new ValueAnimatorCompat(Build.VERSION.SDK_INT >= 12
              ? new ValueAnimatorCompatImplHoneycombMr1()
              : new ValueAnimatorCompatImplGingerbread());
    }
  };

  static ValueAnimatorCompat createAnimator() {
    return DEFAULT_ANIMATOR_CREATOR.createAnimator();
  }

}
