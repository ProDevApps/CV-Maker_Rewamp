package com.professorapps.cvmaker.utils;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.professorapps.cvmaker.PersonalinfoActivity;
import com.professorapps.cvmaker.R;
import com.professorapps.cvmaker.adapters.AboutItemAdapter;
import com.professorapps.cvmaker.interfaces.AboutClick;
import com.professorapps.cvmaker.models.AboutDataModel;
import java.util.ArrayList;
import java.util.List;


public class AboutmeActivity extends AppCompatActivity implements AboutClick {
    public static final String EXTRA_ABOUT_ID = "extra_sticker_id";
    public static final String EXTRA_PROFILE_ID = "extra_profile_id";
    String about;
    AboutItemAdapter adapter;
    LinearLayout back;
    List<AboutDataModel> dataList = null;
    AboutDataModel itemdata;
    RecyclerView rcList;


    @Override
    @RequiresApi(api = 24)
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_aboutme);
        initToolbar();
        this.rcList = (RecyclerView) findViewById(R.id.rcList);
        this.rcList.setLayoutManager(new LinearLayoutManager(this));
        initData();
        this.adapter = new AboutItemAdapter(this, this.dataList, this);
        this.rcList.setAdapter(this.adapter);
        this.adapter.notifyDataSetChanged();
    }

    private void initData() {
        if (this.dataList == null) {
            this.dataList = new ArrayList();
            this.dataList.add(new AboutDataModel("I am an organised and efficient person with an enquiring mind."));
            this.dataList.add(new AboutDataModel("I am a flexible person seeking employment which will allow development, growth and make use of my existing skills."));
            this.dataList.add(new AboutDataModel("I am a good listener and learner, and am able to communicate well with people from all walks of life."));
            this.dataList.add(new AboutDataModel("I am a keen, hard working, reliable and excellent time keeper."));
            this.dataList.add(new AboutDataModel("I am a bright and receptive person, able to communicate well with people at all levels."));
            this.dataList.add(new AboutDataModel("I am good at working using my own initiative and I am flexible in my approach to work duties."));
            this.dataList.add(new AboutDataModel("I have a good sense of humour and a pleasant approach."));
            this.dataList.add(new AboutDataModel("I am a hard working individual with experience of assembly line work, drilling, and spraying."));
            this.dataList.add(new AboutDataModel("I am a loyal and trustworthy employee who can work alone or as part of a team."));
            this.dataList.add(new AboutDataModel("I am a quick to learn and willing to adapt to any job."));
            this.dataList.add(new AboutDataModel("I am a competent, loyal, hard working employee with the ability to achieve tasks when working alone or as part of a team."));
            this.dataList.add(new AboutDataModel("I am punctual with an excellent attendance record."));
            this.dataList.add(new AboutDataModel("I have a flexible and positive approach to employment and am willing to train to suit my next working environment."));
            this.dataList.add(new AboutDataModel("I am an organised, efficient and hard working person, and am willing to discover and accept new ideas which can be put into practice effectively."));
            this.dataList.add(new AboutDataModel("I am a good listener and learner, able to communicate well with a group and on an individual level."));
            this.dataList.add(new AboutDataModel("I am able to motivate and direct my talents and skills to meet objectives."));
            this.dataList.add(new AboutDataModel("I always seek to achieve a high standard in whatever work I undertake."));
            this.dataList.add(new AboutDataModel("I am well organised with a clear and positive approach to problem solving."));
            this.dataList.add(new AboutDataModel("I am a well organised, efficient and professional person."));
            this.dataList.add(new AboutDataModel("I am able to communicate easily in both spoken and written forms."));
            this.dataList.add(new AboutDataModel("I am a hard working, loyal and mature worker with a variety of skills to offer."));
            this.dataList.add(new AboutDataModel("I am keen to obtain new skills and training and have a flexible approach to all work situations, and find it easy to adapt."));
            this.dataList.add(new AboutDataModel("I am capable of relating to people at all levels and am prepared to help them overcome problems in a work situation."));
            this.dataList.add(new AboutDataModel("I am open minded, receptive and innovative, with an enquiring mind."));
            this.dataList.add(new AboutDataModel("I can work unsupervised alone or as a member of a team and I like to use my skills to make a positive contribution to the workforce."));
            this.dataList.add(new AboutDataModel("I am a family person with responsibilities, very reliable and trustworthy."));
            this.dataList.add(new AboutDataModel("I am willing to learn new skills and have many existing ones to offer."));
            this.dataList.add(new AboutDataModel("I have good interpersonal understanding and communication skills and am a confident team worker with the ability to work on own initiative."));
            this.dataList.add(new AboutDataModel("I am a mature person with a sound engineering background."));
            this.dataList.add(new AboutDataModel("I can organise and prioritise my own workload effectively."));
            this.dataList.add(new AboutDataModel("I have the ability to work under stress and keep within set budgets and targets."));
            this.dataList.add(new AboutDataModel("I am a mature and confident person with sales experience, team leadership skills and the ability to create new sales and build on existing accounts."));
            this.dataList.add(new AboutDataModel("I am a quiet and focused person who can work swiftly and effectively either alone or as part of a team."));
            this.dataList.add(new AboutDataModel("I am a reliable, trustworthy and flexible individual who can learn new skills easily and execute them swiftly."));
            this.dataList.add(new AboutDataModel("I am a confident driver with clean driving licence, I have my own transport and an excellent knowledge of local and national road and motorway networks."));
            this.dataList.add(new AboutDataModel("I am a confident Fork Lift Truck operator with a good all round knowledge of warehouse duties."));
            this.dataList.add(new AboutDataModel("I am an experienced machine operator with a good all round knowledge of factory procedures."));
        }
    }

    @Override
    public void callback(AboutDataModel aboutDataModel) {
        this.itemdata = aboutDataModel;
    }



    private void initToolbar() {
        this.back = (LinearLayout) findViewById(R.id.back);
        this.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutmeActivity.this.onBackPressed();
            }
        });
    }
}
