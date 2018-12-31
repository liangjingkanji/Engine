/*
 * Copyright (C) 2018,巨神科技有限公司 All rights reserved.
 * Project：Base
 * Author：两津勘吉
 * Date：12/31/18 2:33 AM
 */

package com.designer.library.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.designer.library.R


class ProgressView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progress: Int

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView)
        progress = typedArray.getInt(R.styleable.ProgressView_progress2, 0)
        typedArray.recycle()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制背景
        val paint = Paint()
        paint.color = Color.parseColor("#F5BB8A")
        paint.isAntiAlias = true
        paint.strokeWidth = 1f
        paint.style = Paint.Style.FILL
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawRoundRect(
            RectF(0f, 0f, width.toFloat(), height.toFloat()),
            height.toFloat() / 2,
            height.toFloat() / 2,
            paint
        )

        //绘制白色背景
        paint.color = Color.WHITE
        paint.strokeCap = Paint.Cap.ROUND
        paint.isAntiAlias = true
        paint.strokeWidth = 1f

        canvas.drawRoundRect(
            RectF(
                (height * 0.05).toFloat(),
                (height * 0.05).toFloat(),
                width.toFloat() - (height * 0.05).toFloat(),
                height.toFloat() - (height * 0.05).toFloat()
            ), (height * 0.45).toFloat(),
            (height * 0.45).toFloat(), paint
        )

        //绘制前景
        paint.color = Color.parseColor("#FADDC4")
        paint.isAntiAlias = true
        paint.strokeWidth = 1f
        paint.style = Paint.Style.FILL
        paint.strokeCap = Paint.Cap.ROUND
        canvas.drawRoundRect(
            RectF(
                (height * 0.15).toFloat(),
                (height * 0.15).toFloat(),
                width.toFloat() * progress / 100 - (height * 0.15).toFloat(),
                height.toFloat() - (height * 0.15).toFloat()
            ), (height * 0.35).toFloat(),
            (height * 0.35).toFloat(), paint
        )
    }

    fun setProgress(mProcess: Int) {
        this.progress = mProcess
    }
}