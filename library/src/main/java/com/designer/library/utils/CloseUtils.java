/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils;

import java.io.Closeable;
import java.io.IOException;


public final class CloseUtils {

  private CloseUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * Close the io stream.
   *
   * @param closeables closeables
   */
  public static void closeIO(final Closeable... closeables) {
    if (closeables == null) {
      return;
    }
    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Close the io stream quietly.
   *
   * @param closeables closeables
   */
  public static void closeIOQuietly(final Closeable... closeables) {
    if (closeables == null) {
      return;
    }
    for (Closeable closeable : closeables) {
      if (closeable != null) {
        try {
          closeable.close();
        } catch (IOException ignored) {
        }
      }
    }
  }
}
