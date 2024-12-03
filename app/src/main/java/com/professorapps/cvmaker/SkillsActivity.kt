package com.professorapps.cvmaker

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.professorapps.cvmaker.adapters.SkillAdapter
import com.professorapps.cvmaker.fragments.SkillsFragment
import com.professorapps.cvmaker.interfaces.SkillClick
import com.professorapps.cvmaker.models.SkillModel

class SkillsActivity() : AppCompatActivity(), SkillClick {
    var about: String? = null
    var adapter: SkillAdapter? = null
    var back: LinearLayout? = null
    var btn_add: ImageView? = null
    var dataList: MutableList<SkillModel?>? = null
    var flag: Int = 0
    var itemdata: SkillModel? = null
    var layout_toolbar: LinearLayout? = null
    var rcitem: RecyclerView? = null
    var store: SharedPreferences? = null
    var titaltext: TextView? = null


    @RequiresApi(api = 24)
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.items_list)
        initToolbar()
        this.store = SharedPreferences.Companion.getInstance(this, "resumemaker")
        this.titaltext = findViewById<View>(R.id.titaltext) as TextView?
        this.btn_add = findViewById<View>(R.id.btn_add) as ImageView?
        btn_add!!.setImageResource(R.drawable.add5)
        this.rcitem = findViewById<View>(R.id.rcitem) as RecyclerView?
        rcitem!!.setLayoutManager(LinearLayoutManager(this))
        initData()
        titaltext!!.setText("Skilles")
        btn_add!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                getSupportFragmentManager().beginTransaction()
                    .add(R.id.f1_container, SkillsFragment()).addToBackStack(null).commit()
            }
        })
        this.adapter = SkillAdapter(this,  dataList!!, this)
        rcitem!!.setAdapter(this.adapter)
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
                if (store!!.contains(getString(R.string.skill1))) {
                    Log.d(
                        "sharedpref>>1",
                        (store!!.getString(getString(R.string.skill1), ""))!!
                    )
                    dataList!!.add(SkillModel(store!!.getString(getString(R.string.skill1), "")))
                }
                if (store!!.contains(getString(R.string.skill2))) {
                    Log.d(
                        "sharedpref>>2",
                        (store!!.getString(getString(R.string.skill2), ""))!!
                    )
                    dataList!!.add(SkillModel(store!!.getString(getString(R.string.skill2), "")))
                }
                if (store!!.contains(getString(R.string.skill3))) {
                    Log.d(
                        "sharedpref>>3",
                        (store!!.getString(getString(R.string.skill3), ""))!!
                    )
                    dataList!!.add(SkillModel(store!!.getString(getString(R.string.skill3), "")))
                }
                if (store!!.contains(getString(R.string.skill4))) {
                    Log.d(
                        "sharedpref>>4",
                        (store!!.getString(getString(R.string.skill4), ""))!!
                    )
                    dataList!!.add(SkillModel(store!!.getString(getString(R.string.skill4), "")))
                }
                if (store!!.contains(getString(R.string.skill5))) {
                    Log.d(
                        "sharedpref>>5",
                        (store!!.getString(getString(R.string.skill5), ""))!!
                    )
                    dataList!!.add(SkillModel(store!!.getString(getString(R.string.skill5), "")))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }

    override fun onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed()
        } else if (SkillsFragment.showback) {
            backdialog()
        } else {
            getSupportFragmentManager().popBackStack()
            finish()
            overridePendingTransition(0, 0)
            startActivity(getIntent())
            overridePendingTransition(0, 0)
        }
    }

    override fun callback(skillModel: SkillModel?) {
        this.itemdata = skillModel
        nextflag()
    }

    private fun nextflag() {
        val gson: Gson = Gson()
        SkillModel()
        val skillsFragment: SkillsFragment = SkillsFragment()
        val bundle: Bundle = Bundle()
        bundle.putInt("position", SkillAdapter.selectedposition)
        bundle.putString("obj", gson.toJson(this.dataList))
        skillsFragment.setArguments(bundle)
        val beginTransaction: FragmentTransaction = getSupportFragmentManager().beginTransaction()
        beginTransaction.setCustomAnimations(R.anim.frag_slide_up, 0, 0, R.anim.frag_slide_down)
        beginTransaction.replace(R.id.f1_container, skillsFragment)
        beginTransaction.addToBackStack(null)
        beginTransaction.commit()
        Log.d("state>>put", gson.toJson(this.dataList))
    }

    override fun onResume() {
        super.onResume()
        val skillAdapter: SkillAdapter? = this.adapter
        if (skillAdapter != null) {
            skillAdapter.notifyDataSetChanged()
        }
    }

    private fun initToolbar() {
        val window: Window = getWindow()
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.color_skill))
        }
        this.layout_toolbar = findViewById<View>(R.id.layout_toolbar) as LinearLayout?
        layout_toolbar!!.setBackgroundResource(R.drawable.toolbar_skill)
        this.back = findViewById<View>(R.id.back) as LinearLayout?
        back!!.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                this@SkillsActivity.onBackPressed()
            }
        })
    }

    fun backdialog() {
        val r0: DialogInterface.OnClickListener = object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface, i: Int) {
                if (i == -3) {
                    dialogInterface.dismiss()
                } else if (i == -1) {
                    dialogInterface.dismiss()
                    getSupportFragmentManager().popBackStack()
                    this@SkillsActivity.finish()
                    this@SkillsActivity.overridePendingTransition(0, 0)
                    val skillsActivity: SkillsActivity = this@SkillsActivity
                    skillsActivity.startActivity(skillsActivity.getIntent())
                    this@SkillsActivity.overridePendingTransition(0, 0)
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
