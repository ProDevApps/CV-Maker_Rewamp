package com.professorapps.cvmaker

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.github.javiersantos.bottomdialogs.BottomDialog
import com.github.javiersantos.bottomdialogs.BottomDialog.ButtonCallback
import com.professorapps.cvmaker.crop.BasicActivity
import com.professorapps.cvmaker.utils.Util.getOutputMediaFile
import java.util.regex.Pattern

class PersonalinfoActivity() : AppCompatActivity() {
    var aboutme: String? = null
    var address: String? = null
    var back: LinearLayout? = null
    var et_Address: EditText? = null
    var et_MailAddress: EditText? = null
    var et_Name: EditText? = null
    var et_Phone: EditText? = null
    var et_aboutme: EditText? = null
    var et_field: EditText? = null
    var field: String? = null
    var getabout: String? = null

    // CircleImageView iv_profile;
    var layout_toolbar: LinearLayout? = null
    var ll_save: ImageView? = null
    var mActivity: Activity? = null
    var mailAddress: String? = null
    var name: String? = null
    var phone: String? = null
    var store: SharedPreferences? = null
    var tv_aboutme: TextView? = null
    private val PICTURE_TAKEN_FROM_CAMERA = 1
    private val PICTURE_TAKEN_FROM_GALLERY = 2
    var flag: Int = 0


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_personalinfo)
        this.mActivity = this
        this.store = SharedPreferences.Companion.getInstance(this, "resumemaker")
        //  this.iv_profile = (CircleImageView) findViewById(R.id.iv_profile);
        this.ll_save = findViewById<View>(R.id.ll_save) as ImageView
        this.et_Name = findViewById<View>(R.id.et_Name) as EditText
        this.et_Phone = findViewById<View>(R.id.et_Phone) as EditText
        this.et_MailAddress = findViewById<View>(R.id.et_MailAddress) as EditText
        this.et_Address = findViewById<View>(R.id.et_Address) as EditText
        this.et_field = findViewById<View>(R.id.et_field) as EditText
        this.et_aboutme = findViewById<View>(R.id.et_aboutme) as EditText
        this.tv_aboutme = findViewById<View>(R.id.tv_aboutme) as TextView
        sharedpref
        initToolbar()
        /*   this.iv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PersonalinfoActivity.this.showPictureDialog();
            }
        });*/
        tv_aboutme!!.setOnClickListener {
            this@PersonalinfoActivity.startActivityForResult(
                Intent(
                    this@PersonalinfoActivity, AboutmeActivity::class.java
                ), 123
            )
        }
        ll_save!!.setOnClickListener(View.OnClickListener { /*if (PersonalinfoActivity.this.store.getString(PersonalinfoActivity.this.getString(R.string.personalinfo_profile), "") == null || PersonalinfoActivity.this.store.getString(PersonalinfoActivity.this.getString(R.string.personalinfo_profile), "") == "") {
                    Toast.makeText(PersonalinfoActivity.this, "Please Select Picture", Toast.LENGTH_SHORT).show();
                    return;
                }*/
            val personalinfoActivity = this@PersonalinfoActivity
            if (personalinfoActivity.isEmpty(personalinfoActivity.et_Name)) {
                Toast.makeText(this@PersonalinfoActivity, "Please Enter Name", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            val personalinfoActivity2 = this@PersonalinfoActivity
            if (personalinfoActivity2.isEmpty(personalinfoActivity2.et_Phone)) {
                Toast.makeText(
                    this@PersonalinfoActivity,
                    "Please Enter Phone Number",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val personalinfoActivity3 = this@PersonalinfoActivity
            if (personalinfoActivity3.isEmpty(personalinfoActivity3.et_MailAddress)) {
                Toast.makeText(this@PersonalinfoActivity, "Please Enter Email", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            val personalinfoActivity4 = this@PersonalinfoActivity
            if (personalinfoActivity4.isEmpty(personalinfoActivity4.et_Address)) {
                Toast.makeText(
                    this@PersonalinfoActivity,
                    "Please Enter Address",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val personalinfoActivity5 = this@PersonalinfoActivity
            if (personalinfoActivity5.isEmpty(personalinfoActivity5.et_field)) {
                Toast.makeText(this@PersonalinfoActivity, "Please Enter Field", Toast.LENGTH_SHORT)
                    .show()
                return@OnClickListener
            }
            val personalinfoActivity6 = this@PersonalinfoActivity
            if (personalinfoActivity6.isEmpty(personalinfoActivity6.et_aboutme)) {
                Toast.makeText(
                    this@PersonalinfoActivity,
                    "Please Enter or Choose About Me",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val personalinfoActivity7 = this@PersonalinfoActivity
            if (!personalinfoActivity7.isValidMobile(personalinfoActivity7.et_Phone!!.text.toString())) {
                Toast.makeText(
                    this@PersonalinfoActivity,
                    "Please Enter valid Phone Number",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val personalinfoActivity8 = this@PersonalinfoActivity
            if (!personalinfoActivity8.isValidMail(personalinfoActivity8.et_MailAddress!!.text.toString())) {
                Toast.makeText(
                    this@PersonalinfoActivity,
                    "Please Enter valid Email",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val personalinfoActivity9 = this@PersonalinfoActivity
            personalinfoActivity9.name = personalinfoActivity9.et_Name!!.text.toString()
            val personalinfoActivity10 = this@PersonalinfoActivity
            personalinfoActivity10.phone = personalinfoActivity10.et_Phone!!.text.toString()
            val personalinfoActivity11 = this@PersonalinfoActivity
            personalinfoActivity11.mailAddress =
                personalinfoActivity11.et_MailAddress!!.text.toString()
            val personalinfoActivity12 = this@PersonalinfoActivity
            personalinfoActivity12.address = personalinfoActivity12.et_Address!!.text.toString()
            val personalinfoActivity13 = this@PersonalinfoActivity
            personalinfoActivity13.field = personalinfoActivity13.et_field!!.text.toString()
            val personalinfoActivity14 = this@PersonalinfoActivity
            personalinfoActivity14.aboutme = personalinfoActivity14.et_aboutme!!.text.toString()
            Log.d(
                "sharedpref>",
                this@PersonalinfoActivity.name + this@PersonalinfoActivity.phone + this@PersonalinfoActivity.mailAddress + this@PersonalinfoActivity.address + this@PersonalinfoActivity.field + this@PersonalinfoActivity.aboutme
            )
            store!!.saveString(
                this@PersonalinfoActivity.getString(R.string.personalinfo_name),
                this@PersonalinfoActivity.name
            )
            store!!.saveString(
                this@PersonalinfoActivity.getString(R.string.personalinfo_phone),
                this@PersonalinfoActivity.phone
            )
            store!!.saveString(
                this@PersonalinfoActivity.getString(R.string.personalinfo_email),
                this@PersonalinfoActivity.mailAddress
            )
            store!!.saveString(
                this@PersonalinfoActivity.getString(R.string.personalinfo_address),
                this@PersonalinfoActivity.address
            )
            store!!.saveString(
                this@PersonalinfoActivity.getString(R.string.personalinfo_field),
                this@PersonalinfoActivity.field
            )
            store!!.saveString(
                this@PersonalinfoActivity.getString(R.string.personalinfo_aboutme),
                this@PersonalinfoActivity.aboutme
            )
            this@PersonalinfoActivity.finish()
        })
    }

    private fun initToolbar() {
        val window = window
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_personalinfo)
        }
        this.layout_toolbar = findViewById<View>(R.id.layout_toolbar) as LinearLayout
        layout_toolbar!!.setBackgroundResource(R.drawable.toolbar_personalinfo)
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@PersonalinfoActivity.onBackPressed() }
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.getString(getString(R.string.personalinfo_name), "") != null) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.personalinfo_name), "")!!
                    )
                    val string =
                        store!!.getString(getString(R.string.personalinfo_profile), "")
                    val string2 =
                        store!!.getString(getString(R.string.personalinfo_name), "")
                    val string3 =
                        store!!.getString(getString(R.string.personalinfo_phone), "")
                    val string4 =
                        store!!.getString(getString(R.string.personalinfo_email), "")
                    val string5 =
                        store!!.getString(getString(R.string.personalinfo_address), "")
                    val string6 =
                        store!!.getString(getString(R.string.personalinfo_field), "")
                    val string7 =
                        store!!.getString(getString(R.string.personalinfo_aboutme), "")
                    //   this.iv_profile.setImageURI(Uri.parse(string));
                    et_Name!!.setText(string2)
                    et_Phone!!.setText(string3)
                    et_MailAddress!!.setText(string4)
                    et_Address!!.setText(string5)
                    et_field!!.setText(string6)
                    et_aboutme!!.setText(string7)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }


    fun showPictureDialog() {
        BottomDialog.Builder(this).setTitle("Select Image from").setNegativeText("From camera")
            .setPositiveText("From gallery").setNegativeTextColorResource(R.color.colorAccent)
            .onPositive(
                ButtonCallback { this@PersonalinfoActivity.openGallery() })
            .onNegative(object : ButtonCallback {
                override fun onClick(bottomDialog: BottomDialog) {
                    this@PersonalinfoActivity.openCamera()
                }
            }).show()
    }


    fun openCamera() {
        uri = FileProvider.getUriForFile(this, packageName, (getOutputMediaFile(1))!!)
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        intent.putExtra("output", uri)
        startActivityForResult(intent, 1)
    }


    fun openGallery() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction("android.intent.action.GET_CONTENT")
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 2)
    }

    override fun onActivityResult(i: Int, i2: Int, intent: Intent?) {
        if (i != 1) {
            if (i != 2) {
                if (i != 12) {
                    if ((i == 123) && (i2 == -1) && (intent != null)) {
                        this.getabout = intent.getStringExtra("extra_sticker_id")
                        et_aboutme!!.setText(this.getabout)
                        Log.d("abouttext>>2", (getabout)!!)
                    }
                } else if (i2 == -1 && intent != null) {
                    uri = Uri.parse(intent.getStringExtra(AboutmeActivity.EXTRA_PROFILE_ID))
                    //    this.iv_profile.setImageURI(uri);
                    val flags = intent.flags and 3
                    try {
                        mActivity!!.contentResolver.takePersistableUriPermission(uri!!, flags!!)
                    } catch (e: SecurityException) {
                        e.printStackTrace()
                    }
                    store!!.saveString(getString(R.string.personalinfo_profile), uri.toString())
                }
            } else if (i2 == -1) {
                uri = intent!!.data
                val intent2 = Intent(this, BasicActivity::class.java)
                intent2.putExtra("imageUri", uri.toString())
                startActivityForResult(intent2, 12)
            }
        } else if (i2 == -1) {
            val intent3 = Intent(this, BasicActivity::class.java)
            intent3.putExtra("imageUri", uri.toString())
            startActivityForResult(intent3, 12)
        }
        super.onActivityResult(i, i2, intent)
    }


    fun isEmpty(editText: EditText?): Boolean {
        return editText!!.text.toString().trim { it <= ' ' }.length == 0
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

    override fun onResume() {
        super.onResume()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        backdialog()
    }

    fun backdialog() {
        val r0: DialogInterface.OnClickListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface, i: Int) {
                if (i == -3) {
                    dialogInterface.dismiss()
                } else if (i == -1) {
                    dialogInterface.dismiss()
                    this@PersonalinfoActivity.finish()
                }
            }
        }
        AlertDialog.Builder(this).setMessage("Discard editing?").setPositiveButton("Ok", r0)
            .setTitle("").setNeutralButton("Cancel", r0).show()
    }

    companion object {
        val SELECT_ABOUT_REQUEST_CODE: Int = 123
        val SELECT_PROFILE_REQUEST_CODE: Int = 12
        var uri: Uri? = null
    }
}
