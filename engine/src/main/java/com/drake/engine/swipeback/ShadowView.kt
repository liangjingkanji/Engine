/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.swipeback

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.view.View

/**
 * @author Simon Lee
 * @e-mail jmlixiaomeng@163.com
 * @github https://github.com/Simon-Leeeeeeeee/SLWidget
 * @createdTime 2018-06-19
 */
class ShadowView @JvmOverloads constructor(
    context: Context,
    showShadowBar: Boolean = true,
    showBackground: Boolean = true
) : View(context) {

    private val density: Float
    private val mShadowBarPaint: Paint
    private var mLinearShaderWidth: Int = 0
    private var mShadowColor: Int = 0

    private var isShowShadowBar: Boolean = false
    private val colors = IntArray(11)
    private val positions = FloatArray(11)

    init {
        this.density = context.resources.displayMetrics.density
        mShadowBarPaint = Paint()
        mShadowBarPaint.isDither = true
        mShadowBarPaint.isAntiAlias = true
        mShadowBarPaint.isSubpixelText = true
        setShowColor(Color.BLACK, showShadowBar, showBackground)
    }

    /**
     * 控制阴影显示
     *
     * @param showShadowBar  是否显示阴影条
     * @param showBackground 是否显示背景
     */
    fun setShadowVisiable(showShadowBar: Boolean, showBackground: Boolean) {
        setShowColor(mShadowColor, showShadowBar, showBackground)
    }

    /**
     * 设置阴影颜色
     *
     * @param shadowColor    阴影颜色
     * @param showShadowBar  是否显示阴影条
     * @param showBackground 是否显示背景
     */
    fun setShowColor(shadowColor: Int, showShadowBar: Boolean, showBackground: Boolean) {
        this.mShadowColor = shadowColor or -0x1000000
        this.isShowShadowBar = showShadowBar
        if (isShowShadowBar) {
            for (i in colors.indices) {
                //根据位置计算透明度
                val alpha = getShadowAlpha(i * 0.1f)
                //计算对应颜色
                colors[i] = mShadowColor and (alpha shl 24 or 0XFFFFFF)
                //根据透明度校正对应位置
                val position = getShadowPosition(alpha)
                positions[i] = position
            }
        }
        val background = if (showBackground) ColorDrawable(mShadowColor and 0X66FFFFFF) else null
        setBackground(background)
    }

    /**
     * 根据透明度计算位置比例
     *
     * @param alpha 透明度，取值范围[0,255]
     * @return 比例，取值范围[0,1]
     */
    private fun getShadowPosition(alpha: Int): Float {
        //        float minAlpha = 0F, maxAlpha = 0.5F;
        //        float differ = maxAlpha - minAlpha;
        //        float radius = (differ + 1F / differ) / 2F;
        val radius = 1.25f
        val squarePosition = radius * radius - Math.pow((radius - alpha / 255f).toDouble(), 2.0)
        return if (squarePosition <= 0) {
            0f
        } else if (squarePosition >= 1) {
            1f
        } else {
            Math.sqrt(squarePosition).toFloat()
        }
    }

    /**
     * 根据位置比例计算透明度
     *
     * @param ratio 比例，取值范围[0,1]
     * @return 透明度，取值范围[0,255]
     */
    private fun getShadowAlpha(ratio: Float): Int {
        //        float minAlpha = 0F, maxAlpha = 0.5F;
        //        float differ = maxAlpha - minAlpha;
        //        float radius = (differ + 1F / differ) / 2F;
        val radius = 1.25f
        val alphaF = radius - Math.sqrt((radius * radius - ratio * ratio).toDouble())
        val alphaI = (alphaF * 255 + 0.5f).toInt()
        return if (alphaI <= 0) {
            0
        } else if (alphaI >= 255) {
            255
        } else {
            alphaI
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (width != mLinearShaderWidth) {
            mLinearShaderWidth = width
            val linearShader = LinearGradient(
                mLinearShaderWidth - density * 22, 0f, mLinearShaderWidth.toFloat(), 0f,
                colors, positions, Shader.TileMode.CLAMP
            )
            mShadowBarPaint.shader = linearShader
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (isShowShadowBar) {
            canvas.save()
            canvas.clipRect(right - density * 22, top.toFloat(), right.toFloat(), bottom.toFloat())
            canvas.drawPaint(mShadowBarPaint)
            canvas.restore()
        }
    }

}
