/*
 * Copyright (C) 2018 Drake, Inc. https://github.com/liangjingkanji
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

package com.drake.engine.text.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.core.view.ViewCompat;

import com.drake.engine.text.lib.ITouchableSpan;

/**
 * 可 Touch 的 Span，在 {@link #setPressed(boolean)} 后根据是否 pressed 来触发不同的UI状态
 * <p>
 * 提供设置 span 的文字颜色和背景颜色的功能, 在构造时传入
 * </p>
 */
public abstract class TouchableSpan extends ClickableSpan implements ITouchableSpan {

    private boolean isPressed;
    @ColorInt
    private int normalBackgroundColor;
    @ColorInt
    private int pressedBackgroundColor;
    @ColorInt
    private int normalTextColor;
    @ColorInt
    private int pressedTextColor;

    private boolean isNeedUnderline = true;

    public TouchableSpan(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    public TouchableSpan(@ColorInt int normalTextColor, @ColorInt int pressedTextColor, @ColorInt int normalBackgroundColor, @ColorInt int pressedBackgroundColor) {
        this.normalTextColor = normalTextColor;
        this.pressedTextColor = pressedTextColor;
        this.normalBackgroundColor = normalBackgroundColor;
        this.pressedBackgroundColor = pressedBackgroundColor;
    }

    public abstract void onSpanClick(View widget);

    @Override
    public final void onClick(View widget) {
        if (ViewCompat.isAttachedToWindow(widget)) {
            onSpanClick(widget);
        }
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        if (pressedTextColor == 0) {
            ds.setColor(normalTextColor);
        } else {
            ds.setColor(isPressed ? pressedTextColor : normalTextColor);
            ds.bgColor = isPressed ? pressedBackgroundColor : normalBackgroundColor;
        }
        ds.setUnderlineText(isNeedUnderline);
    }

    public int getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    public int getPressedBackgroundColor() {
        return pressedBackgroundColor;
    }

    public int getPressedTextColor() {
        return pressedTextColor;
    }

    public void setPressedTextColor(int pressedTextColor) {
        this.pressedTextColor = pressedTextColor;
    }

    public boolean isPressed() {
        return isPressed;
    }

    public void setPressed(boolean isSelected) {
        isPressed = isSelected;
    }

    public void setIsNeedUnderline(boolean isNeedUnderline) {
        this.isNeedUnderline = isNeedUnderline;
    }
}
