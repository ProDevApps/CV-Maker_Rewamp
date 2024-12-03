package com.professorapps.cvmaker.dialogs

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.interfaces.OnBackPressListener

class TedAdmobDialog(private val builder: Builder, i: Int) : AlertDialog(
    builder.context, i
) {
    private var bannerContainer: LinearLayout? = null
    private var progressView: ProgressBar? = null

    @Retention(AnnotationRetention.SOURCE)
    annotation class AdType {
        companion object {
            const val BANNER: Int = 2
            const val NATIVE: Int = 1
        }
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        Log.d("ted", "onCreate()")
        setContentView(R.layout.dialog_tedadmob)
        val layoutParams = WindowManager.LayoutParams()
        val window = window
        layoutParams.copyFrom(window!!.attributes)
        layoutParams.width = -1
        layoutParams.height = -2
        window.attributes = layoutParams
        setCanceledOnTouchOutside(false)
        initView()
        setOnShowListener {
            progressView!!.visibility = View.GONE
            bannerContainer!!.visibility = View.GONE
            bannerContainer!!.removeAllViews()
            val i = builder.adType
        }
    }

    private fun initView() {
        this.progressView = findViewById<View>(R.id.progressView) as ProgressBar?
        this.bannerContainer = findViewById<View>(R.id.view_banner_container) as LinearLayout?
        findViewById<View>(R.id.tv_finish)!!.setOnClickListener { this@TedAdmobDialog.onFinishClick() }
        val textView = findViewById<View>(R.id.tv_review) as TextView?
        val findViewById = findViewById<View>(R.id.view_btn_divider)
        if (builder.showReviewButton) {
            textView!!.setOnClickListener { this@TedAdmobDialog.onReviewClick() }
            return
        }
        textView!!.visibility = View.GONE
        findViewById!!.visibility = View.GONE
    }


    fun onFinishClick() {
        if (builder.onBackPressListener != null) {
            builder.onBackPressListener!!.onFinish()
        }
        dismiss()
    }


    fun onReviewClick() {
        openPlayStore()
        if (builder.onBackPressListener != null) {
            builder.onBackPressListener!!.onReviewClick()
        }
    }

    private fun openPlayStore() {
        val packageName = context.packageName
        try {
            val context = context
            context.startActivity(
                Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("market://details?id=$packageName")
                )
            )
        } catch (unused: ActivityNotFoundException) {
            val context2 = context
            context2.startActivity(
                Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }


    class Builder(internal val context: Context, val adType: Int, private val unitId: String) {
        var onBackPressListener: OnBackPressListener? = null
        private var startMute = true
        var showReviewButton: Boolean = true

        fun setStartMute(z: Boolean): Builder {
            this.startMute = z
            return this
        }


        fun setOnBackPressListener(onBackPressListener: OnBackPressListener?): Builder {
            this.onBackPressListener = onBackPressListener
            return this
        }

        fun showReviewButton(z: Boolean): Builder {
            this.showReviewButton = z
            return this
        }

        fun create(): TedAdmobDialog {
            return TedAdmobDialog(this, R.style.TedAdmobDialog)
        }
    }

    companion object {
        private const val TAG = "ted>>"
    }
}
