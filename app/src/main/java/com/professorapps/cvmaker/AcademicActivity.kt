package com.professorapps.cvmaker

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.professorapps.cvmaker.adapters.AcademicAdapter
import com.professorapps.cvmaker.fragments.AcademicFragment
import com.professorapps.cvmaker.interfaces.AcademicClick
import com.professorapps.cvmaker.models.AcademicModel

class AcademicActivity : AppCompatActivity(), AcademicClick {

    private val TAG = "AcademicActivity>>"
    private lateinit var adapter: AcademicAdapter
    private lateinit var back: LinearLayout
    private lateinit var btnAdd: ImageView
    private var dataList: MutableList<AcademicModel?>? = null
    private lateinit var itemData: AcademicModel
    private lateinit var layoutToolbar: LinearLayout
    private lateinit var rcItem: RecyclerView
    private lateinit var store: SharedPreferences
    private lateinit var titalText: TextView

    @RequiresApi(api = Build.VERSION_CODES.N)
    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.items_list)
        initToolbar()

        store = SharedPreferences.getInstance(this, "resumemaker")!!
        titalText = findViewById(R.id.titaltext)
        btnAdd = findViewById(R.id.btn_add)
        rcItem = findViewById(R.id.rcitem)
        rcItem.layoutManager = LinearLayoutManager(this)

        initData()

        titalText.text = "Academic"
        btnAdd.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .add(R.id.f1_container, AcademicFragment())
                .addToBackStack(null)
                .commit()
        }

        adapter = AcademicAdapter(this, dataList!!, this)
        rcItem.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun initData() {
        if (dataList == null) {
            dataList = mutableListOf()
            getSharedPref()
        }
    }

    private fun getSharedPref() {
        try {
            if (store.contains(getString(R.string.academic_degree1))) {
                Log.d("sharedpref>>2", store.getString(getString(R.string.academic_degree1), "") ?: "")
                dataList?.add(AcademicModel(
                    store.getString(getString(R.string.academic_degree1), "") ?: "",
                    store.getString(getString(R.string.academic_institute1), "") ?: "",
                    store.getString(getString(R.string.academic_percgpa1), "") ?: "",
                    store.getString(getString(R.string.academic_year1), "") ?: "",
                    store.getString(getString(R.string.academic_percgpa1_radio), "") ?: "",
                    store.getString(getString(R.string.academic_year1_radio), "") ?: ""
                ))
                Log.d("academic>>>1", "academic>>>1")
            }
            if (store.contains(getString(R.string.academic_degree2))) {
                dataList?.add(AcademicModel(
                    store.getString(getString(R.string.academic_degree2), "") ?: "",
                    store.getString(getString(R.string.academic_institute2), "") ?: "",
                    store.getString(getString(R.string.academic_percgpa2), "") ?: "",
                    store.getString(getString(R.string.academic_year2), "") ?: "",
                    store.getString(getString(R.string.academic_percgpa2_radio), "") ?: "",
                    store.getString(getString(R.string.academic_year2_radio), "") ?: ""
                ))
                Log.d("academic>>>2", "academic>>>2")
            }
            if (store.contains(getString(R.string.academic_degree3))) {
                dataList?.add(AcademicModel(
                    store.getString(getString(R.string.academic_degree3), "") ?: "",
                    store.getString(getString(R.string.academic_institute3), "") ?: "",
                    store.getString(getString(R.string.academic_percgpa3), "") ?: "",
                    store.getString(getString(R.string.academic_year3), "") ?: "",
                    store.getString(getString(R.string.academic_percgpa3_radio), "") ?: "",
                    store.getString(getString(R.string.academic_year3_radio), "") ?: ""
                ))
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
        } else if (AcademicFragment.showback) {
            backDialog()
        } else {
            supportFragmentManager.popBackStack()
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }

    override fun callback(academicModel: AcademicModel?) {
        itemData = academicModel!!
        nextFlag()
    }

    private fun nextFlag() {
        val gson = Gson()
        val academicFragment = AcademicFragment()
        val bundle = Bundle()
        bundle.putInt("position", AcademicAdapter.selectedposition)
        bundle.putString("obj", gson.toJson(dataList))
        academicFragment.arguments = bundle

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.frag_slide_up, 0, 0, R.anim.frag_slide_down)
            .replace(R.id.f1_container, academicFragment)
            .addToBackStack(null)
            .commit()

        Log.d("state>>put", gson.toJson(dataList))
    }

    private fun initToolbar() {
        val window: Window = window
        window.clearFlags(67108864)
        window.addFlags(Integer.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_academic)
        }
        layoutToolbar = findViewById(R.id.layout_toolbar)
        layoutToolbar.setBackgroundResource(R.drawable.toolbar_academic)
        back = findViewById(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }
    }

    private fun backDialog() {
        val listener = DialogInterface.OnClickListener { dialogInterface, i ->
            if (i == DialogInterface.BUTTON_NEGATIVE) {
                dialogInterface.dismiss()
            } else if (i == DialogInterface.BUTTON_POSITIVE) {
                dialogInterface.dismiss()
                supportFragmentManager.popBackStack()
                finish()
                overridePendingTransition(0, 0)
                startActivity(intent)
                overridePendingTransition(0, 0)
            }
        }

        AlertDialog.Builder(this)
            .setMessage("Discard editing?")
            .setPositiveButton("Ok", listener)
            .setNeutralButton("Cancel", listener)
            .show()
    }
}
