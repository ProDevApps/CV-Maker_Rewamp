package com.professorapps.cvmaker.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.SharedPreferences
import com.professorapps.cvmaker.adapters.LanguageAdapter
import com.professorapps.cvmaker.models.LanguageModel

class LanguageFragment() : Fragment() {
    var bundle: Bundle? = null
    var et_language: EditText? = null
    var flag: Int = 0
    var language: String? = null
    var ll_save: ImageView? = null
    var position: Int = 0
    lateinit var store: SharedPreferences
    var strObj: String? = null
    var userlanguage: String? = null
   lateinit var rootView: View


    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
        rootView = layoutInflater.inflate(R.layout.activity_language, viewGroup, false)
        showback = true
        this.store = SharedPreferences.getInstance(requireContext(), "resumemaker")!!
        this.et_language = rootView.findViewById<View>(R.id.et_language) as EditText?
        this.ll_save = rootView.findViewById<View>(R.id.ll_save) as ImageView?
        this.bundle = getArguments()
        if (this.bundle != null) {
            gsonData
        }
        ll_save!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val languageFragment: LanguageFragment = this@LanguageFragment
                if (languageFragment.isEmpty(languageFragment.et_language)) {
                    Toast.makeText(
                        this@LanguageFragment.getActivity(),
                        "Please Enter Language",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val languageFragment2: LanguageFragment = this@LanguageFragment
                languageFragment2.language = languageFragment2.et_language!!.getText().toString()
                if (this@LanguageFragment.bundle != null) {
                    if ((store.getString(
                            this@LanguageFragment.getString(R.string.language1),
                            ""
                        ) == this@LanguageFragment.userlanguage)
                    ) {
                        store.saveString(
                            this@LanguageFragment.getString(R.string.language1),
                            this@LanguageFragment.language
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@LanguageFragment.getString(R.string.language2),
                            ""
                        ) == this@LanguageFragment.userlanguage)
                    ) {
                        store.saveString(
                            this@LanguageFragment.getString(R.string.language2),
                            this@LanguageFragment.language
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@LanguageFragment.getString(R.string.language3),
                            ""
                        ) == this@LanguageFragment.userlanguage)
                    ) {
                        store.saveString(
                            this@LanguageFragment.getString(R.string.language3),
                            this@LanguageFragment.language
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@LanguageFragment.getString(R.string.language4),
                            ""
                        ) == this@LanguageFragment.userlanguage)
                    ) {
                        store.saveString(
                            this@LanguageFragment.getString(R.string.language4),
                            this@LanguageFragment.language
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@LanguageFragment.getString(R.string.language5),
                            ""
                        ) == this@LanguageFragment.userlanguage)
                    ) {
                        store.saveString(
                            this@LanguageFragment.getString(R.string.language5),
                            this@LanguageFragment.language
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                } else if ((store.getString(
                        this@LanguageFragment.getString(R.string.language1),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@LanguageFragment.getString(R.string.language1),
                        this@LanguageFragment.language
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@LanguageFragment.getString(R.string.language2),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@LanguageFragment.getString(R.string.language2),
                        this@LanguageFragment.language
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@LanguageFragment.getString(R.string.language3),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@LanguageFragment.getString(R.string.language3),
                        this@LanguageFragment.language
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@LanguageFragment.getString(R.string.language4),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@LanguageFragment.getString(R.string.language4),
                        this@LanguageFragment.language
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@LanguageFragment.getString(R.string.language5),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@LanguageFragment.getString(R.string.language5),
                        this@LanguageFragment.language
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(
                        this@LanguageFragment.getActivity(),
                        "Sorry, more than 5 details are not allowed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        })
        return rootView
    }


    fun isEmpty(editText: EditText?): Boolean {
        return editText!!.getText().toString().trim({ it <= ' ' }).length == 0
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.getString(getString(R.string.language1), "") != null) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.language1), "").toString()
                    )
                    this.userlanguage =
                        store!!.getString(getString(R.string.language1), "")
                    et_language!!.setText(this.userlanguage)
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
                this.userlanguage = ((Gson().fromJson<Any>(
                    this.strObj,
                    object : TypeToken<List<LanguageModel?>?>() {
                    }.getType()
                ) as List<*>).get(LanguageAdapter.selectedposition) as LanguageModel).language
                et_language!!.setText(this.userlanguage)
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
