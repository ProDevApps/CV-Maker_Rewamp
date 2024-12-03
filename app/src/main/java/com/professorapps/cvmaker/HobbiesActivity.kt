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
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.professorapps.cvmaker.adapters.HobbiesAdapter
import com.professorapps.cvmaker.fragments.HobbiesFragment
import com.professorapps.cvmaker.interfaces.HobbiesClick
import com.professorapps.cvmaker.models.AcademicModel
import com.professorapps.cvmaker.models.HobbiesModel

class HobbiesActivity : AppCompatActivity(), HobbiesClick {
    var about: String? = null
    var adapter: HobbiesAdapter? = null
    var back: LinearLayout? = null
    var btn_add: ImageView? = null
    private var dataList: MutableList<HobbiesModel?>? = null

     var flag: Int = 0
    var itemdata: HobbiesModel? = null
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
        this.titaltext = findViewById<View>(R.id.titaltext) as TextView
        this.btn_add = findViewById<View>(R.id.btn_add) as ImageView
        btn_add!!.setImageResource(R.drawable.add7)
        this.rcitem = findViewById<View>(R.id.rcitem) as RecyclerView
        rcitem!!.layoutManager = LinearLayoutManager(this)
        initData()
        titaltext!!.text = "Hobbies"
        btn_add!!.setOnClickListener {
            this@HobbiesActivity.supportFragmentManager.beginTransaction()
                .add(R.id.f1_container, HobbiesFragment()).addToBackStack(null).commit()
        }
        this.adapter = HobbiesAdapter(this, dataList!!, this)
        rcitem!!.adapter = this.adapter
        adapter!!.notifyDataSetChanged()
    }



    private fun initData() {
        if (dataList == null) {
            dataList = mutableListOf()
            sharedpref
        }
    }

    private val sharedpref: Unit
        get() {
            try {
                if (store!!.contains(getString(R.string.hobby1))) {
                    Log.d(
                        "sharedpref>>hobby1",
                        store!!.getString(getString(R.string.hobby1), "")!!
                    )
                    dataList!!.add(HobbiesModel(store!!.getString(getString(R.string.hobby1), "")))
                }
                if (store!!.contains(getString(R.string.hobby2))) {
                    Log.d(
                        "sharedpref>>hobby2",
                        store!!.getString(getString(R.string.hobby2), "")!!
                    )
                    dataList!!.add(HobbiesModel(store!!.getString(getString(R.string.hobby2), "")))
                }
                if (store!!.contains(getString(R.string.hobby3))) {
                    Log.d(
                        "sharedpref>>hobby3",
                        store!!.getString(getString(R.string.hobby3), "")!!
                    )
                    dataList!!.add(HobbiesModel(store!!.getString(getString(R.string.hobby3), "")))
                }
                if (store!!.contains(getString(R.string.hobby4))) {
                    Log.d(
                        "sharedpref>>hobby4",
                        store!!.getString(getString(R.string.hobby4), "")!!
                    )
                    dataList!!.add(HobbiesModel(store!!.getString(getString(R.string.hobby4), "")))
                }
                if (store!!.contains(getString(R.string.hobby5))) {
                    Log.d(
                        "sharedpref>>hobby5",
                        store!!.getString(getString(R.string.hobby5), "")!!
                    )
                    dataList!!.add(HobbiesModel(store!!.getString(getString(R.string.hobby5), "")))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else if (HobbiesFragment.showback) {
            backdialog()
        } else {
            supportFragmentManager.popBackStack()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    override fun callback(hobbiesModel: HobbiesModel?) {
        this.itemdata = hobbiesModel
        nextflag()
    }

    private fun nextflag() {
        val gson = Gson()
        HobbiesModel()
        val hobbiesFragment = HobbiesFragment()
        val bundle = Bundle()
        bundle.putInt("position", HobbiesAdapter.selectedposition)
        bundle.putString("obj", gson.toJson(this.dataList))
        hobbiesFragment.arguments = bundle
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.setCustomAnimations(R.anim.frag_slide_up, 0, 0, R.anim.frag_slide_down)
        beginTransaction.replace(R.id.f1_container, hobbiesFragment)
        beginTransaction.addToBackStack(null)
        beginTransaction.commit()
        Log.d("state>>put", gson.toJson(this.dataList))
    }

    override fun onResume() {
        super.onResume()
        val hobbiesAdapter = this.adapter
        hobbiesAdapter?.notifyDataSetChanged()
    }

    private fun initToolbar() {
        val window = window
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_hobbies)
        }
        this.layout_toolbar = findViewById<View>(R.id.layout_toolbar) as LinearLayout
        layout_toolbar!!.setBackgroundResource(R.drawable.toolbar_hobby)
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@HobbiesActivity.onBackPressed() }
    }

    fun backdialog() {
        val r0 = DialogInterface.OnClickListener { dialogInterface, i ->
            if (i == -3) {
                dialogInterface.dismiss()
            } else if (i == -1) {
                dialogInterface.dismiss()
                this@HobbiesActivity.supportFragmentManager.popBackStack()
                this@HobbiesActivity.finish()
                this@HobbiesActivity.overridePendingTransition(0, 0)
                val hobbiesActivity = this@HobbiesActivity
                hobbiesActivity.startActivity(hobbiesActivity.intent)
                this@HobbiesActivity.overridePendingTransition(0, 0)
            }
        }
        AlertDialog.Builder(this).setMessage("Discard editing?").setPositiveButton("Ok", r0)
            .setTitle("").setNeutralButton("Cancel", r0).show()
    }

    companion object {
        private const val TAG = "AcademicActivity>>"
    }
}
