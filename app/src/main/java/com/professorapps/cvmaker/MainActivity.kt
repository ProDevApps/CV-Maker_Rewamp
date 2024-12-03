package com.professorapps.cvmaker

import android.annotation.TargetApi
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class MainActivity : AppCompatActivity() {
    var TIME: Long = 1000
    var back: LinearLayout? = null
    var imgacademic: ImageView? = null
    var imgexperience: ImageView? = null
    var imghobbies: ImageView? = null
    var imglanguages: ImageView? = null
    var imgpersonalinfo: ImageView? = null
    var imgreference: ImageView? = null
    var imgresumesample: ImageView? = null
    var imgskills: ImageView? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_main)
        initToolbar()
        this.imgpersonalinfo = findViewById<View>(R.id.imgpersonalinfo) as ImageView
        this.imgacademic = findViewById<View>(R.id.imgacademic) as ImageView
        this.imgexperience = findViewById<View>(R.id.imgexperience) as ImageView
        this.imgreference = findViewById<View>(R.id.imgreference) as ImageView
        this.imgskills = findViewById<View>(R.id.imgskills) as ImageView
        this.imglanguages = findViewById<View>(R.id.imglanguages) as ImageView
        this.imghobbies = findViewById<View>(R.id.imghobbies) as ImageView
        this.imgresumesample = findViewById<View>(R.id.imgresumesample) as ImageView
        val loadAnimation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.unclick_btn_zoom)
        imgpersonalinfo!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(
                Intent(
                    this@MainActivity,
                    PersonalinfoActivity::class.java
                )
            )
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
        imgacademic!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(Intent(this@MainActivity, AcademicActivity::class.java))
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
        imgexperience!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(
                Intent(
                    this@MainActivity,
                    ExperienceActivity::class.java
                )
            )
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
        imgreference!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(
                Intent(
                    this@MainActivity,
                    ReferenceActivity::class.java
                )
            )
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
        imgskills!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(Intent(this@MainActivity, SkillsActivity::class.java))
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
        imglanguages!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(Intent(this@MainActivity, LanguageActivity::class.java))
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
        imghobbies!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(Intent(this@MainActivity, HobbiesActivity::class.java))
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
        imgresumesample!!.setOnClickListener { view ->
            view.startAnimation(loadAnimation)
            this@MainActivity.startActivity(
                Intent(
                    this@MainActivity,
                    ResumeSamplesActivity::class.java
                )
            )
            view.isEnabled = false
            Handler().postDelayed({ view.isEnabled = true }, this@MainActivity.TIME)
        }
    }

    @TargetApi(21)
    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)
        this.back = findViewById<View>(R.id.back) as LinearLayout
        back!!.setOnClickListener { this@MainActivity.onBackPressed() }
    }

    @RequiresApi(api = 19)
    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.itemId != 16908332) {
            return super.onOptionsItemSelected(menuItem)
        }
        onBackPressed()
        return true
    }
}
