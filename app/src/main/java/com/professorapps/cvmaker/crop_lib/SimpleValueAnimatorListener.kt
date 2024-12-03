package com.professorapps.cvmaker.crop_lib

interface SimpleValueAnimatorListener {
    fun onAnimationStarted()

    fun onAnimationUpdated(scale: Float)

    fun onAnimationFinished()
}
