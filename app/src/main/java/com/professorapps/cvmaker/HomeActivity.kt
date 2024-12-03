package com.professorapps.cvmaker

import android.app.Activity
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.Dialog
import android.app.PendingIntent
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.professorapps.cvmaker.dialogs.TedAdmobDialog
import java.util.Calendar

class HomeActivity : AppCompatActivity() {
    var TIME: Long = 1000
    var imgcreatenew: ImageView? = null
    var imgprivacypolicy: ImageView? = null
    var imgrateapp: ImageView? = null
    var imgshareapp: ImageView? = null
    var imgtips: ImageView? = null
    var imgviewcreated: ImageView? = null
    var imgviewpdf: ImageView? = null
    var nativeTedAdmobDialog: TedAdmobDialog? = null


    fun log(str: String?) {
        Log.d(TAG, str!!)
    }

    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_home)


        startAlarmBroadcastReceiver(this)
        val window = window
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.grey)
        }
        this.imgcreatenew = findViewById<View>(R.id.imgcreatenew) as ImageView
        this.imgviewcreated = findViewById<View>(R.id.imgviewcreated) as ImageView
        this.imgviewpdf = findViewById<View>(R.id.imgviewpdf) as ImageView
        this.imgrateapp = findViewById<View>(R.id.imgrateapp) as ImageView
        this.imgtips = findViewById<View>(R.id.imgtips) as ImageView
        this.imgshareapp = findViewById<View>(R.id.imgshareapp) as ImageView
        this.imgprivacypolicy = findViewById<View>(R.id.imgprivacypolicy) as ImageView
        val loadAnimation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.unclick_btn_zoom)
        imgcreatenew!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@HomeActivity.startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            // HomeActivity.this.requestMultiplePermissions();
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@HomeActivity.TIME)
        }
        imgviewcreated!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@HomeActivity.startActivity(
                Intent(
                    this@HomeActivity,
                    MyCreationActivity::class.java
                )
            )
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@HomeActivity.TIME)
        }
        imgrateapp!!.setOnClickListener { this@HomeActivity.rateApp() }
        imgtips!!.setOnClickListener {
            this@HomeActivity.startActivity(
                Intent(
                    this@HomeActivity,
                    InterviewTipsActivity::class.java
                )
            )
        }

        imgshareapp!!.setOnClickListener { this@HomeActivity.shareApp() }
        imgprivacypolicy!!.setOnClickListener {
            this@HomeActivity.startActivity(
                Intent(
                    this@HomeActivity,
                    PrivacyActivity::class.java
                )
            )
        }
    }


    fun feedbackApp() {
        val intent = Intent("android.intent.action.SEND")
        intent.setType("message/rfc822")
        intent.putExtra("android.intent.extra.EMAIL", arrayOf("support@virtueinfotech.com"))
        intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name))
        intent.putExtra("android.intent.extra.TEXT", "")
        try {
            startActivity(Intent.createChooser(intent, "Send mail..."))
        } catch (unused: ActivityNotFoundException) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show()
        }
    }


    fun rateApp() {
        val intent =
            Intent("android.intent.action.VIEW", Uri.parse("market://details?id=$packageName"))
        intent.addFlags(1208483840)
        try {
            startActivity(intent)
        } catch (unused: ActivityNotFoundException) {
            startActivity(
                Intent(
                    "android.intent.action.VIEW",
                    Uri.parse("http://play.google.com/store/apps/details?id=$packageName")
                )
            )
        }
    }


    fun shareApp() {
        try {
            val intent = Intent("android.intent.action.SEND")
            intent.setType("text/plain")
            intent.putExtra("android.intent.extra.SUBJECT", getString(R.string.app_name))
            intent.putExtra(
                "android.intent.extra.TEXT", """
     
     Let me recommend you this application
     
     https://play.google.com/store/apps/details?id=$packageName
     
     
     """.trimIndent()
            )
            startActivity(Intent.createChooser(intent, "choose one"))
        } catch (unused: Exception) {
        }
    }

    fun showNameDialog(activity: Activity?) {
        val dialog = Dialog(activity!!)
        dialog.requestWindowFeature(1)
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.createresume_dialog)
        val editText = dialog.findViewById<View>(R.id.editText) as EditText
        resumenm = editText.text.toString()
        (dialog.findViewById<View>(R.id.btns) as Button).setOnClickListener(
            View.OnClickListener {
                if (this@HomeActivity.isEmpty(editText)) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Please Enter Resume Name",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@OnClickListener
                }
                dialog.dismiss()
                this@HomeActivity.startActivity(Intent(this@HomeActivity, MainActivity::class.java))
            })
        dialog.show()
    }


    fun isEmpty(editText: EditText): Boolean {
        return editText.text.toString().trim { it <= ' ' }.length == 0
    }


    fun requestMultiplePermissions() {
        Dexter.withActivity(this).withPermissions(
            "android.permission.CAMERA",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE"
        ).withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(multiplePermissionsReport: MultiplePermissionsReport) {
                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                    this@HomeActivity.startActivity(
                        Intent(
                            this@HomeActivity,
                            MainActivity::class.java
                        )
                    )
                }
                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied) {
                    Toast.makeText(
                        this@HomeActivity,
                        "Please allow app to continue",
                        Toast.LENGTH_SHORT
                    ).show()
                    val intent = Intent("android.settings.APPLICATION_DETAILS_SETTINGS")
                    intent.setData(Uri.fromParts("package", this@HomeActivity.packageName, null))
                    this@HomeActivity.startActivity(intent)
                }
            }

            override fun onPermissionRationaleShouldBeShown(
                list: List<PermissionRequest>,
                permissionToken: PermissionToken
            ) {
                Toast.makeText(
                    this@HomeActivity,
                    "Please allow app to continue",
                    Toast.LENGTH_SHORT
                ).show()
                permissionToken.continuePermissionRequest()
            }
        }).withErrorListener { }.onSameThread().check()
    }

    fun exitdialog() {
        val r0 = DialogInterface.OnClickListener { dialogInterface, i ->
            if (i == -3) {
                val homeActivity = this@HomeActivity
                homeActivity.startActivity(
                    Intent(
                        "android.intent.action.VIEW",
                        Uri.parse("market://details?id=" + this@HomeActivity.packageName)
                    )
                )
                dialogInterface.dismiss()
            } else if (i == -1) {
                dialogInterface.dismiss()
                this@HomeActivity.finish()
                System.exit(0)
            }
        }
        AlertDialog.Builder(this).setMessage("Are you sure you want to exit?")
            .setPositiveButton("Exit", r0).setTitle("").setNeutralButton("Rate Us", r0).show()
    }

    override fun onBackPressed() {
        exitdialog()
    }

    companion object {
        private const val TAG = "ted>>"
        var resumenm: String? = null
        fun startAlarmBroadcastReceiver(context: Context) {
            val broadcast = PendingIntent.getBroadcast(
                context,
                0,
                Intent(context, NotificationReceivcer::class.java),
                PendingIntent.FLAG_IMMUTABLE
            )
            val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(broadcast)
            val instance = Calendar.getInstance()
            instance.timeInMillis = System.currentTimeMillis()
            instance[11] = 23
            instance[12] = 59
            instance[13] = 0
            alarmManager[AlarmManager.RTC_WAKEUP, instance.timeInMillis] = broadcast
        }
    }
}
