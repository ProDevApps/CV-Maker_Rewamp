package com.professorapps.cvmaker

import android.annotation.TargetApi
import android.content.ActivityNotFoundException
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.BitmapDrawable
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import android.graphics.text.LineBreaker
import android.media.MediaScannerConnection
import android.media.MediaScannerConnection.OnScanCompletedListener
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.professorapps.cvmaker.adapters.ResumeSampleAdapter
import com.professorapps.cvmaker.models.ResumeSampleModel
import com.professorapps.cvmaker.utils.MoveViewTouchListener
import com.professorapps.cvmaker.utils.ZoomLayout
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ResumeSample1Activity() : AppCompatActivity() {
    var back: LinearLayout? = null
    private var bitmap: Bitmap? = null
    lateinit var bmp: Bitmap
    var flag: Int = 0
    var imgdone: ImageView? = null
    var imgmain: ImageView? = null
    var imgundo: ImageView? = null
    var ivuserprofile: ImageView? = null
    var layoutabout: LinearLayout? = null
    var layoutedu: LinearLayout? = null
    var layoutedu1: LinearLayout? = null
    var layoutedu2: LinearLayout? = null
    var layoutedu3: LinearLayout? = null
    var layouthobby: LinearLayout? = null
    var layoutlanguage: LinearLayout? = null
    var layoutmain: RelativeLayout? = null
    var layoutpersonalinfo1: LinearLayout? = null
    var layoutpersonalinfo2: LinearLayout? = null
    var layoutref: LinearLayout? = null
    var layoutskilles: LinearLayout? = null
    var layoutwexp: LinearLayout? = null
    var layoutwexp1: LinearLayout? = null
    var layoutwexp2: LinearLayout? = null
    var layoutwexp3: LinearLayout? = null
    var newHeight: Float = 0f
    var screenWidth: Float = 0f
    var store: SharedPreferences? = null
    var tv_aboutme: TextView? = null
    var tv_address: TextView? = null
    var tv_clgnm1: TextView? = null
    var tv_clgnm2: TextView? = null
    var tv_clgnm3: TextView? = null
    var tv_degree1: TextView? = null
    var tv_degree2: TextView? = null
    var tv_degree3: TextView? = null
    var tv_desig1: TextView? = null
    var tv_desig2: TextView? = null
    var tv_desig3: TextView? = null
    var tv_email: TextView? = null
    var tv_field: TextView? = null
    var tv_fromtime1: TextView? = null
    var tv_fromtime2: TextView? = null
    var tv_fromtime3: TextView? = null
    var tv_hobby1: TextView? = null
    var tv_hobby2: TextView? = null
    var tv_hobby3: TextView? = null
    var tv_hobby4: TextView? = null
    var tv_hobby5: TextView? = null
    var tv_lan1: TextView? = null
    var tv_lan2: TextView? = null
    var tv_lan3: TextView? = null
    var tv_lan4: TextView? = null
    var tv_lan5: TextView? = null
    var tv_name: TextView? = null
    var tv_orgnm1: TextView? = null
    var tv_orgnm2: TextView? = null
    var tv_orgnm3: TextView? = null
    var tv_pernum1: TextView? = null
    var tv_pernum2: TextView? = null
    var tv_pernum3: TextView? = null
    var tv_perorcpga1: TextView? = null
    var tv_perorcpga2: TextView? = null
    var tv_perorcpga3: TextView? = null
    var tv_phone: TextView? = null
    var tv_refemail: TextView? = null
    var tv_refnm: TextView? = null
    var tv_reforgnm: TextView? = null
    var tv_refphone: TextView? = null
    var tv_skill1: TextView? = null
    var tv_skill2: TextView? = null
    var tv_skill3: TextView? = null
    var tv_skill4: TextView? = null
    var tv_skill5: TextView? = null
    var tv_totime1: TextView? = null
    var tv_totime2: TextView? = null
    var tv_totime3: TextView? = null
    var tv_year1: TextView? = null
    var tv_year2: TextView? = null
    var tv_year3: TextView? = null
    var tvrole1: TextView? = null
    var tvrole2: TextView? = null
    var tvrole3: TextView? = null


    @TargetApi(21)
    private fun initToolbar() {
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)
    }


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        if (ResumeSampleAdapter.selectedposition == 0) {
            setContentView(R.layout.activity_resume_sample1)
            Log.d("resumeposition>>1", ResumeSampleAdapter.selectedposition.toString() + "")
        } else if (ResumeSampleAdapter.selectedposition == 1) {
            setContentView(R.layout.activity_resume_sample2)
        } else if (ResumeSampleAdapter.selectedposition == 2) {
            setContentView(R.layout.activity_resume_sample3)
        } else if (ResumeSampleAdapter.selectedposition == 3) {
            setContentView(R.layout.activity_resume_sample4)
        } else if (ResumeSampleAdapter.selectedposition == 4) {
            setContentView(R.layout.activity_resume_sample5)
        } else if (ResumeSampleAdapter.selectedposition == 5) {
            setContentView(R.layout.activity_resume_sample6)
        } else if (ResumeSampleAdapter.selectedposition == 6) {
            setContentView(R.layout.activity_resume_sample7)
        } else if (ResumeSampleAdapter.selectedposition == 7) {
            setContentView(R.layout.activity_resume_sample8)
        } else if (ResumeSampleAdapter.selectedposition == 8) {
            setContentView(R.layout.activity_resume_sample9)
        } else if (ResumeSampleAdapter.selectedposition == 9) {
            setContentView(R.layout.activity_resume_sample10)
        } else if (ResumeSampleAdapter.selectedposition == 10) {
            setContentView(R.layout.activity_resume_sample11)
        } else if (ResumeSampleAdapter.selectedposition == 11) {
            setContentView(R.layout.activity_resume_sample12)
        } else if (ResumeSampleAdapter.selectedposition == 12) {
            setContentView(R.layout.activity_resume_sample13)
        } else if (ResumeSampleAdapter.selectedposition == 13) {
            setContentView(R.layout.activity_resume_sample14)
        } else if (ResumeSampleAdapter.selectedposition == 14) {
            setContentView(R.layout.activity_resume_sample15)
        }
        initToolbar()

        Gson()
        val stringExtra: String? = getIntent().getStringExtra("obj")
        getIntent().getIntExtra("position", 0)
        val i: Int =
            ((Gson().fromJson<Any>(stringExtra, object : TypeToken<List<ResumeSampleModel?>?>() {
            }.getType()) as List<*>).get(ResumeSampleAdapter.selectedposition) as ResumeSampleModel).getfrm2()
        this.store = SharedPreferences.Companion.getInstance(this, "resumemaker")
        this.bmp = BitmapFactory.decodeResource(getResources(), i)
        this.layoutmain = findViewById<View>(R.id.layoutrelativemain) as RelativeLayout?
        this.layoutedu = findViewById<View>(R.id.layoutedu) as LinearLayout?
        this.layoutedu1 = findViewById<View>(R.id.layoutedu1) as LinearLayout?
        this.layoutedu2 = findViewById<View>(R.id.layoutedu2) as LinearLayout?
        this.layoutedu3 = findViewById<View>(R.id.layoutedu3) as LinearLayout?
        this.layoutpersonalinfo1 = findViewById<View>(R.id.layoutpersonalinfo1) as LinearLayout?
        this.layoutpersonalinfo2 = findViewById<View>(R.id.layoutpersonalinfo2) as LinearLayout?
        this.layoutabout = findViewById<View>(R.id.layoutabout) as LinearLayout?
        this.layoutwexp = findViewById<View>(R.id.layoutwexp) as LinearLayout?
        this.layoutwexp1 = findViewById<View>(R.id.layoutwexp1) as LinearLayout?
        this.layoutwexp2 = findViewById<View>(R.id.layoutwexp2) as LinearLayout?
        this.layoutwexp3 = findViewById<View>(R.id.layoutwexp3) as LinearLayout?
        this.layoutskilles = findViewById<View>(R.id.layoutskilles) as LinearLayout?
        this.layoutlanguage = findViewById<View>(R.id.layoutlanguage) as LinearLayout?
        this.layouthobby = findViewById<View>(R.id.layouthobby) as LinearLayout?
        this.layoutref = findViewById<View>(R.id.layoutref) as LinearLayout?
        this.imgmain = findViewById<View>(R.id.imgmain) as ImageView?
        this.back = findViewById<View>(R.id.back) as LinearLayout?
        this.imgdone = findViewById<View>(R.id.imgdone) as ImageView?
        this.imgundo = findViewById<View>(R.id.imgundo) as ImageView?
        this.ivuserprofile = findViewById<View>(R.id.ivuserprofile) as ImageView?
        this.tv_name = findViewById<View>(R.id.tv_name) as TextView?
        this.tv_field = findViewById<View>(R.id.tv_field) as TextView?
        this.tv_aboutme = findViewById<View>(R.id.tv_aboutme) as TextView?
        this.tv_email = findViewById<View>(R.id.tv_email) as TextView?
        this.tv_phone = findViewById<View>(R.id.tv_phone) as TextView?
        this.tv_address = findViewById<View>(R.id.tv_address) as TextView?
        this.tv_skill1 = findViewById<View>(R.id.tv_skill1) as TextView?
        this.tv_skill2 = findViewById<View>(R.id.tv_skill2) as TextView?
        this.tv_skill3 = findViewById<View>(R.id.tv_skill3) as TextView?
        this.tv_skill4 = findViewById<View>(R.id.tv_skill4) as TextView?
        this.tv_skill5 = findViewById<View>(R.id.tv_skill5) as TextView?
        this.tv_clgnm1 = findViewById<View>(R.id.tv_clgnm1) as TextView?
        this.tv_year1 = findViewById<View>(R.id.tv_year1) as TextView?
        this.tv_degree1 = findViewById<View>(R.id.tv_degree1) as TextView?
        this.tv_pernum1 = findViewById<View>(R.id.tv_pernum1) as TextView?
        this.tv_perorcpga1 = findViewById<View>(R.id.tv_perorcpga1) as TextView?
        this.tv_clgnm2 = findViewById<View>(R.id.tv_clgnm2) as TextView?
        this.tv_year2 = findViewById<View>(R.id.tv_year2) as TextView?
        this.tv_degree2 = findViewById<View>(R.id.tv_degree2) as TextView?
        this.tv_pernum2 = findViewById<View>(R.id.tv_pernum2) as TextView?
        this.tv_perorcpga2 = findViewById<View>(R.id.tv_perorcpga2) as TextView?
        this.tv_clgnm3 = findViewById<View>(R.id.tv_clgnm3) as TextView?
        this.tv_year3 = findViewById<View>(R.id.tv_year3) as TextView?
        this.tv_degree3 = findViewById<View>(R.id.tv_degree3) as TextView?
        this.tv_pernum3 = findViewById<View>(R.id.tv_pernum3) as TextView?
        this.tv_perorcpga3 = findViewById<View>(R.id.tv_perorcpga3) as TextView?
        this.tv_orgnm1 = findViewById<View>(R.id.tv_orgnm1) as TextView?
        this.tv_fromtime1 = findViewById<View>(R.id.tv_fromtime1) as TextView?
        this.tv_totime1 = findViewById<View>(R.id.tv_totime1) as TextView?
        this.tv_desig1 = findViewById<View>(R.id.tv_desig1) as TextView?
        this.tvrole1 = findViewById<View>(R.id.tvrole1) as TextView?
        this.tv_orgnm2 = findViewById<View>(R.id.tv_orgnm2) as TextView?
        this.tv_fromtime2 = findViewById<View>(R.id.tv_fromtime2) as TextView?
        this.tv_totime2 = findViewById<View>(R.id.tv_totime2) as TextView?
        this.tv_desig2 = findViewById<View>(R.id.tv_desig2) as TextView?
        this.tvrole2 = findViewById<View>(R.id.tvrole2) as TextView?
        this.tv_orgnm3 = findViewById<View>(R.id.tv_orgnm3) as TextView?
        this.tv_fromtime3 = findViewById<View>(R.id.tv_fromtime3) as TextView?
        this.tv_totime3 = findViewById<View>(R.id.tv_totime3) as TextView?
        this.tv_desig3 = findViewById<View>(R.id.tv_desig3) as TextView?
        this.tvrole3 = findViewById<View>(R.id.tvrole3) as TextView?
        this.tv_lan1 = findViewById<View>(R.id.tv_lan1) as TextView?
        this.tv_lan2 = findViewById<View>(R.id.tv_lan2) as TextView?
        this.tv_lan3 = findViewById<View>(R.id.tv_lan3) as TextView?
        this.tv_lan4 = findViewById<View>(R.id.tv_lan4) as TextView?
        this.tv_lan5 = findViewById<View>(R.id.tv_lan5) as TextView?
        this.tv_hobby1 = findViewById<View>(R.id.tv_hobby1) as TextView?
        this.tv_hobby2 = findViewById<View>(R.id.tv_hobby2) as TextView?
        this.tv_hobby3 = findViewById<View>(R.id.tv_hobby3) as TextView?
        this.tv_hobby4 = findViewById<View>(R.id.tv_hobby4) as TextView?
        this.tv_hobby5 = findViewById<View>(R.id.tv_hobby5) as TextView?
        this.tv_refnm = findViewById<View>(R.id.tv_refnm) as TextView?
        this.tv_reforgnm = findViewById<View>(R.id.tv_reforgnm) as TextView?
        this.tv_refphone = findViewById<View>(R.id.tv_refphone) as TextView?
        this.tv_refemail = findViewById<View>(R.id.tv_refemail) as TextView?
        back!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val resumeSample1Activity: ResumeSample1Activity = this@ResumeSample1Activity
                resumeSample1Activity.flag = 2
                this@ResumeSample1Activity.onBackPressed()
            }
        })
        imgdone!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val resumeSample1Activity: ResumeSample1Activity = this@ResumeSample1Activity
                resumeSample1Activity.flag = 1
                try {
                    Log.d("size>>>", " " + layoutmain!!.getWidth() + "  " + layoutmain!!.getWidth())
                    this@ResumeSample1Activity.bitmap = loadBitmapFromView(
                        this@ResumeSample1Activity.layoutmain,
                        screenWidth.toInt(),
                        newHeight.toInt()
                    )
                    this@ResumeSample1Activity.createPdf()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        })
        imgundo!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@ResumeSample1Activity.refreshActivity()
            }
        })
        layoutmain!!.addView(ZoomLayout(this))
        val bitmap: Bitmap? = this.bmp
        if (bitmap != null) {
            bitmap.getWidth()
            bmp.getHeight()
            val defaultDisplay: Display = getWindowManager().getDefaultDisplay()
            val displayMetrics: DisplayMetrics = DisplayMetrics()
            defaultDisplay.getMetrics(displayMetrics)
            this.screenWidth = displayMetrics.widthPixels.toFloat()
            this.newHeight = this.screenWidth
            if (!(bmp.getWidth() == 0 || bmp.getHeight() == 0)) {
                this.newHeight =
                    (this.screenWidth * (bmp.getHeight().toFloat())) / (bmp.getWidth().toFloat())
            }
            Log.d(
                "final_bmp",
                bmp.getWidth().toString() + "," + bmp.getHeight()
            )
            Log.d("final_bmp", screenWidth.toString() + "," + this.newHeight)
            this.bmp = Bitmap.createScaledBitmap(
                this.bmp,
                screenWidth.toInt(),
                newHeight.toInt(), false
            )
            BitmapDrawable(getResources(), this.bmp)
            imgmain!!.setImageBitmap(this.bmp)
        }
        sharedpref
        val imageView: ImageView? = this.ivuserprofile
        imageView!!.setOnTouchListener(MoveViewTouchListener((imageView)))
        val linearLayout: LinearLayout? = this.layoutedu
        linearLayout!!.setOnTouchListener(MoveViewTouchListener((linearLayout)))
        val linearLayout2: LinearLayout? = this.layoutwexp
        linearLayout2!!.setOnTouchListener(MoveViewTouchListener((linearLayout2)))
        val linearLayout3: LinearLayout? = this.layoutskilles
        linearLayout3!!.setOnTouchListener(MoveViewTouchListener((linearLayout3)))
        val linearLayout4: LinearLayout? = this.layoutlanguage
        linearLayout4!!.setOnTouchListener(MoveViewTouchListener((linearLayout4)))
        val linearLayout5: LinearLayout? = this.layouthobby
        linearLayout5!!.setOnTouchListener(MoveViewTouchListener((linearLayout5)))
        val linearLayout6: LinearLayout? = this.layoutref
        linearLayout6!!.setOnTouchListener(MoveViewTouchListener((linearLayout6)))
        val linearLayout7: LinearLayout? = this.layoutpersonalinfo1
        linearLayout7!!.setOnTouchListener(MoveViewTouchListener((linearLayout7)))
        val linearLayout8: LinearLayout? = this.layoutpersonalinfo2
        linearLayout8!!.setOnTouchListener(MoveViewTouchListener((linearLayout8)))
        val linearLayout9: LinearLayout? = this.layoutabout
        linearLayout9!!.setOnTouchListener(MoveViewTouchListener((linearLayout9)))
    }

    private val sharedpref: Unit
        get() {
            val str: String
            val str2: String
            try {
                if (store!!.contains(getString(R.string.personalinfo_name))) {
                    Log.d(
                        "sharedpref>>r1",
                        (store!!.getString(getString(R.string.personalinfo_name), ""))!!
                    )
                    val string: String? =
                        store!!.getString(getString(R.string.personalinfo_profile), "")
                    val string2: String? =
                        store!!.getString(getString(R.string.personalinfo_name), "")
                    val string3: String? =
                        store!!.getString(getString(R.string.personalinfo_phone), "")
                    val string4: String? =
                        store!!.getString(getString(R.string.personalinfo_email), "")
                    val string5: String? =
                        store!!.getString(getString(R.string.personalinfo_address), "")
                    val string6: String? =
                        store!!.getString(getString(R.string.personalinfo_field), "")
                    val string7: String? =
                        store!!.getString(getString(R.string.personalinfo_aboutme), "")
                    ivuserprofile!!.setImageURI(Uri.parse(string))
                    tv_name!!.setText(string2)
                    tv_phone!!.setText(string3)
                    tv_email!!.setText(string4)
                    tv_address!!.setText(string5)
                    tv_field!!.setText(string6)
                    if (Build.VERSION.SDK_INT >= 26) {
                        tv_aboutme!!.setJustificationMode(LineBreaker.JUSTIFICATION_MODE_INTER_WORD)
                    }
                    tv_aboutme!!.setText(string7)
                }
                if (store!!.contains(getString(R.string.skill1))) {
                    val string8: String? =
                        store!!.getString(getString(R.string.skill1), "")
                    val textView: TextView? = this.tv_skill1
                    textView!!.setText("• " + string8)
                } else {
                    tv_skill1!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.skill2))) {
                    val string9: String? =
                        store!!.getString(getString(R.string.skill2), "")
                    val textView2: TextView? = this.tv_skill2
                    textView2!!.setText("• " + string9)
                } else {
                    tv_skill2!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.skill3))) {
                    val string10: String? =
                        store!!.getString(getString(R.string.skill3), "")
                    val textView3: TextView? = this.tv_skill3
                    textView3!!.setText("• " + string10)
                } else {
                    tv_skill3!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.skill4))) {
                    val string11: String? =
                        store!!.getString(getString(R.string.skill4), "")
                    val textView4: TextView? = this.tv_skill4
                    textView4!!.setText("• " + string11)
                } else {
                    tv_skill4!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.skill5))) {
                    val string12: String? =
                        store!!.getString(getString(R.string.skill5), "")
                    val textView5: TextView? = this.tv_skill5
                    textView5!!.setText("• " + string12)
                } else {
                    tv_skill5!!.setVisibility(View.GONE)
                }
                var str3: String = "%"
                if (store!!.contains(getString(R.string.academic_degree1))) {
                    val string13: String? =
                        store!!.getString(getString(R.string.academic_institute1), "")
                    val string14: String? =
                        store!!.getString(getString(R.string.academic_year1), "")
                    val string15: String? =
                        store!!.getString(getString(R.string.academic_degree1), "")
                    val string16: String? =
                        store!!.getString(getString(R.string.academic_percgpa1), "")
                    if ((store!!.getString(
                            getString(R.string.academic_percgpa1_radio),
                            ""
                        ) == "Percentage")
                    ) {
                        str2 = str3
                    } else {
                        str2 = "CGPA"
                    }
                    val textView6: TextView? = this.tv_clgnm1
                    textView6!!.setText("• " + string13)
                    val textView7: TextView? = this.tv_year1
                    textView7!!.setText("| " + string14)
                    tv_degree1!!.setText(string15)
                    tv_pernum1!!.setText(string16)
                    tv_perorcpga1!!.setText(str2)
                } else {
                    layoutedu1!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.academic_degree2))) {
                    val string17: String? =
                        store!!.getString(getString(R.string.academic_institute2), "")
                    val string18: String? =
                        store!!.getString(getString(R.string.academic_year2), "")
                    val string19: String? =
                        store!!.getString(getString(R.string.academic_degree2), "")
                    val string20: String? =
                        store!!.getString(getString(R.string.academic_percgpa2), "")
                    if ((store!!.getString(
                            getString(R.string.academic_percgpa2_radio),
                            ""
                        ) == "Percentage")
                    ) {
                        str = str3
                    } else {
                        str = "CGPA"
                    }
                    val textView8: TextView? = this.tv_clgnm2
                    textView8!!.setText("• " + string17)
                    val textView9: TextView? = this.tv_year2
                    textView9!!.setText("| " + string18)
                    tv_degree2!!.setText(string19)
                    tv_pernum2!!.setText(string20)
                    tv_perorcpga2!!.setText(str)
                } else {
                    layoutedu2!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.academic_degree3))) {
                    val string21: String? =
                        store!!.getString(getString(R.string.academic_institute3), "")
                    val string22: String? =
                        store!!.getString(getString(R.string.academic_year3), "")
                    val string23: String? =
                        store!!.getString(getString(R.string.academic_degree3), "")
                    val string24: String? =
                        store!!.getString(getString(R.string.academic_percgpa3), "")
                    if (!(store!!.getString(
                            getString(R.string.academic_percgpa3_radio),
                            ""
                        ) == "Percentage")
                    ) {
                        str3 = "CGPA"
                    }
                    val textView10: TextView? = this.tv_clgnm3
                    textView10!!.setText("• " + string21)
                    val textView11: TextView? = this.tv_year3
                    textView11!!.setText("| " + string22)
                    tv_degree3!!.setText(string23)
                    tv_pernum3!!.setText(string24)
                    tv_perorcpga3!!.setText(str3)
                } else {
                    layoutedu3!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.experience_organization1))) {
                    Log.d(
                        "exp1>>>",
                        store!!.contains(getString(R.string.experience_organization1))
                            .toString() + ">"
                    )
                    val string25: String? =
                        store!!.getString(getString(R.string.experience_organization1), "")
                    val string26: String? =
                        store!!.getString(getString(R.string.experience_designation1), "")
                    val string27: String? =
                        store!!.getString(getString(R.string.experience_role1), "")
                    val string28: String? =
                        store!!.getString(getString(R.string.experience_fromdate1), "")
                    val string29: String? =
                        store!!.getString(getString(R.string.experience_todate1), "")
                    val textView12: TextView? = this.tv_orgnm1
                    textView12!!.setText("• " + string25)
                    tv_desig1!!.setText(string26)
                    tvrole1!!.setText(string27)
                    val textView13: TextView? = this.tv_fromtime1
                    textView13!!.setText("|" + string28 + "-" + string29)
                } else {
                    layoutwexp1!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.experience_organization2))) {
                    val string30: String? =
                        store!!.getString(getString(R.string.experience_organization2), "")
                    val string31: String? =
                        store!!.getString(getString(R.string.experience_designation2), "")
                    val string32: String? =
                        store!!.getString(getString(R.string.experience_role2), "")
                    val string33: String? =
                        store!!.getString(getString(R.string.experience_fromdate2), "")
                    val string34: String? =
                        store!!.getString(getString(R.string.experience_todate2), "")
                    val textView14: TextView? = this.tv_orgnm2
                    textView14!!.setText("• " + string30)
                    tv_desig2!!.setText(string31)
                    tvrole2!!.setText(string32)
                    val textView15: TextView? = this.tv_fromtime2
                    textView15!!.setText("|" + string33 + "-" + string34)
                } else {
                    layoutwexp2!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.experience_organization3))) {
                    val string35: String? =
                        store!!.getString(getString(R.string.experience_organization3), "")
                    val string36: String? =
                        store!!.getString(getString(R.string.experience_designation3), "")
                    val string37: String? =
                        store!!.getString(getString(R.string.experience_role3), "")
                    val string38: String? =
                        store!!.getString(getString(R.string.experience_fromdate3), "")
                    val string39: String? =
                        store!!.getString(getString(R.string.experience_todate3), "")
                    val textView16: TextView? = this.tv_orgnm3
                    textView16!!.setText("• " + string35)
                    tv_desig3!!.setText(string36)
                    tvrole3!!.setText(string37)
                    val textView17: TextView? = this.tv_fromtime3
                    textView17!!.setText("|" + string38 + "-" + string39)
                } else {
                    layoutwexp3!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.language1))) {
                    val string40: String? =
                        store!!.getString(getString(R.string.language1), "")
                    val textView18: TextView? = this.tv_lan1
                    textView18!!.setText("• " + string40)
                } else {
                    tv_lan1!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.language2))) {
                    val string41: String? =
                        store!!.getString(getString(R.string.language2), "")
                    val textView19: TextView? = this.tv_lan2
                    textView19!!.setText("• " + string41)
                } else {
                    tv_lan2!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.language3))) {
                    val string42: String? =
                        store!!.getString(getString(R.string.language3), "")
                    val textView20: TextView? = this.tv_lan3
                    textView20!!.setText("• " + string42)
                } else {
                    tv_lan3!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.language4))) {
                    val string43: String? =
                        store!!.getString(getString(R.string.language4), "")
                    val textView21: TextView? = this.tv_lan4
                    textView21!!.setText("• " + string43)
                } else {
                    tv_lan4!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.language5))) {
                    val string44: String? =
                        store!!.getString(getString(R.string.language5), "")
                    val textView22: TextView? = this.tv_lan5
                    textView22!!.setText("• " + string44)
                } else {
                    tv_lan5!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.hobby1))) {
                    val string45: String? =
                        store!!.getString(getString(R.string.hobby1), "")
                    val textView23: TextView? = this.tv_hobby1
                    textView23!!.setText("• " + string45)
                } else {
                    tv_hobby1!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.hobby2))) {
                    val string46: String? =
                        store!!.getString(getString(R.string.hobby2), "")
                    val textView24: TextView? = this.tv_hobby2
                    textView24!!.setText("• " + string46)
                } else {
                    tv_hobby2!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.hobby3))) {
                    val string47: String? =
                        store!!.getString(getString(R.string.hobby3), "")
                    val textView25: TextView? = this.tv_hobby3
                    textView25!!.setText("• " + string47)
                } else {
                    tv_hobby3!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.hobby4))) {
                    val string48: String? =
                        store!!.getString(getString(R.string.hobby4), "")
                    val textView26: TextView? = this.tv_hobby4
                    textView26!!.setText("• " + string48)
                } else {
                    tv_hobby4!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.hobby5))) {
                    val string49: String? =
                        store!!.getString(getString(R.string.hobby5), "")
                    val textView27: TextView? = this.tv_hobby5
                    textView27!!.setText("• " + string49)
                } else {
                    tv_hobby5!!.setVisibility(View.GONE)
                }
                if (store!!.contains(getString(R.string.reference_name1))) {
                    val string50: String? =
                        store!!.getString(getString(R.string.reference_name1), "")
                    val string51: String? =
                        store!!.getString(getString(R.string.reference_orgnm1), "")
                    val string52: String? =
                        store!!.getString(getString(R.string.reference_contact1), "")
                    val string53: String? =
                        store!!.getString(getString(R.string.reference_mail1), "")
                    val textView28: TextView? = this.tv_refnm
                    textView28!!.setText("• " + string50)
                    tv_reforgnm!!.setText(string51)
                    tv_refphone!!.setText(string52)
                    tv_refemail!!.setText(string53)
                    return
                }
                layoutref!!.setVisibility(View.GONE)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>r1", "null")
            }
        }

    fun createPdf() {
        val windowManager: WindowManager = getSystemService("window") as WindowManager
        val displayMetrics: DisplayMetrics = DisplayMetrics()
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics)
        val i: Int = displayMetrics.heightPixels
        val i2: Int = displayMetrics.widthPixels
        val pdfDocument: PdfDocument = PdfDocument()
        val startPage: PdfDocument.Page = pdfDocument.startPage(
            PageInfo.Builder(
                screenWidth.toInt(),
                newHeight.toInt(), 1
            ).create()
        )
        val canvas: Canvas = startPage.getCanvas()
        val paint: Paint = Paint()
        canvas.drawPaint(paint)
        this.bitmap = Bitmap.createScaledBitmap(
            (bitmap)!!,
            screenWidth.toInt(),
            newHeight.toInt(), true
        )
        paint.setColor(-16776961)
        canvas.drawBitmap(bitmap!!, 0.0f, 0.0f, null as Paint?)
        pdfDocument.finishPage(startPage)
        val externalStorageDirectory: File = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_DOWNLOADS
        )
        val file: File =
            File(externalStorageDirectory.getAbsolutePath() + File.separator + getString(R.string.app_name))
        scanFile(externalStorageDirectory.getAbsolutePath())
        if (!file.exists()) {
            file.mkdirs()
        }


        val save_file: File =
            File(file.toString() + "/" + String.format("%d.pdf", System.currentTimeMillis()))
        try {
            pdfDocument.writeTo(FileOutputStream(save_file))
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Something wrong: " + e.toString(), Toast.LENGTH_LONG).show()
        }
        pdfDocument.close()
        Toast.makeText(this, "PDF saved =" + save_file.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun scanFile(str: String) {
        MediaScannerConnection.scanFile(this, arrayOf(str), null, object : OnScanCompletedListener {
            override fun onScanCompleted(str2: String, uri: Uri) {
                Log.d("scan>>", "Finished scanning " + str2)
            }
        })
    }

    private fun openGeneratedPDF() {
        val file: File = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                .getAbsolutePath() + "/DCIM/pdffromlayout.pdf"
        )
        if (file.exists()) {
            val intent: Intent = Intent("android.intent.action.VIEW")
            intent.setDataAndType(Uri.fromFile(file), "application/pdf")
            intent.setFlags(67108864)
            try {
                startActivity(intent)
            } catch (unused: ActivityNotFoundException) {
                Toast.makeText(
                    this,
                    "No Application available to createdpdf pdf",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    fun refreshActivity() {
        finish()
        overridePendingTransition(0, 0)
        startActivity(getIntent())
        overridePendingTransition(0, 0)
    }

    override fun onResume() {
        super.onResume()
    }

    companion object {
        fun loadBitmapFromView(view: View?, i: Int, i2: Int): Bitmap {
            val createBitmap: Bitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888)
            view!!.draw(Canvas(createBitmap))
            return createBitmap
        }
    }
}
