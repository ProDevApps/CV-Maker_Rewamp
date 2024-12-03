package com.professorapps.cvmaker.crop_lib

import android.net.Uri

interface SaveCallback : Callback {
    fun onSuccess(uri: Uri?)
}
