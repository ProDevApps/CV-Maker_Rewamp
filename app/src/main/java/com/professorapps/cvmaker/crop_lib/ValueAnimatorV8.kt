package com.professorapps.cvmaker.crop_lib

import android.os.SystemClock
import android.view.animation.Interpolator
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import kotlin.math.min

class ValueAnimatorV8(private val mInterpolator: Interpolator) : SimpleValueAnimator {
    lateinit var service: ScheduledExecutorService
    var start: Long = 0
    override var isAnimationStarted: Boolean = false
    var duration: Long = 0
    private var animatorListener: SimpleValueAnimatorListener =
        object : SimpleValueAnimatorListener {
            override fun onAnimationStarted() {
            }

            override fun onAnimationUpdated(scale: Float) {
            }

            override fun onAnimationFinished() {
            }
        }

    private val runnable = Runnable {
        val elapsed = SystemClock.uptimeMillis() - start
        if (elapsed > duration) {
            isAnimationStarted = false
            animatorListener.onAnimationFinished()
            service!!.shutdown()
            return@Runnable
        }
        val scale =
            min(mInterpolator.getInterpolation(elapsed.toFloat() / duration).toDouble(), 1.0)
                .toFloat()
        animatorListener.onAnimationUpdated(scale)
    }

    override fun startAnimation(duration: Long) {
        if (duration >= 0) {
            this.duration = duration
        } else {
            this.duration = DEFAULT_ANIMATION_DURATION.toLong()
        }
        isAnimationStarted = true
        animatorListener.onAnimationStarted()
        start = SystemClock.uptimeMillis()
        service = Executors.newSingleThreadScheduledExecutor()
        service.scheduleAtFixedRate(runnable, 0, UPDATE_SPAN.toLong(), TimeUnit.MILLISECONDS)
    }

    override fun cancelAnimation() {
        isAnimationStarted = false
        service!!.shutdown()
        animatorListener.onAnimationFinished()
    }

    override fun addAnimatorListener(animatorListener: SimpleValueAnimatorListener?) {
        if (animatorListener != null) this.animatorListener = animatorListener
    }

    companion object {
        private const val FRAME_RATE = 30
        private val UPDATE_SPAN = Math.round(1000f / FRAME_RATE.toFloat())
        private const val DEFAULT_ANIMATION_DURATION = 150
    }
}
