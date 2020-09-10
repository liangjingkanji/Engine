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

@file:Suppress("unused")

package com.drake.engine.text

import android.annotation.SuppressLint
import android.graphics.*
import android.graphics.BlurMaskFilter.Blur
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.text.Layout
import android.text.Layout.Alignment
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.*
import android.util.Log
import android.widget.TextView
import androidx.annotation.*
import androidx.annotation.IntRange
import androidx.core.content.ContextCompat
import com.drake.engine.base.getApp
import java.io.Serializable
import java.lang.ref.WeakReference


fun TextView.createSpan(): SpanUtils {
    return SpanUtils(this)
}

class SpanUtils() {

    private var textView: TextView? = null
    private var text: CharSequence? = null
    private var flag: Int = 0
    private var foregroundColor: Int = 0
    private var backgroundColor: Int = 0
    private var lineHeight: Int = 0
    private var alignLine: Int = 0
    private var quoteColor: Int = 0
    private var stripeWidth: Int = 0
    private var quoteGapWidth: Int = 0
    private var first: Int = 0
    private var rest: Int = 0
    private var bulletColor: Int = 0
    private var bulletRadius: Int = 0
    private var bulletGapWidth: Int = 0
    private var fontSize: Int = 0
    private var fontSizeIsDp: Boolean = false
    private var proportion: Float = 0.toFloat()
    private var xProportion: Float = 0.toFloat()
    private var isStrikeThrough: Boolean = false
    private var isUnderline: Boolean = false
    private var isSuperscript: Boolean = false
    private var isSubscript: Boolean = false
    private var isBold: Boolean = false
    private var isItalic: Boolean = false
    private var isBoldItalic: Boolean = false
    private var fontFamily: String? = null
    private var typeface: Typeface? = null
    private var alignment: Alignment? = null
    private var verticalAlign: Int = 0
    private var clickSpan: ClickableSpan? = null
    private var url: String? = null
    private var blurRadius: Float = 0.toFloat()
    private var style: Blur? = null
    private var shader: Shader? = null
    private var shadowRadius: Float = 0.toFloat()
    private var shadowDx: Float = 0.toFloat()
    private var shadowDy: Float = 0.toFloat()
    private var shadowColor: Int = 0
    private var spans: Array<out Any>? = null

    private var imageBitmap: Bitmap? = null
    private var imageDrawable: Drawable? = null
    private var imageUri: Uri? = null
    private var imageResourceId: Int = 0
    private var alignImage: Int = 0

    private var spaceSize: Int = 0
    private var spaceColor: Int = 0

    private val builder: SerializableSpannableStringBuilder

    private var type: Int = 0
    private val typeCharSequence = 0
    private val typeImage = 1
    private val typeSpace = 2


    private var regex: String? = null
    private var regexGlobal: String? = null


    companion object {

        private const val COLOR_DEFAULT = -0x1000001

        const val ALIGN_BOTTOM = 0
        const val ALIGN_BASELINE = 1
        const val ALIGN_CENTER = 2
        const val ALIGN_TOP = 3

        private val LINE_SEPARATOR = System.getProperty("line.separator")
    }

    @IntDef(
        ALIGN_BOTTOM,
        ALIGN_BASELINE,
        ALIGN_CENTER,
        ALIGN_TOP
    )
    annotation class Align


    init {
        builder = SerializableSpannableStringBuilder()
        text = ""
        type = -1
        reset()
    }

    constructor(textView: TextView) : this() {
        this.textView = textView
    }

    private fun reset() {
        flag = Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        foregroundColor = COLOR_DEFAULT
        backgroundColor = COLOR_DEFAULT
        lineHeight = -1
        quoteColor = COLOR_DEFAULT
        first = -1
        bulletColor = COLOR_DEFAULT
        fontSize = -1
        proportion = -1f
        xProportion = -1f
        isStrikeThrough = false
        isUnderline = false
        isSuperscript = false
        isSubscript = false
        isBold = false
        isItalic = false
        isBoldItalic = false
        fontFamily = null
        typeface = null
        alignment = null
        verticalAlign = -1
        clickSpan = null
        url = null
        blurRadius = -1f
        shader = null
        shadowRadius = -1f
        spans = null
        imageBitmap = null
        imageDrawable = null
        imageUri = null
        imageResourceId = -1
        spaceSize = -1
    }

