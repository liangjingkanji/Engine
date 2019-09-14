/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Engine
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.swipeback

import android.animation.Animator
import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityOptions
import android.content.Context
import android.content.res.Configuration
import android.graphics.Rect
import android.os.Build
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.RequiresApi
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Proxy

/**
 * Activity侧滑返回支持，状态栏透明
 *
 *
 * 思路：
 *
 *
 * 1. 在构造方法中设置透明状态栏，利用反射将窗口转为不透明，同时监听布局变化以解决adjustResize失效问题
 *
 *
 * 2. 在Activity的dispatchTouchEvent方法中拦截触摸事件，满足条件则进行侧滑
 *
 *
 * 3. 侧滑事件前，利用反射将窗口转为透明；侧滑取消后，利用反射将窗口转为不透明
 *
 *
 * 使用说明：
 *
 *
 * 1. 仅支持SDK19(Android4.4)及以上
 *
 *
 * 2. 因状态栏透明，布局会从屏幕顶端开始绘制，Toolbar高度需自行调整
 *
 *
 * 3. 状态栏透明会导致输入法的adjustPan模式失效，建议设置为adjustResize
 *
 *
 * 4. 必须设置以下属性，否则侧滑时无法透视下层Activity
 *
 *
 * <item name="android:windowBackground">@android:color/transparent</item>
 *
 *
 * 5. 必须设置以下属性，否则ActionMode会插入页面顶端，且状态栏会显示黑色
 *
 *
 * <item name="windowActionModeOverlay">true</item>
 *
 *
 * 6. SDK21(Android5.0)以下必须设置以下属性，否则无法通过反射将窗口转为透明
 *
 *
 * <item name="android:windowIsTranslucent">true</item>
 *
 *
 * 7. 侧滑时会利用反射将窗口转为透明，此时会引起下层Activity生命周期变化，留意可能因此导致的严重问题
 *
 *
 * (a) onDestory -> onCreat -> onStart -> (onResume -> onPause) -> onStop
 *
 *
 * (b) onRestart -> onStart -> (onResume -> onPause) -> onStop
 *
 *
 * 8. 当顶层Activity方向与下层Activity方向不一致时侧滑会失效（下层方向未锁定除外），建议关闭该层Activity侧滑功能。
 *
 *
 * 示例场景：视频APP的播放页面和下层页面。
 *
 *
 * 9. 如需动态支持横竖屏切换，屏幕方向需指定为"behind"跟随栈底Activity方向，同时在onCreate中判断若不支持横竖屏切换，则锁定屏幕方向（避免behind失效）。
 *
 * @author Simon Lee
 * @e-mail jmlixiaomeng@163.com
 * @github https://github.com/Simon-Leeeeeeeee/SLWidget
 * @createdTime 2018-06-19
 */
@RequiresApi(api = Build.VERSION_CODES.KITKAT)
class SwipeBackHelper constructor(private val mSwipeBackActivity: Activity) {

    /**
     * 根视图
     */
    private val mDecorView: ViewGroup

    /**
     * 判断滑动事件的最小距离
     */
    private val mTouchSlop: Int

    /**
     * 左侧拦截滑动事件的区域
     */
    private val mInterceptRect: Float

    /**
     * 标志状态栏是否透明
     */
    /**
     * 状态栏是否透明
     */

    /**
     * 纵向滑动
     */
    private val vertical = 1

    /**
     * 横向滑动
     */
    private val horizontal = 2

    /**
     * 当前屏幕方向
     */
    private val mOrientation: Int

    /**
     * 滑动事件方向
     */
    private var mDragDirection: Int = 0

    /**
     * 阴影视图
     */
    private var mShadowView: View? = null

    /**
     * 侧滑操作的视图
     */
    private var mSwipeBackView: ViewGroup? = null

    /**
     * 滑动速度追踪
     */
    private var mVelocityTracker: VelocityTracker? = null

    /**
     * 滑动减速动画
     */
    private var mSwipeAnimator: DecelerateAnimator? = null

    /**
     * 触摸点的ID
     */
    private var mTouchPointerId: Int = 0

    /**
     * 触摸点的xy坐标
     */
    private var mStartX: Float = 0.toFloat()
    private var mStartY: Float = 0.toFloat()

    /**
     * 标志侧滑动画是否被取消
     */
    private var isAnimationCancel: Boolean = false

