package com.professorapps.cvmaker

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

class FullScreenCreationActivity : AppCompatActivity() {
    var back: LinearLayout? = null
    var imgshare: ImageView? = null
    var imgshowcreation: ImageView? = null
    var mImageUri: Uri? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_full_screen_creation)


        initToolbar()
        this.imgshowcreation = findViewById<View>(R.id.imgshowcreation) as ImageView
        this.imgshare = findViewById<View>(R.id.imgshare) as ImageView
        this.mImageUri = Uri.parse(intent.getStringExtra("creationuri"))
        imgshowcreation!!.setImageURI(this.mImageUri)
        imgshare!!.setOnClickListener { this@FullScreenCreationActivity.shareImg() }
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeButtonEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@FullScreenCreationActivity.onBackPressed() }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_fullscreencreation, menu)
        return true
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        val itemId = menuItem.itemId
        if (itemId == 16908332) {
            onBackPressed()
            finish()
            return true
        } else if (itemId != R.id.menu_shareimg) {
            return super.onOptionsItemSelected(menuItem)
        } else {
            shareImg()
            return true
        }
    }


    fun shareImg() {
        val bitmapFromView = getBitmapFromView(this.imgshowcreation)
        try {
            val file = File(externalCacheDir, "resume_maker.png")
            val fileOutputStream = FileOutputStream(file)
            bitmapFromView.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()
            file.setReadable(true, false)
            val intent = Intent("android.intent.action.SEND")
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra(
                "android.intent.extra.STREAM", FileProvider.getUriForFile(
                    this, packageName, file
                )
            )
            intent.putExtra(
                "android.intent.extra.TEXT",
                "Shared via " + getString(R.string.app_name)
            )
            intent.setType("image/png")
            startActivity(Intent.createChooser(intent, "Share image via"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun getBitmapFromView(view: View?): Bitmap {
        val createBitmap = Bitmap.createBitmap(view!!.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(createBitmap)
        val background = view.background
        if (background != null) {
            background.draw(canvas)
        } else {
            canvas.drawColor(-1)
        }
        view.draw(canvas)
        return createBitmap
    }
}
