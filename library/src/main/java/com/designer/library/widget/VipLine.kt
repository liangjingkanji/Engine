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
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import org.jetbrains.anko.dip


class VipLine @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val paint = Paint()
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paint.color = Color.parseColor("#FDDEA0")
        paint.strokeWidth = dip(1.5f).toFloat()
        paint.style = Paint.Style.STROKE
        paint.isAntiAlias = true
        paint.strokeCap = Paint.Cap.ROUND
        val path = Path()
        path.moveTo(3f, 0f)
        path.lineTo(3f, height / 2 - dip(5).toFloat())
        path.lineTo(dip(8.66f + 3f).toFloat(), height.toFloat() / 2)
        path.lineTo(3f, height.toFloat() / 2 + dip(5))
        path.lineTo(3f, height.toFloat())
        canvas!!.drawPath(path, paint)
    }
}