package com.professorapps.cvmaker

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.Window
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import java.util.regex.Pattern

class ReferenceActivity() : AppCompatActivity() {
    var back: LinearLayout? = null
    var desig: String? = null
    var et_rDesignation: EditText? = null
    var et_rMail: EditText? = null
    var et_rName: EditText? = null
    var et_rOrganization: EditText? = null
    var et_rPhone: EditText? = null
    var flag: Int = 0
    var layout_toolbar: LinearLayout? = null
    var ll_save: ImageView? = null
    var mail: String? = null
    var name: String? = null
    var orgnm: String? = null
    var phone: String? = null
    var store: SharedPreferences? = null
    var titaltext: TextView? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_references)
        this.titaltext = findViewById<View>(R.id.titaltext) as TextView?
        titaltext!!.setText("References")
        initToolbar()
        this.store = SharedPreferences.Companion.getInstance(this, "resumemaker")
        this.et_rName = findViewById<View>(R.id.et_rName) as EditText?
        this.et_rMail = findViewById<View>(R.id.et_rMail) as EditText?
        this.et_rPhone = findViewById<View>(R.id.et_rPhone) as EditText?
        this.et_rOrganization = findViewById<View>(R.id.et_rOrganization) as EditText?
        this.et_rDesignation = findViewById<View>(R.id.et_rDesignation) as EditText?
        this.ll_save = findViewById<View>(R.id.ll_save) as ImageView?
        sharedpref
        ll_save!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val referenceActivity: ReferenceActivity = this@ReferenceActivity
                if (referenceActivity.isEmpty(referenceActivity.et_rName)) {
                    Toast.makeText(this@ReferenceActivity, "Please Enter Name", Toast.LENGTH_SHORT)
                        .show()
                    return
                }
                val referenceActivity2: ReferenceActivity = this@ReferenceActivity
                if (referenceActivity2.isEmpty(referenceActivity2.et_rPhone)) {
                    Toast.makeText(
                        this@ReferenceActivity,
                        "Please Enter Contact",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceActivity3: ReferenceActivity = this@ReferenceActivity
                if (referenceActivity3.isEmpty(referenceActivity3.et_rOrganization)) {
                    Toast.makeText(
                        this@ReferenceActivity,
                        "Please Enter Organization",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceActivity4: ReferenceActivity = this@ReferenceActivity
                if (referenceActivity4.isEmpty(referenceActivity4.et_rDesignation)) {
                    Toast.makeText(
                        this@ReferenceActivity,
                        "Please Enter Designation",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceActivity5: ReferenceActivity = this@ReferenceActivity
                if (!referenceActivity5.isValidMobile(
                        referenceActivity5.et_rPhone!!.getText().toString()
                    )
                ) {
                    Toast.makeText(
                        this@ReferenceActivity,
                        "Please Enter valid Phone Number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                if (!(et_rMail!!.getText().toString() == "")) {
                    val referenceActivity6: ReferenceActivity = this@ReferenceActivity
                    if (!referenceActivity6.isValidMail(
                            referenceActivity6.et_rMail!!.getText().toString()
                        )
                    ) {
                        Toast.makeText(
                            this@ReferenceActivity,
                            "Please Enter valid Email",
                            Toast.LENGTH_SHORT
                        ).show()
                        return
                    }
                }
                val referenceActivity7: ReferenceActivity = this@ReferenceActivity
                if (referenceActivity7.isEmpty(referenceActivity7.et_rMail)) {
                    et_rMail!!.setText(" ")
                }
                val referenceActivity8: ReferenceActivity = this@ReferenceActivity
                referenceActivity8.name = referenceActivity8.et_rName!!.getText().toString()
                val referenceActivity9: ReferenceActivity = this@ReferenceActivity
                referenceActivity9.mail = referenceActivity9.et_rMail!!.getText().toString()
                val referenceActivity10: ReferenceActivity = this@ReferenceActivity
                referenceActivity10.phone = referenceActivity10.et_rPhone!!.getText().toString()
                val referenceActivity11: ReferenceActivity = this@ReferenceActivity
                referenceActivity11.orgnm =
                    referenceActivity11.et_rOrganization!!.getText().toString()
                val referenceActivity12: ReferenceActivity = this@ReferenceActivity
                referenceActivity12.desig =
                    referenceActivity12.et_rDesignation!!.getText().toString()
                store!!.saveString(
                    this@ReferenceActivity.getString(R.string.reference_name1),
                    this@ReferenceActivity.name
                )
                store!!.saveString(
                    this@ReferenceActivity.getString(R.string.reference_mail1),
                    this@ReferenceActivity.mail
                )
                store!!.saveString(
                    this@ReferenceActivity.getString(R.string.reference_contact1),
                    this@ReferenceActivity.phone
                )
                store!!.saveString(
                    this@ReferenceActivity.getString(R.string.reference_orgnm1),
                    this@ReferenceActivity.orgnm
                )
                store!!.saveString(
                    this@ReferenceActivity.getString(R.string.reference_desig1),
                    this@ReferenceActivity.desig
                )
                this@ReferenceActivity.finish()
            }
        })
    }


    fun isEmpty(editText: EditText?): Boolean {
        return editText!!.getText().toString().trim({ it <= ' ' }).length == 0
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.getString(getString(R.string.reference_name1), "") != null) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.reference_name1), "")!!
                    )
                    val string: String? =
                        store!!.getString(getString(R.string.reference_name1), "")
                    val string2: String? =
                        store!!.getString(getString(R.string.reference_mail1), "")
                    val string3: String? =
                        store!!.getString(getString(R.string.reference_contact1), "")
                    val string4: String? =
                        store!!.getString(getString(R.string.reference_orgnm1), "")
                    val string5: String? =
                        store!!.getString(getString(R.string.reference_desig1), "")
                    et_rName!!.setText(string)
                    et_rMail!!.setText(string2)
                    et_rPhone!!.setText(string3)
                    et_rOrganization!!.setText(string4)
                    et_rDesignation!!.setText(string5)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }


    fun isValidMobile(str: String): Boolean {
        if (Pattern.matches("[a-zA-Z]+", str) || (str.length <= 6) || (str.length > 13)) {
            return false
        }
        return true
    }


    fun isValidMail(str: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(str).matches()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backdialog()
    }

    private fun initToolbar() {
        val window: Window = getWindow()
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_reference))
        }
        this.layout_toolbar = findViewById<View>(R.id.layout_toolbar) as LinearLayout?
        layout_toolbar!!.setBackgroundResource(R.drawable.toolbar_ref)
        this.back = findViewById<View>(R.id.back) as LinearLayout?
        back!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@ReferenceActivity.onBackPressed()
            }
        })
    }

    override fun onResume() {
        super.onResume()
    }

    fun backdialog() {
        val r0: DialogInterface.OnClickListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface, i: Int) {
                if (i == -3) {
                    dialogInterface.dismiss()
                } else if (i == -1) {
                    dialogInterface.dismiss()
                    this@ReferenceActivity.finish()
                }
            }
        }
        AlertDialog.Builder(this).setMessage("Discard editing?").setPositiveButton("Ok", r0)
            .setTitle("").setNeutralButton("Cancel", r0).show()
    }

    companion object {
        private val TAG: String = "AcademicActivity>>"
    }
}
