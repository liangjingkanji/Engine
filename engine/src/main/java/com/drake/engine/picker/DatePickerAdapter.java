/*
 * Copyright (C) 2018 Drake, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.drake.engine.picker;

import java.text.DecimalFormat;


public class DatePickerAdapter implements PickAdapter {

    private final DecimalFormat mDecimalFormat;
    private int mMinValue;
    private int mMaxValue;

    public DatePickerAdapter(int minValue, int maxValue) {
        this(minValue, maxValue, null);
    }

    public DatePickerAdapter(int minValue, int maxValue, DecimalFormat decimalFormat) {
        this.mMinValue = minValue;
        this.mMaxValue = maxValue;
        this.mDecimalFormat = decimalFormat;
    }

    @Override
    public int getCount() {
        return mMaxValue - mMinValue + 1;
    }

    @Override
    public String getItem(int position) {
        if (position >= 0 && position < getCount()) {
            if (mDecimalFormat == null) {
                return String.valueOf(mMinValue + position);
            } else {
                return mDecimalFormat.format(mMinValue + position);
            }
        }
        return null;
    }

    public int getDate(int position) {
        if (position >= 0 && position < getCount()) {
            return mMinValue + position;
        }
        return 0;
    }

    public int indexOf(String valueString) {
        int value;
        try {
            value = Integer.parseInt(valueString);
        } catch (NumberFormatException e) {
            return -1;
        }
        return indexOf(value);
    }

    public int indexOf(int value) {
        if (value < mMinValue || value > mMaxValue) {
            return -1;
        }
        return value - mMinValue;
    }

    public int getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        this.mMinValue = minValue;
    }

    public int getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        this.mMaxValue = maxValue;
    }

}
