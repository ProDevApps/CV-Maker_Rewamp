package com.professorapps.cvmaker.fragments

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.SharedPreferences
import java.util.regex.Pattern

class ReferenceFragment() : Fragment() {
    var desig: String? = null
    var et_rDesignation: EditText? = null
    var et_rMail: EditText? = null
    var et_rName: EditText? = null
    var et_rOrganization: EditText? = null
    var et_rPhone: EditText? = null
    var ll_save: ImageView? = null
    var mail: String? = null
    var name: String? = null
    var orgnm: String? = null
    var phone: String? = null
    lateinit var store: SharedPreferences
    lateinit var rootView: View

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        this.rootView = layoutInflater.inflate(R.layout.activity_references, viewGroup, false)
        this.store = SharedPreferences.getInstance(requireContext(), "resumemaker")!!
        this.et_rName = rootView.findViewById<View>(R.id.et_rName) as EditText?
        this.et_rMail = rootView.findViewById<View>(R.id.et_rMail) as EditText?
        this.et_rPhone = rootView.findViewById<View>(R.id.et_rPhone) as EditText?
        this.et_rOrganization = rootView.findViewById<View>(R.id.et_rOrganization) as EditText?
        this.et_rDesignation = rootView.findViewById<View>(R.id.et_rDesignation) as EditText?
        this.ll_save = rootView.findViewById<View>(R.id.ll_save) as ImageView?
        sharedpref
        ll_save!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val referenceFragment: ReferenceFragment = this@ReferenceFragment
                if (referenceFragment.isEmpty(referenceFragment.et_rName)) {
                    Toast.makeText(
                        this@ReferenceFragment.getActivity(),
                        "Please Enter Name",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceFragment2: ReferenceFragment = this@ReferenceFragment
                if (referenceFragment2.isEmpty(referenceFragment2.et_rMail)) {
                    Toast.makeText(
                        this@ReferenceFragment.getActivity(),
                        "Please Enter Email",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceFragment3: ReferenceFragment = this@ReferenceFragment
                if (referenceFragment3.isEmpty(referenceFragment3.et_rPhone)) {
                    Toast.makeText(
                        this@ReferenceFragment.getActivity(),
                        "Please Enter Contact",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceFragment4: ReferenceFragment = this@ReferenceFragment
                if (referenceFragment4.isEmpty(referenceFragment4.et_rOrganization)) {
                    Toast.makeText(
                        this@ReferenceFragment.getActivity(),
                        "Please Enter Organization",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceFragment5: ReferenceFragment = this@ReferenceFragment
                if (referenceFragment5.isEmpty(referenceFragment5.et_rDesignation)) {
                    Toast.makeText(
                        this@ReferenceFragment.getActivity(),
                        "Please Enter Designation",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceFragment6: ReferenceFragment = this@ReferenceFragment
                if (!referenceFragment6.isValidMobile(
                        referenceFragment6.et_rPhone!!.getText().toString()
                    )
                ) {
                    Toast.makeText(
                        this@ReferenceFragment.getActivity(),
                        "Please Enter valid Phone Number",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceFragment7: ReferenceFragment = this@ReferenceFragment
                if (!referenceFragment7.isValidMail(
                        referenceFragment7.et_rMail!!.getText().toString()
                    )
                ) {
                    Toast.makeText(
                        this@ReferenceFragment.getActivity(),
                        "Please Enter valid Email",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val referenceFragment8: ReferenceFragment = this@ReferenceFragment
                referenceFragment8.name = referenceFragment8.et_rName!!.getText().toString()
                val referenceFragment9: ReferenceFragment = this@ReferenceFragment
                referenceFragment9.mail = referenceFragment9.et_rMail!!.getText().toString()
                val referenceFragment10: ReferenceFragment = this@ReferenceFragment
                referenceFragment10.phone = referenceFragment10.et_rPhone!!.getText().toString()
                val referenceFragment11: ReferenceFragment = this@ReferenceFragment
                referenceFragment11.orgnm =
                    referenceFragment11.et_rOrganization!!.getText().toString()
                val referenceFragment12: ReferenceFragment = this@ReferenceFragment
                referenceFragment12.desig =
                    referenceFragment12.et_rDesignation!!.getText().toString()
                store.saveString(
                    this@ReferenceFragment.getString(R.string.reference_name1),
                    this@ReferenceFragment.name
                )
                store.saveString(
                    this@ReferenceFragment.getString(R.string.reference_mail1),
                    this@ReferenceFragment.mail
                )
                store.saveString(
                    this@ReferenceFragment.getString(R.string.reference_contact1),
                    this@ReferenceFragment.phone
                )
                store.saveString(
                    this@ReferenceFragment.getString(R.string.reference_orgnm1),
                    this@ReferenceFragment.orgnm
                )
                store.saveString(
                    this@ReferenceFragment.getString(R.string.reference_desig1),
                    this@ReferenceFragment.desig
                )
                requireActivity().finish()
            }
        })
        return this.view
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
                        store!!.getString(getString(R.string.reference_name1), "").toString()
                    )
                    val string: String =
                        store!!.getString(getString(R.string.reference_name1), "").toString()
                    val string2: String =
                        store!!.getString(getString(R.string.reference_mail1), "").toString()
                    val string3: String =
                        store!!.getString(getString(R.string.reference_contact1), "").toString()
                    val string4: String =
                        store!!.getString(getString(R.string.reference_orgnm1), "").toString()
                    val string5: String =
                        store!!.getString(getString(R.string.reference_desig1), "").toString()
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
}
