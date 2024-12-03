package com.professorapps.cvmaker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.professorapps.cvmaker.adapters.AboutItemAdapter
import com.professorapps.cvmaker.interfaces.AboutClick
import com.professorapps.cvmaker.models.AboutDataModel

class AboutmeActivity : AppCompatActivity(), AboutClick {

    companion object {
        const val EXTRA_ABOUT_ID = "extra_sticker_id"
        const val EXTRA_PROFILE_ID = "extra_profile_id"
    }

    private var about: String? = null
    private lateinit var adapter: AboutItemAdapter
    private lateinit var back: LinearLayout
    private var dataList: MutableList<AboutDataModel>? = null


    private  var itemdata:AboutDataModel? = null
    private lateinit var rcList: RecyclerView

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_aboutme)
        initToolbar()
        rcList = findViewById(R.id.rcList)
        rcList.layoutManager = LinearLayoutManager(this)
        initData()
        adapter = AboutItemAdapter(this, (dataList ?: emptyList()).toMutableList(), this)
        rcList.adapter = adapter
        adapter.notifyDataSetChanged()
    }


    private fun initData() {
        if (dataList == null) {
            dataList = mutableListOf(
                AboutDataModel("I am an organised and efficient person with an enquiring mind."),
                AboutDataModel("I am a flexible person seeking employment which will allow development, growth and make use of my existing skills."),
                AboutDataModel("I am a good listener and learner, and am able to communicate well with people from all walks of life."),
                AboutDataModel("I am a keen, hard working, reliable and excellent time keeper."),
                AboutDataModel("I am a bright and receptive person, able to communicate well with people at all levels."),
                AboutDataModel("I am good at working using my own initiative and I am flexible in my approach to work duties."),
                AboutDataModel("I have a good sense of humour and a pleasant approach."),
                AboutDataModel("I am a hard working individual with experience of assembly line work, drilling, and spraying."),
                AboutDataModel("I am a loyal and trustworthy employee who can work alone or as part of a team."),
                AboutDataModel("I am quick to learn and willing to adapt to any job."),
                AboutDataModel("I am a competent, loyal, hard working employee with the ability to achieve tasks when working alone or as part of a team."),
                AboutDataModel("I am punctual with an excellent attendance record."),
                AboutDataModel("I have a flexible and positive approach to employment and am willing to train to suit my next working environment."),
                AboutDataModel("I am an organised, efficient and hard working person, and am willing to discover and accept new ideas which can be put into practice effectively."),
                AboutDataModel("I am a good listener and learner, able to communicate well with a group and on an individual level."),
                AboutDataModel("I am able to motivate and direct my talents and skills to meet objectives."),
                AboutDataModel("I always seek to achieve a high standard in whatever work I undertake."),
                AboutDataModel("I am well organised with a clear and positive approach to problem solving."),
                AboutDataModel("I am a well organised, efficient and professional person."),
                AboutDataModel("I am able to communicate easily in both spoken and written forms."),
                AboutDataModel("I am a hard working, loyal and mature worker with a variety of skills to offer."),
                AboutDataModel("I am keen to obtain new skills and training and have a flexible approach to all work situations, and find it easy to adapt."),
                AboutDataModel("I am capable of relating to people at all levels and am prepared to help them overcome problems in a work situation."),
                AboutDataModel("I am open minded, receptive and innovative, with an enquiring mind."),
                AboutDataModel("I can work unsupervised alone or as a member of a team and I like to use my skills to make a positive contribution to the workforce."),
                AboutDataModel("I am a family person with responsibilities, very reliable and trustworthy."),
                AboutDataModel("I am willing to learn new skills and have many existing ones to offer."),
                AboutDataModel("I have good interpersonal understanding and communication skills and am a confident team worker with the ability to work on own initiative."),
                AboutDataModel("I am a mature person with a sound engineering background."),
                AboutDataModel("I can organise and prioritise my own workload effectively."),
                AboutDataModel("I have the ability to work under stress and keep within set budgets and targets."),
                AboutDataModel("I am a mature and confident person with sales experience, team leadership skills and the ability to create new sales and build on existing accounts."),
                AboutDataModel("I am a quiet and focused person who can work swiftly and effectively either alone or as part of a team."),
                AboutDataModel("I am a reliable, trustworthy and flexible individual who can learn new skills easily and execute them swiftly."),
                AboutDataModel("I am a confident driver with clean driving licence, I have my own transport and an excellent knowledge of local and national road and motorway networks."),
                AboutDataModel("I am a confident Fork Lift Truck operator with a good all round knowledge of warehouse duties."),
                AboutDataModel("I am an experienced machine operator with a good all round knowledge of factory procedures.")
            )
        }
    }



    private fun initToolbar() {
        back = findViewById(R.id.back)
        back.setOnClickListener {
            onBackPressed()
        }

    }

    override fun callback(aboutDataModel: AboutDataModel?) {
        itemdata = aboutDataModel
    }

}
