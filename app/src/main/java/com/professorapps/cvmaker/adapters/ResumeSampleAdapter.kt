package com.professorapps.cvmaker.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.ResumeSample1Activity
import com.professorapps.cvmaker.adapters.ResumeSampleAdapter.ImageViewHolder
import com.professorapps.cvmaker.interfaces.AboutClick
import com.professorapps.cvmaker.models.ResumeSampleModel

class ResumeSampleAdapter(
    private val context: Context,
    private val stickerIds: ArrayList<ResumeSampleModel>,
    private val mActivity: Activity
) : RecyclerView.Adapter<ImageViewHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    var listner: AboutClick? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ImageViewHolder {
        return ImageViewHolder(
            LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.rowlayout_resume_sample, viewGroup, false)
        )
    }

    @SuppressLint("RecyclerView")
    override fun onBindViewHolder(imageViewHolder: ImageViewHolder, i: Int) {
        Glide.with(this.context).load(stickerIds[i].getfrm1()).into(imageViewHolder.image)
        imageViewHolder.image.setOnClickListener {
            selectedposition = i
            val gson = Gson()
            ResumeSampleModel()
            val intent = Intent(this@ResumeSampleAdapter.context, ResumeSample1Activity::class.java)
            intent.putExtra("obj", gson.toJson(this@ResumeSampleAdapter.stickerIds))
            intent.putExtra("position", i)
            Log.d("state>>put", gson.toJson(this@ResumeSampleAdapter.stickerIds))
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return stickerIds.size
    }


    inner class ImageViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById<View>(R.id.imgsticker) as ImageView
        var imgfrm1: ImageView? = null
    }

    companion object {
        @JvmField
        var selectedposition: Int = 0
    }
}
