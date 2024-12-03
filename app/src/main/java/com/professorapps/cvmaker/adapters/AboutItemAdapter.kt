package com.professorapps.cvmaker.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.interfaces.AboutClick
import com.professorapps.cvmaker.models.AboutDataModel

class AboutItemAdapter @RequiresApi(api = 24) constructor(
    private val mActivity: Activity,
    var datalist: MutableList<AboutDataModel>,
    var listner: AboutClick
) : RecyclerView.Adapter<AboutItemAdapter.ItemHolder>() {
    var abouttext: String? = null
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mActivity)

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemHolder {
        return ItemHolder(
            layoutInflater.inflate(R.layout.rowlayout_about, viewGroup, false)
        )
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, @SuppressLint("RecyclerView") i: Int) {
        itemHolder.tv_aboutme.text = datalist[i].about
        itemHolder.loutMain.setOnClickListener {
            selectedposition = i
            val aboutItemAdapter = this@AboutItemAdapter
            aboutItemAdapter.abouttext = aboutItemAdapter.datalist[i].about
            Log.d(
                "abouttext>>1",
                abouttext!!
            )
            val intent = Intent()
            intent.putExtra("extra_sticker_id", this@AboutItemAdapter.abouttext)
            mActivity.setResult(-1, intent)
            mActivity.finish()
        }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var loutMain: LinearLayout = view.findViewById<View>(R.id.loutMain) as LinearLayout
        var tv_aboutme: TextView = view.findViewById<View>(R.id.tv_aboutme) as TextView
    }

    fun setFilter(list: List<AboutDataModel>) {
        this.datalist = list.toMutableList()
        notifyDataSetChanged()
    }

    companion object {
        @JvmField
        var selectedposition: Int = 0
    }
}
