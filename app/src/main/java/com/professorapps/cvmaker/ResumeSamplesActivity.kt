package com.professorapps.cvmaker

import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.professorapps.cvmaker.adapters.ResumeSampleAdapter
import com.professorapps.cvmaker.models.ResumeSampleModel

class ResumeSamplesActivity : AppCompatActivity() {


    private lateinit var back: LinearLayout
    private var itemdata: ResumeSampleModel? = null
    private lateinit var layoutToolbar: LinearLayout
    private lateinit var rcFrame: RecyclerView
    private val dataList: MutableList<ResumeSampleModel> = mutableListOf()
    private val numFrame = 15

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resume_samples)
        initToolbar()

        rcFrame = findViewById(R.id.rcframe)
        rcFrame.layoutManager = GridLayoutManager(this, 2)

        prepareData("frm_", numFrame)
        prepareData("frm", numFrame)
    }

    private fun prepareData(prefix: String, count: Int): ArrayList<ResumeSampleModel> {
        val arrayList = ArrayList<ResumeSampleModel>()
        for (i in 1..count) {
            val resumeSampleModel = ResumeSampleModel()
            val resources: Resources = resources
            val identifier = resources.getIdentifier("$prefix$i", "drawable", packageName)
            resumeSampleModel.frm1 = identifier
            resumeSampleModel.frm2 = resources.getIdentifier("frm_$i", "drawable", packageName)
            Log.d("imageurl>>_", identifier.toString())
            arrayList.add(resumeSampleModel)
        }
        rcFrame.adapter = ResumeSampleAdapter(this, arrayList, this)
        return arrayList
    }

    private fun initToolbar() {
        val window: Window = window
        window.clearFlags(0x04000000) // WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        window.addFlags(-0x80000000) // WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.color_resumesample)
        }

        layoutToolbar = findViewById(R.id.layout_toolbar)
        layoutToolbar.setBackgroundResource(R.drawable.resume_toolbar)

        back = findViewById(R.id.back)
        back.setOnClickListener { onBackPressed() }
    }
}