    /**
     * Set the span of flag.
     *
     * @param flag The flag.
     *
     *  * [Spanned.SPAN_INCLUSIVE_EXCLUSIVE]
     *  * [Spanned.SPAN_INCLUSIVE_INCLUSIVE]
     *  * [Spanned.SPAN_EXCLUSIVE_EXCLUSIVE]
     *  * [Spanned.SPAN_EXCLUSIVE_INCLUSIVE]
     *
     * @return the single [SpanUtils] instance
     */
    fun setFlag(flag: Int): SpanUtils {
        this.flag = flag
        return this
    }

    // <editor-fold desc="Style">

    /**
     * Set the span of foreground's color.
     *
     * @param color The color of foreground
     * @return the single [SpanUtils] instance
     */
    fun setForegroundColor(@ColorInt color: Int): SpanUtils {
        this.foregroundColor = color
        return this
    }

    /**
     * Set the span of background's color.
     *
     * @param color The color of background
     * @return the single [SpanUtils] instance
     */
    fun setBackgroundColor(@ColorInt color: Int): SpanUtils {
        this.backgroundColor = color
        return this
    }

    /**
     * Set the span of line height.
     *
     * @param lineHeight The line height, in pixel.
     * @param align      The alignment.
     *
     *  * [Align.ALIGN_TOP]
     *  * [Align.ALIGN_CENTER]
     *  * [Align.ALIGN_BOTTOM]
     *
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun setLineHeight(
        @IntRange(from = 0) lineHeight: Int,
        @Align align: Int = ALIGN_CENTER
    ): SpanUtils {
        this.lineHeight = lineHeight
        this.alignLine = align
        return this
    }

    /**
     * Set the span of quote's color.
     *
     * @param color       The color of quote.
     * @param stripeWidth The width of stripe, in pixel.
     * @param gapWidth    The width of gap, in pixel.
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun setQuoteColor(
        @ColorInt color: Int,
        @IntRange(from = 1) stripeWidth: Int = 2,
        @IntRange(from = 0) gapWidth: Int = 2
    ): SpanUtils {
        this.quoteColor = color
        this.stripeWidth = stripeWidth
        this.quoteGapWidth = gapWidth
        return this
    }

    /**
     * Set the span of leading margin.
     *
     * @param first The indent for the first line of the paragraph.
     * @param rest  The indent for the remaining lines of the paragraph.
     * @return the single [SpanUtils] instance
     */
    fun setLeadingMargin(
        @IntRange(from = 0) first: Int,
        @IntRange(from = 0) rest: Int
    ): SpanUtils {
        this.first = first
        this.rest = rest
        return this
    }

    /**
     * Set the span of bullet.
     *
     * @param gapWidth The width of gap, in pixel.
     * @return the single [SpanUtils] instance
     */
    fun setBullet(@IntRange(from = 0) gapWidth: Int): SpanUtils {
        return setBullet(0, 3, gapWidth)
    }

    /**
     * Set the span of bullet.
     *
     * @param color    The color of bullet.
     * @param radius   The radius of bullet, in pixel.
     * @param gapWidth The width of gap, in pixel.
     * @return the single [SpanUtils] instance
     */
    fun setBullet(
        @ColorInt color: Int,
        @IntRange(from = 0) radius: Int,
        @IntRange(from = 0) gapWidth: Int
    ): SpanUtils {
        this.bulletColor = color
        this.bulletRadius = radius
        this.bulletGapWidth = gapWidth
        return this
    }

    /**
     * Set the span of size of font.
     *
     * @param size The size of font.
     * @param isSp True to use sp, false to use pixel.
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun setFontSize(@IntRange(from = 0) size: Int, isSp: Boolean = false): SpanUtils {
        this.fontSize = size
        this.fontSizeIsDp = isSp
        return this
    }

    /**
     * Set the span of proportion of font.
     *
     * @param proportion The proportion of font.
     * @return the single [SpanUtils] instance
     */
    fun setFontProportion(proportion: Float): SpanUtils {
        this.proportion = proportion
        return this
    }

    /**
     * Set the span of transverse proportion of font.
     *
     * @param proportion The transverse proportion of font.
     * @return the single [SpanUtils] instance
     */
    fun setFontXProportion(proportion: Float): SpanUtils {
        this.xProportion = proportion
        return this
    }

    /**
     * Set the span of strikethrough.
     *
     * @return the single [SpanUtils] instance
     */
    fun setStrikethrough(): SpanUtils {
        this.isStrikeThrough = true
        return this
    }

