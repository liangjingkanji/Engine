/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.log;

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
