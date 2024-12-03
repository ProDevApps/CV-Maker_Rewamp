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
import com.professorapps.cvmaker.adapters.HobbiesAdapter
import com.professorapps.cvmaker.models.HobbiesModel

class HobbiesFragment() : Fragment() {
    var bundle: Bundle? = null
    var et_hobbies: EditText? = null
    var flag: Int = 0
    var hobbies: String? = null
    var ll_save: ImageView? = null
    var position: Int = 0
    lateinit var store: SharedPreferences
    var strObj: String? = null
    var userhobby: String? = null
    lateinit var rootView: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("HobbiesFragment", "Fragment loaded")
        rootView= inflater.inflate(R.layout.activity_hobbies, container, false)


        showback = true
        this.store = SharedPreferences.getInstance(requireContext(), "resumemaker")!!
        this.et_hobbies = rootView.findViewById<View>(R.id.et_hobbies) as EditText?
        this.ll_save = rootView.findViewById<View>(R.id.ll_save) as ImageView?
        this.bundle = getArguments()
        if (this.bundle != null) {
            gsonData
        }
        ll_save!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val hobbiesFragment: HobbiesFragment = this@HobbiesFragment
                if (hobbiesFragment.isEmpty(hobbiesFragment.et_hobbies)) {
                    Toast.makeText(
                        this@HobbiesFragment.getActivity(),
                        "Please Enter Hobby",
                        Toast.LENGTH_SHORT
                    ).show()
                    return
                }
                val hobbiesFragment2: HobbiesFragment = this@HobbiesFragment
                hobbiesFragment2.hobbies = hobbiesFragment2.et_hobbies!!.getText().toString()
                if (this@HobbiesFragment.bundle != null) {
                    if ((store.getString(
                            this@HobbiesFragment.getString(R.string.hobby1),
                            ""
                        ) == this@HobbiesFragment.userhobby)
                    ) {
                        store.saveString(
                            this@HobbiesFragment.getString(R.string.hobby1),
                            this@HobbiesFragment.hobbies
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@HobbiesFragment.getString(R.string.hobby2),
                            ""
                        ) == this@HobbiesFragment.userhobby)
                    ) {
                        store.saveString(
                            this@HobbiesFragment.getString(R.string.hobby2),
                            this@HobbiesFragment.hobbies
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@HobbiesFragment.getString(R.string.hobby3),
                            ""
                        ) == this@HobbiesFragment.userhobby)
                    ) {
                        store.saveString(
                            this@HobbiesFragment.getString(R.string.hobby3),
                            this@HobbiesFragment.hobbies
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@HobbiesFragment.getString(R.string.hobby4),
                            ""
                        ) == this@HobbiesFragment.userhobby)
                    ) {
                        store.saveString(
                            this@HobbiesFragment.getString(R.string.hobby4),
                            this@HobbiesFragment.hobbies
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                    if ((store.getString(
                            this@HobbiesFragment.getString(R.string.hobby5),
                            ""
                        ) == this@HobbiesFragment.userhobby)
                    ) {
                        store.saveString(
                            this@HobbiesFragment.getString(R.string.hobby5),
                            this@HobbiesFragment.hobbies
                        )
                        showback = false
                        requireActivity().onBackPressed()
                    }
                } else if ((store.getString(
                        this@HobbiesFragment.getString(R.string.hobby1),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@HobbiesFragment.getString(R.string.hobby1),
                        this@HobbiesFragment.hobbies
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@HobbiesFragment.getString(R.string.hobby2),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@HobbiesFragment.getString(R.string.hobby2),
                        this@HobbiesFragment.hobbies
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@HobbiesFragment.getString(R.string.hobby3),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@HobbiesFragment.getString(R.string.hobby3),
                        this@HobbiesFragment.hobbies
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@HobbiesFragment.getString(R.string.hobby4),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@HobbiesFragment.getString(R.string.hobby4),
                        this@HobbiesFragment.hobbies
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else if ((store.getString(
                        this@HobbiesFragment.getString(R.string.hobby5),
                        ""
                    ) == "")
                ) {
                    store.saveString(
                        this@HobbiesFragment.getString(R.string.hobby5),
                        this@HobbiesFragment.hobbies
                    )
                    showback = false
                    requireActivity().onBackPressed()
                } else {
                    Toast.makeText(
                        this@HobbiesFragment.getActivity(),
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
                if (store!!.getString(getString(R.string.hobby1), "") != null) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.hobby1), "").toString()
                    )
                    this.userhobby =
                        store!!.getString(getString(R.string.hobby1), "")
                    et_hobbies!!.setText(this.userhobby)
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
                this.userhobby =
                    ((Gson().fromJson<Any>(this.strObj, object : TypeToken<List<HobbiesModel?>?>() {
                    }.getType()) as List<*>).get(HobbiesAdapter.selectedposition) as HobbiesModel).hobby
                et_hobbies!!.setText(this.userhobby)
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
