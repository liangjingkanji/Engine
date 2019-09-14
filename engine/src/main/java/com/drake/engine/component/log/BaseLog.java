/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.log;

import android.util.Log;


public class BaseLog {

  private static final int MAX_LENGTH = 4000;

  public static void printDefault(int type, String tag, String msg) {
    int index = 0;
    int length = msg.length();
    int countOfSub = length / MAX_LENGTH;
    if (countOfSub > 0) {
      for (int i = 0; i < countOfSub; i++) {
        String sub = msg.substring(index, index + MAX_LENGTH);
        printSub(type, tag, sub);
        index += MAX_LENGTH;
      }
      printSub(type, tag, msg.substring(index, length));
    } else {
      printSub(type, tag, msg);
    }
  }

  private static void printSub(int type, String tag, String sub) {
    switch (type) {
      case LogCat.V:
        Log.v(tag, sub);
        break;
      case LogCat.D:
        Log.d(tag, sub);
        break;
      case LogCat.I:
        Log.i(tag, sub);
        break;
      case LogCat.W:
        Log.w(tag, sub);
        break;
      case LogCat.E:
        Log.e(tag, sub);
        break;
      case LogCat.A:
        Log.wtf(tag, sub);
        break;
    }
  }

}
