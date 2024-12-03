package com.professorapps.cvmaker

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class PrivacyActivity() : AppCompatActivity() {
    var progressDialog: ProgressDialog? = null
    var webView: WebView? = null


    public override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        setContentView(R.layout.activity_privacy)
        initToolbar()
        this.webView = findViewById<View>(R.id.webview) as WebView?
        startWebView("https://www.google.com/")
    }

    private fun startWebView(str: String) {
        webView!!.getSettings().setJavaScriptEnabled(true)
        webView!!.setScrollBarStyle(33554432)
        webView!!.getSettings().setBuiltInZoomControls(true)
        webView!!.getSettings().setDisplayZoomControls(false)
        webView!!.getSettings().setUseWideViewPort(true)
        webView!!.getSettings().setLoadWithOverviewMode(true)
        this.progressDialog = ProgressDialog(this)
        progressDialog!!.setMessage("Loading...")
        progressDialog!!.show()
        webView!!.setWebViewClient(object : WebViewClient() {
            override fun shouldOverrideUrlLoading(webView: WebView, str2: String): Boolean {
                webView.loadUrl(str2)
                return true
            }

            override fun onPageFinished(webView: WebView, str2: String) {
                if (progressDialog!!.isShowing()) {
                    progressDialog!!.dismiss()
                }
            }

            override fun onReceivedError(webView: WebView, i: Int, str2: String, str3: String) {
                val privacyActivity: PrivacyActivity = this@PrivacyActivity
                Toast.makeText(privacyActivity, "Error:" + str2, Toast.LENGTH_SHORT).show()
            }
        })
        webView!!.loadUrl(str)
    }

    private fun initToolbar() {
        val toolbar: Toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val supportActionBar: ActionBar? = getSupportActionBar()
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar.setHomeButtonEnabled(true)
        toolbar.setNavigationIcon(R.drawable.back)
    }

    private val isNetworkConnected: Boolean
        get() {
            val connectivityManager: ConnectivityManager =
                getSystemService("connectivity") as ConnectivityManager
            return (connectivityManager.getActiveNetworkInfo() != null) && connectivityManager.getActiveNetworkInfo()!!
                .isAvailable() && connectivityManager.getActiveNetworkInfo()!!.isConnected()
        }

    fun showAlertDialog(context: Context?, str: String?, str2: String?, bool: Boolean?) {
        val create: AlertDialog = AlertDialog.Builder(context).create()
        create.setCancelable(false)
        create.setTitle(str)
        create.setMessage(str2)
        create.setButton("OK", object : DialogInterface.OnClickListener {
            override fun onClick(dialogInterface: DialogInterface, i: Int) {
                this@PrivacyActivity.finish()
            }
        })
        create.show()
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        if (menuItem.getItemId() != 16908332) {
            return super.onOptionsItemSelected(menuItem)
        }
        onBackPressed()
        return true
    }
}
