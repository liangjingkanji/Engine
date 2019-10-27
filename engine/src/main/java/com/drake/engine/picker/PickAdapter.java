/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.picker;

/**
 * @author Simon Lee
 * @e-mail jmlixiaomeng@163.com
 * @github https://github.com/Simon-Leeeeeeeee/SLWidget
 * @createdTime 2018-05-17
 */
public interface PickAdapter {

    /**
     * 返回数据总个数
     */
    int getCount();

    /**
     * 返回一条对应index的数据
     */
    String getItem(int position);

}
