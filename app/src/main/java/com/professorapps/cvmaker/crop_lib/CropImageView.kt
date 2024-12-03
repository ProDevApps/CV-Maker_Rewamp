package com.professorapps.cvmaker.crop_lib

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.BitmapRegionDecoder
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import com.professorapps.cvmaker.crop_lib.CropImageView
import io.reactivex.Completable
import io.reactivex.CompletableEmitter
import io.reactivex.CompletableOnSubscribe
import io.reactivex.Single
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.concurrent.Callable
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

@SuppressLint("AppCompatCustomView")
@Suppress("unused")
class CropImageView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ImageView(context, attrs, defStyle) {
    // Member variables ////////////////////////////////////////////////////////////////////////////
    private var mViewWidth = 0
    private var mViewHeight = 0
    private var mScale = 1.0f
    private var mAngle = 0.0f
    private var mImgWidth = 0.0f
    private var mImgHeight = 0.0f

    private var mIsInitialized = false
    private lateinit var mMatrix: Matrix
    private val mPaintTranslucent: Paint
    private val mPaintFrame: Paint
    private val mPaintBitmap: Paint
    private val mPaintDebug: Paint
    private var mFrameRect: RectF? = null
    private var mInitialFrameRect: RectF? = null
    private var mImageRect: RectF? = null
    private var mCenter = PointF()
    private var mLastX = 0f
    private var mLastY = 0f
    private var mIsRotating = false
    private var mIsAnimating = false
    private var mAnimator: SimpleValueAnimator? = null
    private val DEFAULT_INTERPOLATOR: Interpolator = DecelerateInterpolator()
    private var mInterpolator = DEFAULT_INTERPOLATOR
    private val mHandler = Handler(Looper.getMainLooper())

    /**
     * source uri
     *
     * @return source uri
     */
    var sourceUri: Uri? = null
        private set

    /**
     * save uri
     *
     * @return save uri
     */
    var saveUri: Uri? = null
        private set
    private var mExifRotation = 0
    private var mOutputMaxWidth = 0
    private var mOutputMaxHeight = 0
    private var mOutputWidth = 0
    private var mOutputHeight = 0
    private var mIsDebug = false
    private var mCompressFormat: CompressFormat? = CompressFormat.PNG
    private var mCompressQuality = 100
    private var mInputImageWidth = 0
    private var mInputImageHeight = 0
    private var mOutputImageWidth = 0
    private var mOutputImageHeight = 0
    private val mIsLoading = AtomicBoolean(false)
    private val mIsCropping = AtomicBoolean(false)
    private val mIsSaving = AtomicBoolean(false)
    private val mExecutor: ExecutorService = Executors.newSingleThreadExecutor()

    // Instance variables for customizable attributes //////////////////////////////////////////////
    private var mTouchArea = TouchArea.OUT_OF_BOUNDS

    private var mCropMode: CropMode? = CropMode.SQUARE
    private var mGuideShowMode: ShowMode? = ShowMode.SHOW_ALWAYS
    private var mHandleShowMode: ShowMode? = ShowMode.SHOW_ALWAYS
    private var mMinFrameSize: Float
    private var mHandleSize: Int
    private var mTouchPadding = 0
    private var mShowGuide = true
    private var mShowHandle = true
    private var mIsCropEnabled = true
    private var mIsEnabled = true
    private var mCustomRatio = PointF(1.0f, 1.0f)
    private var mFrameStrokeWeight = 2.0f
    private var mGuideStrokeWeight = 2.0f
    private var mBackgroundColor: Int
    private var mOverlayColor: Int
    private var mFrameColor: Int
    private var mHandleColor: Int
    private var mGuideColor: Int
    private var mInitialFrameScale = 0f // 0.01 ~ 1.0, 0.75 is default value
    private var mIsAnimationEnabled = true
    private var mAnimationDurationMillis = DEFAULT_ANIMATION_DURATION_MILLIS
    private var mIsHandleShadowEnabled = true

    // Constructor /////////////////////////////////////////////////////////////////////////////////
    init {
        val density = density
        mHandleSize = (density * HANDLE_SIZE_IN_DP).toInt()
        mMinFrameSize = density * MIN_FRAME_SIZE_IN_DP
        mFrameStrokeWeight = density * FRAME_STROKE_WEIGHT_IN_DP
        mGuideStrokeWeight = density * GUIDE_STROKE_WEIGHT_IN_DP

        mPaintFrame = Paint()
        mPaintTranslucent = Paint()
        mPaintBitmap = Paint()
        mPaintBitmap.isFilterBitmap = true
        mPaintDebug = Paint()
        mPaintDebug.isAntiAlias = true
        mPaintDebug.style = Paint.Style.STROKE
        mPaintDebug.color = WHITE
        mPaintDebug.textSize = DEBUG_TEXT_SIZE_IN_DP.toFloat() * density

        mMatrix = Matrix()
        mScale = 1.0f
        mBackgroundColor = TRANSPARENT
        mFrameColor = WHITE
        mOverlayColor = TRANSLUCENT_BLACK
        mHandleColor = WHITE
        mGuideColor = TRANSLUCENT_WHITE

        // handle Styleable
        //  handleStyleable(context, attrs, defStyle, density);
    }

    // Lifecycle methods ///////////////////////////////////////////////////////////////////////////
    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.mode = this.mCropMode
        ss.backgroundColor = this.mBackgroundColor
        ss.overlayColor = this.mOverlayColor
        ss.frameColor = this.mFrameColor
        ss.guideShowMode = this.mGuideShowMode
        ss.handleShowMode = this.mHandleShowMode
        ss.showGuide = this.mShowGuide
        ss.showHandle = this.mShowHandle
        ss.handleSize = this.mHandleSize
        ss.touchPadding = this.mTouchPadding
        ss.minFrameSize = this.mMinFrameSize
        ss.customRatioX = mCustomRatio.x
        ss.customRatioY = mCustomRatio.y
        ss.frameStrokeWeight = this.mFrameStrokeWeight
        ss.guideStrokeWeight = this.mGuideStrokeWeight
        ss.isCropEnabled = this.mIsCropEnabled
        ss.handleColor = this.mHandleColor
        ss.guideColor = this.mGuideColor
        ss.initialFrameScale = this.mInitialFrameScale
        ss.angle = this.mAngle
        ss.isAnimationEnabled = this.mIsAnimationEnabled
        ss.animationDuration = this.mAnimationDurationMillis
        ss.exifRotation = this.mExifRotation
        ss.sourceUri = this.sourceUri
        ss.saveUri = this.saveUri
        ss.compressFormat = this.mCompressFormat
        ss.compressQuality = this.mCompressQuality
        ss.isDebug = this.mIsDebug
        ss.outputMaxWidth = this.mOutputMaxWidth
        ss.outputMaxHeight = this.mOutputMaxHeight
        ss.outputWidth = this.mOutputWidth
        ss.outputHeight = this.mOutputHeight
        ss.isHandleShadowEnabled = this.mIsHandleShadowEnabled
        ss.inputImageWidth = this.mInputImageWidth
        ss.inputImageHeight = this.mInputImageHeight
        ss.outputImageWidth = this.mOutputImageWidth
        ss.outputImageHeight = this.mOutputImageHeight
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        this.mCropMode = ss.mode
        this.mBackgroundColor = ss.backgroundColor
        this.mOverlayColor = ss.overlayColor
        this.mFrameColor = ss.frameColor
        this.mGuideShowMode = ss.guideShowMode
        this.mHandleShowMode = ss.handleShowMode
        this.mShowGuide = ss.showGuide
        this.mShowHandle = ss.showHandle
        this.mHandleSize = ss.handleSize
        this.mTouchPadding = ss.touchPadding
        this.mMinFrameSize = ss.minFrameSize
        this.mCustomRatio = PointF(ss.customRatioX, ss.customRatioY)
        this.mFrameStrokeWeight = ss.frameStrokeWeight
        this.mGuideStrokeWeight = ss.guideStrokeWeight
        this.mIsCropEnabled = ss.isCropEnabled
        this.mHandleColor = ss.handleColor
        this.mGuideColor = ss.guideColor
        this.mInitialFrameScale = ss.initialFrameScale
        this.mAngle = ss.angle
        this.mIsAnimationEnabled = ss.isAnimationEnabled
        this.mAnimationDurationMillis = ss.animationDuration
        this.mExifRotation = ss.exifRotation
        this.sourceUri = ss.sourceUri
        this.saveUri = ss.saveUri
        this.mCompressFormat = ss.compressFormat
        this.mCompressQuality = ss.compressQuality
        this.mIsDebug = ss.isDebug
        this.mOutputMaxWidth = ss.outputMaxWidth
        this.mOutputMaxHeight = ss.outputMaxHeight
        this.mOutputWidth = ss.outputWidth
        this.mOutputHeight = ss.outputHeight
        this.mIsHandleShadowEnabled = ss.isHandleShadowEnabled
        this.mInputImageWidth = ss.inputImageWidth
        this.mInputImageHeight = ss.inputImageHeight
        this.mOutputImageWidth = ss.outputImageWidth
        this.mOutputImageHeight = ss.outputImageHeight
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val viewWidth = MeasureSpec.getSize(widthMeasureSpec)
        val viewHeight = MeasureSpec.getSize(heightMeasureSpec)

        setMeasuredDimension(viewWidth, viewHeight)

