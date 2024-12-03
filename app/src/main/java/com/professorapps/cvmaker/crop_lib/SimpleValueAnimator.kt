package com.professorapps.cvmaker.crop_lib

@Suppress("unused")
interface SimpleValueAnimator {
    fun startAnimation(duration: Long)

    fun cancelAnimation()

    val isAnimationStarted: Boolean

    fun addAnimatorListener(animatorListener: SimpleValueAnimatorListener?)
}
