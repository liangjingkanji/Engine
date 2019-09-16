/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.viewpager.widget.PagerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.drake.engine.base.EngineKt.App;

public class BasePagerAdapter extends PagerAdapter {

  private List<String> titles = new ArrayList<>();
  private List<ViewDataBinding> viewDataBindings = new ArrayList<>();

  /**
   * 布局
   */
  public BasePagerAdapter(Integer[] layoutIds) {
    for (Integer layoutId : layoutIds) {
      ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(App), layoutId, null, false);
      viewDataBindings.add(viewDataBinding);
    }
  }

  public BasePagerAdapter() {
  }

  /**
   * 标题和布局
   */
  public BasePagerAdapter(String[] titles, Integer[] layoutIds) {
    this.titles.addAll(Arrays.asList(titles));
    for (Integer layoutId : layoutIds) {
      ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(App), layoutId, null, false);
      viewDataBindings.add(viewDataBinding);
    }
  }


  @Override
  public int getCount() {
    return viewDataBindings.size();
  }

  @NonNull
  @Override
  public Object instantiateItem(@NonNull ViewGroup container, int position) {
    ViewDataBinding viewDataBinding = viewDataBindings.get(position);
    View view = viewDataBinding.getRoot();
    container.addView(view);
    return view;
  }

  @Override
  public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
    container.removeView((View) object);
  }

  @Override
  public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
    return view == object;
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    if (titles != null) {
      return titles.get(position);
    } else {
      return null;
    }
  }

  public <B extends ViewDataBinding> B getViewDataBinding(int position) {
    return (B) viewDataBindings.get(position);
  }

  /**
   * 通过布局创建并且返回ViewDataBinding
   */
  public <B extends ViewDataBinding> B add(int layoutId) {
    ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(App), layoutId, null, false);
    viewDataBindings.add(viewDataBinding);
    return (B) viewDataBinding;
  }

  public <B extends ViewDataBinding> B add(int layoutId, String title) {
    ViewDataBinding viewDataBinding = DataBindingUtil.inflate(LayoutInflater.from(App), layoutId, null, false);
    viewDataBindings.add(viewDataBinding);
    titles.add(title);
    return (B) viewDataBinding;
  }
}