        mViewWidth = viewWidth - paddingLeft - paddingRight
        mViewHeight = viewHeight - paddingTop - paddingBottom
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (drawable != null) setupLayout(mViewWidth, mViewHeight)
    }

    public override fun onDraw(canvas: Canvas) {
        canvas.drawColor(mBackgroundColor)

        if (mIsInitialized) {
            setMatrix()
            val bm = bitmap
            if (bm != null) {
                canvas.drawBitmap(bm, mMatrix!!, mPaintBitmap)
                // draw edit frame
                drawCropFrame(canvas)
            }

            if (mIsDebug) {
                drawDebugInfo(canvas)
            }
        }
    }

    override fun onDetachedFromWindow() {
        mExecutor.shutdown()
        super.onDetachedFromWindow()
    }

    // Handle styleable ////////////////////////////////////////////////////////////////////////////
    /*  private void handleStyleable(Context context, AttributeSet attrs, int defStyle, float mDensity) {
    TypedArray ta =
        context.obtainStyledAttributes(attrs, R.styleable.scv_CropImageView, defStyle, 0);
    Drawable drawable;
    mCropMode = CropMode.SQUARE;
    try {
      drawable = ta.getDrawable(R.styleable.scv_CropImageView_scv_img_src);
      if (drawable != null) setImageDrawable(drawable);
      for (CropMode mode : CropMode.values()) {
        if (ta.getInt(R.styleable.scv_CropImageView_scv_crop_mode, 3) == mode.getId()) {
          mCropMode = mode;
          break;
        }
      }
      mBackgroundColor =
          ta.getColor(R.styleable.scv_CropImageView_scv_background_color, TRANSPARENT);
      mOverlayColor =
          ta.getColor(R.styleable.scv_CropImageView_scv_overlay_color, TRANSLUCENT_BLACK);
      mFrameColor = ta.getColor(R.styleable.scv_CropImageView_scv_frame_color, WHITE);
      mHandleColor = ta.getColor(R.styleable.scv_CropImageView_scv_handle_color, WHITE);
      mGuideColor = ta.getColor(R.styleable.scv_CropImageView_scv_guide_color, TRANSLUCENT_WHITE);
      for (ShowMode mode : ShowMode.values()) {
        if (ta.getInt(R.styleable.scv_CropImageView_scv_guide_show_mode, 1) == mode.getId()) {
          mGuideShowMode = mode;
          break;
        }
      }

      for (ShowMode mode : ShowMode.values()) {
        if (ta.getInt(R.styleable.scv_CropImageView_scv_handle_show_mode, 1) == mode.getId()) {
          mHandleShowMode = mode;
          break;
        }
      }
      setGuideShowMode(mGuideShowMode);
      setHandleShowMode(mHandleShowMode);
      mHandleSize = ta.getDimensionPixelSize(R.styleable.scv_CropImageView_scv_handle_size,   (int) (HANDLE_SIZE_IN_DP * mDensity));
      mTouchPadding = ta.getDimensionPixelSize(R.styleable.scv_CropImageView_scv_touch_padding, 0);
      mMinFrameSize = ta.getDimensionPixelSize(R.styleable.scv_CropImageView_scv_min_frame_size,  (int) (MIN_FRAME_SIZE_IN_DP * mDensity));
      mFrameStrokeWeight =    ta.getDimensionPixelSize(R.styleable.scv_CropImageView_scv_frame_stroke_weight,   (int) (FRAME_STROKE_WEIGHT_IN_DP * mDensity));
      mGuideStrokeWeight =    ta.getDimensionPixelSize(R.styleable.scv_CropImageView_scv_guide_stroke_weight,     (int) (GUIDE_STROKE_WEIGHT_IN_DP * mDensity));
      mIsCropEnabled = ta.getBoolean(R.styleable.scv_CropImageView_scv_crop_enabled, true);
      mInitialFrameScale = constrain(   ta.getFloat(R.styleable.scv_CropImageView_scv_initial_frame_scale,   DEFAULT_INITIAL_FRAME_SCALE), 0.01f, 1.0f, DEFAULT_INITIAL_FRAME_SCALE);
      mIsAnimationEnabled =  ta.getBoolean(R.styleable.scv_CropImageView_scv_animation_enabled, true);
      mAnimationDurationMillis = ta.getInt(R.styleable.scv_CropImageView_scv_animation_duration,     DEFAULT_ANIMATION_DURATION_MILLIS);
      mIsHandleShadowEnabled =  ta.getBoolean(R.styleable.scv_CropImageView_scv_handle_shadow_enabled, true);
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      ta.recycle();
    }
  }*/
    // Drawing method //////////////////////////////////////////////////////////////////////////////
    private fun drawDebugInfo(canvas: Canvas) {
        val fontMetrics = mPaintDebug.fontMetrics
        mPaintDebug.measureText("W")
        val textHeight = (fontMetrics.descent - fontMetrics.ascent).toInt()
        val x = (mImageRect!!.left + mHandleSize.toFloat() * 0.5f * density).toInt()
        var y = (mImageRect!!.top + textHeight + mHandleSize.toFloat() * 0.5f * density).toInt()
        var builder = StringBuilder()
        builder.append("LOADED FROM: ").append(if (sourceUri != null) "Uri" else "Bitmap")
        canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
        builder = StringBuilder()

        if (sourceUri == null) {
            builder.append("INPUT_IMAGE_SIZE: ")
                .append(mImgWidth.toInt())
                .append("x")
                .append(mImgHeight.toInt())
            y += textHeight
            canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
            builder = StringBuilder()
        } else {
            builder = StringBuilder().append("INPUT_IMAGE_SIZE: ")
                .append(mInputImageWidth)
                .append("x")
                .append(mInputImageHeight)
            y += textHeight
            canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
            builder = StringBuilder()
        }
        builder.append("LOADED_IMAGE_SIZE: ")
            .append(bitmap!!.width)
            .append("x")
            .append(bitmap!!.height)
        y += textHeight
        canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
        builder = StringBuilder()
        if (mOutputImageWidth > 0 && mOutputImageHeight > 0) {
            builder.append("OUTPUT_IMAGE_SIZE: ")
                .append(mOutputImageWidth)
                .append("x")
                .append(mOutputImageHeight)
            y += textHeight
            canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
            builder = StringBuilder().append("EXIF ROTATION: ").append(mExifRotation)
            y += textHeight
            canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
            builder = StringBuilder().append("CURRENT_ROTATION: ").append(mAngle.toInt())
            y += textHeight
            canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
        }
        builder = StringBuilder()
        builder.append("FRAME_RECT: ").append(mFrameRect.toString())
        y += textHeight
        canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
        builder = StringBuilder()
        builder.append("ACTUAL_CROP_RECT: ")
            .append(if (actualCropRect != null) actualCropRect.toString() else "")
        y += textHeight
        canvas.drawText(builder.toString(), x.toFloat(), y.toFloat(), mPaintDebug)
    }

    private fun drawCropFrame(canvas: Canvas) {
        if (!mIsCropEnabled) return
        if (mIsRotating) return
        drawOverlay(canvas)
        drawFrame(canvas)
        if (mShowGuide) drawGuidelines(canvas)
        if (mShowHandle) drawHandles(canvas)
    }

    private fun drawOverlay(canvas: Canvas) {
        mPaintTranslucent.isAntiAlias = true
        mPaintTranslucent.isFilterBitmap = true
        mPaintTranslucent.color = mOverlayColor
        mPaintTranslucent.style = Paint.Style.FILL
        val path = Path()
        val overlayRect =
            RectF(
                floor(mImageRect!!.left.toDouble())
                    .toFloat(), floor(mImageRect!!.top.toDouble()).toFloat(),
                ceil(mImageRect!!.right.toDouble()).toFloat(), ceil(mImageRect!!.bottom.toDouble())
                    .toFloat()
            )
        if (!mIsAnimating && (mCropMode == CropMode.CIRCLE || mCropMode == CropMode.CIRCLE_SQUARE)) {
            path.addRect(overlayRect, Path.Direction.CW)
            val circleCenter = PointF(
                (mFrameRect!!.left + mFrameRect!!.right) / 2,
                (mFrameRect!!.top + mFrameRect!!.bottom) / 2
            )
            val circleRadius = (mFrameRect!!.right - mFrameRect!!.left) / 2
            path.addCircle(circleCenter.x, circleCenter.y, circleRadius, Path.Direction.CCW)
            canvas.drawPath(path, mPaintTranslucent)
        } else {
            path.addRect(overlayRect, Path.Direction.CW)
            path.addRect(mFrameRect!!, Path.Direction.CCW)
            canvas.drawPath(path, mPaintTranslucent)
        }
    }

    private fun drawFrame(canvas: Canvas) {
        mPaintFrame.isAntiAlias = true
        mPaintFrame.isFilterBitmap = true
        mPaintFrame.style = Paint.Style.STROKE
        mPaintFrame.color = mFrameColor
        mPaintFrame.strokeWidth = mFrameStrokeWeight
        canvas.drawRect(mFrameRect!!, mPaintFrame)
    }

    private fun drawGuidelines(canvas: Canvas) {
        mPaintFrame.color = mGuideColor
        mPaintFrame.strokeWidth = mGuideStrokeWeight
        val h1 = mFrameRect!!.left + (mFrameRect!!.right - mFrameRect!!.left) / 3.0f
        val h2 = mFrameRect!!.right - (mFrameRect!!.right - mFrameRect!!.left) / 3.0f
        val v1 = mFrameRect!!.top + (mFrameRect!!.bottom - mFrameRect!!.top) / 3.0f
        val v2 = mFrameRect!!.bottom - (mFrameRect!!.bottom - mFrameRect!!.top) / 3.0f
        canvas.drawLine(h1, mFrameRect!!.top, h1, mFrameRect!!.bottom, mPaintFrame)
        canvas.drawLine(h2, mFrameRect!!.top, h2, mFrameRect!!.bottom, mPaintFrame)
        canvas.drawLine(mFrameRect!!.left, v1, mFrameRect!!.right, v1, mPaintFrame)
        canvas.drawLine(mFrameRect!!.left, v2, mFrameRect!!.right, v2, mPaintFrame)
    }

    private fun drawHandles(canvas: Canvas) {
        if (mIsHandleShadowEnabled) drawHandleShadows(canvas)
        mPaintFrame.style = Paint.Style.FILL
        mPaintFrame.color = mHandleColor
        canvas.drawCircle(mFrameRect!!.left, mFrameRect!!.top, mHandleSize.toFloat(), mPaintFrame)
        canvas.drawCircle(mFrameRect!!.right, mFrameRect!!.top, mHandleSize.toFloat(), mPaintFrame)
        canvas.drawCircle(
            mFrameRect!!.left,
            mFrameRect!!.bottom,
            mHandleSize.toFloat(),
            mPaintFrame
        )
        canvas.drawCircle(
            mFrameRect!!.right,
            mFrameRect!!.bottom,
            mHandleSize.toFloat(),
            mPaintFrame
        )
    }

    private fun drawHandleShadows(canvas: Canvas) {
        mPaintFrame.style = Paint.Style.FILL
        mPaintFrame.color = TRANSLUCENT_BLACK
        val rect = RectF(mFrameRect)
        rect.offset(0f, 1f)
        canvas.drawCircle(rect.left, rect.top, mHandleSize.toFloat(), mPaintFrame)
        canvas.drawCircle(rect.right, rect.top, mHandleSize.toFloat(), mPaintFrame)
        canvas.drawCircle(rect.left, rect.bottom, mHandleSize.toFloat(), mPaintFrame)
        canvas.drawCircle(rect.right, rect.bottom, mHandleSize.toFloat(), mPaintFrame)
    }

    private fun setMatrix() {
        mMatrix!!.reset()
        mMatrix.setTranslate(mCenter.x - mImgWidth * 0.5f, mCenter.y - mImgHeight * 0.5f)
        mMatrix.postScale(mScale, mScale, mCenter.x, mCenter.y)
        mMatrix.postRotate(mAngle, mCenter.x, mCenter.y)
    }

    // Layout calculation //////////////////////////////////////////////////////////////////////////
    private fun setupLayout(viewW: Int, viewH: Int) {
        if (viewW == 0 || viewH == 0) return
        setCenter(PointF(paddingLeft + viewW * 0.5f, paddingTop + viewH * 0.5f))
        setScale(calcScale(viewW, viewH, mAngle))
        setMatrix()
        mImageRect = calcImageRect(RectF(0f, 0f, mImgWidth, mImgHeight), mMatrix)

        mFrameRect = if (mInitialFrameRect != null) {
            applyInitialFrameRect(mInitialFrameRect!!)
        } else {
            calcFrameRect(mImageRect)
        }

        mIsInitialized = true
        invalidate()
    }

    private fun calcScale(viewW: Int, viewH: Int, angle: Float): Float {
        mImgWidth = drawable.intrinsicWidth.toFloat()
        mImgHeight = drawable.intrinsicHeight.toFloat()
        if (mImgWidth <= 0) mImgWidth = viewW.toFloat()
        if (mImgHeight <= 0) mImgHeight = viewH.toFloat()
        val viewRatio = viewW.toFloat() / viewH.toFloat()
        val imgRatio = getRotatedWidth(angle) / getRotatedHeight(angle)
        var scale = 1.0f
        if (imgRatio >= viewRatio) {
            scale = viewW / getRotatedWidth(angle)
        } else if (imgRatio < viewRatio) {
            scale = viewH / getRotatedHeight(angle)
        }
        return scale
    }

    private fun calcImageRect(rect: RectF, matrix: Matrix?): RectF {
        val applied = RectF()
        matrix!!.mapRect(applied, rect)
        return applied
    }

    private fun calcFrameRect(imageRect: RectF?): RectF {
        val frameW = getRatioX(imageRect!!.width())
        val frameH = getRatioY(imageRect.height())
        val imgRatio = imageRect.width() / imageRect.height()
        val frameRatio = frameW / frameH
        var l = imageRect.left
        var t = imageRect.top
        var r = imageRect.right
        var b = imageRect.bottom
        if (frameRatio >= imgRatio) {
            l = imageRect.left
            r = imageRect.right
            val hy = (imageRect.top + imageRect.bottom) * 0.5f
            val hh = (imageRect.width() / frameRatio) * 0.5f
            t = hy - hh
            b = hy + hh
        } else if (frameRatio < imgRatio) {
            t = imageRect.top
            b = imageRect.bottom
            val hx = (imageRect.left + imageRect.right) * 0.5f
            val hw = imageRect.height() * frameRatio * 0.5f
            l = hx - hw
            r = hx + hw
        }
        val w = r - l
        val h = b - t
        val cx = l + w / 2
        val cy = t + h / 2
        val sw = w * mInitialFrameScale
        val sh = h * mInitialFrameScale
        return RectF(cx - sw / 2, cy - sh / 2, cx + sw / 2, cy + sh / 2)
    }

    // Touch Event /////////////////////////////////////////////////////////////////////////////////
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!mIsInitialized) return false
        if (!mIsCropEnabled) return false
        if (!mIsEnabled) return false
        if (mIsRotating) return false
        if (mIsAnimating) return false
        if (mIsLoading.get()) return false
        if (mIsCropping.get()) return false

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                onDown(event)
                return true
            }

            MotionEvent.ACTION_MOVE -> {
                onMove(event)
                if (mTouchArea != TouchArea.OUT_OF_BOUNDS) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                return true
            }

            MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                onCancel()
                return true
            }

            MotionEvent.ACTION_UP -> {
                parent.requestDisallowInterceptTouchEvent(false)
                onUp(event)
                return true
            }
        }
        return false
    }

    private fun onDown(e: MotionEvent) {
        invalidate()
        mLastX = e.x
        mLastY = e.y
        checkTouchArea(e.x, e.y)
    }

    private fun onMove(e: MotionEvent) {
        val diffX = e.x - mLastX
        val diffY = e.y - mLastY
        when (mTouchArea) {
            TouchArea.CENTER -> moveFrame(diffX, diffY)
            TouchArea.LEFT_TOP -> moveHandleLT(diffX, diffY)
            TouchArea.RIGHT_TOP -> moveHandleRT(diffX, diffY)
            TouchArea.LEFT_BOTTOM -> moveHandleLB(diffX, diffY)
            TouchArea.RIGHT_BOTTOM -> moveHandleRB(diffX, diffY)
            TouchArea.OUT_OF_BOUNDS -> {}
        }
        invalidate()
        mLastX = e.x
        mLastY = e.y
    }

    private fun onUp(e: MotionEvent) {
        if (mGuideShowMode == ShowMode.SHOW_ON_TOUCH) mShowGuide = false
        if (mHandleShowMode == ShowMode.SHOW_ON_TOUCH) mShowHandle = false
        mTouchArea = TouchArea.OUT_OF_BOUNDS
        invalidate()
    }

    private fun onCancel() {
        mTouchArea = TouchArea.OUT_OF_BOUNDS
        invalidate()
    }

    // Hit test ////////////////////////////////////////////////////////////////////////////////////
    private fun checkTouchArea(x: Float, y: Float) {
        if (isInsideCornerLeftTop(x, y)) {
            mTouchArea = TouchArea.LEFT_TOP
            if (mHandleShowMode == ShowMode.SHOW_ON_TOUCH) mShowHandle = true
            if (mGuideShowMode == ShowMode.SHOW_ON_TOUCH) mShowGuide = true
            return
        }
        if (isInsideCornerRightTop(x, y)) {
            mTouchArea = TouchArea.RIGHT_TOP
            if (mHandleShowMode == ShowMode.SHOW_ON_TOUCH) mShowHandle = true
            if (mGuideShowMode == ShowMode.SHOW_ON_TOUCH) mShowGuide = true
            return
        }
        if (isInsideCornerLeftBottom(x, y)) {
            mTouchArea = TouchArea.LEFT_BOTTOM
            if (mHandleShowMode == ShowMode.SHOW_ON_TOUCH) mShowHandle = true
            if (mGuideShowMode == ShowMode.SHOW_ON_TOUCH) mShowGuide = true
            return
        }
        if (isInsideCornerRightBottom(x, y)) {
            mTouchArea = TouchArea.RIGHT_BOTTOM
            if (mHandleShowMode == ShowMode.SHOW_ON_TOUCH) mShowHandle = true
            if (mGuideShowMode == ShowMode.SHOW_ON_TOUCH) mShowGuide = true
            return
        }
        if (isInsideFrame(x, y)) {
            if (mGuideShowMode == ShowMode.SHOW_ON_TOUCH) mShowGuide = true
            mTouchArea = TouchArea.CENTER
            return
        }
        mTouchArea = TouchArea.OUT_OF_BOUNDS
    }

    private fun isInsideFrame(x: Float, y: Float): Boolean {
        if (mFrameRect!!.left <= x && mFrameRect!!.right >= x) {
            if (mFrameRect!!.top <= y && mFrameRect!!.bottom >= y) {
                mTouchArea = TouchArea.CENTER
                return true
            }
        }
        return false
    }

    private fun isInsideCornerLeftTop(x: Float, y: Float): Boolean {
        val dx = x - mFrameRect!!.left
        val dy = y - mFrameRect!!.top
        val d = dx * dx + dy * dy
        return sq((mHandleSize + mTouchPadding).toFloat()) >= d
    }

    private fun isInsideCornerRightTop(x: Float, y: Float): Boolean {
        val dx = x - mFrameRect!!.right
        val dy = y - mFrameRect!!.top
        val d = dx * dx + dy * dy
        return sq((mHandleSize + mTouchPadding).toFloat()) >= d
    }

    private fun isInsideCornerLeftBottom(x: Float, y: Float): Boolean {
        val dx = x - mFrameRect!!.left
        val dy = y - mFrameRect!!.bottom
        val d = dx * dx + dy * dy
        return sq((mHandleSize + mTouchPadding).toFloat()) >= d
    }

    private fun isInsideCornerRightBottom(x: Float, y: Float): Boolean {
        val dx = x - mFrameRect!!.right
        val dy = y - mFrameRect!!.bottom
        val d = dx * dx + dy * dy
        return sq((mHandleSize + mTouchPadding).toFloat()) >= d
    }

    // Adjust frame ////////////////////////////////////////////////////////////////////////////////
    private fun moveFrame(x: Float, y: Float) {
        mFrameRect!!.left += x
        mFrameRect!!.right += x
        mFrameRect!!.top += y
        mFrameRect!!.bottom += y
        checkMoveBounds()
    }

    private fun moveHandleLT(diffX: Float, diffY: Float) {
        if (mCropMode == CropMode.FREE) {
            mFrameRect!!.left += diffX
            mFrameRect!!.top += diffY
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.left -= offsetX
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.top -= offsetY
            }
            checkScaleBounds()
        } else {
            val dx = diffX
            val dy = diffX * ratioY / ratioX
            mFrameRect!!.left += dx
            mFrameRect!!.top += dy
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.left -= offsetX
                val offsetY = offsetX * ratioY / ratioX
                mFrameRect!!.top -= offsetY
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.top -= offsetY
                val offsetX = offsetY * ratioX / ratioY
                mFrameRect!!.left -= offsetX
            }
            var ox: Float
            var oy: Float
            if (!isInsideHorizontal(mFrameRect!!.left)) {
                ox = mImageRect!!.left - mFrameRect!!.left
                mFrameRect!!.left += ox
                oy = ox * ratioY / ratioX
                mFrameRect!!.top += oy
            }
            if (!isInsideVertical(mFrameRect!!.top)) {
                oy = mImageRect!!.top - mFrameRect!!.top
                mFrameRect!!.top += oy
                ox = oy * ratioX / ratioY
                mFrameRect!!.left += ox
            }
        }
    }

    private fun moveHandleRT(diffX: Float, diffY: Float) {
        if (mCropMode == CropMode.FREE) {
            mFrameRect!!.right += diffX
            mFrameRect!!.top += diffY
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.right += offsetX
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.top -= offsetY
            }
            checkScaleBounds()
        } else {
            val dx = diffX
            val dy = diffX * ratioY / ratioX
            mFrameRect!!.right += dx
            mFrameRect!!.top -= dy
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.right += offsetX
                val offsetY = offsetX * ratioY / ratioX
                mFrameRect!!.top -= offsetY
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.top -= offsetY
                val offsetX = offsetY * ratioX / ratioY
                mFrameRect!!.right += offsetX
            }
            var ox: Float
            var oy: Float
            if (!isInsideHorizontal(mFrameRect!!.right)) {
                ox = mFrameRect!!.right - mImageRect!!.right
                mFrameRect!!.right -= ox
                oy = ox * ratioY / ratioX
                mFrameRect!!.top += oy
            }
            if (!isInsideVertical(mFrameRect!!.top)) {
                oy = mImageRect!!.top - mFrameRect!!.top
                mFrameRect!!.top += oy
                ox = oy * ratioX / ratioY
                mFrameRect!!.right -= ox
            }
        }
    }

    private fun moveHandleLB(diffX: Float, diffY: Float) {
        if (mCropMode == CropMode.FREE) {
            mFrameRect!!.left += diffX
            mFrameRect!!.bottom += diffY
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.left -= offsetX
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.bottom += offsetY
            }
            checkScaleBounds()
        } else {
            val dx = diffX
            val dy = diffX * ratioY / ratioX
            mFrameRect!!.left += dx
            mFrameRect!!.bottom -= dy
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.left -= offsetX
                val offsetY = offsetX * ratioY / ratioX
                mFrameRect!!.bottom += offsetY
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.bottom += offsetY
                val offsetX = offsetY * ratioX / ratioY
                mFrameRect!!.left -= offsetX
            }
            var ox: Float
            var oy: Float
            if (!isInsideHorizontal(mFrameRect!!.left)) {
                ox = mImageRect!!.left - mFrameRect!!.left
                mFrameRect!!.left += ox
                oy = ox * ratioY / ratioX
                mFrameRect!!.bottom -= oy
            }
            if (!isInsideVertical(mFrameRect!!.bottom)) {
                oy = mFrameRect!!.bottom - mImageRect!!.bottom
                mFrameRect!!.bottom -= oy
                ox = oy * ratioX / ratioY
                mFrameRect!!.left += ox
            }
        }
    }

    private fun moveHandleRB(diffX: Float, diffY: Float) {
        if (mCropMode == CropMode.FREE) {
            mFrameRect!!.right += diffX
            mFrameRect!!.bottom += diffY
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.right += offsetX
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.bottom += offsetY
            }
            checkScaleBounds()
        } else {
            val dx = diffX
            val dy = diffX * ratioY / ratioX
            mFrameRect!!.right += dx
            mFrameRect!!.bottom += dy
            if (isWidthTooSmall) {
                val offsetX = mMinFrameSize - frameW
                mFrameRect!!.right += offsetX
                val offsetY = offsetX * ratioY / ratioX
                mFrameRect!!.bottom += offsetY
            }
            if (isHeightTooSmall) {
                val offsetY = mMinFrameSize - frameH
                mFrameRect!!.bottom += offsetY
                val offsetX = offsetY * ratioX / ratioY
                mFrameRect!!.right += offsetX
            }
            var ox: Float
            var oy: Float
            if (!isInsideHorizontal(mFrameRect!!.right)) {
                ox = mFrameRect!!.right - mImageRect!!.right
                mFrameRect!!.right -= ox
                oy = ox * ratioY / ratioX
                mFrameRect!!.bottom -= oy
            }
            if (!isInsideVertical(mFrameRect!!.bottom)) {
                oy = mFrameRect!!.bottom - mImageRect!!.bottom
                mFrameRect!!.bottom -= oy
                ox = oy * ratioX / ratioY
                mFrameRect!!.right -= ox
            }
        }
    }

    // Frame position correction ///////////////////////////////////////////////////////////////////
    private fun checkScaleBounds() {
        val lDiff = mFrameRect!!.left - mImageRect!!.left
        val rDiff = mFrameRect!!.right - mImageRect!!.right
        val tDiff = mFrameRect!!.top - mImageRect!!.top
        val bDiff = mFrameRect!!.bottom - mImageRect!!.bottom

        if (lDiff < 0) {
            mFrameRect!!.left -= lDiff
        }
        if (rDiff > 0) {
            mFrameRect!!.right -= rDiff
        }
        if (tDiff < 0) {
            mFrameRect!!.top -= tDiff
        }
        if (bDiff > 0) {
            mFrameRect!!.bottom -= bDiff
        }
    }

    private fun checkMoveBounds() {
        var diff = mFrameRect!!.left - mImageRect!!.left
        if (diff < 0) {
            mFrameRect!!.left -= diff
            mFrameRect!!.right -= diff
        }
        diff = mFrameRect!!.right - mImageRect!!.right
        if (diff > 0) {
            mFrameRect!!.left -= diff
            mFrameRect!!.right -= diff
        }
        diff = mFrameRect!!.top - mImageRect!!.top
        if (diff < 0) {
            mFrameRect!!.top -= diff
            mFrameRect!!.bottom -= diff
        }
        diff = mFrameRect!!.bottom - mImageRect!!.bottom
        if (diff > 0) {
            mFrameRect!!.top -= diff
            mFrameRect!!.bottom -= diff
        }
    }

    private fun isInsideHorizontal(x: Float): Boolean {
        return mImageRect!!.left <= x && mImageRect!!.right >= x
    }

    private fun isInsideVertical(y: Float): Boolean {
        return mImageRect!!.top <= y && mImageRect!!.bottom >= y
    }

    private val isWidthTooSmall: Boolean
        get() = frameW < mMinFrameSize

    private val isHeightTooSmall: Boolean
        get() = frameH < mMinFrameSize

    // Frame aspect ratio correction ///////////////////////////////////////////////////////////////
    private fun recalculateFrameRect(durationMillis: Int) {
        if (mImageRect == null) return
        if (mIsAnimating) {
            animator!!.cancelAnimation()
        }
        val currentRect = RectF(mFrameRect)
        val newRect = calcFrameRect(mImageRect)
        val diffL = newRect.left - currentRect.left
        val diffT = newRect.top - currentRect.top
        val diffR = newRect.right - currentRect.right
        val diffB = newRect.bottom - currentRect.bottom
        if (mIsAnimationEnabled) {
            val animator = animator
            animator!!.addAnimatorListener(object : SimpleValueAnimatorListener {
                override fun onAnimationStarted() {
                    mIsAnimating = true
                }

                override fun onAnimationUpdated(scale: Float) {
                    mFrameRect = RectF(
                        currentRect.left + diffL * scale, currentRect.top + diffT * scale,
                        currentRect.right + diffR * scale, currentRect.bottom + diffB * scale
                    )
                    invalidate()
                }

                override fun onAnimationFinished() {
                    mFrameRect = newRect
                    invalidate()
                    mIsAnimating = false
                }
            })
            animator.startAnimation(durationMillis.toLong())
        } else {
            mFrameRect = calcFrameRect(mImageRect)
            invalidate()
        }
    }

    private fun getRatioX(w: Float): Float {
        return when (mCropMode) {
            CropMode.FIT_IMAGE -> mImageRect!!.width()
            CropMode.FREE -> w
            CropMode.RATIO_4_3 -> 4F
            CropMode.RATIO_3_4 -> 3F
            CropMode.RATIO_16_9 -> 16F
            CropMode.RATIO_9_16 -> 9F
            CropMode.SQUARE, CropMode.CIRCLE, CropMode.CIRCLE_SQUARE -> 1F
            CropMode.CUSTOM -> mCustomRatio.x
            else -> w
        }
    }

    private fun getRatioY(h: Float): Float {
        return when (mCropMode) {
            CropMode.FIT_IMAGE -> mImageRect!!.height()
            CropMode.FREE -> h
            CropMode.RATIO_4_3 -> 3F
            CropMode.RATIO_3_4 -> 4F
            CropMode.RATIO_16_9 -> 9F
            CropMode.RATIO_9_16 -> 16F
            CropMode.SQUARE, CropMode.CIRCLE, CropMode.CIRCLE_SQUARE -> 1F
            CropMode.CUSTOM -> mCustomRatio.y
            else -> h
        }
    }

    private val ratioX: Float
        get() = when (mCropMode) {
            CropMode.FIT_IMAGE -> mImageRect!!.width()
            CropMode.RATIO_4_3 -> 4F
            CropMode.RATIO_3_4 -> 3F
            CropMode.RATIO_16_9 -> 16F
            CropMode.RATIO_9_16 -> 9F
            CropMode.SQUARE, CropMode.CIRCLE, CropMode.CIRCLE_SQUARE -> 1F
            CropMode.CUSTOM -> mCustomRatio.x
            else -> 1F
        }

    private val ratioY: Float
        get() = when (mCropMode) {
            CropMode.FIT_IMAGE -> mImageRect!!.height()
            CropMode.RATIO_4_3 -> 3F
            CropMode.RATIO_3_4 -> 4F
            CropMode.RATIO_16_9 -> 9F
            CropMode.RATIO_9_16 -> 16F
            CropMode.SQUARE, CropMode.CIRCLE, CropMode.CIRCLE_SQUARE -> 1F
            CropMode.CUSTOM -> mCustomRatio.y
            else -> 1F
        }

    private val density: Float
        // Utility /////////////////////////////////////////////////////////////////////////////////////
        get() {
            val displayMetrics = DisplayMetrics()
            (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
                .getMetrics(displayMetrics)
            return displayMetrics.density
        }

    private fun sq(value: Float): Float {
        return value * value
    }

    private fun constrain(`val`: Float, min: Float, max: Float, defaultVal: Float): Float {
        if (`val` < min || `val` > max) return defaultVal
        return `val`
    }

    private fun postErrorOnMainThread(callback: Callback?, e: Throwable) {
        if (callback == null) return
        if (Looper.myLooper() == Looper.getMainLooper()) {
            callback.onError(e)
        } else {
            mHandler.post { callback.onError(e) }
        }
    }

    private val bitmap: Bitmap?
        get() {
            var bm: Bitmap? = null
            val d = drawable
            if (d != null && d is BitmapDrawable) bm = d.bitmap
            return bm
        }

    private fun getRotatedWidth(angle: Float): Float {
        return getRotatedWidth(angle, mImgWidth, mImgHeight)
    }

    private fun getRotatedWidth(angle: Float, width: Float, height: Float): Float {
        return if (angle % 180 == 0f) width else height
    }

    private fun getRotatedHeight(angle: Float): Float {
        return getRotatedHeight(angle, mImgWidth, mImgHeight)
    }

    private fun getRotatedHeight(angle: Float, width: Float, height: Float): Float {
        return if (angle % 180 == 0f) height else width
    }

    private fun getRotatedBitmap(bitmap: Bitmap?): Bitmap {
        val rotateMatrix = Matrix()
        rotateMatrix.setRotate(
            mAngle,
            (bitmap!!.width / 2).toFloat(),
            (bitmap.height / 2).toFloat()
        )
        return Bitmap.createBitmap(
            bitmap, 0, 0, bitmap.width, bitmap.height, rotateMatrix,
            true
        )
    }

    private val animator: SimpleValueAnimator?
        // Animation ///////////////////////////////////////////////////////////////////////////////////
        get() {
            setupAnimatorIfNeeded()
            return mAnimator
        }

    private fun setupAnimatorIfNeeded() {
        if (mAnimator == null) {
            mAnimator =
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    ValueAnimatorV8(mInterpolator)
                } else {
                    ValueAnimatorV14(mInterpolator)
                }
        }
    }

    @get:Throws(IOException::class)
    private val croppedBitmapFromUri: Bitmap?
        // Cropping ////////////////////////////////////////////////////////////////////////////////////
        get() {
            var cropped: Bitmap? = null
            var `is`: InputStream? = null
            try {
                `is` = context.contentResolver.openInputStream(sourceUri!!)
                val decoder = BitmapRegionDecoder.newInstance(`is`!!, false)
                val originalImageWidth = decoder!!.width
                val originalImageHeight = decoder.height
                var cropRect = calcCropRect(originalImageWidth, originalImageHeight)
                if (mAngle != 0f) {
                    val matrix = Matrix()
                    matrix.setRotate(-mAngle)
                    val rotated = RectF()
                    matrix.mapRect(rotated, RectF(cropRect))
                    rotated.offset(
                        (if (rotated.left < 0) originalImageWidth else 0).toFloat(),
                        (if (rotated.top < 0) originalImageHeight else 0).toFloat()
                    )
                    cropRect = Rect(
                        rotated.left.toInt(), rotated.top.toInt(), rotated.right.toInt(),
                        rotated.bottom.toInt()
                    )
                }
                cropped = decoder.decodeRegion(cropRect, BitmapFactory.Options())
                if (mAngle != 0f) {
                    val rotated = getRotatedBitmap(cropped)
                    if (cropped != bitmap && cropped != rotated) {
                        cropped.recycle()
                    }
                    cropped = rotated
                }
            } finally {
                Utils.closeQuietly(`is`)
            }
            return cropped
        }

    private fun calcCropRect(originalImageWidth: Int, originalImageHeight: Int): Rect {
        val scaleToOriginal =
            getRotatedWidth(
                mAngle,
                originalImageWidth.toFloat(),
                originalImageHeight.toFloat()
            ) / mImageRect!!.width()
        val offsetX = mImageRect!!.left * scaleToOriginal
        val offsetY = mImageRect!!.top * scaleToOriginal
        val left = Math.round(mFrameRect!!.left * scaleToOriginal - offsetX)
        val top = Math.round(mFrameRect!!.top * scaleToOriginal - offsetY)
        val right = Math.round(mFrameRect!!.right * scaleToOriginal - offsetX)
        val bottom = Math.round(mFrameRect!!.bottom * scaleToOriginal - offsetY)
        val imageW = Math.round(
            getRotatedWidth(
                mAngle,
                originalImageWidth.toFloat(),
                originalImageHeight.toFloat()
            )
        )
        val imageH = Math.round(
            getRotatedHeight(
                mAngle,
                originalImageWidth.toFloat(),
                originalImageHeight.toFloat()
            )
        )
        return Rect(
            max(left.toDouble(), 0.0).toInt(), max(top.toDouble(), 0.0)
                .toInt(), min(right.toDouble(), imageW.toDouble()).toInt(),
            min(bottom.toDouble(), imageH.toDouble()).toInt()
        )
    }

    private fun scaleBitmapIfNeeded(cropped: Bitmap?): Bitmap? {
        var cropped = cropped
        val width = cropped!!.width
        val height = cropped.height
        var outWidth = 0
        var outHeight = 0
        val imageRatio = getRatioX(mFrameRect!!.width()) / getRatioY(mFrameRect!!.height())

        if (mOutputWidth > 0) {
            outWidth = mOutputWidth
            outHeight = Math.round(mOutputWidth / imageRatio)
        } else if (mOutputHeight > 0) {
            outHeight = mOutputHeight
            outWidth = Math.round(mOutputHeight * imageRatio)
        } else {
            if (mOutputMaxWidth > 0 && mOutputMaxHeight > 0 && (width > mOutputMaxWidth
                        || height > mOutputMaxHeight)
            ) {
                val maxRatio = mOutputMaxWidth.toFloat() / mOutputMaxHeight.toFloat()
                if (maxRatio >= imageRatio) {
                    outHeight = mOutputMaxHeight
                    outWidth = Math.round(mOutputMaxHeight.toFloat() * imageRatio)
                } else {
                    outWidth = mOutputMaxWidth
                    outHeight = Math.round(mOutputMaxWidth.toFloat() / imageRatio)
                }
            }
        }

        if (outWidth > 0 && outHeight > 0) {
            val scaled = Utils.getScaledBitmap(cropped, outWidth, outHeight)
            if (cropped != bitmap && cropped != scaled) {
                cropped.recycle()
            }
            cropped = scaled
        }
        return cropped
    }

    // File save ///////////////////////////////////////////////////////////////////////////////////
    @Throws(IOException::class, IllegalStateException::class)
    private fun saveImage(bitmap: Bitmap?, uri: Uri): Uri {
        saveUri = uri
        checkNotNull(saveUri) { "Save uri must not be null." }

        var outputStream: OutputStream? = null
        try {
            outputStream = context.contentResolver.openOutputStream(uri)
            bitmap!!.compress(mCompressFormat!!, mCompressQuality, outputStream!!)
            Utils.copyExifInfo(context, sourceUri, uri, bitmap.width, bitmap.height)
            Utils.updateGalleryInfo(context, uri)
            return uri
        } finally {
            Utils.closeQuietly(outputStream)
        }
    }

    // Public methods //////////////////////////////////////////////////////////////////////////////
    /**
     * Get source image bitmap
     *
     * @return src bitmap
     */
    fun getImageBitmap(): Bitmap? {
        return bitmap
    }

    /**
     * Set source image bitmap
     *
     * @param bitmap src image bitmap
     */
    override fun setImageBitmap(bitmap: Bitmap) {
        super.setImageBitmap(bitmap) // calls setImageDrawable internally
    }

    /**
     * Set source image resource id
     *
     * @param resId source image resource id
     */
    override fun setImageResource(resId: Int) {
        mIsInitialized = false
        resetImageInfo()
        super.setImageResource(resId)
        updateLayout()
    }

    /**
     * Set image drawable.
     *
     * @param drawable source image drawable
     */
    override fun setImageDrawable(drawable: Drawable?) {
        mIsInitialized = false
        resetImageInfo()
        setImageDrawableInternal(drawable)
    }

    private fun setImageDrawableInternal(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        updateLayout()
    }

    /**
     * Set image uri
     *
     * @param uri source image local uri
     */
    override fun setImageURI(uri: Uri?) {
        mIsInitialized = false
        super.setImageURI(uri)
        updateLayout()
    }

    private fun updateLayout() {
        val d = drawable
        if (d != null) {
            setupLayout(mViewWidth, mViewHeight)
        }
    }

    private fun resetImageInfo() {
        if (mIsLoading.get()) return
        sourceUri = null
        saveUri = null
        mInputImageWidth = 0
        mInputImageHeight = 0
        mOutputImageWidth = 0
        mOutputImageHeight = 0
        mAngle = mExifRotation.toFloat()
    }

    /**
     * Load image from Uri.
     * This method is deprecated. Use loadAsync(Uri, LoadCallback) instead.
     *
     * @param sourceUri Image Uri
     * @param callback Callback
     *
     * @see .loadAsync
     */
    fun startLoad(sourceUri: Uri?, callback: LoadCallback?) {
        loadAsync(sourceUri, callback)
    }

    /**
     * Load image from Uri.
     *
     * @param sourceUri Image Uri
     * @param callback Callback
     *
     * @see .loadAsync
     */
    fun loadAsync(sourceUri: Uri?, callback: LoadCallback?) {
        loadAsync(sourceUri, false, null, callback)
    }

    /**
     * Load image from Uri.
     *
     * @param sourceUri Image Uri
     * @param callback Callback
     *
     * @see .load
     * @see .loadAsCompletable
     */
    fun loadAsync(
        sourceUri: Uri?, useThumbnail: Boolean,
        initialFrameRect: RectF?, callback: LoadCallback?
    ) {
        mExecutor.submit(object : Runnable {
            override fun run() {
                try {
                    mIsLoading.set(true)

                    this@CropImageView.sourceUri = sourceUri
                    mInitialFrameRect = initialFrameRect

                    if (useThumbnail) {
                        applyThumbnail(sourceUri)
                    }

                    val sampled = getImage(sourceUri)

                    mHandler.post {
                        mAngle = mExifRotation.toFloat()
                        setImageDrawableInternal(BitmapDrawable(resources, sampled))
                        callback?.onSuccess()
                    }
                } catch (e: Exception) {
                    postErrorOnMainThread(callback, e)
                } finally {
                    mIsLoading.set(false)
                }
            }
        })
    }

    /**
     * Load image from Uri with RxJava2
     *
     * @param sourceUri Image Uri
     *
     * @see .load
     * @return Completable of loading image
     */
    /**
     * Load image from Uri with RxJava2
     *
     * @param sourceUri Image Uri
     *
     * @see .load
     */
    @JvmOverloads
    fun loadAsCompletable(
        sourceUri: Uri?, useThumbnail: Boolean = false,
        initialFrameRect: RectF? = null
    ): Completable {
        return Completable.create(object : CompletableOnSubscribe {
            @Throws(Exception::class)
            override fun subscribe(emitter: CompletableEmitter) {
                mInitialFrameRect = initialFrameRect
                this@CropImageView.sourceUri = sourceUri

                if (useThumbnail) {
                    applyThumbnail(sourceUri)
                }

                val sampled = getImage(sourceUri)

                mHandler.post {
                    mAngle = mExifRotation.toFloat()
                    setImageDrawableInternal(BitmapDrawable(resources, sampled))
                    emitter.onComplete()
                }
            }
        }).doOnSubscribe { mIsLoading.set(true) }
            .doFinally { mIsLoading.set(false) }
    }

    /**
     * Load image from Uri with Builder Pattern
     *
     * @param sourceUri Image Uri
     *
     * @return Builder
     */
    fun load(sourceUri: Uri?): LoadRequest {
        return LoadRequest(this, sourceUri)
    }

    private fun applyThumbnail(sourceUri: Uri?) {
        val thumb = getThumbnail(sourceUri) ?: return
        mHandler.post {
            mAngle = mExifRotation.toFloat()
            setImageDrawableInternal(BitmapDrawable(resources, thumb))
        }
    }

    private fun getImage(sourceUri: Uri?): Bitmap? {
        checkNotNull(sourceUri) { "Source Uri must not be null." }

        mExifRotation = Utils.getExifOrientation(context, this.sourceUri)
        val maxSize = Utils.maxSize
        var requestSize = max(mViewWidth.toDouble(), mViewHeight.toDouble())
            .toInt()
        if (requestSize == 0) requestSize = maxSize

        val sampledBitmap =
            Utils.decodeSampledBitmapFromUri(context, this.sourceUri, requestSize)
        mInputImageWidth = Utils.sInputImageWidth
        mInputImageHeight = Utils.sInputImageHeight
        return sampledBitmap
    }

    private fun getThumbnail(sourceUri: Uri?): Bitmap? {
        checkNotNull(sourceUri) { "Source Uri must not be null." }

        mExifRotation = Utils.getExifOrientation(context, this.sourceUri)
        val requestSize = (max(mViewWidth.toDouble(), mViewHeight.toDouble()) * 0.1f).toInt()
        if (requestSize == 0) return null

        val sampledBitmap =
            Utils.decodeSampledBitmapFromUri(context, this.sourceUri, requestSize)
        mInputImageWidth = Utils.sInputImageWidth
        mInputImageHeight = Utils.sInputImageHeight
        return sampledBitmap
    }

    /**
     * Rotate image
     *
     * @param degrees rotation angle
     * @param durationMillis animation duration in milliseconds
     */
    /**
     * Rotate image
     *
     * @param degrees rotation angle
     */
    @JvmOverloads
    fun rotateImage(degrees: RotateDegrees, durationMillis: Int = mAnimationDurationMillis) {
        if (mIsRotating) {
            animator!!.cancelAnimation()
        }

        val currentAngle = mAngle
        val newAngle = (mAngle + degrees.value)
        val angleDiff = newAngle - currentAngle
        val currentScale = mScale
        val newScale = calcScale(mViewWidth, mViewHeight, newAngle)

        if (mIsAnimationEnabled) {
            val scaleDiff = newScale - currentScale
            val animator = animator
            animator!!.addAnimatorListener(object : SimpleValueAnimatorListener {
                override fun onAnimationStarted() {
                    mIsRotating = true
                }

                override fun onAnimationUpdated(scale: Float) {
                    mAngle = currentAngle + angleDiff * scale
                    mScale = currentScale + scaleDiff * scale
                    setMatrix()
                    invalidate()
                }

                override fun onAnimationFinished() {
                    mAngle = newAngle % 360
                    mScale = newScale
                    mInitialFrameRect = null
                    setupLayout(mViewWidth, mViewHeight)
                    mIsRotating = false
                }
            })
            animator.startAnimation(durationMillis.toLong())
        } else {
            mAngle = newAngle % 360
            mScale = newScale
            setupLayout(mViewWidth, mViewHeight)
        }
    }

    val croppedBitmap: Bitmap?
        /**
         * Get cropped image bitmap
         *
         * @return cropped image bitmap
         */
        get() {
            val source = bitmap ?: return null

            val rotated = getRotatedBitmap(source)
            val cropRect = calcCropRect(source.width, source.height)
            var cropped: Bitmap? = Bitmap.createBitmap(
                rotated, cropRect.left, cropRect.top, cropRect.width(),
                cropRect.height(), null, false
            )
            if (rotated != cropped && rotated != source) {
                rotated.recycle()
            }

            if (mCropMode == CropMode.CIRCLE) {
                val circle = getCircularBitmap(cropped)
                if (cropped != bitmap) {
                    cropped!!.recycle()
                }
                cropped = circle
            }
            return cropped
        }

    /**
     * Crop the square image in a circular
     *
     * @param square image bitmap
     * @return circular image bitmap
     */
    fun getCircularBitmap(square: Bitmap?): Bitmap? {
        if (square == null) return null
        val output =
            Bitmap.createBitmap(square.width, square.height, Bitmap.Config.ARGB_8888)

        val rect = Rect(0, 0, square.width, square.height)
        val canvas = Canvas(output)

        val halfWidth = square.width / 2
        val halfHeight = square.height / 2

        val paint = Paint()
        paint.isAntiAlias = true
        paint.isFilterBitmap = true

        canvas.drawCircle(
            halfWidth.toFloat(),
            halfHeight.toFloat(),
            min(halfWidth.toDouble(), halfHeight.toDouble())
                .toFloat(),
            paint
        )
        paint.setXfermode(PorterDuffXfermode(PorterDuff.Mode.SRC_IN))
        canvas.drawBitmap(square, rect, rect, paint)
        return output
    }

    /**
     * Crop image
     * This method is separated to #crop(Uri) and #save(Bitmap)
     * Use #crop(Uri) and #save(Bitmap)
     *
     * @param saveUri Uri for saving the cropped image
     * @param cropCallback Callback for cropping the image
     * @param saveCallback Callback for saving the image
     *
     * @see .crop
     * @see .save
     */
    fun startCrop(
        saveUri: Uri, cropCallback: CropCallback?,
        saveCallback: SaveCallback?
    ) {
        mExecutor.submit {
            var croppedImage: Bitmap? = null
            try {
                mIsCropping.set(true)

                croppedImage = cropImage()

                val cropped = croppedImage
                mHandler.post {
                    cropCallback?.onSuccess(cropped)
                    if (mIsDebug) invalidate()
                }

                saveImage(croppedImage, saveUri)

                mHandler.post { saveCallback?.onSuccess(saveUri) }
            } catch (e: Exception) {
                if (croppedImage == null) {
                    postErrorOnMainThread(cropCallback, e)
                } else {
                    postErrorOnMainThread(saveCallback, e)
                }
            } finally {
                mIsCropping.set(false)
            }
        }
    }

    /**
     * Crop image
     *
     * @param sourceUri Uri for cropping(If null, the Uri set in loadAsync() is used)
     * @param cropCallback Callback for cropping the image
     *
     * @see .crop
     * @see .cropAsSingle
     */
    fun cropAsync(sourceUri: Uri?, cropCallback: CropCallback?) {
        mExecutor.submit(object : Runnable {
            override fun run() {
                try {
                    mIsCropping.set(true)

                    if (sourceUri != null) this@CropImageView.sourceUri = sourceUri

                    val cropped = cropImage()

                    mHandler.post {
                        cropCallback?.onSuccess(cropped)
                        if (mIsDebug) invalidate()
                    }
                } catch (e: Exception) {
                    postErrorOnMainThread(cropCallback, e)
                } finally {
                    mIsCropping.set(false)
                }
            }
        })
    }

    fun cropAsync(cropCallback: CropCallback?) {
        cropAsync(null, cropCallback)
    }

    /**
     * Crop image with RxJava2
     *
     * @param sourceUri Uri for cropping(If null, the Uri set in loadAsSingle() is used)
     *
     * @return Single of cropping image
     */
    @JvmOverloads
    fun cropAsSingle(sourceUri: Uri? = null): Single<Bitmap> {
        return Single.fromCallable<Bitmap>(object : Callable<Bitmap?> {
            @Throws(Exception::class)
            override fun call(): Bitmap? {
                if (sourceUri != null) this@CropImageView.sourceUri = sourceUri
                return cropImage()
            }
        }).doOnSubscribe { mIsCropping.set(true) }
            .doFinally { mIsCropping.set(false) }
    }

    /**
     * Crop image with Builder Pattern
     *
     * @param sourceUri Uri for cropping(If null, the Uri set in loadAsSingle() is used)
     *
     * @return Builder
     */
    fun crop(sourceUri: Uri?): CropRequest {
        return CropRequest(this, sourceUri)
    }

    /**
     * Save image
     *
     * @param saveUri Uri for saving the cropped image
     * @param image Image for saving
     * @param saveCallback Callback for saving the image
     */
    fun saveAsync(saveUri: Uri, image: Bitmap?, saveCallback: SaveCallback?) {
        mExecutor.submit {
            try {
                mIsSaving.set(true)
                saveImage(image, saveUri)

                mHandler.post { saveCallback?.onSuccess(saveUri) }
            } catch (e: Exception) {
                postErrorOnMainThread(saveCallback, e)
            } finally {
                mIsSaving.set(false)
            }
        }
    }

    /**
     * Save image with RxJava2
     *
     * @param bitmap Bitmap for saving
     * @param saveUri Uri for saving the cropped image
     *
     * @return Single of saving image
     */
    fun saveAsSingle(bitmap: Bitmap?, saveUri: Uri): Single<Uri> {
        return Single.fromCallable { saveImage(bitmap, saveUri) }
            .doOnSubscribe { mIsSaving.set(true) }
            .doFinally { mIsSaving.set(false) }
    }

    /**
     * Save image with Builder Pattern
     *
     * @param bitmap image for saving
     *
     * @return Builder
     */
    fun save(bitmap: Bitmap?): SaveRequest {
        return SaveRequest(this, bitmap)
    }

    @Throws(IOException::class, IllegalStateException::class)
    private fun cropImage(): Bitmap? {
        var cropped: Bitmap?

        // Use thumbnail for getCroppedBitmap
        if (sourceUri == null) {
            cropped = croppedBitmap
        } else {
            cropped = croppedBitmapFromUri
            if (mCropMode == CropMode.CIRCLE) {
                val circle = getCircularBitmap(cropped)
                if (cropped != bitmap) {
                    cropped!!.recycle()
                }
                cropped = circle
            }
        }

        cropped = scaleBitmapIfNeeded(cropped)

        mOutputImageWidth = cropped!!.width
        mOutputImageHeight = cropped.height

        return cropped
    }

    val actualCropRect: RectF?
        /**
         * Get frame position relative to the source bitmap.
         * @see .load
         * @see .loadAsync
         * @see .loadAsCompletable
         * @return getCroppedBitmap area boundaries.
         */
        get() {
            if (mImageRect == null) return null
            val offsetX = (mImageRect!!.left / mScale)
            val offsetY = (mImageRect!!.top / mScale)
            var l = (mFrameRect!!.left / mScale) - offsetX
            var t = (mFrameRect!!.top / mScale) - offsetY
            var r = (mFrameRect!!.right / mScale) - offsetX
            var b = (mFrameRect!!.bottom / mScale) - offsetY
            l = max(0.0, l.toDouble()).toFloat()
            t = max(0.0, t.toDouble()).toFloat()
            r = min((mImageRect!!.right / mScale).toDouble(), r.toDouble()).toFloat()
            b = min((mImageRect!!.bottom / mScale).toDouble(), b.toDouble()).toFloat()
            return RectF(l, t, r, b)
        }

    private fun applyInitialFrameRect(initialFrameRect: RectF): RectF {
        val frameRect = RectF()
        frameRect[initialFrameRect.left * mScale, initialFrameRect.top * mScale, initialFrameRect.right * mScale] =
            initialFrameRect.bottom * mScale
        frameRect.offset(mImageRect!!.left, mImageRect!!.top)
        val l = max(mImageRect!!.left.toDouble(), frameRect.left.toDouble())
            .toFloat()
        val t = max(mImageRect!!.top.toDouble(), frameRect.top.toDouble())
            .toFloat()
        val r = min(mImageRect!!.right.toDouble(), frameRect.right.toDouble())
            .toFloat()
        val b = min(mImageRect!!.bottom.toDouble(), frameRect.bottom.toDouble())
            .toFloat()
        frameRect[l, t, r] = b
        return frameRect
    }

    /**
     * Set getCroppedBitmap mode
     *
     * @param mode getCroppedBitmap mode
     * @param durationMillis animation duration in milliseconds
     */
    fun setCropMode(mode: CropMode, durationMillis: Int) {
        if (mode == CropMode.CUSTOM) {
            setCustomRatio(1, 1)
        } else {
            mCropMode = mode
            recalculateFrameRect(durationMillis)
        }
    }

    /**
     * Set getCroppedBitmap mode
     *
     * @param mode getCroppedBitmap mode
     */
    fun setCropMode(mode: CropMode) {
        setCropMode(mode, mAnimationDurationMillis)
    }

    /**
     * Set custom aspect ratio to getCroppedBitmap frame
     *
     * @param ratioX ratio x
     * @param ratioY ratio y
     * @param durationMillis animation duration in milliseconds
     */
    fun setCustomRatio(ratioX: Int, ratioY: Int, durationMillis: Int) {
        if (ratioX == 0 || ratioY == 0) return
        mCropMode = CropMode.CUSTOM
        mCustomRatio = PointF(ratioX.toFloat(), ratioY.toFloat())
        recalculateFrameRect(durationMillis)
    }

    /**
     * Set custom aspect ratio to getCroppedBitmap frame
     *
     * @param ratioX ratio x
     * @param ratioY ratio y
     */
    fun setCustomRatio(ratioX: Int, ratioY: Int) {
        setCustomRatio(ratioX, ratioY, mAnimationDurationMillis)
    }

    /**
     * Set image overlay color
     *
     * @param overlayColor color resId or color int(ex. 0xFFFFFFFF)
     */
    fun setOverlayColor(overlayColor: Int) {
        this.mOverlayColor = overlayColor
        invalidate()
    }

    /**
     * Set getCroppedBitmap frame color
     *
     * @param frameColor color resId or color int(ex. 0xFFFFFFFF)
     */
    fun setFrameColor(frameColor: Int) {
        this.mFrameColor = frameColor
        invalidate()
    }

    /**
     * Set handle color
     *
     * @param handleColor color resId or color int(ex. 0xFFFFFFFF)
     */
    fun setHandleColor(handleColor: Int) {
        this.mHandleColor = handleColor
        invalidate()
    }

    /**
     * Set guide color
     *
     * @param guideColor color resId or color int(ex. 0xFFFFFFFF)
     */
    fun setGuideColor(guideColor: Int) {
        this.mGuideColor = guideColor
        invalidate()
    }

    /**
     * Set view background color
     *
     * @param bgColor color resId or color int(ex. 0xFFFFFFFF)
     */
    override fun setBackgroundColor(bgColor: Int) {
        this.mBackgroundColor = bgColor
        invalidate()
    }

    /**
     * Set getCroppedBitmap frame minimum size in density-independent pixels.
     *
     * @param minDp getCroppedBitmap frame minimum size in density-independent pixels
     */
    fun setMinFrameSizeInDp(minDp: Int) {
        mMinFrameSize = minDp * density
    }

    /**
     * Set getCroppedBitmap frame minimum size in pixels.
     *
     * @param minPx getCroppedBitmap frame minimum size in pixels
     */
    fun setMinFrameSizeInPx(minPx: Int) {
        mMinFrameSize = minPx.toFloat()
    }

    /**
     * Set handle radius in density-independent pixels.
     *
     * @param handleDp handle radius in density-independent pixels
     */
    fun setHandleSizeInDp(handleDp: Int) {
        mHandleSize = (handleDp * density).toInt()
    }

    /**
     * Set getCroppedBitmap frame handle touch padding(touch area) in density-independent pixels.
     *
     * handle touch area : a circle of radius R.(R = handle size + touch padding)
     *
     * @param paddingDp getCroppedBitmap frame handle touch padding(touch area) in
     * density-independent
     * pixels
     */
    fun setTouchPaddingInDp(paddingDp: Int) {
        mTouchPadding = (paddingDp * density).toInt()
    }

    /**
     * Set guideline show mode.
     * (SHOW_ALWAYS/NOT_SHOW/SHOW_ON_TOUCH)
     *
     * @param mode guideline show mode
     */
    fun setGuideShowMode(mode: ShowMode?) {
        mGuideShowMode = mode
        when (mode) {
            ShowMode.SHOW_ALWAYS -> mShowGuide = true
            ShowMode.NOT_SHOW, ShowMode.SHOW_ON_TOUCH -> mShowGuide = false
            null -> mShowGuide = true
        }
        invalidate()
    }

    /**
     * Set handle show mode.
     * (SHOW_ALWAYS/NOT_SHOW/SHOW_ON_TOUCH)
     *
     * @param mode handle show mode
     */
    fun setHandleShowMode(mode: ShowMode?) {
        mHandleShowMode = mode
        when (mode) {
            ShowMode.SHOW_ALWAYS -> mShowHandle = true
            ShowMode.NOT_SHOW, ShowMode.SHOW_ON_TOUCH -> mShowHandle = false
            null ->     mShowHandle = true
        }
        invalidate()
    }

    /**
     * Set frame stroke weight in density-independent pixels.
     *
     * @param weightDp frame stroke weight in density-independent pixels.
     */
    fun setFrameStrokeWeightInDp(weightDp: Int) {
        mFrameStrokeWeight = weightDp * density
        invalidate()
    }

    /**
     * Set guideline stroke weight in density-independent pixels.
     *
     * @param weightDp guideline stroke weight in density-independent pixels.
     */
    fun setGuideStrokeWeightInDp(weightDp: Int) {
        mGuideStrokeWeight = weightDp * density
        invalidate()
    }

    /**
     * Set whether to show getCroppedBitmap frame.
     *
     * @param enabled should show getCroppedBitmap frame?
     */
    fun setCropEnabled(enabled: Boolean) {
        mIsCropEnabled = enabled
        invalidate()
    }

    /**
     * Set locking the getCroppedBitmap frame.
     *
     * @param enabled should lock getCroppedBitmap frame?
     */
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        mIsEnabled = enabled
    }

    /**
     * Set initial scale of the frame.(0.01 ~ 1.0)
     *
     * @param initialScale initial scale
     */
    fun setInitialFrameScale(initialScale: Float) {
        mInitialFrameScale = constrain(initialScale, 0.01f, 1.0f, DEFAULT_INITIAL_FRAME_SCALE)
    }

    /**
     * Set whether to animate
     *
     * @param enabled is animation enabled?
     */
    fun setAnimationEnabled(enabled: Boolean) {
        mIsAnimationEnabled = enabled
    }

    /**
     * Set duration of animation
     *
     * @param durationMillis animation duration in milliseconds
     */
    fun setAnimationDuration(durationMillis: Int) {
        mAnimationDurationMillis = durationMillis
    }

    /**
     * Set interpolator of animation
     * (Default interpolator is DecelerateInterpolator)
     *
     * @param interpolator interpolator used for animation
     */
    fun setInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
        mAnimator = null
        setupAnimatorIfNeeded()
    }

    /**
     * Set whether to show debug display
     *
     * @param debug is logging enabled
     */
    fun setDebug(debug: Boolean) {
        mIsDebug = debug
        Logger.enabled = true
        invalidate()
    }

    /**
     * Set whether to log exception
     *
     * @param enabled is logging enabled
     */
    fun setLoggingEnabled(enabled: Boolean) {
        Logger.enabled = enabled
    }

    /**
     * Set fixed width for output
     * (After cropping, the image is scaled to the specified size.)
     *
     * @param outputWidth output width
     */
    fun setOutputWidth(outputWidth: Int) {
        mOutputWidth = outputWidth
        mOutputHeight = 0
    }

    /**
     * Set fixed height for output
     * (After cropping, the image is scaled to the specified size.)
     *
     * @param outputHeight output height
     */
    fun setOutputHeight(outputHeight: Int) {
        mOutputHeight = outputHeight
        mOutputWidth = 0
    }

    /**
     * Set maximum size for output
     * (If cropped image size is larger than max size, the image is scaled to the smaller size.
     * If fixed output width/height has already set, these parameters are ignored.)
     *
     * @param maxWidth max output width
     * @param maxHeight max output height
     */
    fun setOutputMaxSize(maxWidth: Int, maxHeight: Int) {
        mOutputMaxWidth = maxWidth
        mOutputMaxHeight = maxHeight
    }

    /**
     * Set compress format for output
     *
     * @param format compress format[Bitmap.CompressFormat]
     */
    fun setCompressFormat(format: CompressFormat?) {
        mCompressFormat = format
    }

    /**
     * Set compress quality for output
     *
     * @param quality compress quality(0-100: 100 is default.)
     */
    fun setCompressQuality(quality: Int) {
        mCompressQuality = quality
    }

    /**
     * Set whether to show handle shadows
     *
     * @param handleShadowEnabled should show handle shadows?
     */
    fun setHandleShadowEnabled(handleShadowEnabled: Boolean) {
        mIsHandleShadowEnabled = handleShadowEnabled
    }

    val isCropping: Boolean
        /**
         * cropping status
         *
         * @return is cropping process running
         */
        get() = mIsCropping.get()

    val isSaving: Boolean
        /**
         * saving status
         *
         * @return is saving process running
         */
        get() = mIsSaving.get()

    private fun setScale(mScale: Float) {
        this.mScale = mScale
    }

    private fun setCenter(mCenter: PointF) {
        this.mCenter = mCenter
    }

    private val frameW: Float
        get() = (mFrameRect!!.right - mFrameRect!!.left)

    private val frameH: Float
        get() = (mFrameRect!!.bottom - mFrameRect!!.top)

    // Enum ////////////////////////////////////////////////////////////////////////////////////////
    private enum class TouchArea {
        OUT_OF_BOUNDS, CENTER, LEFT_TOP, RIGHT_TOP, LEFT_BOTTOM, RIGHT_BOTTOM
    }

    enum class CropMode(val id: Int) {
        FIT_IMAGE(0), RATIO_4_3(1), RATIO_3_4(2), SQUARE(3), RATIO_16_9(4), RATIO_9_16(5), FREE(
            6
        ),
        CUSTOM(7), CIRCLE(8), CIRCLE_SQUARE(9)
    }

    enum class ShowMode(val id: Int) {
        SHOW_ALWAYS(1), SHOW_ON_TOUCH(2), NOT_SHOW(3)
    }

    enum class RotateDegrees(val value: Int) {
        ROTATE_90D(90), ROTATE_180D(180), ROTATE_270D(270), ROTATE_M90D(-90), ROTATE_M180D(
            -180
        ),
        ROTATE_M270D(-270)
    }

    // Save/Restore support ////////////////////////////////////////////////////////////////////////
    class SavedState : BaseSavedState {
        var mode: CropMode? = null
        var backgroundColor: Int = 0
        var overlayColor: Int = 0
        var frameColor: Int = 0
        var guideShowMode: ShowMode? = null
        var handleShowMode: ShowMode? = null
        var showGuide: Boolean = false
        var showHandle: Boolean = false
        var handleSize: Int = 0
        var touchPadding: Int = 0
        var minFrameSize: Float = 0f
        var customRatioX: Float = 0f
        var customRatioY: Float = 0f
        var frameStrokeWeight: Float = 0f
        var guideStrokeWeight: Float = 0f
        var isCropEnabled: Boolean = false
        var handleColor: Int = 0
        var guideColor: Int = 0
        var initialFrameScale: Float = 0f
        var angle: Float = 0f
        var isAnimationEnabled: Boolean = false
        var animationDuration: Int = 0
        var exifRotation: Int = 0
        var sourceUri: Uri? = null
        var saveUri: Uri? = null
        var compressFormat: CompressFormat? = null
        var compressQuality: Int = 0
        var isDebug: Boolean = false
        var outputMaxWidth: Int = 0
        var outputMaxHeight: Int = 0
        var outputWidth: Int = 0
        var outputHeight: Int = 0
        var isHandleShadowEnabled: Boolean = false
        var inputImageWidth: Int = 0
        var inputImageHeight: Int = 0
        var outputImageWidth: Int = 0
        var outputImageHeight: Int = 0

        internal constructor(superState: Parcelable?) : super(superState)

        private constructor(`in`: Parcel) : super(`in`) {
            mode = `in`.readSerializable() as CropMode?
            backgroundColor = `in`.readInt()
            overlayColor = `in`.readInt()
            frameColor = `in`.readInt()
            guideShowMode = `in`.readSerializable() as ShowMode?
            handleShowMode = `in`.readSerializable() as ShowMode?
            showGuide = (`in`.readInt() != 0)
            showHandle = (`in`.readInt() != 0)
            handleSize = `in`.readInt()
            touchPadding = `in`.readInt()
            minFrameSize = `in`.readFloat()
            customRatioX = `in`.readFloat()
            customRatioY = `in`.readFloat()
            frameStrokeWeight = `in`.readFloat()
            guideStrokeWeight = `in`.readFloat()
            isCropEnabled = (`in`.readInt() != 0)
            handleColor = `in`.readInt()
            guideColor = `in`.readInt()
            initialFrameScale = `in`.readFloat()
            angle = `in`.readFloat()
            isAnimationEnabled = (`in`.readInt() != 0)
            animationDuration = `in`.readInt()
            exifRotation = `in`.readInt()
            sourceUri = `in`.readParcelable(Uri::class.java.classLoader)
            saveUri = `in`.readParcelable(Uri::class.java.classLoader)
            compressFormat = `in`.readSerializable() as CompressFormat?
            compressQuality = `in`.readInt()
            isDebug = (`in`.readInt() != 0)
            outputMaxWidth = `in`.readInt()
            outputMaxHeight = `in`.readInt()
            outputWidth = `in`.readInt()
            outputHeight = `in`.readInt()
            isHandleShadowEnabled = (`in`.readInt() != 0)
            inputImageWidth = `in`.readInt()
            inputImageHeight = `in`.readInt()
            outputImageWidth = `in`.readInt()
            outputImageHeight = `in`.readInt()
        }

        override fun writeToParcel(out: Parcel, flag: Int) {
            super.writeToParcel(out, flag)
            out.writeSerializable(mode)
            out.writeInt(backgroundColor)
            out.writeInt(overlayColor)
            out.writeInt(frameColor)
            out.writeSerializable(guideShowMode)
            out.writeSerializable(handleShowMode)
            out.writeInt(if (showGuide) 1 else 0)
            out.writeInt(if (showHandle) 1 else 0)
            out.writeInt(handleSize)
            out.writeInt(touchPadding)
            out.writeFloat(minFrameSize)
            out.writeFloat(customRatioX)
            out.writeFloat(customRatioY)
            out.writeFloat(frameStrokeWeight)
            out.writeFloat(guideStrokeWeight)
            out.writeInt(if (isCropEnabled) 1 else 0)
            out.writeInt(handleColor)
            out.writeInt(guideColor)
            out.writeFloat(initialFrameScale)
            out.writeFloat(angle)
            out.writeInt(if (isAnimationEnabled) 1 else 0)
            out.writeInt(animationDuration)
            out.writeInt(exifRotation)
            out.writeParcelable(sourceUri, flag)
            out.writeParcelable(saveUri, flag)
            out.writeSerializable(compressFormat)
            out.writeInt(compressQuality)
            out.writeInt(if (isDebug) 1 else 0)
            out.writeInt(outputMaxWidth)
            out.writeInt(outputMaxHeight)
            out.writeInt(outputWidth)
            out.writeInt(outputHeight)
            out.writeInt(if (isHandleShadowEnabled) 1 else 0)
            out.writeInt(inputImageWidth)
            out.writeInt(inputImageHeight)
            out.writeInt(outputImageWidth)
            out.writeInt(outputImageHeight)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator<Any?> {
                override fun createFromParcel(inParcel: Parcel): SavedState? {
                    return SavedState(inParcel)
                }

                override fun newArray(inSize: Int): Array<SavedState?> {
                    return arrayOfNulls(inSize)
                }
            }
        }
    }

    companion object {
        private val TAG: String = CropImageView::class.java.simpleName

        // Constants ///////////////////////////////////////////////////////////////////////////////////
        private const val HANDLE_SIZE_IN_DP = 14
        private const val MIN_FRAME_SIZE_IN_DP = 50
        private const val FRAME_STROKE_WEIGHT_IN_DP = 1
        private const val GUIDE_STROKE_WEIGHT_IN_DP = 1
        private const val DEFAULT_INITIAL_FRAME_SCALE = 1f
        private const val DEFAULT_ANIMATION_DURATION_MILLIS = 100
        private const val DEBUG_TEXT_SIZE_IN_DP = 15

        private const val TRANSPARENT = 0x00000000
        private const val TRANSLUCENT_WHITE = -0x44000001
        private const val WHITE = -0x1
        private const val TRANSLUCENT_BLACK = -0x45000000
    }
}
