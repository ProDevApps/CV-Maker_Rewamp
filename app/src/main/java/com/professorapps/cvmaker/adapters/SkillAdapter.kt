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
import com.professorapps.cvmaker.interfaces.SkillClick
import com.professorapps.cvmaker.models.SkillModel

class SkillAdapter constructor(
    private val mActivity: Activity,
    private var datalist: MutableList<SkillModel?>,
    var listner: SkillClick
) : RecyclerView.Adapter<SkillAdapter.ItemHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mActivity)
    var store: SharedPreferences? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemHolder {
        this.store = SharedPreferences.getInstance(this.mActivity, "resumemaker")
        return ItemHolder(
            layoutInflater.inflate(R.layout.rowlayout_itemlist, viewGroup, false)
        )
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, @SuppressLint("RecyclerView") i: Int) {
        if (datalist[i]!!.skill == null || (datalist[i]!!.skill == "")) {
            itemHolder.loutMain.visibility = View.GONE
        } else {
            itemHolder.loutMain.visibility = View.VISIBLE
            itemHolder.tv_item.text = datalist[i]!!.skill
        }
        itemHolder.tv_item.setOnClickListener {
            listner.callback(
                datalist[i]
            )
            selectedposition = i
        }
        itemHolder.imgdelete.setOnClickListener {
            selectedposition = i
            this@SkillAdapter.removeItem(selectedposition)
        }
    }

    fun removeItem(i: Int) {
        datalist.removeAt(i)
        notifyItemRemoved(i)
        if (i == 0) {
            store!!.removeValue(mActivity.getString(R.string.skill1))
        } else if (i == 1) {
            store!!.removeValue(mActivity.getString(R.string.skill2))
        } else if (i == 2) {
            store!!.removeValue(mActivity.getString(R.string.skill3))
        } else if (i == 3) {
            store!!.removeValue(mActivity.getString(R.string.skill4))
        } else {
            store!!.removeValue(mActivity.getString(R.string.skill5))
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
            layout_bg.setBackgroundResource(R.drawable.bg_skill)
        }
    }


    fun setFilter(filteredData: List<SkillModel>) {
        datalist = filteredData.toMutableList()
        notifyDataSetChanged()
    }

    companion object {
        @JvmField
        var selectedposition: Int = 0
    }
}