    /**
     * Set the span of underline.
     *
     * @return the single [SpanUtils] instance
     */
    fun setUnderline(): SpanUtils {
        this.isUnderline = true
        return this
    }

    /**
     * Set the span of superscript.
     *
     * @return the single [SpanUtils] instance
     */
    fun setSuperscript(): SpanUtils {
        this.isSuperscript = true
        return this
    }

    /**
     * Set the span of subscript.
     *
     * @return the single [SpanUtils] instance
     */
    fun setSubscript(): SpanUtils {
        this.isSubscript = true
        return this
    }

    /**
     * Set the span of bold.
     *
     * @return the single [SpanUtils] instance
     */
    fun setBold(): SpanUtils {
        isBold = true
        return this
    }

    /**
     * Set the span of italic.
     *
     * @return the single [SpanUtils] instance
     */
    fun setItalic(): SpanUtils {
        isItalic = true
        return this
    }

    /**
     * Set the span of bold italic.
     *
     * @return the single [SpanUtils] instance
     */
    fun setBoldItalic(): SpanUtils {
        isBoldItalic = true
        return this
    }

    /**
     * Set the span of font family.
     *
     * @param fontFamily The font family.
     *
     *  * monospace
     *  * serif
     *  * sans-serif
     *
     * @return the single [SpanUtils] instance
     */
    fun setFontFamily(fontFamily: String): SpanUtils {
        this.fontFamily = fontFamily
        return this
    }

    /**
     * Set the span of typeface.
     *
     * @param typeface The typeface.
     * @return the single [SpanUtils] instance
     */
    fun setTypeface(typeface: Typeface): SpanUtils {
        this.typeface = typeface
        return this
    }

    /**
     * Set the span of horizontal alignment.
     *
     * @param alignment The alignment.
     *
     *  * [Alignment.ALIGN_NORMAL]
     *  * [Alignment.ALIGN_OPPOSITE]
     *  * [Alignment.ALIGN_CENTER]
     *
     * @return the single [SpanUtils] instance
     */
    fun setHorizontalAlign(alignment: Alignment): SpanUtils {
        this.alignment = alignment
        return this
    }

    /**
     * Set the span of vertical alignment.
     *
     * @param align The alignment.
     *
     *  * [Align.ALIGN_TOP]
     *  * [Align.ALIGN_CENTER]
     *  * [Align.ALIGN_BASELINE]
     *  * [Align.ALIGN_BOTTOM]
     *
     * @return the single [SpanUtils] instance
     */
    fun setVerticalAlign(@Align align: Int): SpanUtils {
        this.verticalAlign = align
        return this
    }

    /**
     * Set the span of click.
     *
     * Must set `view.setMovementMethod(LinkMovementMethod.getInstance())`
     *
     * @param clickSpan The span of click.
     * @return the single [SpanUtils] instance
     */
    fun setClickSpan(clickSpan: ClickableSpan): SpanUtils {
        if (textView != null && textView!!.movementMethod == null) {
            textView!!.movementMethod = LinkMovementMethod.getInstance()
        }
        this.clickSpan = clickSpan
        return this
    }

    /**
     * Set the span of url.
     *
     * Must set `view.setMovementMethod(LinkMovementMethod.getInstance())`
     *
     * @param url The url.
     * @return the single [SpanUtils] instance
     */
    fun setUrl(url: String): SpanUtils {
        if (textView != null && textView!!.movementMethod == null) {
            textView!!.movementMethod = LinkMovementMethod.getInstance()
        }
        this.url = url
        return this
    }

    /**
     * Set the span of blur.
     *
     * @param radius The radius of blur.
     * @param style  The style.
     *
     *  * [Blur.NORMAL]
     *  * [Blur.SOLID]
     *  * [Blur.OUTER]
     *  * [Blur.INNER]
     *
     * @return the single [SpanUtils] instance
     */
    fun setBlur(
        @FloatRange(from = 0.0, fromInclusive = false) radius: Float,
        style: Blur
    ): SpanUtils {
        this.blurRadius = radius
        this.style = style
        return this
    }

    /**
     * Set the span of shader.
     *
     * @param shader The shader.
     * @return the single [SpanUtils] instance
     */
    fun setShader(shader: Shader): SpanUtils {
        this.shader = shader
        return this
    }

