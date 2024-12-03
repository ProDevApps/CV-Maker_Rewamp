package com.professorapps.cvmaker.crop_lib

import android.util.Log

@Suppress("unused")
object Logger {
    private const val TAG = "SimpleCropView"
    var enabled: Boolean = false

    fun e(msg: String?) {
        if (!enabled) return
        Log.e(TAG, msg!!)
    }

    fun e(msg: String?, e: Throwable?) {
        if (!enabled) return
        Log.e(TAG, msg, e)
    }

    @JvmStatic
    fun i(msg: String?) {
        if (!enabled) return
        Log.i(TAG, msg!!)
    }

    fun i(msg: String?, e: Throwable?) {
        if (!enabled) return
        Log.i(TAG, msg, e)
    }
}
