/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;

/**
 * 可以多行布局的RadioGroup，但是会用掉子RadioButton的OnCheckedChangeListener
 * A RadioGroup allow multiple rows layout, will use the RadioButton's OnCheckedChangeListener
 */
public class NestedRadioGroup extends RadioGroup {

  public NestedRadioGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public NestedRadioGroup(Context context) {
    super(context);
    init();
  }

  private void init() {
    setOnHierarchyChangeListener(new OnHierarchyChangeListener() {
      public void onChildViewRemoved(View parent, View child) {
        if (parent == NestedRadioGroup.this && child instanceof ViewGroup) {
          for (RadioButton radioButton : getRadioButtonFromGroup((ViewGroup) child)) {
            radioButton.setOnCheckedChangeListener(null);
          }
        }
      }

      @Override
      public void onChildViewAdded(View parent, View child) {
        if (parent == NestedRadioGroup.this && child instanceof ViewGroup) {
          for (final RadioButton radioButton : getRadioButtonFromGroup((ViewGroup) child)) {
            int id = radioButton.getId();
            // generates an id if it's missing
            if (id == View.NO_ID) {
              if (VERSION.SDK_INT >= 17) id = View.generateViewId();
              else id = radioButton.hashCode();
              radioButton.setId(id);
            }
            if (radioButton.isChecked()) {
              check(id);
            }
            radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
              public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                  radioButton.setOnCheckedChangeListener(null);
                  check(buttonView.getId());
                  radioButton.setOnCheckedChangeListener(this);
                }
              }
            });

          }
        }
      }
    });
  }

  private boolean checking = false;

  @Override
  public void check(int id) {
    if (checking) return;
    checking = true;
    super.check(id);
    checking = false;
  }

  private ArrayList<RadioButton> getRadioButtonFromGroup(ViewGroup group) {
    if (group == null) return new ArrayList<>();
    ArrayList<RadioButton> list = new ArrayList<>();
    getRadioButtonFromGroup(group, list);
    return list;
  }

  private void getRadioButtonFromGroup(ViewGroup group, ArrayList<RadioButton> list) {
    for (int i = 0, count = group.getChildCount(); i < count; i++) {
      View child = group.getChildAt(i);
      if (child instanceof RadioButton) {
        list.add((RadioButton) child);

      } else if (child instanceof ViewGroup) {
        getRadioButtonFromGroup((ViewGroup) child, list);
      }
    }
  }

}