    /**
     * Set the span of shadow.
     *
     * @param radius      The radius of shadow.
     * @param dx          X-axis offset, in pixel.
     * @param dy          Y-axis offset, in pixel.
     * @param shadowColor The color of shadow.
     * @return the single [SpanUtils] instance
     */
    fun setShadow(
        @FloatRange(from = 0.0, fromInclusive = false) radius: Float,
        dx: Float,
        dy: Float,
        shadowColor: Int
    ): SpanUtils {
        this.shadowRadius = radius
        this.shadowDx = dx
        this.shadowDy = dy
        this.shadowColor = shadowColor
        return this
    }


    /**
     * Set the spans.
     *
     * @param spans The spans.
     * @return the single [SpanUtils] instance
     */
    fun setSpans(vararg spans: Any): SpanUtils {
        if (spans.isNotEmpty()) {
            this.spans = spans
        }
        return this
    }

    // </editor-fold>


    // <editor-fold desc="append">

    /**
     * Append the text text.
     *
     * @param text The text.
     * @return the single [SpanUtils] instance
     */
    fun append(text: CharSequence): SpanUtils {
        apply(typeCharSequence)
        this.text = text
        return this
    }

    /**
     * Append one line.
     *
     * @return the single [SpanUtils] instance
     */
    fun appendLine(): SpanUtils {
        apply(typeCharSequence)
        text = LINE_SEPARATOR
        return this
    }

    /**
     * Append text and one line.
     *
     * @return the single [SpanUtils] instance
     */
    fun appendLine(text: CharSequence): SpanUtils {
        apply(typeCharSequence)
        this.text = text.toString() + LINE_SEPARATOR!!
        return this
    }

    /**
     * Append one image.
     *
     * @param bitmap The bitmap.
     * @param align  The alignment.
     *
     *  * [Align.ALIGN_TOP]
     *  * [Align.ALIGN_CENTER]
     *  * [Align.ALIGN_BASELINE]
     *  * [Align.ALIGN_BOTTOM]
     *
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun appendImage(bitmap: Bitmap, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(typeImage)
        this.imageBitmap = bitmap
        this.alignImage = align
        return this
    }

    /**
     * Append one image.
     *
     * @param drawable The drawable of image.
     * @param align    The alignment.
     *
     *  * [Align.ALIGN_TOP]
     *  * [Align.ALIGN_CENTER]
     *  * [Align.ALIGN_BASELINE]
     *  * [Align.ALIGN_BOTTOM]
     *
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun appendImage(drawable: Drawable, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(typeImage)
        this.imageDrawable = drawable
        this.alignImage = align
        return this
    }

    /**
     * Append one image.
     *
     * @param uri   The uri of image.
     * @param align The alignment.
     *
     *  * [Align.ALIGN_TOP]
     *  * [Align.ALIGN_CENTER]
     *  * [Align.ALIGN_BASELINE]
     *  * [Align.ALIGN_BOTTOM]
     *
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun appendImage(uri: Uri, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(typeImage)
        this.imageUri = uri
        this.alignImage = align
        return this
    }

    /**
     * Append one image.
     *
     * @param resourceId The resource id of image.
     * @param align      The alignment.
     *
     *  * [Align.ALIGN_TOP]
     *  * [Align.ALIGN_CENTER]
     *  * [Align.ALIGN_BASELINE]
     *  * [Align.ALIGN_BOTTOM]
     *
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun appendImage(@DrawableRes resourceId: Int, @Align align: Int = ALIGN_BOTTOM): SpanUtils {
        apply(typeImage)
        this.imageResourceId = resourceId
        this.alignImage = align
        return this
    }

    /**
     * Append space.
     *
     * @param size  The size of space.
     * @param color The color of space.
     * @return the single [SpanUtils] instance
     */
    @JvmOverloads
    fun appendSpace(@IntRange(from = 0) size: Int, @ColorInt color: Int = Color.TRANSPARENT): SpanUtils {
        apply(typeSpace)
        spaceSize = size
        spaceColor = color
        return this
    }


    /**
     * 添加全局匹配规则样式
     *
     * @param regex String
     */
    fun appendRegexGlobal(regex: String) {
        this.regexGlobal = regex
    }

    /**
     * 应用正则到当前跨度文本
     *
     * @param regex String
     */
    fun appendRegex(regex: String) {
        this.regex = regex
    }

    // </editor-fold>

    fun get(): SpannableStringBuilder {
        return builder
    }


    /**
     * Create the span string.
     *
     * @return the span string
     */
    fun create(): SpannableStringBuilder {
        applyLast()
        textView?.text = builder
        return builder
    }

    private fun apply(type: Int) {
        applyLast()
        this.type = type
    }

