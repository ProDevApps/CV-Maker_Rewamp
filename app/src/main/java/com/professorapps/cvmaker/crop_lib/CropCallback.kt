package com.professorapps.cvmaker.crop_lib

import android.graphics.Bitmap

interface CropCallback : Callback {
    fun onSuccess(cropped: Bitmap?)
}
