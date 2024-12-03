package com.professorapps.cvmaker.adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.professorapps.cvmaker.R
import com.professorapps.cvmaker.interfaces.AboutClick
import com.professorapps.cvmaker.models.InterviewTipsModel
import java.util.Random

class InterviewTipsAdapter @RequiresApi(api = 24) constructor(
    private val mActivity: Activity,
    var datalist: MutableList<InterviewTipsModel?>
) : RecyclerView.Adapter<InterviewTipsAdapter.ItemHolder>() {
    var abouttext: String? = null
    private val backgroundColors = intArrayOf(
        R.color.list_color1,
        R.color.list_color2,
        R.color.list_color3,
        R.color.list_color4,
        R.color.list_color5,
        R.color.list_color6,
        R.color.list_color7
    )
    private val layoutInflater: LayoutInflater = LayoutInflater.from(mActivity)
    var listner: AboutClick? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ItemHolder {
        return ItemHolder(
            layoutInflater.inflate(R.layout.rowlayout_interviewtips, viewGroup, false)
        )
    }

    override fun onBindViewHolder(itemHolder: ItemHolder, @SuppressLint("RecyclerView") i: Int) {
        selectedposition = i
        bgColor = ContextCompat.getColor(
            this.mActivity,
            backgroundColors[(Random().nextInt(18) + 1) % 7]
        )
        ItemHolder.card_view.setCardBackgroundColor(bgColor)
        ItemHolder.tv_title.text = datalist[i]!!.title
        ItemHolder.tv_desc.text = datalist[i]!!.desc
        itemHolder.loutMain.setOnClickListener {
            Log.d(
                "rclistclick>>adapter",
                selectedposition.toString() + "m"
            )
        }
    }

    override fun getItemCount(): Int {
        return datalist.size
    }


    class ItemHolder(view: View) : RecyclerView.ViewHolder(view) {
        var loutMain: LinearLayout

        companion object {
            lateinit var card_view: CardView
            lateinit var tv_desc: TextView
            lateinit var tv_title: TextView
        }

        init {
            card_view = view.findViewById<View>(R.id.card_view) as CardView
            tv_title = view.findViewById<View>(R.id.tv_title) as TextView
            tv_desc = view.findViewById<View>(R.id.tv_desc) as TextView
            this.loutMain = view.findViewById<View>(R.id.loutMain) as LinearLayout
        }


    }

    fun setFilter(list: List<InterviewTipsModel>) {
        this.datalist = list.toMutableList()
        notifyDataSetChanged()
    }

    companion object {
        var bgColor: Int = 0
        @JvmField
        var selectedposition: Int = 0
    }
}