    private fun applyLast() {
        when (type) {
            typeCharSequence -> updateCharCharSequence()
            typeImage -> updateImage()
            typeSpace -> updateSpace()
        }
        reset()
    }

    private fun updateCharCharSequence() {
        if (text!!.isEmpty()) return
        var start = builder.length
        if (start == 0 && lineHeight != -1) {// bug of LineHeightSpan when first line
            builder.append(Character.toString(2.toChar()))
                .append("\n")
                .setSpan(AbsoluteSizeSpan(0), 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            start = 2
        }
        builder.append(text)
        val end = builder.length
        if (verticalAlign != -1) {
            builder.setSpan(
                VerticalAlignSpan(
                    verticalAlign
                ), start, end, flag
            )
        }
        if (foregroundColor != COLOR_DEFAULT) {
            builder.setSpan(ForegroundColorSpan(foregroundColor), start, end, flag)
        }
        if (backgroundColor != COLOR_DEFAULT) {
            builder.setSpan(BackgroundColorSpan(backgroundColor), start, end, flag)
        }
        if (first != -1) {
            builder.setSpan(LeadingMarginSpan.Standard(first, rest), start, end, flag)
        }
        if (quoteColor != COLOR_DEFAULT) {
            builder.setSpan(
                CustomQuoteSpan(
                    quoteColor,
                    stripeWidth,
                    quoteGapWidth
                ),
                start,
                end,
                flag
            )
        }
        if (bulletColor != COLOR_DEFAULT) {
            builder.setSpan(
                CustomBulletSpan(
                    bulletColor,
                    bulletRadius,
                    bulletGapWidth
                ),
                start,
                end,
                flag
            )
        }
        if (fontSize != -1) {
            builder.setSpan(AbsoluteSizeSpan(fontSize, fontSizeIsDp), start, end, flag)
        }
        if (proportion != -1f) {
            builder.setSpan(RelativeSizeSpan(proportion), start, end, flag)
        }
        if (xProportion != -1f) {
            builder.setSpan(ScaleXSpan(xProportion), start, end, flag)
        }
        if (lineHeight != -1) {
            builder.setSpan(
                CustomLineHeightSpan(
                    lineHeight,
                    alignLine
                ), start, end, flag
            )
        }
        if (isStrikeThrough) {
            builder.setSpan(StrikethroughSpan(), start, end, flag)
        }
        if (isUnderline) {
            builder.setSpan(UnderlineSpan(), start, end, flag)
        }
        if (isSuperscript) {
            builder.setSpan(SuperscriptSpan(), start, end, flag)
        }
        if (isSubscript) {
            builder.setSpan(SubscriptSpan(), start, end, flag)
        }
        if (isBold) {
            builder.setSpan(StyleSpan(Typeface.BOLD), start, end, flag)
        }
        if (isItalic) {
            builder.setSpan(StyleSpan(Typeface.ITALIC), start, end, flag)
        }
        if (isBoldItalic) {
            builder.setSpan(StyleSpan(Typeface.BOLD_ITALIC), start, end, flag)
        }
        if (fontFamily != null) {
            builder.setSpan(TypefaceSpan(fontFamily), start, end, flag)
        }
        if (typeface != null) {
            builder.setSpan(CustomTypefaceSpan(typeface!!), start, end, flag)
        }
        if (alignment != null) {
            builder.setSpan(AlignmentSpan.Standard(alignment!!), start, end, flag)
        }
        if (clickSpan != null) {
            builder.setSpan(clickSpan, start, end, flag)
        }
        if (url != null) {
            builder.setSpan(URLSpan(url), start, end, flag)
        }
        if (blurRadius != -1f) {
            builder.setSpan(
                MaskFilterSpan(BlurMaskFilter(blurRadius, style)),
                start,
                end,
                flag
            )
        }
        if (shader != null) {
            builder.setSpan(ShaderSpan(shader!!), start, end, flag)
        }
        if (shadowRadius != -1f) {
            builder.setSpan(
                ShadowSpan(
                    shadowRadius,
                    shadowDx,
                    shadowDy,
                    shadowColor
                ),
                start,
                end,
                flag
            )
        }
        if (spans != null) {
            for (span in spans!!) {
                builder.setSpan(span, start, end, flag)
            }
        }
    }

    private fun updateImage() {
        val start = builder.length
        text = "<img>"
        updateCharCharSequence()
        val end = builder.length
        when {
            imageBitmap != null -> builder.setSpan(
                CustomImageSpan(
                    imageBitmap!!,
                    alignImage
                ),
                start,
                end,
                flag
            )
            imageDrawable != null -> builder.setSpan(
                CustomImageSpan(
                    imageDrawable!!,
                    alignImage
                ),
                start,
                end,
                flag
            )
            imageUri != null -> builder.setSpan(
                CustomImageSpan(imageUri!!, alignImage),
                start,
                end,
                flag
            )
            imageResourceId != -1 -> builder.setSpan(
                CustomImageSpan(
                    imageResourceId,
                    alignImage
                ),
                start,
                end,
                flag
            )
        }
    }

    private fun updateSpace() {
        val start = builder.length
        text = "< >"
        updateCharCharSequence()
        val end = builder.length
        builder.setSpan(SpaceSpan(spaceSize, spaceColor), start, end, flag)
    }


    internal class VerticalAlignSpan(val verticalAlignment: Int) : ReplacementSpan() {

        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            val temp = text.subSequence(start, end)
            return paint.measureText(temp.toString()).toInt()
        }

        override fun draw(
            canvas: Canvas,
            text: CharSequence,
            start: Int,
            end: Int,
            x: Float,
            top: Int,
            y: Int,
            bottom: Int,
            paint: Paint
        ) {
            val temp = text.subSequence(start, end)
            val fm = paint.fontMetricsInt
            //            int need = height - (v + fm.descent - fm.ascent - spanstartv);
            //            if (need > 0) {
            //                if (verticalAlignment == ALIGN_TOP) {
            //                    fm.descent += need;
            //                } else if (verticalAlignment == ALIGN_CENTER) {
            //                    fm.descent += need / 2;
            //                    fm.ascent -= need / 2;
            //                } else {
            //                    fm.ascent -= need;
            //                }
            //            }
            //            need = height - (v + fm.bottom - fm.top - spanstartv);
            //            if (need > 0) {
            //                if (verticalAlignment == ALIGN_TOP) {
            //                    fm.bottom += need;
            //                } else if (verticalAlignment == ALIGN_CENTER) {
            //                    fm.bottom += need / 2;
            //                    fm.top -= need / 2;
            //                } else {
            //                    fm.top -= need;
            //                }
            //            }
            canvas.drawText(
                temp.toString(),
                x,
                (y - ((y + fm.descent + y + fm.ascent) / 2 - (bottom + top) / 2)).toFloat(),
                paint
            )
        }

        companion object {

            const val ALIGN_CENTER = 2
            const val ALIGN_TOP = 3
        }
    }

