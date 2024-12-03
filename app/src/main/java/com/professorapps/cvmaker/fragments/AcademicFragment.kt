package com.professorapps.cvmaker.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
import android.text.InputFilter.LengthFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.professorapps.cvmaker.adapters.AcademicAdapter
import com.professorapps.cvmaker.models.AcademicModel

class AcademicFragment : Fragment() {
    var bundle: Bundle? = null
    var degree: String? = null
    var et_Degree: EditText? = null
    var et_Institute: EditText? = null
    var et_Marks: EditText? = null
    var et_Year: EditText? = null
    var flag: Int = 0
    var institute: String? = null
    var ll_save: ImageView? = null
    var marks: String? = null
    var position: Int = 0
    var radiogp1: RadioGroup? = null
    var radiogp2: RadioGroup? = null
    var rb_CGPA: RadioButton? = null
    var rb_Graduated: RadioButton? = null
    var rb_Percentage: RadioButton? = null
    var rb_Pursuing: RadioButton? = null
    lateinit var store: SharedPreferences
    var strObj: String? = null
    var userdegree: String? = null
    var userinstitute: String? = null
    var userpercgpa: String? = null
    var userperradio: String? = null
    var useryear: String? = null
    var useryearradio: String? = null
    lateinit var rootView: View
    var year: String? = null


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        this.rootView = layoutInflater.inflate(R.layout.activity_academic, viewGroup, false)

