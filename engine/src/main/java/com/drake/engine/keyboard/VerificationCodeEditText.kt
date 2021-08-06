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
package com.drake.engine.keyboard

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.InputFilter
import android.util.AttributeSet
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.drake.engine.R

/**
 * 验证码输入框
 * https://github.com/kongpf8848/ViewWorld/blob/master/app/src/main/java/com/github/kongpf8848/viewworld/views/VerificationCodeEditText.kt
 */
class VerificationCodeEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatEditText(context, attrs, defStyleAttr) {

    companion object {
        const val DEFAULT_CODE_LENGTH = 6
        const val DEFAULT_CODE_MARGIN = 20f
        const val DEFAULT_CODE_WIDTH = 150
        const val DEFAULT_CODE_HEIGHT = 150
        const val BLINK = 500L
    }

    /**
     *验证码个数
     */
    private var mCodeLength = DEFAULT_CODE_LENGTH


    /**
     * 验证码之间的间隔
     */
    private var mCodeMargin = DEFAULT_CODE_MARGIN

    /**
     * 验证码背景
     */
    private var mCodeBackground: Drawable? = null

    /**
     * 验证码宽度
     */
    private var mCodeWidth = DEFAULT_CODE_WIDTH

    /**
     * 验证码高度
     */
    private var mCodeHeight = DEFAULT_CODE_HEIGHT

    /**
     * 光标相关
     */
    private var mCursorDrawableRes = 0
    private var mCursorDrawable: Drawable? = null
    private var mBlink: Blink? = null
    private var mCursorVisible = true
    private var mCursorFlag = false