    internal class CustomLineHeightSpan(private val height: Int, val verticalAlignment: Int) :
        LineHeightSpan {

        override fun chooseHeight(
            text: CharSequence, start: Int, end: Int,
            spanstartv: Int, v: Int, fm: Paint.FontMetricsInt
        ) {
            if (sfm == null) {
                sfm = Paint.FontMetricsInt()
                sfm!!.top = fm.top
                sfm!!.ascent = fm.ascent
                sfm!!.descent = fm.descent
                sfm!!.bottom = fm.bottom
                sfm!!.leading = fm.leading
            } else {
                fm.top = sfm!!.top
                fm.ascent = sfm!!.ascent
                fm.descent = sfm!!.descent
                fm.bottom = sfm!!.bottom
                fm.leading = sfm!!.leading
            }
            var need = height - (v + fm.descent - fm.ascent - spanstartv)
            if (need > 0) {
                when (verticalAlignment) {
                    ALIGN_TOP -> fm.descent += need
                    ALIGN_CENTER -> {
                        fm.descent += need / 2
                        fm.ascent -= need / 2
                    }
                    else -> fm.ascent -= need
                }
            }
            need = height - (v + fm.bottom - fm.top - spanstartv)
            if (need > 0) {
                when (verticalAlignment) {
                    ALIGN_TOP -> fm.bottom += need
                    ALIGN_CENTER -> {
                        fm.bottom += need / 2
                        fm.top -= need / 2
                    }
                    else -> fm.top -= need
                }
            }
            if (end == (text as Spanned).getSpanEnd(this)) {
                sfm = null
            }
        }

        companion object {

            const val ALIGN_CENTER = 2
            const val ALIGN_TOP = 3
            var sfm: Paint.FontMetricsInt? = null
        }
    }