        showback = true
        this.store = SharedPreferences.getInstance(requireContext(), "resumemaker")!!
        this.et_Degree = rootView.findViewById<View>(R.id.et_Degree) as EditText
        this.et_Institute = rootView.findViewById<View>(R.id.et_Institute) as EditText
        this.et_Marks = rootView.findViewById<View>(R.id.et_Marks) as EditText
        this.et_Year = rootView.findViewById<View>(R.id.et_Year) as EditText
        this.ll_save = rootView.findViewById<View>(R.id.ll_save) as ImageView
        this.radiogp1 = rootView.findViewById<View>(R.id.radiogp1) as RadioGroup
        this.radiogp2 = rootView.findViewById<View>(R.id.radiogp2) as RadioGroup
        this.rb_Percentage = rootView.findViewById<View>(R.id.rb_Percentage) as RadioButton
        this.rb_CGPA = rootView.findViewById<View>(R.id.rb_CGPA) as RadioButton
        this.rb_Graduated = rootView.findViewById<View>(R.id.rb_Graduated) as RadioButton
        this.rb_Pursuing = rootView.findViewById<View>(R.id.rb_Pursuing) as RadioButton
        radiogp2!!.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            if (i == rb_Graduated!!.id) {
                et_Year!!.isEnabled = true
                et_Year!!.isClickable = true
                et_Year!!.hint = "Year"
                et_Year!!.setText("")
                et_Year!!.filters = arrayOf<InputFilter>(LengthFilter(4))
                return@OnCheckedChangeListener
            }
            et_Year!!.isEnabled = false
            et_Year!!.isClickable = true
            et_Year!!.filters = arrayOf<InputFilter>(LengthFilter(8))
            et_Year!!.setText("Pursuing")
        })
        this.bundle = arguments
        if (this.bundle != null) {
            gsonData
        }
        ll_save!!.setOnClickListener(View.OnClickListener {
            val radioButton = radiogp1!!.findViewById<View>(
                radiogp1!!.checkedRadioButtonId
            ) as RadioButton
            val academicFragment = this@AcademicFragment
            if (academicFragment.isEmpty(academicFragment.et_Degree)) {
                Toast.makeText(
                    this@AcademicFragment.activity,
                    "Please Enter Degree Name",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val academicFragment2 = this@AcademicFragment
            if (academicFragment2.isEmpty(academicFragment2.et_Institute)) {
                Toast.makeText(
                    this@AcademicFragment.activity,
                    "Please Enter Institute Name",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val academicFragment3 = this@AcademicFragment
            if (academicFragment3.isEmpty(academicFragment3.et_Marks)) {
                Toast.makeText(
                    this@AcademicFragment.activity,
                    "Please Enter Percentage/CGPA",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val academicFragment4 = this@AcademicFragment
            if (academicFragment4.isEmpty(academicFragment4.et_Year)) {
                Toast.makeText(
                    this@AcademicFragment.activity,
                    "Please Enter Year",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (radioButton.text.toString() == "Percentage" && (et_Marks!!.text.toString()
                    .toFloat() <= 1.0f || et_Marks!!.text.toString().toFloat() >= 101.0f)
            ) {
                Toast.makeText(
                    this@AcademicFragment.activity,
                    "please enter valid percentage",
                    Toast.LENGTH_SHORT
                ).show()
            } else if (radioButton.text.toString() != "CGPA" || (et_Marks!!.text.toString()
                    .toFloat() > 1.0f && et_Marks!!.text.toString().toFloat() < 11.0f)
            ) {
                val academicFragment5 = this@AcademicFragment
                academicFragment5.degree = academicFragment5.et_Degree!!.text.toString()
                val academicFragment6 = this@AcademicFragment
                academicFragment6.institute = academicFragment6.et_Institute!!.text.toString()
                val academicFragment7 = this@AcademicFragment
                academicFragment7.marks = academicFragment7.et_Marks!!.text.toString()
                val academicFragment8 = this@AcademicFragment
                academicFragment8.year = academicFragment8.et_Year!!.text.toString()
                val checkedRadioButtonId =
                    radiogp1!!.checkedRadioButtonId
                val checkedRadioButtonId2 =
                    radiogp2!!.checkedRadioButtonId
                val radioButton2 =
                    radiogp1!!.findViewById<View>(checkedRadioButtonId) as RadioButton
                val radioButton3 =
                    radiogp2!!.findViewById<View>(checkedRadioButtonId2) as RadioButton
                if (this@AcademicFragment.bundle != null) {
                    if (store.getString(
                            this@AcademicFragment.getString(R.string.academic_degree1),
                            ""
                        ) == this@AcademicFragment.userdegree
                    ) {
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_degree1),
                            this@AcademicFragment.degree
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_institute1),
                            this@AcademicFragment.institute
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_percgpa1),
                            this@AcademicFragment.marks
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_year1),
                            this@AcademicFragment.year
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_percgpa1_radio),
                            radioButton2.text.toString()
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_year1_radio),
                            radioButton3.text.toString()
                        )
                        Log.d("academic>>1", "academic>>1" + AcademicAdapter.selectedposition)
                        showback = false
                        this@AcademicFragment.activity!!.onBackPressed()
                    }
                    if (store.getString(
                            this@AcademicFragment.getString(R.string.academic_degree2),
                            ""
                        ) == this@AcademicFragment.userdegree
                    ) {
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_degree2),
                            this@AcademicFragment.degree
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_institute2),
                            this@AcademicFragment.institute
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_percgpa2),
                            this@AcademicFragment.marks
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_year2),
                            this@AcademicFragment.year
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_percgpa2_radio),
                            radioButton2.text.toString()
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_year2_radio),
                            radioButton3.text.toString()
                        )
                        Log.d("academic>>2", "academic>>2")
                        showback = false
                        this@AcademicFragment.activity!!.onBackPressed()
                    }
                    if (store.getString(
                            this@AcademicFragment.getString(R.string.academic_degree3),
                            ""
                        ) == this@AcademicFragment.userdegree
                    ) {
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_degree3),
                            this@AcademicFragment.degree
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_institute3),
                            this@AcademicFragment.institute
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_percgpa3),
                            this@AcademicFragment.marks
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_year3),
                            this@AcademicFragment.year
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_percgpa3_radio),
                            radioButton2.text.toString()
                        )
                        store.saveString(
                            this@AcademicFragment.getString(R.string.academic_year3_radio),
                            radioButton3.text.toString()
                        )
                        Log.d("academic>>3", "academic>>3")
                        showback = false
                        this@AcademicFragment.activity!!.onBackPressed()
                    }
                } else if (store.getString(
                        this@AcademicFragment.getString(R.string.academic_degree1),
                        ""
                    ) == ""
                ) {
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_degree1),
                        this@AcademicFragment.degree
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_institute1),
                        this@AcademicFragment.institute
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_percgpa1),
                        this@AcademicFragment.marks
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_year1),
                        this@AcademicFragment.year
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_percgpa1_radio),
                        radioButton2.text.toString()
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_year1_radio),
                        radioButton3.text.toString()
                    )
                    Log.d("academic>>1", "academic>>1" + AcademicAdapter.selectedposition)
                    showback = false
                    this@AcademicFragment.activity!!.onBackPressed()
                } else if (store.getString(
                        this@AcademicFragment.getString(R.string.academic_degree2),
                        ""
                    ) == ""
                ) {
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_degree2),
                        this@AcademicFragment.degree
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_institute2),
                        this@AcademicFragment.institute
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_percgpa2),
                        this@AcademicFragment.marks
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_year2),
                        this@AcademicFragment.year
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_percgpa2_radio),
                        radioButton2.text.toString()
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_year2_radio),
                        radioButton3.text.toString()
                    )
                    Log.d("academic>>2", "academic>>2")
                    showback = false
                    this@AcademicFragment.activity!!.onBackPressed()
                } else if (store.getString(
                        this@AcademicFragment.getString(R.string.academic_degree3),
                        ""
                    ) == ""
                ) {
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_degree3),
                        this@AcademicFragment.degree
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_institute3),
                        this@AcademicFragment.institute
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_percgpa3),
                        this@AcademicFragment.marks
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_year3),
                        this@AcademicFragment.year
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_percgpa3_radio),
                        radioButton2.text.toString()
                    )
                    store.saveString(
                        this@AcademicFragment.getString(R.string.academic_year3_radio),
                        radioButton3.text.toString()
                    )
                    Log.d("academic>>3", "academic>>3")
                    showback = false
                    this@AcademicFragment.activity!!.onBackPressed()
                } else {
                    Toast.makeText(
                        this@AcademicFragment.activity,
                        "Sorry, more than 3 details are not allowed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this@AcademicFragment.activity,
                    "please enter valid CGPA",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return this.view
    }


    fun isEmpty(editText: EditText?): Boolean {
        return editText!!.text.toString().trim { it <= ' ' }.length == 0
    }

    private fun isEmpty(textView: TextView): Boolean {
        return textView.text.toString().trim { it <= ' ' }.length == 0
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.getString(getString(R.string.academic_degree1), "") != null) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.academic_degree1), "").toString()
                    )
                    val string =
                        store!!.getString(getString(R.string.academic_degree1), "")
                    val string2 =
                        store!!.getString(getString(R.string.academic_institute1), "")
                    val string3 =
                        store!!.getString(getString(R.string.academic_percgpa1), "")
                    val string4 =
                        store!!.getString(getString(R.string.academic_year1), "")
                    val string5 =
                        store!!.getString(getString(R.string.academic_percgpa1_radio), "")
                    val string6 =
                        store!!.getString(getString(R.string.academic_year1_radio), "")
                    et_Degree!!.setText(string)
                    et_Institute!!.setText(string2)
                    et_Marks!!.setText(string3)
                    et_Year!!.setText(string4)
                    if (string5 == "Percentage") {
                        rb_Percentage!!.isChecked = true
                    } else {
                        rb_CGPA!!.isChecked = true
                    }
                    if (string6 == "Graduated") {
                        rb_Graduated!!.isChecked = true
                    } else {
                        rb_Pursuing!!.isChecked = true
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }

    private val gsonData: Unit
        get() {
            try {
                Gson()
                this.strObj = bundle!!.getString("obj")
                this.position = bundle!!.getInt("position", 0)
                val list =
                    Gson().fromJson<Any>(this.strObj, object : TypeToken<List<AcademicModel?>?>() {
                    }.type) as List<*>
                this.userdegree = (list[AcademicAdapter.selectedposition] as AcademicModel).degree
                this.userinstitute =
                    (list[AcademicAdapter.selectedposition] as AcademicModel).institute
                this.userpercgpa = (list[AcademicAdapter.selectedposition] as AcademicModel).percgpa
                this.useryear = (list[AcademicAdapter.selectedposition] as AcademicModel).year
                this.userperradio =
                    (list[AcademicAdapter.selectedposition] as AcademicModel).perradio
                this.useryearradio =
                    (list[AcademicAdapter.selectedposition] as AcademicModel).yearradio
                et_Degree!!.setText(this.userdegree)
                et_Institute!!.setText(this.userinstitute)
                et_Marks!!.setText(this.userpercgpa)
                et_Year!!.setText(this.useryear)
                if (this.userperradio == "Percentage") {
                    rb_Percentage!!.isChecked = true
                } else {
                    rb_CGPA!!.isChecked = true
                }
                if (this.useryearradio == "Graduated") {
                    rb_Graduated!!.isChecked = true
                    et_Year!!.filters = arrayOf<InputFilter>(LengthFilter(4))
                    return
                }
                rb_Pursuing!!.isChecked = true
                et_Year!!.filters = arrayOf<InputFilter>(LengthFilter(8))
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }

    companion object {
        @JvmField
        var showback: Boolean = true
    }
}
