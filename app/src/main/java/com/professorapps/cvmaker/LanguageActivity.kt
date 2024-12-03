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
import com.professorapps.cvmaker.adapters.LanguageAdapter
import com.professorapps.cvmaker.fragments.LanguageFragment
import com.professorapps.cvmaker.interfaces.LanguageClick
import com.professorapps.cvmaker.models.LanguageModel

class LanguageActivity : AppCompatActivity(), LanguageClick {
    var about: String? = null
    var adapter: LanguageAdapter? = null
    var back: LinearLayout? = null
    var btn_add: ImageView? = null
    var dataList: MutableList<LanguageModel?>? = null
    var flag: Int = 0
    var itemdata: LanguageModel? = null
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
        btn_add!!.setImageResource(R.drawable.add6)
        this.rcitem = findViewById<View>(R.id.rcitem) as RecyclerView
        rcitem!!.layoutManager = LinearLayoutManager(this)
        initData()
        titaltext!!.text = "Language"
        btn_add!!.setOnClickListener {
            this@LanguageActivity.supportFragmentManager.beginTransaction()
                .add(R.id.f1_container, LanguageFragment()).addToBackStack(null).commit()
        }
        this.adapter = LanguageAdapter(this, dataList!!, this)
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
                if (store!!.contains(getString(R.string.language1))) {
                    Log.d(
                        "sharedpref>>1",
                        store!!.getString(getString(R.string.language1), "")!!
                    )
                    dataList!!.add(
                        LanguageModel(
                            store!!.getString(
                                getString(R.string.language1),
                                ""
                            )
                        )
                    )
                }
                if (store!!.contains(getString(R.string.language2))) {
                    Log.d(
                        "sharedpref>>2",
                        store!!.getString(getString(R.string.language2), "")!!
                    )
                    dataList!!.add(
                        LanguageModel(
                            store!!.getString(
                                getString(R.string.language2),
                                ""
                            )
                        )
                    )
                }
                if (store!!.contains(getString(R.string.language3))) {
                    Log.d(
                        "sharedpref>>3",
                        store!!.getString(getString(R.string.language3), "")!!
                    )
                    dataList!!.add(
                        LanguageModel(
                            store!!.getString(
                                getString(R.string.language3),
                                ""
                            )
                        )
                    )
                }
                if (store!!.contains(getString(R.string.language4))) {
                    Log.d(
                        "sharedpref>>4",
                        store!!.getString(getString(R.string.language4), "")!!
                    )
                    dataList!!.add(
                        LanguageModel(
                            store!!.getString(
                                getString(R.string.language4),
                                ""
                            )
                        )
                    )
                }
                if (store!!.contains(getString(R.string.language5))) {
                    Log.d(
                        "sharedpref>>5",
                        store!!.getString(getString(R.string.language5), "")!!
                    )
                    dataList!!.add(
                        LanguageModel(
                            store!!.getString(
                                getString(R.string.language5),
                                ""
                            )
                        )
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.v("sharedpref>>2", "null")
            }
        }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            super.onBackPressed()
        } else if (LanguageFragment.showback) {
            backdialog()
        } else {
            supportFragmentManager.popBackStack()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    override fun callback(languageModel: LanguageModel?) {
        this.itemdata = languageModel
        nextflag()
    }

    private fun nextflag() {
        val gson = Gson()
        LanguageModel()
        val languageFragment = LanguageFragment()
        val bundle = Bundle()
        bundle.putInt("position", LanguageAdapter.selectedposition)
        bundle.putString("obj", gson.toJson(this.dataList))
        languageFragment.arguments = bundle
        val beginTransaction = supportFragmentManager.beginTransaction()
        beginTransaction.setCustomAnimations(R.anim.frag_slide_up, 0, 0, R.anim.frag_slide_down)
        beginTransaction.replace(R.id.f1_container, languageFragment)
        beginTransaction.addToBackStack(null)
        beginTransaction.commit()
        Log.d("state>>put", gson.toJson(this.dataList))
    }

    private fun initToolbar() {
        val window = window
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_language)
        }
        this.layout_toolbar = findViewById<View>(R.id.layout_toolbar) as LinearLayout
        layout_toolbar!!.setBackgroundResource(R.drawable.toolbar_language)
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@LanguageActivity.onBackPressed() }
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
                this@LanguageActivity.supportFragmentManager.popBackStack()
                this@LanguageActivity.finish()
                this@LanguageActivity.overridePendingTransition(0, 0)
                val languageActivity = this@LanguageActivity
                languageActivity.startActivity(languageActivity.intent)
                this@LanguageActivity.overridePendingTransition(0, 0)
            }
        }
        AlertDialog.Builder(this).setMessage("Discard editing?").setPositiveButton("Ok", r0)
            .setTitle("").setNeutralButton("Cancel", r0).show()
    }

    companion object {
        private const val TAG = "AcademicActivity>>"
    }
}