    internal class SpaceSpan constructor(
        private val width: Int,
        color: Int = Color.TRANSPARENT
    ) : ReplacementSpan() {
        private val paint = Paint()

        init {
            paint.color = color
            paint.style = Paint.Style.FILL
        }

        override fun getSize(
            paint: Paint, text: CharSequence,
            @IntRange(from = 0) start: Int,
            @IntRange(from = 0) end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            return width
        }

        override fun draw(
            canvas: Canvas, text: CharSequence,
            @IntRange(from = 0) start: Int,
            @IntRange(from = 0) end: Int,
            x: Float, top: Int, y: Int, bottom: Int,
            paint: Paint
        ) {
            canvas.drawRect(x, top.toFloat(), x + width, bottom.toFloat(), this.paint)
        }
    }

    internal class CustomQuoteSpan constructor(
        private val color: Int,
        private val stripeWidth: Int,
        private val gapWidth: Int
    ) : LeadingMarginSpan {

        override fun getLeadingMargin(first: Boolean): Int {
            return stripeWidth + gapWidth
        }

        override fun drawLeadingMargin(
            c: Canvas, p: Paint, x: Int, dir: Int,
            top: Int, baseline: Int, bottom: Int,
            text: CharSequence, start: Int, end: Int,
            first: Boolean, layout: Layout
        ) {
            val style = p.style
            val color = p.color
            p.style = Paint.Style.FILL
            p.color = this.color
            c.drawRect(
                x.toFloat(),
                top.toFloat(),
                (x + dir * stripeWidth).toFloat(),
                bottom.toFloat(),
                p
            )
            p.style = style
            p.color = color
        }
    }

    internal class CustomBulletSpan constructor(
        private val color: Int,
        private val radius: Int,
        private val gapWidth: Int
    ) : LeadingMarginSpan {

        private var sBulletPath: Path? = null

        override fun getLeadingMargin(first: Boolean): Int {
            return 2 * radius + gapWidth
        }

        override fun drawLeadingMargin(
            c: Canvas, p: Paint, x: Int, dir: Int,
            top: Int, baseline: Int, bottom: Int,
            text: CharSequence, start: Int, end: Int,
            first: Boolean, l: Layout
        ) {
            if ((text as Spanned).getSpanStart(this) == start) {
                val style = p.style
                var oldColor: Int = p.color
                p.color = color
                p.style = Paint.Style.FILL
                if (c.isHardwareAccelerated) {
                    if (sBulletPath == null) {
                        sBulletPath = Path()
                        // Bullet is slightly better to avoid aliasing artifacts on mdpi devices.
                        sBulletPath!!.addCircle(0.0f, 0.0f, radius.toFloat(), Path.Direction.CW)
                    }
                    c.save()
                    c.translate((x + dir * radius).toFloat(), (top + bottom) / 2.0f)
                    c.drawPath(sBulletPath!!, p)
                    c.restore()
                } else {
                    c.drawCircle(
                        (x + dir * radius).toFloat(),
                        (top + bottom) / 2.0f,
                        radius.toFloat(),
                        p
                    )
                }
                p.color = oldColor
                p.style = style
            }
        }
    }

    @SuppressLint("ParcelCreator")
    internal class CustomTypefaceSpan constructor(private val newType: Typeface) :
        TypefaceSpan("") {

        override fun updateDrawState(textPaint: TextPaint) {
            apply(textPaint, newType)
        }

        override fun updateMeasureState(paint: TextPaint) {
            apply(paint, newType)
        }

        private fun apply(paint: Paint, tf: Typeface) {
            val oldStyle: Int
            val old = paint.typeface
            oldStyle = old?.style ?: 0

            val fake = oldStyle and tf.style.inv()
            if (fake and Typeface.BOLD != 0) {
                paint.isFakeBoldText = true
            }
            if (fake and Typeface.ITALIC != 0) {
                paint.textSkewX = -0.25f
            }
            paint.shader
            paint.typeface = tf
        }
    }