    /**
     * 标志是否允许侧滑
     */
    private var isSwipeBackEnabled = true

    /**
     * 窗口背景颜色，用于覆盖当输入法及导航栏变化时底部的黑色，默认不处理
     *
     *
     * 因{android:windowBackground}透明，输入法及导航栏变化时底部为黑色
     */
    private var mWindowBackgroundColor: Int = 0

    /**
     * 窗口背景视图，用于覆盖当输入法及导航栏变化时底部的黑色，默认不处理
     *
     *
     * 因{android:windowBackground}透明，输入法及导航栏变化时底部为黑色
     */
    private var mWindowBackGroundView: View? = null

    /**
     * Activity转为透明的回调
     */
    private var mTranslucentConversionListener: Any? = null

    /**
     * Activity转为透明的回调类
     */
    private var mTranslucentConversionListenerClass: Class<*>? = null

    /**
     * 标志Activity转为透明完成
     */
    private var isTranslucentComplete: Boolean = false

    /**
     * 布局和动画的监听，使用内部类的方式避免暴露回调接口
     */
    private val mPrivateListener = PrivateListener()


    init {
        this.mOrientation = mSwipeBackActivity.resources.configuration.orientation
        //获取根View
        this.mDecorView = mSwipeBackActivity.window.decorView as ViewGroup
        //判断滑动事件的最小距离
        this.mTouchSlop = ViewConfiguration.get(mSwipeBackActivity).scaledTouchSlop
        //左侧拦截滑动事件的区域
        this.mInterceptRect = 18 * mSwipeBackActivity.resources.displayMetrics.density//18dp
        //设置Activity不透明
        convertFromTranslucent(mSwipeBackActivity)
    }// 目标Activity

    /**
     * 返回侧滑事件操作的视图
     */
    fun getSwipeBackView(decorView: ViewGroup): ViewGroup {
        if (mSwipeBackView == null) {
            //使用contentView的父View，可包含ActionBar
            mSwipeBackView =
                    decorView.findViewById<View>(Window.ID_ANDROID_CONTENT).parent as ViewGroup
        }
        return mSwipeBackView!!
    }

