/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.password.imebugfixer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;

/**
 * @see <a href="http://stackoverflow.com/questions/4886858/android-edittext-deletebackspace-key-event">Stack
 * Overflow</a>
 */
public class ImeDelBugFixedEditText extends androidx.appcompat.widget.AppCompatEditText {

  private OnDelKeyEventListener delKeyEventListener;

  public ImeDelBugFixedEditText(Context context) {
    super(context);
  }

  public ImeDelBugFixedEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public ImeDelBugFixedEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
    return new ZanyInputConnection(super.onCreateInputConnection(outAttrs), true);
  }

  public void setDelKeyEventListener(OnDelKeyEventListener delKeyEventListener) {
    this.delKeyEventListener = delKeyEventListener;
  }

  public interface OnDelKeyEventListener {

    void onDeleteClick();

  }

  private class ZanyInputConnection extends InputConnectionWrapper {

    public ZanyInputConnection(InputConnection target, boolean mutable) {
      super(target, mutable);
    }

    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
      if (beforeLength == 1 && afterLength == 0) {
        return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL)) &&
                sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
      }
      return super.deleteSurroundingText(beforeLength, afterLength);
    }

    @Override
    public boolean sendKeyEvent(KeyEvent event) {
      if (event.getAction() == KeyEvent.ACTION_DOWN && event.getKeyCode() == KeyEvent.KEYCODE_DEL) {
        if (delKeyEventListener != null) {
          delKeyEventListener.onDeleteClick();
          return true;
        }
      }
      return super.sendKeyEvent(event);
    }
  }

}