/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.utils;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.designer.library.base.Library;

/**
 * <pre>
 *     author: Blankj
 *     blog  : http://blankj.com
 *     time  : 2016/09/25
 *     desc  : 剪贴板相关工具类
 * </pre>
 */
public final class ClipboardUtils {

  private ClipboardUtils() {
    throw new UnsupportedOperationException("u can't instantiate me...");
  }

  /**
   * 复制文本到剪贴板
   *
   * @param text 文本
   */
  public static void copyText(final CharSequence text) {
    ClipboardManager clipboard = (ClipboardManager) Library.INSTANCE.getApp()
            .getSystemService(Context.CLIPBOARD_SERVICE);
    clipboard.setPrimaryClip(ClipData.newPlainText("text", text));
  }

  /**
   * 获取剪贴板的文本
   *
   * @return 剪贴板的文本
   */
  public static CharSequence getText() {
    ClipboardManager clipboard = (ClipboardManager) Library.INSTANCE.getApp()
            .getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = clipboard.getPrimaryClip();
    if (clip != null && clip.getItemCount() > 0) {
      return clip.getItemAt(0)
              .coerceToText(Library.INSTANCE.getApp());
    }
    return null;
  }

  /**
   * 复制uri到剪贴板
   *
   * @param uri uri
   */
  public static void copyUri(final Uri uri) {
    ClipboardManager clipboard = (ClipboardManager) Library.INSTANCE.getApp()
            .getSystemService(Context.CLIPBOARD_SERVICE);
    clipboard.setPrimaryClip(ClipData.newUri(Library.INSTANCE.getApp()
            .getContentResolver(), "uri", uri));
  }

  /**
   * 获取剪贴板的uri
   *
   * @return 剪贴板的uri
   */
  public static Uri getUri() {
    ClipboardManager clipboard = (ClipboardManager) Library.INSTANCE.getApp()
            .getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = clipboard.getPrimaryClip();
    if (clip != null && clip.getItemCount() > 0) {
      return clip.getItemAt(0)
              .getUri();
    }
    return null;
  }

  /**
   * 复制意图到剪贴板
   *
   * @param intent 意图
   */
  public static void copyIntent(final Intent intent) {
    ClipboardManager clipboard = (ClipboardManager) Library.INSTANCE.getApp()
            .getSystemService(Context.CLIPBOARD_SERVICE);
    clipboard.setPrimaryClip(ClipData.newIntent("intent", intent));
  }

  /**
   * 获取剪贴板的意图
   *
   * @return 剪贴板的意图
   */
  public static Intent getIntent() {
    ClipboardManager clipboard = (ClipboardManager) Library.INSTANCE.getApp()
            .getSystemService(Context.CLIPBOARD_SERVICE);
    ClipData clip = clipboard.getPrimaryClip();
    if (clip != null && clip.getItemCount() > 0) {
      return clip.getItemAt(0)
              .getIntent();
    }
    return null;
  }
}
