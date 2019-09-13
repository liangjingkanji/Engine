/*
 * Copyright (C) 2018, Umbrella CompanyLimited All rights reserved.
 * Project：Base
 * Author：Drake
 * Date：9/11/19 7:25 PM
 */

package com.drake.engine.component.swipeback

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.hardware.SensorManager
import android.view.ViewConfiguration
import android.view.animation.LinearInterpolator

/**
 * 减速动画，默认启用回弹效果。
 *
 * @author Simon Lee
 * @e-mail jmlixiaomeng@163.com
 * @github https://github.com/Simon-Leeeeeeeee/SLWidget
 * @createdTime 2018-07-23
 */
class DecelerateAnimator
/**
 * 减速动画
 *
 * @param context     上下文
 * @param bounceCoeff 回弹系数
 * @param bouncing    是否开启回弹效果
 */
@JvmOverloads constructor(
    context: Context,
    /**
     * 弹性系数
     */
    private val mBounceCoeff: Float = 10f,
    /**
     * 是否启用回弹效果
     */
    private val isBouncing: Boolean = true
) : ValueAnimator() {

    private val DECELERATION_RATE = 2.358201815f//Math.log(0.78) / Math.log(0.9)
    private val INFLEXION = 0.35f // Tension lines cross at (INFLEXION, 1)

    /**
     * 动摩擦系数
     */
    private var mFlingFriction: Float = 0.toFloat()

    /**
     * 动摩擦系数倍率
     */
    private var mFlingFrictionRatio = 1f

    /**
     * 物理系数
     */
    private val mPhysicalCoeff: Float

    /**
     * 估值器
     */
    private val mDecelerateEvaluator: DecelerateEvaluator

    /**
     * 动画起始值
     */
    private var mInitialValue: Float = 0.toFloat()

    /**
     * 动画终止值
     */
    private var mFinalValue: Float = 0.toFloat()

    /**
     * 动画总持续时间
     */
    private var mDuration: Long = 0

    /**
     * 位移距离
     */
    private var mDistance: Float = 0.toFloat()

    /**
     * 回弹持续时间
     */
    private var mBounceDuration: Long = 0

    /**
     * 回弹位移距离
     */
    private var mBounceDistance: Float = 0.toFloat()

    /**
     * 未处理越界情况下的动画时间
     */
    private var mOriginalDuration: Long = 0

    /**
     * 未处理越界情况下的位移距离
     */
    private var mOriginalDistance: Float = 0.toFloat()

    /**
     * 摩擦系数，用于计算越界情况下的动画时间和位移
     */
    private var mFrictionCoeff: Float = 0.toFloat()

    /**
     * 是否越界（只有越界了才可能会发生回弹）
     */
    private var isOutside: Boolean = false

    constructor(context: Context, bouncing: Boolean) : this(context, 10f, bouncing)

    init {
        this.mDecelerateEvaluator = DecelerateEvaluator()
        this.mPhysicalCoeff = (context.resources.displayMetrics.density
                * SensorManager.GRAVITY_EARTH * 5291.328f)// = 160.0f * 39.37f * 0.84f
        interpolator = LinearInterpolator()
    }

    /**
     * 指定位移距离和最大动画时间，开始减速动画。
     *
     * @param startValue  起始值
     * @param finalValue  终止值
     * @param maxDuration 最大动画时间
     */
    fun startAnimator(startValue: Float, finalValue: Float, maxDuration: Long) {
        reset()
        mInitialValue = startValue
        mDistance = finalValue - startValue
        if (mDistance == 0f) {
            return
        }
        mFinalValue = finalValue
        mDuration = getDurationByDistance(mDistance)
        if (mDuration > maxDuration) {
            resetFlingFriction(mDistance, maxDuration)
            mDuration = maxDuration
        }
        startAnimator()
    }

    /**
     * 指定起止值和初始速度，开始减速动画
     * 终点值一定是极小值或者极大值
     *
     * @param startValue    初始值
     * @param minFinalValue 极小值
     * @param maxFinalValue 极大值
     * @param velocity      初速度
     */
    fun startAnimator(
        startValue: Float,
        minFinalValue: Float,
        maxFinalValue: Float,
        velocity: Float
    ) {
        if (minFinalValue >= maxFinalValue) {
            throw ArithmeticException("maxFinalValue must be larger than minFinalValue!")
        }
        reset()
        mInitialValue = startValue
        // 1.根据速度计算位移距离
        val distance = getDistanceByVelocity(velocity)
        val finalValue = startValue + distance
        // 2.确定终点值、位移距离、动画时间
        if (finalValue < minFinalValue || finalValue > maxFinalValue) {//终点值在界外
            //确定终点值
            mFinalValue = if (finalValue < minFinalValue) minFinalValue else maxFinalValue
            //起止值都在界外同侧
            if (startValue < minFinalValue && finalValue < minFinalValue || startValue > maxFinalValue && finalValue > maxFinalValue) {
                //改变动摩擦系数，减少动画时间
                mFrictionCoeff = mBounceCoeff
                //直接校正位移距离并计算动画时间
                mDistance = mFinalValue - startValue
                mDuration = getDurationByDistance(mDistance, mFrictionCoeff)
            } else if (isBouncing) {//起止值跨越边界，且启用回弹效果
                isOutside = true
                //记录未处理越界情况下的位移距离和动画时间，用于计算回弹第一阶段的位移
                mOriginalDistance = distance
                mOriginalDuration = getDurationByDistance(distance)
                //获取越界时的速度
                val bounceVelocity = getVelocityByDistance(finalValue - mFinalValue)
                //改变动摩擦系数，减少回弹时间
                mFrictionCoeff = mBounceCoeff
                //计算越界后的回弹时间
                mBounceDuration = getDurationByVelocity(bounceVelocity, mFrictionCoeff)
                //根据回弹时间计算回弹位移
                mBounceDistance = getDistanceByDuration(mBounceDuration / 2, mFrictionCoeff) *
                        Math.signum(bounceVelocity)
                //总的动画时间 = 原本动画时间 - 界外时间 + 回弹时间
                mDuration = mOriginalDuration - getDurationByDistance(finalValue - mFinalValue) +
                        mBounceDuration
            } else {//禁用回弹效果，按未越界处理。当越界达到边界值时会提前结束动画
                isOutside = true
                mDistance = distance
                //计算动画时间
                mDuration = getDurationByDistance(distance)
            }
        } else {//终点值在界内
            //校正终点值，计算位移距离和动画时间
            mFinalValue =
                    if (finalValue * 2 < minFinalValue + maxFinalValue) minFinalValue else maxFinalValue
            mDistance = mFinalValue - startValue
            mDuration = getDurationByDistance(mDistance)
        }
        startAnimator()
    }

    /**
     * 指定初始速度，开始减速动画。
     * 无边界
     *
     * @param startValue 起始位置
     * @param velocity   初始速度
     * @param modulus    终点值的模，会对滑动距离进行微调，以保证终点位置一定是modulus的整数倍
     */
    fun startAnimator_Velocity(startValue: Float, velocity: Float, modulus: Float) {
        startAnimator_Velocity(startValue, 0f, 0f, velocity, modulus)
    }

    /**
     * 指定初始速度，开始减速动画。
     * 当极大值大于极小值时有边界
     *
     * @param startValue 起始位置
     * @param minValue   极小值
     * @param maxValue   极大值
     * @param velocity   初始速度
     * @param modulus    终点值的模，会对滑动距离进行微调，以保证终点位置一定是modulus的整数倍
     */
    fun startAnimator_Velocity(
        startValue: Float,
        minValue: Float,
        maxValue: Float,
        velocity: Float,
        modulus: Float
    ) {
        reset()
        mInitialValue = startValue
        // 1.计算位移距离
        var distance = getDistanceByVelocity(velocity)
        // 2.校正位移距离
        distance = reviseDistance(distance, startValue, modulus)
        val finalValue = startValue + distance
        // 3.确定终点值、位移距离、动画时间
        if (maxValue > minValue && (finalValue < minValue || finalValue > maxValue)) {//终点值在界外
            //确定终点值
            mFinalValue = if (finalValue < minValue) minValue else maxValue
            //起止值都在界外同侧
            if (startValue < minValue && finalValue < minValue || startValue > maxValue && finalValue > maxValue) {
                //改变动摩擦系数，减少动画时间
                mFrictionCoeff = mBounceCoeff
                //直接校正位移距离并计算动画时间
                mDistance = mFinalValue - startValue
                mDuration = getDurationByDistance(mDistance, mFrictionCoeff)
            } else if (isBouncing) {//起止值跨越边界，且启用回弹效果
                isOutside = true
                //记录未处理越界情况下的位移距离和动画时间，用于计算回弹第一阶段的位移
                mOriginalDistance = distance
                mOriginalDuration = getDurationByDistance(distance)
                //获取越界时的速度
                val bounceVelocity = getVelocityByDistance(finalValue - mFinalValue)
                //改变动摩擦系数，减少回弹时间
                mFrictionCoeff = mBounceCoeff
                //计算越界后的回弹时间
                mBounceDuration = getDurationByVelocity(bounceVelocity, mFrictionCoeff)
                //根据回弹时间计算回弹位移
                mBounceDistance = getDistanceByDuration(mBounceDuration / 2, mFrictionCoeff) *
                        Math.signum(bounceVelocity)
                //总的动画时间 = 原本动画时间 - 界外时间 + 回弹时间
                mDuration = mOriginalDuration - getDurationByDistance(finalValue - mFinalValue) +
                        mBounceDuration
            } else {//禁用回弹效果，按未越界处理。当越界达到边界值时会提前结束动画
                isOutside = true
                mDistance = distance
                //计算动画时间
                mDuration = getDurationByDistance(distance)
            }
        } else {//终点值在界内
            //确定终点值、位移距离和动画时间
            mFinalValue = finalValue
            mDistance = distance
            mDuration = getDurationByDistance(mDistance)
        }
        startAnimator()
    }

    /**
     * 指定位移距离，开始减速动画。
     * 无边界
     *
     * @param startValue 起始位置
     * @param distance   位移距离
     * @param modulus    终点值的模，会对滑动距离进行微调，以保证终点位置一定是modulus的整数倍
     */
    fun startAnimator_Distance(startValue: Float, distance: Float, modulus: Float) {
        startAnimator_Distance(startValue, 0f, 0f, distance, modulus)
    }

    /**
     * 指定位移距离，开始减速动画。
     * 当极大值大于极小值时有边界
     *
     * @param startValue 起始位置
     * @param minValue   极小值
     * @param maxValue   极大值
     * @param distance   位移距离
     * @param modulus    终点值的模，会对滑动距离进行微调，以保证终点位置一定是modulus的整数倍
     */
    fun startAnimator_Distance(
        startValue: Float,
        minValue: Float,
        maxValue: Float,
        distance: Float,
        modulus: Float
    ) {
        reset()
        mInitialValue = startValue
        // 1.先校正位移
        mDistance = reviseDistance(distance, startValue, modulus)
        if (mDistance == 0f) {
            return
        }
        mFinalValue = startValue + mDistance
        // 2.极值处理
        if (maxValue > minValue && (mFinalValue < minValue || mFinalValue > maxValue)) {
            return
        }
        // 3.计算时间
        mDuration = getDurationByDistance(mDistance)
        startAnimator()
    }

    private fun reset() {
        this.isOutside = false
        this.mFrictionCoeff = 1f
        this.mBounceDuration = 0
        this.mBounceDistance = 0f
        this.mOriginalDuration = 0
        this.mOriginalDistance = 0f
        this.mFlingFriction = ViewConfiguration.getScrollFriction() * mFlingFrictionRatio
    }

    private fun startAnimator() {
        // 1.设置起止值
        setFloatValues(mInitialValue, mFinalValue)
        // 2.设置估值器
        setEvaluator(mDecelerateEvaluator)
        // 3.设置持续时间
        duration = mDuration
        // 4.开始动画
        start()
    }

    /**
     * 校正位移，确保终点值是模的整数倍
     *
     * @param distance   位移距离
     * @param startValue 起始位置
     * @param modulus    终点值的模，会对滑动距离进行微调，以保证终点位置一定是modulus的整数倍
     */
    fun reviseDistance(distance: Float, startValue: Float, modulus: Float): Float {
        if (modulus != 0f) {
            val multiple = ((startValue + distance) / modulus).toInt()
            val remainder = startValue + distance - multiple * modulus
            if (remainder != 0f) {
                return if (remainder * 2 < -modulus) {
                    distance - remainder - modulus
                } else if (remainder * 2 < modulus) {
                    distance - remainder
                } else {
                    distance - remainder + modulus
                }
            }
        }
        return distance
    }

    /**
     * 根据位移计算初速度
     *
     * @param distance      位移距离
     * @param frictionCoeff 摩擦系数
     */
    @JvmOverloads
    fun getVelocityByDistance(distance: Float, frictionCoeff: Float = 1f): Float {
        var velocity = 0f
        if (distance != 0f) {
            val decelMinusOne = DECELERATION_RATE - 1.0
            val l = Math.pow(
                (Math.abs(distance) / (mFlingFriction * frictionCoeff * mPhysicalCoeff)).toDouble(),
                decelMinusOne / DECELERATION_RATE
            )
            velocity =
                    (l * mFlingFriction.toDouble() * frictionCoeff.toDouble() * mPhysicalCoeff.toDouble() / INFLEXION * 4.0 * Math.signum(
                        distance
                    ).toDouble()).toFloat()
        }
        return velocity
    }

    /**
     * 根据初速度计算位移
     *
     * @param velocity      初速度
     * @param frictionCoeff 摩擦系数
     */
    @JvmOverloads
    fun getDistanceByVelocity(velocity: Float, frictionCoeff: Float = 1f): Float {
        var distance = 0f
        if (velocity != 0f) {
            val decelMinusOne = DECELERATION_RATE - 1.0
            val l = Math.pow(
                (INFLEXION * Math.abs(velocity / 4) / (mFlingFriction * frictionCoeff * mPhysicalCoeff)).toDouble(),
                DECELERATION_RATE / decelMinusOne
            )
            distance =
                    (l * mFlingFriction.toDouble() * frictionCoeff.toDouble() * mPhysicalCoeff.toDouble() * Math.signum(
                        velocity
                    ).toDouble()).toFloat()
        }
        return distance
    }

    /**
     * 根据时间计算位移距离，无方向性
     *
     * @param duration      动画时间
     * @param frictionCoeff 摩擦系数
     */
    @JvmOverloads
    fun getDistanceByDuration(duration: Long, frictionCoeff: Float = 1f): Float {
        var distance = 0f
        if (duration > 0) {
            val base = Math.pow((duration / 1000f).toDouble(), DECELERATION_RATE.toDouble())
            distance =
                    (base * mFlingFriction.toDouble() * frictionCoeff.toDouble() * mPhysicalCoeff.toDouble()).toFloat()
        }
        return distance
    }

    /**
     * 根据初速度计算持续时间
     *
     * @param velocity      初速度
     * @param frictionCoeff 摩擦系数
     */
    @JvmOverloads
    fun getDurationByVelocity(velocity: Float, frictionCoeff: Float = 1f): Long {
        var duration: Long = 0
        if (velocity != 0f) {
            val decelMinusOne = DECELERATION_RATE - 1.0
            duration = (1000 * Math.pow(
                (INFLEXION * Math.abs(velocity / 4) / (mFlingFriction * frictionCoeff * mPhysicalCoeff)).toDouble(),
                1 / decelMinusOne
            )).toLong()
        }
        return duration
    }

    /**
     * 根据位移距离计算持续时间
     *
     * @param distance      位移距离
     * @param frictionCoeff 摩擦系数
     */
    @JvmOverloads
    fun getDurationByDistance(distance: Float, frictionCoeff: Float = 1f): Long {
        var duration: Long = 0
        if (distance != 0f) {
            val base =
                (Math.abs(distance) / (mFlingFriction * frictionCoeff * mPhysicalCoeff)).toDouble()
            duration = (1000 * Math.pow(base, (1 / DECELERATION_RATE).toDouble())).toLong()
        }
        return duration
    }

    /**
     * 根据位移距离和时间重置动摩擦系数
     *
     * @param distance 位移距离
     */
    private fun resetFlingFriction(distance: Float, duration: Long) {
        val base = Math.pow((duration / 1000f).toDouble(), DECELERATION_RATE.toDouble())
        mFlingFriction = Math.abs(distance / (base * mPhysicalCoeff)).toFloat()
    }

    /**
     * 设置动摩擦系数倍率
     */
    fun setFlingFrictionRatio(ratio: Float) {
        if (ratio > 0) {
            this.mFlingFrictionRatio = ratio
        }
    }

    private inner class DecelerateEvaluator : TypeEvaluator<Float> {

        override fun evaluate(fraction: Float, startValue: Float?, endValue: Float?): Float? {
            var temp = fraction
            if (!isBouncing) {//禁用回弹效果（可能越界，需要提前结束动画）
                val distance = getDistance(temp, duration, mDistance, mFrictionCoeff)
                if (isOutside && (distance - endValue!! + startValue!!) * mDistance > 0) {//越界了
                    if (temp > 0 && temp < 1) {//动画还将继续，提前结束
                        end()
                    }
                    return endValue
                }
                return startValue!! + distance
            } else if (isOutside) {//回弹效果触发（发生越界）
                val bounceFraction = 1f * mBounceDuration / duration
                if (temp <= 1f - bounceFraction) {//第一阶段，按原本位移距离和动画时间进行计算
                    //校正进度值
                    temp = temp * duration / mOriginalDuration
                    val distance = getDistance(temp, mOriginalDuration, mOriginalDistance, 1f)
                    return startValue!! + distance
                } else if (temp <= 1f - bounceFraction / 2f) {//第二阶段，越过边界开始减速
                    //校正进度值
                    temp = 2f * (temp + bounceFraction - 1f) / bounceFraction
                    val distance =
                        getDistance(temp, mBounceDuration / 2, mBounceDistance, mFrictionCoeff)
                    return endValue!! + distance
                } else {//第三阶段，加速回归边界
                    //校正进度值
                    temp = 2f * (1f - temp) / bounceFraction
                    val distance =
                        getDistance(temp, mBounceDuration / 2, mBounceDistance, mFrictionCoeff)
                    return endValue!! + distance
                }
            } else {//回弹效果未触发（未越界）
                val distance = getDistance(temp, duration, mDistance, mFrictionCoeff)
                return startValue!! + distance
            }
        }

        /**
         * 计算位移距离
         *
         * @param fraction      动画进度
         * @param duration      动画时间
         * @param distance      动画总距离
         * @param frictionCoeff 摩擦系数
         */
        private fun getDistance(
            fraction: Float,
            duration: Long,
            distance: Float,
            frictionCoeff: Float
        ): Float {
            //获取剩余动画时间
            val surplusDuration = ((1f - fraction) * duration).toLong()
            //计算剩余位移距离
            val surplusDistance =
                getDistanceByDuration(surplusDuration, frictionCoeff) * Math.signum(distance)
            //计算位移距离
            return distance - surplusDistance
        }

    }
}
/**
 * 根据位移计算初速度
 *
 * @param distance 位移距离
 */
/**
 * 根据初速度计算位移
 *
 * @param velocity 初速度
 */
/**
 * 根据时间计算位移距离，无方向性
 *
 * @param duration 动画时间
 */
/**
 * 根据初速度计算持续时间
 *
 * @param velocity 初速度
 */
/**
 * 根据位移距离计算持续时间
 *
 * @param distance 位移距离
 */
