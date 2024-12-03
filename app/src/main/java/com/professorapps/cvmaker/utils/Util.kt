package com.professorapps.cvmaker.utils

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Util {
    @JvmStatic
    fun getOutputMediaFile(i: Int): File? {
        val file =
            File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "")
        if (!file.exists() && !file.mkdirs()) {
            return null
        }
        val format = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        return if (i == 1) {
            File(file.path + File.separator + "IMG_" + format + ".jpg")
        } else if (i != 3) {
            null
        } else {
            File(file.path + File.separator + "VID_" + format + ".mp4")
        }
    }

    fun getRealPathFromUri(context: Context, uri: Uri?): String {
        var cursor: Cursor? = null
        try {
            cursor = context.contentResolver.query(uri!!, arrayOf("_data"), null, null, null)
            val columnIndexOrThrow = cursor!!.getColumnIndexOrThrow("_data")
            cursor.moveToFirst()
            return cursor.getString(columnIndexOrThrow)
        } finally {
            cursor?.close()
        }
    }
}
