/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.HashMap;
import java.util.LinkedHashMap;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

/**
 * Author     : shandirong
 * Date       : 2018/11/24 14:27
 */
public class AutoHeightViewPager extends ViewPager {
  private int current;
  private int height = 0;
  /**
   * 保存position与对于的View
   */
  private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

  private boolean scrollble = true;

  public AutoHeightViewPager(Context context) {
    super(context);
  }

  public AutoHeightViewPager(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (mChildrenViews.size() > current) {
      View child = mChildrenViews.get(current);
      if (child != null) {
        child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        height = child.getMeasuredHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
      }
    }
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  public void resetHeight(int current) {
    this.current = current;
    if (mChildrenViews.size() > current) {
      ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) getLayoutParams();
      if (layoutParams == null) {
        layoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, height);
      } else {
        layoutParams.height = height;
      }
      setLayoutParams(layoutParams);
    }
  }

  /**
   * 保存position与对于的View
   */
  public void setObjectForPosition(View view, int position) {
    mChildrenViews.put(position, view);
  }

  public void clear() {
    mChildrenViews.clear();
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (!scrollble) {
      return true;
    }
    return super.onTouchEvent(ev);
  }


  public boolean isScrollble() {
    return scrollble;
  }

  public void setScrollble(boolean scrollble) {
    this.scrollble = scrollble;
  }

}
