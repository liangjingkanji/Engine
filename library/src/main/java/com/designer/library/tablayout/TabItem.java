/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.tablayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.designer.library.R;

import androidx.appcompat.widget.TintTypedArray;

/**
 * TabItem is a special 'view' which allows you to declare tab items for a {@link TabLayout}
 * within a layout. This view is not actually added to TabLayout, it is just a dummy which allows
 * setting of a tab items's text, icon and custom layout. See TabLayout for more information on how
 * to use it.
 *
 * @attr ref android.support.design.R.styleable#TabItem_android_icon
 * @attr ref android.support.design.R.styleable#TabItem_android_text
 * @attr ref android.support.design.R.styleable#TabItem_android_layout
 * @see TabLayout
 */
public final class TabItem extends View {
  final CharSequence mText;
  final Drawable mIcon;
  final int mCustomLayout;

  public TabItem(Context context) {
    this(context, null);
  }

  @SuppressLint("RestrictedApi")
  public TabItem(Context context, AttributeSet attrs) {
    super(context, attrs);
    @SuppressLint("RestrictedApi") final TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
            R.styleable.TabItem);
    mText = a.getText(R.styleable.TabItem_android_text);
    mIcon = a.getDrawable(R.styleable.TabItem_android_icon);
    mCustomLayout = a.getResourceId(R.styleable.TabItem_android_layout, 0);
    a.recycle();
  }
}