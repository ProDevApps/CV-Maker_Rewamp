package com.professorapps.cvmaker

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.professorapps.cvmaker.adapters.MyCreationAdapter
import java.io.File

class MyCreationActivity : AppCompatActivity() {
    var back: LinearLayout? = null
    var layout_toolbar: LinearLayout? = null
    var myImageAdapter: MyCreationAdapter? = null
    var rcmycreation: RecyclerView? = null


    public override fun onCreate(bundle: Bundle?) {
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath + File.separator + getString(
                R.string.app_name
            ) + File.separator
        )
        val listFiles = file.listFiles()
        super.onCreate(bundle)
        try {
            if (!file.exists() || listFiles.size <= 0) {
                setContentView(R.layout.no_media)
                initToolbar()
                return
            }
            requestWindowFeature(1)
            setContentView(R.layout.activity_my_creation)


            initToolbar()
            this.rcmycreation = findViewById<View>(R.id.rcmycreation) as RecyclerView
            rcmycreation!!.layoutManager = GridLayoutManager(this, 2)
            this.myImageAdapter = MyCreationAdapter(this, this)
            rcmycreation!!.adapter = this.myImageAdapter
            for (file2 in file.listFiles()) {
                myImageAdapter!!.add(file2.absolutePath)
            }
        } catch (unused: Exception) {
            setContentView(R.layout.no_media)
            initToolbar()
        }
    }

    private fun initToolbar() {
        val window = window
        window.clearFlags(67108864)
        window.addFlags(Int.MIN_VALUE)
        if (Build.VERSION.SDK_INT >= 21) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.colorPrimaryDark)
        }
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@MyCreationActivity.onBackPressed() }
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId != 16908332) {
            return super.onOptionsItemSelected(menuItem)
        }
        onBackPressed()
        finish()
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}