    /**
     * 输入完成监听
     */
    private var inputTextListener: OnInputTextListener? = null

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.VerificationCodeEditText)
        for (i in 0 until typedArray.indexCount) {
            when (val index = typedArray.getIndex(i)) {
                R.styleable.VerificationCodeEditText_codeLength -> {
                    mCodeLength = typedArray.getInteger(index, DEFAULT_CODE_LENGTH)
                }
                R.styleable.VerificationCodeEditText_codeBackground -> {
                    mCodeBackground = typedArray.getDrawable(index)
                }
                R.styleable.VerificationCodeEditText_codeMargin -> {
                    mCodeMargin = typedArray.getDimension(index, DEFAULT_CODE_MARGIN)
                }
                R.styleable.VerificationCodeEditText_codeWidth -> {
                    mCodeWidth = typedArray.getDimensionPixelSize(index, DEFAULT_CODE_WIDTH)
                }
                R.styleable.VerificationCodeEditText_codeCursorVisible -> {
                    mCursorVisible = typedArray.getBoolean(index, false)
                }
                R.styleable.VerificationCodeEditText_codeCursorDrawable -> {
                    mCursorDrawableRes = typedArray.getResourceId(index, 0)
                }
            }
        }
        typedArray.recycle()


        if (mCodeLength <= 0) {
            throw IllegalArgumentException("code length must large than 0!!!")
        }
        if (mCodeBackground == null) {
            throw NullPointerException("code background drawable not allowed to be null!!!")
        }
        if (mCursorVisible) {
            if (mCursorDrawable == null && mCursorDrawableRes == 0) {
                mCursorDrawable = GradientDrawable().apply {
                    setColor(ContextCompat.getColor(context, R.color.black))
                    setSize((context.resources.displayMetrics.density * 1).toInt(), 0)
                }
            }
        }

        /**
         * 禁用长按事件
         */
        isLongClickable = false
        /**
         * 隐藏EditText自带光标，防止onDraw方法一直被调用
         */
        isCursorVisible = false
        /**
         * 设置输入框最大长度
         */
        setMaxLength(mCodeLength)
        /**
         * 设置背景为透明
         */
        setBackgroundColor(Color.TRANSPARENT)
    }


    override fun onTextContextMenuItem(id: Int): Boolean {
        return false
    }


    /**
     *设置输入框最大长度
     */
    private fun setMaxLength(maxLength: Int) {
        if (maxLength >= 0) {
            filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            mCodeHeight = mCodeWidth
            val newWidth = mCodeWidth * mCodeLength + (mCodeLength - 1) * mCodeMargin
            setMeasuredDimension(
                MeasureSpec.makeMeasureSpec(newWidth.toInt(), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(mCodeHeight, MeasureSpec.EXACTLY)
            )
        } else {
            mCodeWidth = ((widthSize - mCodeMargin * (mCodeLength - 1)) / mCodeLength).toInt()
            mCodeHeight = mCodeWidth
            setMeasuredDimension(
                widthMeasureSpec,
                MeasureSpec.makeMeasureSpec(mCodeHeight, MeasureSpec.EXACTLY)
            )
        }

    }

    override fun onDraw(canvas: Canvas) {
        drawBackground(canvas)
        drawText(canvas)
        drawCursor(canvas)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        resumeBlink()
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        suspendBlink()
    }

    override fun onTextChanged(
        text: CharSequence?,
        start: Int,
        lengthBefore: Int,
        lengthAfter: Int
    ) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter)
        text?.apply {
            if (length >= mCodeLength) {
                suspendBlink()
                val imm =
                    context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(windowToken, 0)
                inputTextListener?.onInputTextComplete(this)
            } else if (length + 1 == mCodeLength && lengthBefore == 1) {
                resumeBlink()
            }
        }
    }


    override fun onWindowFocusChanged(hasWindowFocus: Boolean) {
        super.onWindowFocusChanged(hasWindowFocus)
        if (hasWindowFocus) {
            mBlink?.unCancel()
            makeBlink()
        } else {
            mBlink?.cancel()
        }
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (focused) {
            makeBlink()
        }
    }

    /**
     * 绘制背景
     */
    private fun drawBackground(canvas: Canvas) {
        mCodeBackground?.run {
            val currentIndex = 0.coerceAtLeast(editableText.length)
            val count = canvas.save()
            for (i in 0 until mCodeLength) {
                bounds = Rect(0, 0, mCodeWidth, mCodeHeight)
                if (currentIndex == i) {
                    state = intArrayOf(android.R.attr.state_selected)
                } else {
                    state = intArrayOf(android.R.attr.state_enabled)
                }
                draw(canvas)
                canvas.translate(mCodeWidth + mCodeMargin, 0f)
            }
            canvas.restoreToCount(count)
        }

    }

    /**
     * 绘制文本
     */
    private fun drawText(canvas: Canvas) {
        val count = canvas.save()
        canvas.translate(0f, 0f)
        val textColor = currentTextColor
        for (i in editableText.indices) {
            val textWidth = paint.measureText(editableText[i].toString())
            val fontMetrics = Paint.FontMetrics()
            paint.getFontMetrics(fontMetrics)
            paint.color = textColor
            val x = (mCodeWidth + mCodeMargin) * i + mCodeWidth / 2f - textWidth / 2f
            val y = mCodeHeight / 2f - (fontMetrics.top + fontMetrics.bottom) / 2f
            canvas.drawText(editableText[i].toString(), x, y, paint)
        }
        canvas.restoreToCount(count)
    }

    /**
     * 绘制光标
     */
    private fun drawCursor(canvas: Canvas) {
        if (!mCursorVisible) return
        mCursorFlag = !mCursorFlag
        if (mCursorFlag) {
            if (mCursorDrawable == null && mCursorDrawableRes != 0) {
                mCursorDrawable = context.resources.getDrawable(mCursorDrawableRes)
            }
            mCursorDrawable?.apply {
                val currentIndex = 0.coerceAtLeast(editableText.length)
                val count = canvas.save()
                val line = layout.getLineForOffset(selectionStart)
                val top = layout.getLineTop(line)
                val bottom = layout.getLineBottom(line)
                val mTempRect = Rect()
                getPadding(mTempRect)
                bounds = Rect(0, top - mTempRect.top, intrinsicWidth, bottom + mTempRect.bottom)
                canvas.translate(
                    (mCodeWidth + mCodeMargin) * currentIndex + mCodeWidth / 2f - intrinsicWidth / 2f,
                    (mCodeHeight - bounds.height()) / 2f
                )
                draw(canvas)
                canvas.restoreToCount(count)
            }
        }
    }


    private fun suspendBlink() {
        mBlink?.cancel()
    }

    private fun resumeBlink() {
        if (mBlink != null) {
            mBlink?.unCancel()
            makeBlink()
        }
    }

    private fun makeBlink() {
        if (shouldBlink()) {
            if (mBlink == null) mBlink = Blink()
            removeCallbacks(mBlink)
            postDelayed(mBlink, BLINK)
        } else {
            if (mBlink != null) removeCallbacks(mBlink)
        }
    }

    private fun shouldBlink(): Boolean {
        if (!mCursorVisible || !isFocused) return false
        val start: Int = selectionStart
        if (start < 0) return false
        val end: Int = selectionEnd
        return if (end < 0) false else start == end
    }

    inner class Blink : Runnable {
        private var mCancelled = false
        override fun run() {
            if (mCancelled) {
                return
            }
            removeCallbacks(this)
            if (shouldBlink()) {
                if (layout != null) {
                    invalidate()
                }
                postDelayed(this, BLINK)
            }
        }

        fun cancel() {
            if (!mCancelled) {
                removeCallbacks(this)
                mCancelled = true
            }
        }

        fun unCancel() {
            mCancelled = false
        }
    }

    fun setOnInputTextListener(listener: OnInputTextListener) {
        this.inputTextListener = listener
    }

    interface OnInputTextListener {
        fun onInputTextComplete(text: CharSequence)
    }

}