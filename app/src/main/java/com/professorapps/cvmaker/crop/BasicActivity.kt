package com.professorapps.cvmaker.crop

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.professorapps.cvmaker.AboutmeActivity
import com.professorapps.cvmaker.R

class BasicActivity : AppCompatActivity() {
    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_basic)
        if (bundle == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, BasicFragment.Companion.newInstance()).commit()
        }
        initToolbar()
    }

    override fun onConfigurationChanged(configuration: Configuration) {
        super.onConfigurationChanged(configuration)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun initToolbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val supportActionBar = supportActionBar
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeButtonEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)
    }

    fun startResultActivity(uri: Uri?) {
        if (!isFinishing) {
            val intent = Intent()
            intent.putExtra("imageUri2", uri.toString())
            intent.putExtra(AboutmeActivity.EXTRA_PROFILE_ID, uri.toString())
            setResult(-1, intent)
            finish()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        private const val TAG = "BasicActivity"

        fun createIntent(activity: Activity?): Intent {
            return Intent(activity, BasicActivity::class.java)
        }
    }
}
