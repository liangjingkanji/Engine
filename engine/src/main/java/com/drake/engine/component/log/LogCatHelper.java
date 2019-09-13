/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.log;

import android.text.TextUtils;
import android.util.Log;


public class LogCatHelper {

  public static boolean isEmpty(String line) {
    return TextUtils.isEmpty(line) || line.equals("\n") || line.equals("\t")
            || TextUtils.isEmpty(line.trim());
  }

  public static void printLine(String tag, boolean isTop) {
    if (isTop) {
      Log.d(tag, "╔═══════════════════════════════════════════════════════════════════════════════════════");
    } else {
      Log.d(tag, "╚═══════════════════════════════════════════════════════════════════════════════════════");
    }
  }

}
