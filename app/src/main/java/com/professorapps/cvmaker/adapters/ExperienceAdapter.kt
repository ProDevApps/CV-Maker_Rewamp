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
import com.professorapps.cvmaker.interfaces.ExperienceClick
import com.professorapps.cvmaker.models.ExperienceModel

class ExperienceAdapter (
    private val mActivity: Activity,
    var datalist: MutableList<ExperienceModel?>,
    var listner: ExperienceClick
) : RecyclerView.Adapter<ExperienceAdapter.ItemHolder>() {
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mActivity)
    var store: SharedPreferences? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemHolder {
        this.store = SharedPreferences.getInstance(this.mActivity, "resumemaker")
        return ItemHolder(
            layoutInflater.inflate(R.layout.rowlayout_experience, viewGroup, false)
        )
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, @SuppressLint("RecyclerView") i: Int) {
        if (datalist[i]!!.organization == null || (datalist[i]!!.organization == "")) {
            itemHolder.loutMain.visibility = View.GONE
        } else {
            itemHolder.loutMain.visibility = View.VISIBLE
            itemHolder.tvorgnm.text = datalist[i]!!.organization
            itemHolder.tvorgnm.isSelected = true
            itemHolder.tvdesig.text = datalist[i]!!.designation
            itemHolder.tvdesig.isSelected = true
            val textView = itemHolder.tvfromtime
            textView.text = "(" + datalist[i]!!.fromtime
            val textView2 = itemHolder.tvtotime
            textView2.text = datalist[i]!!.totime + ")"
            itemHolder.tvrole.text = datalist[i]!!.role
        }
        itemHolder.loutMain.setOnClickListener {
            listner.callback(
                datalist[i]
            )
            selectedposition = i
        }
        itemHolder.imgdelete.setOnClickListener {
            selectedposition = i
            this@ExperienceAdapter.removeItem(selectedposition)
            Log.d("pos>>selectedpos>>", i.toString() + ", selected:" + selectedposition)
        }
    }

    fun removeItem(i: Int) {
        datalist.removeAt(i)
        notifyItemRemoved(i)
        if (i == 0) {
            store!!.removeValue(mActivity.getString(R.string.experience_organization1))
        } else if (i == 1) {
            store!!.removeValue(mActivity.getString(R.string.experience_organization2))
        } else {
            store!!.removeValue(mActivity.getString(R.string.experience_organization3))
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
        var tvdesig: TextView = view.findViewById<View>(R.id.tvdesig) as TextView
        var tvfromtime: TextView = view.findViewById<View>(R.id.tvfromtime) as TextView
        var tvorgnm: TextView = view.findViewById<View>(R.id.tvorgnm) as TextView
        var tvrole: TextView = view.findViewById<View>(R.id.tvrole) as TextView
        var tvtotime: TextView = view.findViewById<View>(R.id.tvtotime) as TextView
    }

    fun setFilter(list: List<ExperienceModel>) {
        this.datalist = list.toMutableList()
        notifyDataSetChanged()
    }

    companion object {
        @JvmField
        var selectedposition: Int = 0
    }
}
