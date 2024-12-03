package com.professorapps.cvmaker.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.Environment
import android.os.ParcelFileDescriptor
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.professorapps.cvmaker.FullScreenCreationActivity
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.adapters.MyCreationAdapter.MyCreationViewHolder2
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class MyCreationAdapter(context: Context, private val mActivity: Activity) :
    RecyclerView.Adapter<MyCreationViewHolder2>() {
    private val context: Context
    var itemList: ArrayList<String> = ArrayList()
    private val layoutInflater: LayoutInflater

    init {
        this.itemList = this.itemList
        this.context = context
        this.layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): MyCreationViewHolder2 {
        return MyCreationViewHolder2(
            layoutInflater.inflate(
                R.layout.rowlayout_mycreation,
                viewGroup,
                false
            )
        )
    }

    @RequiresApi(api = 21)
    override fun onBindViewHolder(myCreationViewHolder2: MyCreationViewHolder2, i: Int) {
        var parcelFileDescriptor: ParcelFileDescriptor?
        var pdfRenderer: PdfRenderer?
        val displayMetrics = DisplayMetrics()
        mActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val i2 = displayMetrics.heightPixels
        Log.d("screenwidth>>", (displayMetrics.widthPixels / 2).toString() + "")
        try {
            parcelFileDescriptor = ParcelFileDescriptor.open(File(itemList[i]), 268435456)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
            parcelFileDescriptor = null
        }
        try {
            pdfRenderer = PdfRenderer(parcelFileDescriptor!!)
        } catch (e2: IOException) {
            e2.printStackTrace()
            pdfRenderer = null
        }
        val openPage = pdfRenderer!!.openPage(0)
        val createBitmap =
            Bitmap.createBitmap(openPage.width, openPage.height, Bitmap.Config.ARGB_8888)
        openPage.render(createBitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        val imageUri = getImageUri(this.context, createBitmap)
        myCreationViewHolder2.image.setImageURI(imageUri)
        openPage.close()
        pdfRenderer.close()
        try {
            parcelFileDescriptor!!.close()
        } catch (e3: IOException) {
            e3.printStackTrace()
        }
        myCreationViewHolder2.image.setOnClickListener {
            val intent =
                Intent(this@MyCreationAdapter.context, FullScreenCreationActivity::class.java)
            intent.putExtra("creationuri", imageUri.toString())
            context.startActivity(intent)
        }
    }

    private fun getImageUri(context: Context, bitmap: Bitmap): Uri {
        val externalStorageDirectory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(externalStorageDirectory.absolutePath + "/.temp/")
        file.mkdir()
        var file2: File? = null
        try {
            file2 = File.createTempFile("resume_maker", ".jpg", file)
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val fileOutputStream = FileOutputStream(file2)
            fileOutputStream.write(byteArray)
            fileOutputStream.flush()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.fromFile(file2)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    fun add(str: String) {
        itemList.add(str)
    }


    inner class MyCreationViewHolder2(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById<View>(R.id.imgsticker) as ImageView

        init {
            view.setOnClickListener { this@MyCreationViewHolder2.adapterPosition }
        }
    }

    fun decodeSampledBitmapFromUri(str: String?, i: Int, i2: Int): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(str, options)
        options.inSampleSize = calculateInSampleSize(options, i, i2)
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(str, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, i: Int, i2: Int): Int {
        val i3 = options.outHeight
        val i4 = options.outWidth
        if (i3 <= i2 && i4 <= i) {
            return 1
        }
        if (i4 > i3) {
            return Math.round((i3.toFloat()) / (i2.toFloat()))
        }
        return Math.round((i4.toFloat()) / (i.toFloat()))
    }
}