    /**
     * 返回侧滑时左侧的阴影视图
     *
     *
     * 被子类重写时，注意要添加到swipeBackView的父容器中
     */
    fun getShadowView(swipeBackView: ViewGroup?): View {
        if (mShadowView == null) {
            mShadowView = ShadowView(mSwipeBackActivity)
            mShadowView!!.translationX = (-swipeBackView!!.width).toFloat()
            (swipeBackView.parent as ViewGroup).addView(
                mShadowView,
                0,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
        return mShadowView!!
    }

    /**
     * 返回窗口背景视图，用于覆盖当输入法及导航栏变化时底部的黑色
     *
     *
     * 因{android:windowBackground}透明，输入法及导航栏变化时底部为黑色
     */
    fun getWindowBackGroundView(decorView: ViewGroup): View? {
        if (mWindowBackGroundView == null && mWindowBackgroundColor.ushr(24) > 0) {
            mWindowBackGroundView = View(mSwipeBackActivity)
            mWindowBackGroundView!!.translationY = decorView.height.toFloat()
            mWindowBackGroundView!!.setBackgroundColor(mWindowBackgroundColor)
            decorView.addView(
                mWindowBackGroundView,
                0,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
            //监听DecorView的布局变化
            mDecorView.addOnLayoutChangeListener(mPrivateListener)
        }
        return mWindowBackGroundView
    }

    /**
     * 设置窗口背景颜色，用于覆盖当输入法及导航栏变化时底部的黑色
     *
     *
     * 因{android:windowBackground}透明，输入法及导航栏变化时底部为黑色
     */
    fun setBackgroundColor(color: Int) {
        mWindowBackgroundColor = color
        if (mWindowBackGroundView != null) {
            mWindowBackGroundView!!.setBackgroundColor(mWindowBackgroundColor)
        }
    }

    /**
     * Activity触摸事件分发，当横向滑动时触发侧滑返回，同时触摸事件改变为取消下发给childView
     */
    fun dispatchTouchEvent(event: MotionEvent) {
        if (!isSwipeBackEnabled || mSwipeBackActivity.isTaskRoot) {
            return
        }
        this.mSwipeBackView = getSwipeBackView(mDecorView)
        this.mShadowView = getShadowView(mSwipeBackView)
        val actionIndex = event.actionIndex
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
        mVelocityTracker!!.addMovement(event)
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (mSwipeAnimator != null && mSwipeAnimator!!.isStarted) {
                    mDragDirection = horizontal
                    mSwipeAnimator!!.cancel()
                    mStartX = event.getX(actionIndex) - mSwipeBackView!!.translationX
                } else {
                    //记录偏移坐标
                    mStartX = event.getX(actionIndex)
                    mStartY = event.getY(actionIndex)
                    //记录当前控制指针ID
                    mTouchPointerId = event.getPointerId(actionIndex)
                    //重置拖动方向
                    mDragDirection = 0
                    if (mStartX <= mInterceptRect) {
                        convertToTranslucent(mSwipeBackActivity)
                    }
                }
            }
            MotionEvent.ACTION_POINTER_UP -> {
                //如果抬起的指针是当前控制指针，则进行切换
                if (event.getPointerId(actionIndex) == mTouchPointerId) {
                    mVelocityTracker!!.clear()
                    //从列表中选择一个指针（非当前抬起的指针）作为下一个控制指针
                    for (index in 0 until event.pointerCount) {
                        if (index != actionIndex) {
                            //重置偏移坐标
                            mStartX = event.getX(index) - mSwipeBackView!!.translationX
                            mStartY = event.getY(index)
                            //重置触摸ID
                            mTouchPointerId = event.getPointerId(index)
                            break
                        }
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                for (index in 0 until event.pointerCount) {
                    //只响应当前控制指针的移动操作
                    if (event.getPointerId(index) == mTouchPointerId) {
                        if (mDragDirection == horizontal) {//横向滑动事件
                            var offsetX = (event.getX(index) - mStartX).toInt()
                            //处理偏移量越界的情况
                            if (offsetX < 0) {
                                offsetX = 0
                                mStartX = event.getX(index)
                            } else if (offsetX > mShadowView!!.width) {
                                offsetX = mShadowView!!.width
                                mStartX = event.getX(index) - offsetX
                            }
                            //滑动返回事件
                            swipeBackEvent(offsetX)
                        } else if (mDragDirection == 0 && mStartX <= mInterceptRect) {//还未产生滑动，且触点在拦截区域内
                            if (Math.abs(event.getX(index) - mStartX) >= mTouchSlop * 0.8f) {//横向滑动，系数0.8为增加横向检测的灵敏度
                                mStartX = event.getX(index)
                                mDragDirection = horizontal
                                //下放一个触摸取消事件，传递给子View
                                event.action = MotionEvent.ACTION_CANCEL
                                hideInputSoft()
                            } else if (Math.abs(event.getY(index) - mStartY) >= mTouchSlop) {//纵向滑动
                                mDragDirection = vertical
                            }
                        }
                        break
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (mDragDirection == horizontal) {//横向滑动事件
                    //计算横向手势速度
                    mVelocityTracker!!.computeCurrentVelocity(1000)
                    val velocityX = mVelocityTracker!!.xVelocity
                    var offsetX = event.getX(actionIndex) - mStartX
                    //处理偏移量越界的情况
                    if (offsetX < 0) {
                        offsetX = 0f
                    } else if (offsetX > mShadowView!!.width) {
                        offsetX = mShadowView!!.width.toFloat()
                    }
                    startSwipeAnimator(offsetX, 0f, mShadowView!!.width.toFloat(), velocityX)
                } else {
                    convertFromTranslucent(mSwipeBackActivity)
                }
                if (mVelocityTracker != null) {
                    mVelocityTracker!!.recycle()
                    mVelocityTracker = null
                }
                //重置拖动方向
                mDragDirection = 0
            }
        }
    }

    /**
     * Activity触摸事件，当子View未消费时进行滑动方向判断
     */
    fun onTouchEvent(event: MotionEvent) {
        if (!isSwipeBackEnabled || mSwipeBackActivity.isTaskRoot) {
            return
        }
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                if (mDragDirection == 0 && mStartX > mInterceptRect) {
                    convertToTranslucent(mSwipeBackActivity)
                }
            }
            MotionEvent.ACTION_MOVE -> {
                //还未产生滑动，触点不在拦截区域内
                if (mDragDirection == 0 && mStartX > mInterceptRect) {
                    for (index in 0 until event.pointerCount) {
                        //只响应当前控制指针的移动操作
                        if (event.getPointerId(index) == mTouchPointerId) {
                            if (Math.abs(event.getY(index) - mStartY) >= mTouchSlop) {//纵向滑动
                                mDragDirection = vertical
                            } else if (Math.abs(event.getX(index) - mStartX) >= mTouchSlop) {
                                mStartX = event.getX(index)
                                mDragDirection = horizontal
                                hideInputSoft()
                            }
                            break
                        }
                    }
                }
            }
        }
    }

    /**
     * 利用反射将activity转为透明
     */
    private fun convertToTranslucent(activity: Activity) {
        if (activity.isTaskRoot) return
        isTranslucentComplete = false
        try {
            if (mTranslucentConversionListenerClass == null) {
                val clazzArray = Activity::class.java.declaredClasses
                for (clazz in clazzArray) {
                    if (clazz.simpleName.contains("TranslucentConversionListener")) {
                        mTranslucentConversionListenerClass = clazz
                    }
                }
            }
            if (mTranslucentConversionListener == null && mTranslucentConversionListenerClass != null) {
                val invocationHandler = InvocationHandler { _, _, _ ->
                    isTranslucentComplete = true
                    null
                }
                mTranslucentConversionListener = Proxy.newProxyInstance(
                    mTranslucentConversionListenerClass!!.classLoader,
                    arrayOf(mTranslucentConversionListenerClass!!),
                    invocationHandler
                )
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                var options: Any? = null
                try {
                    val getActivityOptions =
                        Activity::class.java.getDeclaredMethod("getActivityOptions")
                    getActivityOptions.isAccessible = true
                    options = getActivityOptions.invoke(this)
                } catch (ignored: Exception) {
                }

                val convertToTranslucent = Activity::class.java.getDeclaredMethod(
                    "convertToTranslucent",
                    mTranslucentConversionListenerClass,
                    ActivityOptions::class.java
                )
                convertToTranslucent.isAccessible = true
                convertToTranslucent.invoke(activity, mTranslucentConversionListener, options)
            } else {
                val convertToTranslucent = Activity::class.java.getDeclaredMethod(
                    "convertToTranslucent",
                    mTranslucentConversionListenerClass!!
                )
                convertToTranslucent.isAccessible = true
                convertToTranslucent.invoke(activity, mTranslucentConversionListener)
            }
        } catch (ignored: Throwable) {
            isTranslucentComplete = true
        }

        if (mTranslucentConversionListener == null) {
            isTranslucentComplete = true
        }
    }

    /**
     * 利用反射将activity转为不透明
     */
    private fun convertFromTranslucent(activity: Activity) {
        if (activity.isTaskRoot) return
        try {
            val convertFromTranslucent =
                Activity::class.java.getDeclaredMethod("convertFromTranslucent")
            convertFromTranslucent.isAccessible = true
            convertFromTranslucent.invoke(activity)
        } catch (ignored: Throwable) {
        }

    }

    /**
     * 设置是否开启侧滑返回
     */
    fun setEnable(enabled: Boolean) {
        isSwipeBackEnabled = enabled
        if (!enabled) {
            mSwipeBackView?.translationX = 0f
            mShadowView?.translationX = (-mShadowView!!.width).toFloat()
        }
    }

    /**
     * 隐藏输入法
     */
    fun hideInputSoft() {
        val view = mSwipeBackActivity.currentFocus
        if (view != null) {
            if (view is EditText) {
                view.clearFocus()
            }
            val inputMethodManager =
                mSwipeBackActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    /**
     * 开始侧滑返回动画
     *
     * @param startValue    初始位移值
     * @param minFinalValue 终点位移值（小值）
     * @param maxFinalValue 终点位移值（大值）
     * @param velocity      滑动速度
     */
    private fun startSwipeAnimator(
        startValue: Float,
        minFinalValue: Float,
        maxFinalValue: Float,
        velocity: Float
    ) {
        if (maxFinalValue <= minFinalValue) {
            swipeBackEvent(0)
            convertFromTranslucent(mSwipeBackActivity)
            return
        }
        if (mSwipeAnimator == null) {
            mSwipeAnimator = DecelerateAnimator(mSwipeBackActivity, false)
            mSwipeAnimator!!.addListener(mPrivateListener)
            mSwipeAnimator!!.addUpdateListener(mPrivateListener)
        }
        mSwipeAnimator!!.setFlingFrictionRatio(9f)
        if (mOrientation == Configuration.ORIENTATION_LANDSCAPE) {
            mSwipeAnimator!!.startAnimator(startValue, minFinalValue, maxFinalValue, velocity * 8f)
        } else {
            mSwipeAnimator!!.startAnimator(startValue, minFinalValue, maxFinalValue, velocity * 4f)
        }
    }

    /**
     * 侧滑返回事件
     *
     * @param translation 移动距离
     */
    private fun swipeBackEvent(translation: Int) {
        if (!isTranslucentComplete) return
        if (mShadowView!!.background != null) {
            var alpha = ((1f - 1f * translation / mShadowView!!.width) * 255).toInt()
            if (alpha < 0) {
                alpha = 0
            } else if (alpha > 255) {
                alpha = 255
            }
            mShadowView!!.background.alpha = alpha
        }
        mShadowView!!.translationX = (translation - mShadowView!!.width).toFloat()
        mSwipeBackView!!.translationX = translation.toFloat()
    }

    private inner class PrivateListener : Animator.AnimatorListener,
        ValueAnimator.AnimatorUpdateListener,
        View.OnLayoutChangeListener {

        override fun onLayoutChange(
            v: View,
            left: Int,
            top: Int,
            right: Int,
            bottom: Int,
            oldLeft: Int,
            oldTop: Int,
            oldRight: Int,
            oldBottom: Int
        ) {
            //获取DecorView的可见区域
            val visibleDisplayRect = Rect()
            mDecorView.getWindowVisibleDisplayFrame(visibleDisplayRect)
            //调整mWindowBackGroundView的Y轴偏移量，用于覆盖不可见区域出现的黑色（不可见区域常见为当输入法及导航栏变化时的背景）
            mWindowBackGroundView = getWindowBackGroundView(mDecorView)
            if (mWindowBackGroundView != null) {
                mWindowBackGroundView!!.translationY = visibleDisplayRect.bottom.toFloat()
            }
            //状态栏透明情况下，输入法的adjustResize不会生效，这里手动调整View的高度以适配
/*            if (isStatusBarTransparent) {
                for (i in 0 until mDecorView.childCount) {
                    val child = mDecorView.getChildAt(i)
                    if (child is ViewGroup) {
                        //获取DecorView的子ViewGroup
                        val childLp = child.getLayoutParams()
                        //调整子ViewGroup的paddingBottom
                        var paddingBottom = bottom - visibleDisplayRect.bottom
                        if (childLp is ViewGroup.MarginLayoutParams) {
                            //此处减去bottomMargin，是考虑到导航栏的高度
                            paddingBottom -= childLp.bottomMargin
                        }
                        if (paddingBottom < 0) {
                            paddingBottom = 0
                        }
                        if (paddingBottom != child.getPaddingBottom()) {
                            //调整子ViewGroup的paddingBottom，以保证整个ViewGroup可见
                            child.setPadding(
                                child.getPaddingLeft(),
                                child.getPaddingTop(),
                                child.getPaddingRight(),
                                paddingBottom
                            )
                        }
                        break
                    }
                }
            }*/
        }

        override fun onAnimationUpdate(animation: ValueAnimator) {
            val translation = animation.animatedValue as Float
            swipeBackEvent(translation.toInt())
        }

        override fun onAnimationEnd(animation: Animator) {
            if (!isAnimationCancel) {
                //最终移动距离位置超过半宽，结束当前Activity
                if (mShadowView!!.width + 2 * mShadowView!!.translationX >= 0) {
                    mShadowView!!.visibility = View.GONE
                    mSwipeBackActivity.finish()
                    mSwipeBackActivity.overridePendingTransition(-1, -1)//取消返回动画
                } else {
                    mShadowView!!.translationX = (-mShadowView!!.width).toFloat()
                    mSwipeBackView!!.translationX = 0f
                    convertFromTranslucent(mSwipeBackActivity)
                }
            }
        }

        override fun onAnimationStart(animation: Animator) {
            isAnimationCancel = false
        }

        override fun onAnimationCancel(animation: Animator) {
            isAnimationCancel = true
        }

        override fun onAnimationRepeat(animation: Animator) {}

    }

}
