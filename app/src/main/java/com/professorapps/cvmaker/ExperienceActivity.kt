package com.professorapps.cvmaker

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.professorapps.cvmaker.SharedPreferences.Companion.getInstance
import com.professorapps.cvmaker.adapters.ExperienceAdapter
import com.professorapps.cvmaker.fragments.ExperienceFragment
import com.professorapps.cvmaker.interfaces.ExperienceClick
import com.professorapps.cvmaker.models.ExperienceModel

class ExperienceActivity : AppCompatActivity(), ExperienceClick {
    var adapter: ExperienceAdapter? = null
    var back: LinearLayout? = null
    var btn_add: ImageView? = null
    var dataList: MutableList<ExperienceModel?>? = null
    var itemdata: ExperienceModel? = null
    var layout_toolbar: LinearLayout? = null
    var rcitem: RecyclerView? = null
    var store: SharedPreferences? = null
    var titaltext: TextView? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.items_list)
        initToolbar()
        this.store = getInstance(this, "resumemaker")
        this.titaltext = findViewById<View>(R.id.titaltext) as TextView
        this.btn_add = findViewById<View>(R.id.btn_add) as ImageView
        btn_add!!.setImageResource(R.drawable.add2)
        this.rcitem = findViewById<View>(R.id.rcitem) as RecyclerView
        rcitem!!.layoutManager = LinearLayoutManager(this)
        initData()
        titaltext!!.text = "Experience"
        btn_add!!.setOnClickListener {
            this@ExperienceActivity.supportFragmentManager.beginTransaction().add(
                R.id.f1_container,
                ExperienceFragment()
            ).addToBackStack(null).commit()
        }
        this.adapter = ExperienceAdapter(this, dataList!!, this)
        rcitem!!.adapter = this.adapter
        adapter!!.notifyDataSetChanged()
    }

    private fun initData() {
        if (this.dataList == null) {
            dataList = mutableListOf()
            sharedpref
        }
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.contains(getString(R.string.experience_organization1))) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.experience_organization1), "")!!
                    )
                    val string =
                        store!!.getString(getString(R.string.experience_organization1), "")
                    val string2 =
                        store!!.getString(getString(R.string.experience_designation1), "")
                    val string3 =
                        store!!.getString(getString(R.string.experience_role1), "")
                    dataList!!.add(
                        ExperienceModel(
                            string,
                            string2,
                            store!!.getString(getString(R.string.experience_fromdate1), ""),
                            store!!.getString(getString(R.string.experience_todate1), ""),
                            store!!.getString(getString(R.string.experience_pre_cur1_radio), ""),
                            string3
                        )
                    )
                    Log.d("academic>>>1", "academic>>>1")
                }
                if (store!!.contains(getString(R.string.experience_organization2))) {
                    val string4 =
                        store!!.getString(getString(R.string.experience_organization2), "")
                    val string5 =
                        store!!.getString(getString(R.string.experience_designation2), "")
                    val string6 =
                        store!!.getString(getString(R.string.experience_role2), "")
                    dataList!!.add(
                        ExperienceModel(
                            string4,
                            string5,
                            store!!.getString(getString(R.string.experience_fromdate2), ""),
                            store!!.getString(getString(R.string.experience_todate2), ""),
                            store!!.getString(getString(R.string.experience_pre_cur2_radio), ""),
                            string6
                        )
                    )
                    Log.d("academic>>>2", "academic>>>2")
                }
                if (store!!.contains(getString(R.string.experience_organization3))) {
                    val string7 =
                        store!!.getString(getString(R.string.experience_organization3), "")
                    val string8 =
                        store!!.getString(getString(R.string.experience_designation3), "")
                    val string9 =
                        store!!.getString(getString(R.string.experience_role3), "")
                    dataList!!.add(
                        ExperienceModel(
                            string7,
                            string8,
                            store!!.getString(getString(R.string.experience_fromdate3), ""),
                            store!!.getString(getString(R.string.experience_todate3), ""),
                            store!!.getString(getString(R.string.experience_pre_cur3_radio), ""),
                            string9
                        )
                    )
                    Log.d("academic>>>3", "academic>>>3")
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else if (ExperienceFragment.showback) {
            backdialog()
        } else {
            supportFragmentManager.popBackStack()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    override fun callback(experienceModel: ExperienceModel?) {
        this.itemdata = experienceModel
        nextflag()
    }

    private fun nextflag() {
        val gson = Gson()
        ExperienceModel()
        val experienceFragment = ExperienceFragment()
        val bundle = Bundle()
        bundle.putInt("position", ExperienceAdapter.selectedposition)
        bundle.putString("obj", gson.toJson(this.dataList))
        experienceFragment.arguments = bundle
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.setCustomAnimations(R.anim.frag_slide_up, 0, 0, R.anim.frag_slide_down)
        beginTransaction.replace(R.id.f1_container, experienceFragment)
        beginTransaction.addToBackStack(null)
        beginTransaction.commit()
        Log.d("state>>put", gson.toJson(this.dataList))
    }

    private fun initToolbar() {
        val window = window
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_experience)
        }
        this.layout_toolbar = findViewById<View>(R.id.layout_toolbar) as LinearLayout
        layout_toolbar!!.setBackgroundResource(R.drawable.toolbar_experience)
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@ExperienceActivity.onBackPressed() }
    }

    override fun onResume() {
        super.onResume()
    }

    fun backdialog() {
        val r0 = DialogInterface.OnClickListener { dialogInterface, i ->
            if (i == -3) {
                dialogInterface.dismiss()
            } else if (i == -1) {
                dialogInterface.dismiss()
                this@ExperienceActivity.supportFragmentManager.popBackStack()
                this@ExperienceActivity.finish()
                this@ExperienceActivity.overridePendingTransition(0, 0)
                val experienceActivity = this@ExperienceActivity
                experienceActivity.startActivity(experienceActivity.intent)
                this@ExperienceActivity.overridePendingTransition(0, 0)
            }
        }
        AlertDialog.Builder(this).setMessage("Discard editing?").setPositiveButton("Ok", r0)
            .setTitle("").setNeutralButton("Cancel", r0).show()
    }

    companion object {
        private const val TAG = "AcademicActivity>>"
    }
}
