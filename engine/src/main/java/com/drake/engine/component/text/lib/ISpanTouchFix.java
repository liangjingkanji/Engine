/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.text.lib;

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
