/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.utils

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.drake.engine.base.getApp
import org.jetbrains.anko.dip
import java.util.*

/**
 * 用于图形验证码的工具类
 */
class ImageCodeUtils {
    private var mPaddingLeft: Int = 0
    private var mPaddingTop: Int = 0
    private val mBuilder = StringBuilder()
    private val mRandom = Random()
    private var DEFAULT_WIDTH = 300//默认宽度.图片的总宽
    private var DEFAULT_HEIGHT = 100//默认高度.图片的总高
    /**
     * 得到图片中的验证码字符串
     */
    var code: String? = null
        private set

    //生成验证码图片  返回类型为bitmap 直接用imageview.setbitmap()即可
    fun createBitmap(): Bitmap {
        mPaddingLeft = 0 //每次生成验证码图片时初始化
        mPaddingTop = 0
        DEFAULT_WIDTH = App.dip(100f)
        DEFAULT_HEIGHT = App.dip(40)
        val bitmap = Bitmap.createBitmap(DEFAULT_WIDTH, DEFAULT_HEIGHT, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        code = createCode()

        canvas.drawColor(Color.rgb(DEFAULT_COLOR, DEFAULT_COLOR, DEFAULT_COLOR))
        val paint = Paint()
        paint.textSize = DEFAULT_FONT_SIZE.toFloat()

        for (i in 0 until code!!.length) {
            randomTextStyle(paint)
            randomPadding()
            canvas.drawText(code!![i] + "", mPaddingLeft.toFloat(), mPaddingTop.toFloat(), paint)
        }

        //干扰线
        for (i in 0 until DEFAULT_LINE_NUMBER) {
            drawLine(canvas, paint)
        }

        canvas.save()//保存
        canvas.restore()
        return bitmap
    }

    //生成验证码
    fun createCode(): String {
        mBuilder.delete(0, mBuilder.length) //使用之前首先清空内容

        for (i in 0 until DEFAULT_CODE_LENGTH) {
            mBuilder.append(CHARS[mRandom.nextInt(CHARS.size)])
        }

        return mBuilder.toString()
    }

    //生成干扰线
    private fun drawLine(canvas: Canvas, paint: Paint) {
        val color = randomColor()
        val startX = mRandom.nextInt(DEFAULT_WIDTH)
        val startY = mRandom.nextInt(DEFAULT_HEIGHT)
        val stopX = mRandom.nextInt(DEFAULT_WIDTH)
        val stopY = mRandom.nextInt(DEFAULT_HEIGHT)
        paint.strokeWidth = 1f
        paint.color = color
        canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
    }

    //随机颜色
    private fun randomColor(): Int {
        mBuilder.delete(0, mBuilder.length) //使用之前首先清空内容

        var haxString: String
        for (i in 0..2) {
            haxString = Integer.toHexString(mRandom.nextInt(0xFF))
            if (haxString.length == 1) {
                haxString = "0$haxString"
            }

            mBuilder.append(haxString)
        }

        return Color.parseColor("#" + mBuilder.toString())
    }

    //随机文本样式
    private fun randomTextStyle(paint: Paint) {
        val color = randomColor()
        paint.color = color
        paint.isFakeBoldText = mRandom.nextBoolean()  //true为粗体，false为非粗体
        var skewX = (mRandom.nextInt(11) / 10).toFloat()
        skewX = if (mRandom.nextBoolean()) skewX else -skewX
        paint.textSkewX = skewX //float类型参数，负数表示右斜，整数左斜
        //        paint.setUnderlineText(true); //true为下划线，false为非下划线
        //        paint.setStrikeThruText(true); //true为删除线，false为非删除线
    }

    //随机间距
    private fun randomPadding() {
        mPaddingLeft += BASE_PADDING_LEFT + mRandom.nextInt(RANGE_PADDING_LEFT)
        mPaddingTop = BASE_PADDING_TOP + mRandom.nextInt(RANGE_PADDING_TOP)
    }

    companion object {

        //    private static final char[] CHARS = {
        //            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        //            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        //            'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
        //            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        //            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
        //    };

        private val CHARS = charArrayOf(
            '0',
            '1',
            '2',
            '3',
            '4',
            '5',
            '6',
            '7',
            '8',
            '9',
            'a',
            'b',
            'c',
            'd',
            'e',
            'f',
            'g',
            'h',
            'i',
            'j',
            'k',
            'l',
            'm',
            'n',
            'o',
            'p',
            'q',
            'r',
            's',
            't',
            'u',
            'v',
            'w',
            'x',
            'y',
            'z',
            'A',
            'B',
            'C',
            'D',
            'E',
            'F',
            'G',
            'H',
            'I',
            'J',
            'K',
            'L',
            'M',
            'N',
            'O',
            'P',
            'Q',
            'R',
            'S',
            'T',
            'U',
            'V',
            'W',
            'X',
            'Y',
            'Z'
        )
        //Default Settings
        //    private static final int DEFAULT_CODE_LENGTH = 6;//验证码的长度  这里是6位
        private val DEFAULT_CODE_LENGTH = 4//验证码的长度  这里是4位
        private val DEFAULT_FONT_SIZE = 60//字体大小
        private val DEFAULT_LINE_NUMBER = 3//多少条干扰线
        private val BASE_PADDING_LEFT = 20 //左边距
        private val RANGE_PADDING_LEFT = 30//左边距范围值
        private val BASE_PADDING_TOP = 60//上边距
        private val RANGE_PADDING_TOP = 15//上边距范围值
        private val DEFAULT_COLOR = 0xDF//默认背景颜色值
        private var mImageCodeUtils: ImageCodeUtils? = null

        val instance: ImageCodeUtils
            get() {
                if (mImageCodeUtils == null) {
                    mImageCodeUtils = ImageCodeUtils()
                }
                return mImageCodeUtils as ImageCodeUtils
            }
    }
}