    internal class CustomImageSpan :
        CustomDynamicDrawableSpan {
        private var drawable: Drawable? = null
        private var contentUri: Uri? = null
        private var resourceId: Int = 0

        constructor(b: Bitmap, verticalAlignment: Int) : super(verticalAlignment) {
            drawable = BitmapDrawable(getApp().resources, b)
            drawable!!.setBounds(
                0, 0, drawable!!.intrinsicWidth, drawable!!.intrinsicHeight
            )
        }

        constructor(d: Drawable, verticalAlignment: Int) : super(verticalAlignment) {
            drawable = d
            drawable!!.setBounds(
                0, 0, drawable!!.intrinsicWidth, drawable!!.intrinsicHeight
            )
        }

        constructor(uri: Uri, verticalAlignment: Int) : super(verticalAlignment) {
            contentUri = uri
        }

        constructor(@DrawableRes resourceId: Int, verticalAlignment: Int) : super(verticalAlignment) {
            this.resourceId = resourceId
        }

        override fun getDrawable(): Drawable? {
            return when {
                drawable != null -> drawable

                contentUri != null -> {
                    val bitmap: Bitmap

                    try {
                        val inputStream = getApp().contentResolver.openInputStream(contentUri!!)
                        bitmap = BitmapFactory.decodeStream(inputStream)
                        inputStream?.close()
                        BitmapDrawable(getApp().resources, bitmap).apply {
                            setBounds(
                                0, 0, intrinsicWidth, intrinsicHeight
                            )
                        }
                    } catch (e: Exception) {
                        Log.e("sms", "Failed to loaded content $contentUri", e)
                        return null
                    }
                }
                else -> {
                    ContextCompat.getDrawable(getApp(), resourceId).apply {
                        this?.setBounds(0, 0, intrinsicWidth, intrinsicHeight)
                    }
                }
            }
        }
    }

    internal abstract class CustomDynamicDrawableSpan(private val verticalAlignment: Int = ALIGN_BOTTOM) :
        ReplacementSpan() {

        private var drawableRef: WeakReference<Drawable?>? = null


        companion object {

            const val ALIGN_BOTTOM = 0
            const val ALIGN_BASELINE = 1
            const val ALIGN_CENTER = 2
            const val ALIGN_TOP = 3
        }

        abstract fun getDrawable(): Drawable?

        private fun getCachedDrawable(): Drawable? {
            val wr = drawableRef
            var d: Drawable? = null
            if (wr != null) {
                d = wr.get()
            }
            if (d == null) {
                d = getDrawable()
                drawableRef = WeakReference(d)
            }
            return d
        }

        override fun getSize(
            paint: Paint,
            text: CharSequence,
            start: Int,
            end: Int,
            fm: Paint.FontMetricsInt?
        ): Int {
            val d = getCachedDrawable()
            val rect = d!!.bounds

            if (fm != null) {
                val lineHeight = fm.bottom - fm.top
                if (lineHeight < rect.height()) {
                    when (verticalAlignment) {
                        ALIGN_TOP -> {
                            fm.top = fm.top
                            fm.bottom = rect.height() + fm.top
                        }
                        ALIGN_CENTER -> {
                            fm.top = -rect.height() / 2 - lineHeight / 4
                            fm.bottom = rect.height() / 2 - lineHeight / 4
                        }
                        else -> {
                            fm.top = -rect.height() + fm.bottom
                            fm.bottom = fm.bottom
                        }
                    }
                    fm.ascent = fm.top
                    fm.descent = fm.bottom
                }
            }
            return rect.right
        }

        override fun draw(
            canvas: Canvas, text: CharSequence,
            start: Int, end: Int, x: Float,
            top: Int, y: Int, bottom: Int, paint: Paint
        ) {
            val d = getCachedDrawable()
            val rect = d!!.bounds
            canvas.save()
            val transY: Float
            val lineHeight = bottom - top
            if (rect.height() < lineHeight) {
                transY = when (verticalAlignment) {
                    ALIGN_TOP -> top.toFloat()
                    ALIGN_CENTER -> ((bottom + top - rect.height()) / 2).toFloat()
                    ALIGN_BASELINE -> (y - rect.height()).toFloat()
                    else -> (bottom - rect.height()).toFloat()
                }
                canvas.translate(x, transY)
            } else {
                canvas.translate(x, top.toFloat())
            }
            d.draw(canvas)
            canvas.restore()
        }

    }

    internal class ShaderSpan constructor(private val shader: Shader) : CharacterStyle(),
        UpdateAppearance {

        override fun updateDrawState(tp: TextPaint) {
            tp.shader = shader
        }
    }

    internal class ShadowSpan constructor(
        private val radius: Float,
        private val dx: Float,
        private val dy: Float,
        private val shadowColor: Int
    ) : CharacterStyle(), UpdateAppearance {

        override fun updateDrawState(tp: TextPaint) {
            tp.setShadowLayer(radius, dx, dy, shadowColor)
        }
    }

    private class SerializableSpannableStringBuilder : SpannableStringBuilder(), Serializable {
        companion object {

            private const val serialVersionUID = 4909567650765875771L
        }
    }

}