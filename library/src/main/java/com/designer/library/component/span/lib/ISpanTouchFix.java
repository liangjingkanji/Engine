/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.component.span.lib;

/**
 * @author cginechen
 * @date 2017-08-07
 */

public interface ISpanTouchFix {
  /**
   * 记录当前 Touch 事件对应的点是不是点在了 span 上面
   */
  void setTouchSpanHit(boolean hit);
}
