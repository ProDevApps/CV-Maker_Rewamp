package com.professorapps.cvmaker.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.SharedPreferences
import com.professorapps.cvmaker.interfaces.LanguageClick
import com.professorapps.cvmaker.models.LanguageModel

class LanguageAdapter (private val mActivity: Activity, var datalist: MutableList<LanguageModel?>, var listner: LanguageClick
) : RecyclerView.Adapter<LanguageAdapter.ItemHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mActivity)
    var store: SharedPreferences? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemHolder {
        this.store = SharedPreferences.getInstance(this.mActivity, "resumemaker")
        return ItemHolder(
            layoutInflater.inflate(R.layout.rowlayout_itemlist, viewGroup, false)
        )
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, @SuppressLint("RecyclerView") i: Int) {
        if (datalist[i]!!.language == null || (datalist[i]!!.language == "")) {
            itemHolder.loutMain.visibility = View.GONE
        } else {
            itemHolder.loutMain.visibility = View.VISIBLE
            itemHolder.tv_item.text = datalist[i]!!.language
        }
        itemHolder.tv_item.setOnClickListener {
            listner.callback(
                datalist[i]
            )
            selectedposition = i
        }
        itemHolder.imgdelete.setOnClickListener {
            selectedposition = i
            this@LanguageAdapter.removeItem(selectedposition)
        }
    }

    fun removeItem(i: Int) {
        datalist.removeAt(i)
        notifyItemRemoved(i)
        if (i == 0) {
            store!!.removeValue(mActivity.getString(R.string.language1))
        } else if (i == 1) {
            store!!.removeValue(mActivity.getString(R.string.language2))
        } else if (i == 2) {
            store!!.removeValue(mActivity.getString(R.string.language3))
        } else if (i == 3) {
            store!!.removeValue(mActivity.getString(R.string.language4))
        } else {
            store!!.removeValue(mActivity.getString(R.string.language5))
        }
        notifyItemRangeChanged(i, datalist.size)
        selectedposition = i
    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgdelete: ImageView = view.findViewById<View>(R.id.imgdelete) as ImageView
        var layout_bg: LinearLayout = view.findViewById<View>(R.id.layout_bg) as LinearLayout
        var loutMain: LinearLayout = view.findViewById<View>(R.id.loutMain) as LinearLayout
        var tv_item: TextView = view.findViewById<View>(R.id.tv_item) as TextView

        init {
            layout_bg.setBackgroundResource(R.drawable.bg_language)
        }
    }

    fun setFilter(list: List<LanguageModel>) {
        this.datalist = list.toMutableList()
        notifyDataSetChanged()
    }

    companion object {
        @JvmField
        var selectedposition: Int = 0
    }
}
