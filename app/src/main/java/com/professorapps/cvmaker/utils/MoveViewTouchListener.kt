package com.professorapps.cvmaker.utils

import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View

class MoveViewTouchListener(view: View) : View.OnTouchListener {
    private val mGestureDetector: GestureDetector
    private val mGestureListener: GestureDetector.OnGestureListener =
        object : SimpleOnGestureListener() {
            private var mMotionDownX = 0f
            private var mMotionDownY = 0f

            override fun onDown(motionEvent: MotionEvent): Boolean {
                this.mMotionDownX = motionEvent.rawX - mView.translationX
                this.mMotionDownY = motionEvent.rawY - mView.translationY
                return true
            }

            override fun onScroll(
                motionEvent: MotionEvent?,
                motionEvent2: MotionEvent,
                f: Float,
                f2: Float
            ): Boolean {
                mView.translationX =
                    motionEvent2.rawX - this.mMotionDownX
                mView.translationY = motionEvent2.rawY - this.mMotionDownY
                return true
            }
        }
    private val mView: View

    init {
        this.mGestureDetector = GestureDetector(view.context, this.mGestureListener)
        this.mView = view
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return mGestureDetector.onTouchEvent(motionEvent)
    }
}
