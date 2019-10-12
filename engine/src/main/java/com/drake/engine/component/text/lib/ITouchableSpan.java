/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.text.lib;

import android.view.View;

/**
 * @author cginechen
 * @date 2017-03-20
 */

public interface ITouchableSpan {
    void setPressed(boolean pressed);

    void onClick(View widget);
}
