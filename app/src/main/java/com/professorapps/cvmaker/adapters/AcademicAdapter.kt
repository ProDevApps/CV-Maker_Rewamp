package com.professorapps.cvmaker.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
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
import com.professorapps.cvmaker.interfaces.AcademicClick
import com.professorapps.cvmaker.models.AcademicModel
import java.util.Collections

class AcademicAdapter @RequiresApi(api = 24) constructor(
    private val mActivity: Activity,
    var datalist: MutableList<AcademicModel?>,
    var listner: AcademicClick
) : RecyclerView.Adapter<AcademicAdapter.ItemHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mActivity)
    var store: SharedPreferences? = null

    init {
        Collections.shuffle(datalist)
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemHolder {
        this.store = SharedPreferences.getInstance(this.mActivity, "resumemaker")
        return ItemHolder(
            layoutInflater.inflate(R.layout.rowlayout_academic, viewGroup, false)
        )
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, @SuppressLint("RecyclerView") i: Int) {
        if (datalist[i]!!.degree == null || (datalist[i]!!.degree == "")) {
            itemHolder.loutMain.visibility = View.GONE
        } else {
            itemHolder.loutMain.visibility = View.VISIBLE
            itemHolder.tv_item.text = datalist[i]!!.degree
            itemHolder.tv_item.isSelected = true
            itemHolder.tv_institutenm.text = datalist[i]!!.institute
            itemHolder.tv_institutenm.isSelected = true
            itemHolder.tvcgpa.text = datalist[i]!!.percgpa
            itemHolder.tvyear.text = datalist[i]!!.year
        }
        itemHolder.loutMain.setOnClickListener {
            listner.callback(
                datalist[i]
            )
            selectedposition = i
        }
        itemHolder.imgdelete.setOnClickListener {
            selectedposition = i
            this@AcademicAdapter.removeItem(selectedposition)
            Log.d("pos>>selectedpos>>", i.toString() + ", selected:" + selectedposition)
        }
    }

    fun removeItem(i: Int) {
        datalist.removeAt(i)
        notifyItemRemoved(i)
        if (i == 0) {
            store!!.removeValue(mActivity.getString(R.string.academic_degree1))
        } else if (i == 1) {
            store!!.removeValue(mActivity.getString(R.string.academic_degree2))
        } else {
            store!!.removeValue(mActivity.getString(R.string.academic_degree3))
        }
        notifyItemRangeChanged(i, datalist.size)
        selectedposition = i
    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    inner class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgdelete: ImageView = view.findViewById<View>(R.id.imgdelete) as ImageView
        var loutMain: LinearLayout = view.findViewById<View>(R.id.loutMain) as LinearLayout
        var tv_institutenm: TextView = view.findViewById<View>(R.id.tv_institutenm) as TextView
        var tv_item: TextView = view.findViewById<View>(R.id.tv_item) as TextView
        var tvcgpa: TextView = view.findViewById<View>(R.id.tvcgpa) as TextView
        var tvyear: TextView = view.findViewById<View>(R.id.tvyear) as TextView
    }

    fun setFilter(list: List<AcademicModel?>) {
        this.datalist = list.toMutableList()
        notifyDataSetChanged()
    }

    companion object {
        @JvmField
        var selectedposition: Int = 0
    }
}
