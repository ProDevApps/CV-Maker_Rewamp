package com.professorapps.cvmaker.fragments

import android.annotation.SuppressLint
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
import com.professorapps.cvmaker.adapters.SkillAdapter
import com.professorapps.cvmaker.models.SkillModel

class SkillsFragment : Fragment() {
    var bundle: Bundle? = null
    lateinit var et_skill: EditText
    var flag: Int = 0
    var ll_save: ImageView? = null
    var position: Int = 0
    lateinit var skill: String
    lateinit var store: SharedPreferences
    var strObj: String? = null
    var userskill: String? = null
    lateinit var rootView: View


    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View? {
         rootView = layoutInflater.inflate(R.layout.activity_skills, viewGroup, false)
        showback = true
        this.store = SharedPreferences.getInstance(requireContext(), "resumemaker")!!
        this.et_skill = rootView.findViewById<View>(R.id.et_skill) as EditText
        this.ll_save = rootView.findViewById<View>(R.id.ll_save) as ImageView
        this.bundle = arguments
        if (this.bundle != null) {
            gsonData
        }
        ll_save!!.setOnClickListener(View.OnClickListener {
            val skillsFragment = this@SkillsFragment
            if (skillsFragment.isEmpty(skillsFragment.et_skill)) {
                Toast.makeText(
                    this@SkillsFragment.activity,
                    "Please Enter Skill",
                    Toast.LENGTH_SHORT
                ).show()
                return@OnClickListener
            }
            val skillsFragment2 = this@SkillsFragment
            skillsFragment2.skill = skillsFragment2.et_skill!!.text.toString()
            if (this@SkillsFragment.bundle != null) {
                if (store.getString(
                        this@SkillsFragment.getString(R.string.skill1),
                        ""
                    ) == this@SkillsFragment.userskill
                ) {
                    store.saveString(
                        this@SkillsFragment.getString(R.string.skill1),
                        this@SkillsFragment.skill
                    )
                    showback = false
                    this@SkillsFragment.activity!!.onBackPressed()
                }
                if (store.getString(
                        this@SkillsFragment.getString(R.string.skill2),
                        ""
                    ) == this@SkillsFragment.userskill
                ) {
                    store.saveString(
                        this@SkillsFragment.getString(R.string.skill2),
                        this@SkillsFragment.skill
                    )
                    showback = false
                    this@SkillsFragment.activity!!.onBackPressed()
                }
                if (store.getString(
                        this@SkillsFragment.getString(R.string.skill3),
                        ""
                    ) == this@SkillsFragment.userskill
                ) {
                    store.saveString(
                        this@SkillsFragment.getString(R.string.skill3),
                        this@SkillsFragment.skill
                    )
                    showback = false
                    this@SkillsFragment.activity!!.onBackPressed()
                }
                if (store.getString(
                        this@SkillsFragment.getString(R.string.skill4),
                        ""
                    ) == this@SkillsFragment.userskill
                ) {
                    store.saveString(
                        this@SkillsFragment.getString(R.string.skill4),
                        this@SkillsFragment.skill
                    )
                    showback = false
                    this@SkillsFragment.activity!!.onBackPressed()
                }
                if (store.getString(
                        this@SkillsFragment.getString(R.string.skill5),
                        ""
                    ) == this@SkillsFragment.userskill
                ) {
                    store.saveString(
                        this@SkillsFragment.getString(R.string.skill5),
                        this@SkillsFragment.skill
                    )
                    showback = false
                    this@SkillsFragment.activity!!.onBackPressed()
                }
            } else if (store.getString(this@SkillsFragment.getString(R.string.skill1), "") == "") {
                store.saveString(
                    this@SkillsFragment.getString(R.string.skill1),
                    this@SkillsFragment.skill
                )
                showback = false
                this@SkillsFragment.activity!!.onBackPressed()
            } else if (store.getString(this@SkillsFragment.getString(R.string.skill2), "") == "") {
                store.saveString(
                    this@SkillsFragment.getString(R.string.skill2),
                    this@SkillsFragment.skill
                )
                showback = false
                this@SkillsFragment.activity!!.onBackPressed()
            } else if (store.getString(this@SkillsFragment.getString(R.string.skill3), "") == "") {
                store.saveString(
                    this@SkillsFragment.getString(R.string.skill3),
                    this@SkillsFragment.skill
                )
                showback = false
                this@SkillsFragment.activity!!.onBackPressed()
            } else if (store.getString(this@SkillsFragment.getString(R.string.skill4), "") == "") {
                store.saveString(
                    this@SkillsFragment.getString(R.string.skill4),
                    this@SkillsFragment.skill
                )
                showback = false
                this@SkillsFragment.activity!!.onBackPressed()
            } else if (store.getString(this@SkillsFragment.getString(R.string.skill5), "") == "") {
                store.saveString(
                    this@SkillsFragment.getString(R.string.skill5),
                    this@SkillsFragment.skill
                )
                showback = false
                this@SkillsFragment.activity!!.onBackPressed()
            } else {
                Toast.makeText(
                    this@SkillsFragment.activity,
                    "Sorry, more than 5 details are not allowed",
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
        return rootView
    }


    fun isEmpty(editText: EditText?): Boolean {
        return editText!!.text.toString().trim { it <= ' ' }.length == 0
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.getString(getString(R.string.skill1), "") != null) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.skill1), "").toString()
                    )
                    et_skill!!.setText(store!!.getString(getString(R.string.skill1), ""))
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
                this.userskill =
                    ((Gson().fromJson<Any>(this.strObj, object : TypeToken<List<SkillModel?>?>() {
                    }.type) as List<*>)[SkillAdapter.selectedposition] as SkillModel).skill
                et_skill!!.setText(this.userskill)
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
