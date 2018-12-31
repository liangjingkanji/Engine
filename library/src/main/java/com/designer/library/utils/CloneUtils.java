/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


public final class CloneUtils {

  /**
   * Deep clone.
   *
   * @param data The data.
   * @param <T>  The value type.
   * @return The object of cloned
   */
  public static <T> T deepClone(final Serializable data) {
    if (data == null) {
      return null;
    }
    return (T) bytes2Object(serializable2Bytes(data));
  }

  private static byte[] serializable2Bytes(final Serializable serializable) {
    if (serializable == null) {
      return null;
    }
    ByteArrayOutputStream baos;
    ObjectOutputStream oos = null;
    try {
      oos = new ObjectOutputStream(baos = new ByteArrayOutputStream());
      oos.writeObject(serializable);
      return baos.toByteArray();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        if (oos != null) {
          oos.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static Object bytes2Object(final byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    ObjectInputStream ois = null;
    try {
      ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
      return ois.readObject();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    } finally {
      try {
        if (ois != null) {
          ois.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
