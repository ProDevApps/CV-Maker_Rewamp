package com.professorapps.cvmaker.utils

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.OnScaleGestureListener
import android.view.View
import android.widget.RelativeLayout
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class ZoomLayout : RelativeLayout, OnScaleGestureListener {
    private var mode = Mode.NONE
    private var scale = 1.0f
    private var lastScaleFactor = 0.0f
    private var startX = 0.0f
    private var startY = 0.0f
    private var dx = 0.0f
    private var dy = 0.0f
    private var prevDx = 0.0f
    private var prevDy = 0.0f


    private enum class Mode {
        NONE,
        DRAG,
        ZOOM
    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?) : super(context, attributeSet) {
        init(context)
    }

    constructor(context: Context, attributeSet: AttributeSet?, i: Int) : super(
        context,
        attributeSet,
        i
    ) {
        init(context)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init(context: Context) {
        val scaleGestureDetector = ScaleGestureDetector(context, this)
        setOnTouchListener { view, motionEvent ->
            val action = motionEvent.action and 255
            if (action == 0) {
                Log.i("ZoomLayout", "DOWN")
                if (this@ZoomLayout.scale > 1.0f) {
                    this@ZoomLayout.mode = Mode.DRAG
                    this@ZoomLayout.startX = motionEvent.x - this@ZoomLayout.prevDx
                    this@ZoomLayout.startY = motionEvent.y - this@ZoomLayout.prevDy
                }
            } else if (action == 1) {
                Log.i("ZoomLayout", "UP")
                this@ZoomLayout.mode = Mode.NONE
                val zoomLayout = this@ZoomLayout
                zoomLayout.prevDx = zoomLayout.dx
                val zoomLayout2 = this@ZoomLayout
                zoomLayout2.prevDy = zoomLayout2.dy
            } else if (action != 2) {
                if (action == 5) {
                    this@ZoomLayout.mode = Mode.ZOOM
                } else if (action == 6) {
                    this@ZoomLayout.mode = Mode.DRAG
                }
            } else if (this@ZoomLayout.mode == Mode.DRAG) {
                this@ZoomLayout.dx = motionEvent.x - this@ZoomLayout.startX
                this@ZoomLayout.dy = motionEvent.y - this@ZoomLayout.startY
            }
            scaleGestureDetector.onTouchEvent(motionEvent)
            if ((this@ZoomLayout.mode == Mode.DRAG && this@ZoomLayout.scale >= 1.0f) || this@ZoomLayout.mode == Mode.ZOOM) {
                this@ZoomLayout.parent.requestDisallowInterceptTouchEvent(true)
                val width =
                    (((child().width.toFloat()) - ((child().width.toFloat()) / this@ZoomLayout.scale)) / 2.0f) * this@ZoomLayout.scale
                val height =
                    (((child().height.toFloat()) - ((child().height.toFloat()) / this@ZoomLayout.scale)) / 2.0f) * this@ZoomLayout.scale
                val zoomLayout3 = this@ZoomLayout
                zoomLayout3.dx =
                    min(max(zoomLayout3.dx.toDouble(), -width.toDouble()), width.toDouble())
                        .toFloat()
                val zoomLayout4 = this@ZoomLayout
                zoomLayout4.dy =
                    min(max(zoomLayout4.dy.toDouble(), -height.toDouble()), height.toDouble())
                        .toFloat()
                Log.i(
                    "ZoomLayout",
                    "Width: " + child().width + ", scale " + this@ZoomLayout.scale + ", dx " + this@ZoomLayout.dx + ", max " + width
                )
                this@ZoomLayout.applyScaleAndTranslation()
            }
            true
        }
    }

    override fun onScaleBegin(scaleGestureDetector: ScaleGestureDetector): Boolean {
        Log.i("ZoomLayout", "onScaleBegin")
        return true
    }

    override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
        val scaleFactor = scaleGestureDetector.scaleFactor
        Log.i("ZoomLayout", "onScale$scaleFactor")
        if (this.lastScaleFactor == 0.0f || sign(scaleFactor.toDouble()) == sign(
                lastScaleFactor.toDouble()
            )
        ) {
            this.scale *= scaleFactor
            this.scale = max(
                1.0,
                min(
                    scale.toDouble(),
                    MAX_ZOOM.toDouble()
                )
            ).toFloat()
            this.lastScaleFactor = scaleFactor
            return true
        }
        this.lastScaleFactor = 0.0f
        return true
    }

    override fun onScaleEnd(scaleGestureDetector: ScaleGestureDetector) {
        Log.i("ZoomLayout", "onScaleEnd")
    }


    fun applyScaleAndTranslation() {
        child().scaleX = scale
        child().scaleY = scale
        child().translationX = dx
        child().translationY = dy
    }


    fun child(): View {
        return getChildAt(0)
    }

    companion object {
        private const val MAX_ZOOM = 4.0f
        private const val MIN_ZOOM = 1.0f
        private const val TAG = "ZoomLayout"
    }
}
