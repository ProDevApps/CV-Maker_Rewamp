package com.professorapps.cvmaker.fragments

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.SharedPreferences
import com.professorapps.cvmaker.adapters.ExperienceAdapter
import com.professorapps.cvmaker.models.ExperienceModel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

class ExperienceFragment() : Fragment() {
    var bundle: Bundle? = null
    var designaion: String? = null
    var et_Designaion: EditText? = null
    var et_Organization: EditText? = null
    var et_Role: EditText? = null
    var flag: Int = 0
    var fromdate: String? = null
    var ll_save: ImageView? = null
    var organization: String? = null
    var position: Int = 0
    var radiogpexp: RadioGroup? = null
    var rb_Employed: RadioButton? = null
    var rb_Employee: RadioButton? = null
    var role: String? = null
    lateinit var store: SharedPreferences
    var strObj: String? = null
    var todate: String? = null
    var tv_FromDate: TextView? = null
    var tv_toDate: TextView? = null
    var userdesignation: String? = null
    var userempradio: String? = null
    var userfromtime: String? = null
    var userorganization: String? = null
    var userrole: String? = null
    var usertotime: String? = null
    lateinit var rootView: View



    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
         rootView = layoutInflater.inflate(R.layout.activity_experience, viewGroup, false)



        showback = true
        this.store = SharedPreferences.getInstance(requireContext(), "resumemaker")!!
        this.et_Organization = rootView.findViewById<View>(R.id.et_Organization) as EditText
        this.et_Designaion = rootView.findViewById<View>(R.id.et_Designaion) as EditText
        this.tv_FromDate = rootView.findViewById<View>(R.id.tv_FromDate) as TextView
        this.tv_toDate = rootView.findViewById<View>(R.id.tv_toDate) as TextView
        this.radiogpexp = rootView.findViewById<View>(R.id.radiogpexp) as RadioGroup
        this.rb_Employed = rootView.findViewById<View>(R.id.rb_Employed) as RadioButton
        this.rb_Employee = rootView.findViewById<View>(R.id.rb_Employee) as RadioButton
        this.et_Role = rootView.findViewById<View>(R.id.et_Role) as EditText
        this.ll_save = rootView.findViewById<View>(R.id.ll_save) as ImageView
        this.bundle = arguments
        if (this.bundle != null) {
            gsonData
        }
        tv_FromDate!!.setOnClickListener(View.OnClickListener { view ->
            val experienceFragment = this@ExperienceFragment
            experienceFragment.setDate(view, experienceFragment.tv_FromDate)
        })
        tv_toDate!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val experienceFragment = this@ExperienceFragment
                experienceFragment.setDate(view, experienceFragment.tv_toDate)
            }
        })
        radiogpexp!!.setOnCheckedChangeListener(object : RadioGroup.OnCheckedChangeListener {
            override fun onCheckedChanged(radioGroup: RadioGroup, i: Int) {
                if (i == rb_Employed!!.id) {
                    tv_toDate!!.isClickable = true
                    tv_toDate!!.isEnabled = true
                    tv_toDate!!.hint = "To time"
                    tv_toDate!!.text = ""
                    return
                }
                Log.d("radioid>>2", i.toString() + "")
                tv_toDate!!.isClickable = false
                tv_toDate!!.isEnabled = false
                tv_toDate!!.text = "Continue"
            }
        })
        ll_save!!.setOnClickListener(object : View.OnClickListener {
            @SuppressLint("UseRequireInsteadOfGet")
            override fun onClick(view: View) {
                val experienceFragment = this@ExperienceFragment
                if (experienceFragment.isEmpty(experienceFragment.et_Organization)) {
                    Toast.makeText(
                        this@ExperienceFragment.activity,
                        "Please Enter Organization",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val experienceFragment2 = this@ExperienceFragment
                if (experienceFragment2.isEmpty(experienceFragment2.et_Designaion)) {
                    Toast.makeText(
                        this@ExperienceFragment.activity,
                        "Please Enter Designaion",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val experienceFragment3 = this@ExperienceFragment
                if (experienceFragment3.isEmpty(experienceFragment3.et_Role)) {
                    Toast.makeText(
                        this@ExperienceFragment.activity,
                        "Please Enter Role",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val experienceFragment4 = this@ExperienceFragment
                if (experienceFragment4.isEmpty(experienceFragment4.tv_FromDate)) {
                    Toast.makeText(
                        this@ExperienceFragment.activity,
                        "Please Select From Time",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val experienceFragment5 = this@ExperienceFragment
                if (experienceFragment5.isEmpty(experienceFragment5.tv_toDate)) {
                    Toast.makeText(
                        this@ExperienceFragment.activity,
                        "Please Select To Time",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val experienceFragment6 = this@ExperienceFragment
                experienceFragment6.organization =
                    experienceFragment6.et_Organization!!.text.toString()
                val experienceFragment7 = this@ExperienceFragment
                experienceFragment7.designaion = experienceFragment7.et_Designaion!!.text.toString()
                val experienceFragment8 = this@ExperienceFragment
                experienceFragment8.role = experienceFragment8.et_Role!!.text.toString()
                val experienceFragment9 = this@ExperienceFragment
                experienceFragment9.fromdate = experienceFragment9.tv_FromDate!!.text.toString()
                val experienceFragment10 = this@ExperienceFragment
                experienceFragment10.todate = experienceFragment10.tv_toDate!!.text.toString()
                val radioButton = radiogpexp!!.findViewById<View>(
                    radiogpexp!!.checkedRadioButtonId
                ) as RadioButton
                if (this@ExperienceFragment.bundle != null) {
                    if ((store.getString(
                            this@ExperienceFragment.getString(R.string.experience_organization1),
                            ""
                        ) == this@ExperienceFragment.userorganization)
                    ) {
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_organization1),
                            this@ExperienceFragment.organization
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_designation1),
                            this@ExperienceFragment.designaion
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_role1),
                            this@ExperienceFragment.role
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_fromdate1),
                            this@ExperienceFragment.fromdate
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_todate1),
                            this@ExperienceFragment.todate
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_pre_cur1_radio),
                            radioButton.text.toString()
                        )
                        showback = false
                        this@ExperienceFragment.activity!!.onBackPressed()
                    }
                    if ((store.getString(
                            this@ExperienceFragment.getString(R.string.experience_organization2),
                            ""
                        ) == this@ExperienceFragment.userorganization)
                    ) {
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_organization2),
                            this@ExperienceFragment.organization
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_designation2),
                            this@ExperienceFragment.designaion
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_role2),
                            this@ExperienceFragment.role
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_fromdate2),
                            this@ExperienceFragment.fromdate
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_todate2),
                            this@ExperienceFragment.todate
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_pre_cur2_radio),
                            radioButton.text.toString()
                        )
                        showback = false
                        this@ExperienceFragment.activity!!.onBackPressed()
                    }
                    if ((store.getString(
                            this@ExperienceFragment.getString(R.string.experience_organization3),
                            ""
                        ) == this@ExperienceFragment.userorganization)
                    ) {
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_organization3),
                            this@ExperienceFragment.organization
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_designation3),
                            this@ExperienceFragment.designaion
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_role3),
                            this@ExperienceFragment.role
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_fromdate3),
                            this@ExperienceFragment.fromdate
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_todate3),
                            this@ExperienceFragment.todate
                        )
                        store.saveString(
                            this@ExperienceFragment.getString(R.string.experience_pre_cur3_radio),
                            radioButton.text.toString()
                        )
                        showback = false
                        this@ExperienceFragment.activity!!.onBackPressed()
                    }
                } else if ((store.getString(
                        this@ExperienceFragment.getString(R.string.experience_organization1),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_organization1),
                        this@ExperienceFragment.organization
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_designation1),
                        this@ExperienceFragment.designaion
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_role1),
                        this@ExperienceFragment.role
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_fromdate1),
                        this@ExperienceFragment.fromdate
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_todate1),
                        this@ExperienceFragment.todate
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_pre_cur1_radio),
                        radioButton.text.toString()
                    )
                    showback = false
                    this@ExperienceFragment.activity!!.onBackPressed()
                } else if ((store.getString(
                        this@ExperienceFragment.getString(R.string.experience_organization2),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_organization2),
                        this@ExperienceFragment.organization
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_designation2),
                        this@ExperienceFragment.designaion
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_role2),
                        this@ExperienceFragment.role
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_fromdate2),
                        this@ExperienceFragment.fromdate
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_todate2),
                        this@ExperienceFragment.todate
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_pre_cur2_radio),
                        radioButton.text.toString()
                    )
                    showback = false
                    this@ExperienceFragment.activity!!.onBackPressed()
                } else if ((store.getString(
                        this@ExperienceFragment.getString(R.string.experience_organization3),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_organization3),
                        this@ExperienceFragment.organization
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_designation3),
                        this@ExperienceFragment.designaion
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_role3),
                        this@ExperienceFragment.role
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_fromdate3),
                        this@ExperienceFragment.fromdate
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_todate3),
                        this@ExperienceFragment.todate
                    )
                    store.saveString(
                        this@ExperienceFragment.getString(R.string.experience_pre_cur3_radio),
                        radioButton.text.toString()
                    )
                    showback = false
                    this@ExperienceFragment.activity!!.onBackPressed()
                    return
                } else {
                    Toast.makeText(
                        this@ExperienceFragment.activity,
                        "Sorry, more than 3 details are not allowed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        return rootView
    }

    fun setDate(view: View?, textView: TextView?) {
        val instance = Calendar.getInstance()
        DatePickerDialog((activity)!!, object : OnDateSetListener {
            override fun onDateSet(datePicker: DatePicker, i: Int, i2: Int, i3: Int) {
                val textView2 = textView
                textView2!!.text = i3.toString() + "-" + (i2 + 1) + "-" + i
            }
        }, instance[1], instance[2], instance[5]).show()
    }


    fun isEmpty(editText: EditText?): Boolean {
        return editText!!.text.toString().trim { it <= ' ' }.length == 0
    }


    fun isEmpty(textView: TextView?): Boolean {
        return textView!!.text.toString().trim { it <= ' ' }.length == 0
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.getString(getString(R.string.experience_organization1), "") != null) {
                    Log.d(  "sharedpref>>2",   store!!.getString(getString(R.string.experience_organization1), "").toString())
                    val string =
                        store!!.getString(getString(R.string.experience_organization1), "")
                    val string2 =
                        store!!.getString(getString(R.string.experience_designation1), "")
                    val string3 =
                        store!!.getString(getString(R.string.experience_role1), "")
                    val string4 =
                        store!!.getString(getString(R.string.experience_fromdate1), "")
                    val string5 =
                        store!!.getString(getString(R.string.experience_todate1), "")
                    val string6 =
                        store!!.getString(getString(R.string.experience_pre_cur1_radio), "")
                    et_Organization!!.setText(string)
                    et_Designaion!!.setText(string2)
                    et_Role!!.setText(string3)
                    tv_FromDate!!.text = string4
                    tv_toDate!!.text = string5
                    if ((string6 == "Previously employed")) {
                        rb_Employed!!.isChecked = true
                    } else {
                        rb_Employee!!.isChecked = true
                    }
                }
            } catch (e: Exception) {
                while (true) {
                    e.printStackTrace()
                    Log.v("sharedpref>>2", "null")
                    return
                }
            }
        }

    private val gsonData: Unit
        get() {
            try {
                Gson()
                this.strObj = bundle!!.getString("obj")
                this.position = bundle!!.getInt("position", 0)
                val list = Gson().fromJson<Any>(
                    this.strObj,
                    object : TypeToken<List<ExperienceModel?>?>() {
                    }.type
                ) as List<*>
                this.userorganization =
                    (list[ExperienceAdapter.selectedposition] as ExperienceModel).organization
                this.userdesignation =
                    (list[ExperienceAdapter.selectedposition] as ExperienceModel).designation
                this.userrole = (list[ExperienceAdapter.selectedposition] as ExperienceModel).role
                this.userfromtime =
                    (list[ExperienceAdapter.selectedposition] as ExperienceModel).fromtime
                this.usertotime =
                    (list[ExperienceAdapter.selectedposition] as ExperienceModel).totime
                this.userempradio =
                    (list[ExperienceAdapter.selectedposition] as ExperienceModel).empradio
                et_Organization!!.setText(this.userorganization)
                et_Designaion!!.setText(this.userdesignation)
                et_Role!!.setText(this.userrole)
                tv_FromDate!!.text = this.userfromtime
                tv_toDate!!.text = this.usertotime
                if ((this.userempradio == "Previously employed")) {
                    rb_Employed!!.isChecked = true
                    tv_toDate!!.isClickable = true
                    tv_toDate!!.isEnabled = true
                    return
                }
                rb_Employee!!.isChecked = true
                tv_toDate!!.isClickable = false
                tv_toDate!!.isEnabled = false
            } catch (e: Exception) {
                while (true) {
                    e.printStackTrace()
                    Log.v("sharedpref>>2", "null")
                    return
                }
            }
        }

    companion object {
        @JvmField
        var showback: Boolean = true
        fun CheckDates(str: String?, str2: String?): Boolean {
            val simpleDateFormat = SimpleDateFormat("dd-MMM-yyyy")
            try {
                if (!simpleDateFormat.parse(str).before(simpleDateFormat.parse(str2))) {
                    if (simpleDateFormat.parse(str) != simpleDateFormat.parse(str2)) {
                        return false
                    }
                }
                return true
            } catch (e: ParseException) {
                e.printStackTrace()
                return false
            }
        }
    }
}
