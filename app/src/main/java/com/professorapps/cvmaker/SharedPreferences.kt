package com.professorapps.cvmaker

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log

class SharedPreferences {
    private val editor: android.content.SharedPreferences.Editor
    private val prefs: android.content.SharedPreferences
    private val TAG: String = "SharedPreferences"
    private val filename: String
        get() {
            return "preferences"
        }
    var isLoggingEnabled: Boolean = false
        private set

    @SuppressLint("CommitPrefEdits")
    private constructor(context: Context) {
        this.prefs = context.getSharedPreferences("preferences", 0)
        this.editor = prefs.edit()
    }

    @SuppressLint("CommitPrefEdits")
    private constructor(context: Context, str: String) {
        this.prefs = context.getSharedPreferences(str, 0)
        this.editor = prefs.edit()
    }

    fun setLogging(z: Boolean) {
        this.isLoggingEnabled = z
    }

    fun clearAll() {
        editor.clear()
        editor.apply()
    }

    fun contains(str: String?): Boolean {
        return prefs.contains(str)
    }

    fun removeValue(str: String) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Removing key " + str + " from preferences")
        }
        editor.remove(str)
        editor.apply()
    }

    fun getBoolean(str: String, z: Boolean): Boolean {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Value: " + str + " is " + prefs.getBoolean(str, z))
        }
        return prefs.getBoolean(str, z)
    }

    fun getInteger(str: String, i: Int): Int {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Value: " + str + " is " + prefs.getInt(str, i))
        }
        return prefs.getInt(str, i)
    }

    fun getString(str: String?, str2: String?): String? {
        if (this.isLoggingEnabled) {
            val sb: StringBuilder = StringBuilder()
            sb.append("Value: ")
            sb.append(str)
            sb.append(" is ")
            sb.append(if (str2 != null) prefs.getString(str, str2)!!.trim({ it <= ' ' }) else null)
            Log.d("SharedPreferences", sb.toString())
        }
        if (str2 != null) {
            return prefs.getString(str, str2)!!.trim({ it <= ' ' })
        }
        return null
    }

    fun getFloat(str: String, f: Float): Float {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Value: " + str + " is " + prefs.getFloat(str, f))
        }
        return prefs.getFloat(str, f)
    }

    fun getLong(str: String, j: Long): Long {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Value: " + str + " is " + prefs.getLong(str, j))
        }
        return prefs.getLong(str, j)
    }

    fun getDouble(str: String, d: Double): Double {
        if (this.isLoggingEnabled) {
            Log.d(
                "SharedPreferences", "Value: " + str + " is " + java.lang.Double.longBitsToDouble(
                    prefs.getLong(str, java.lang.Double.doubleToLongBits(d))
                )
            )
        }
        return java.lang.Double.longBitsToDouble(
            prefs.getLong(
                str,
                java.lang.Double.doubleToLongBits(d)
            )
        )
    }

    fun getStringSet(str: String, set: Set<String?>?): Set<String>? {
        if (this.isLoggingEnabled) {
            Log.d(
                "SharedPreferences",
                "Value: " + str + " is " + prefs.getStringSet(str, set).toString()
            )
        }
        return prefs.getStringSet(str, set)
    }

    val all: Map<String, *>
        get() {
            if (this.isLoggingEnabled) {
                Log.d("SharedPreferences", "Total of " + prefs.getAll().size + " values stored")
            }
            return prefs.getAll()
        }

    fun saveBoolean(str: String, z: Boolean) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Saving " + str + " with value " + z)
        }
        editor.putBoolean(str, z)
        editor.apply()
    }

    fun saveInteger(str: String, i: Int) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Saving " + str + " with value " + i)
        }
        editor.putInt(str, i)
        editor.apply()
    }

    fun saveString(str: String, str2: String?) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Saving " + str + " with value " + str2)
        }
        editor.putString(str, if (str2 != null) str2.trim({ it <= ' ' }) else null)
        editor.apply()
    }

    fun saveFloat(str: String, f: Float) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Saving " + str + " with value " + f)
        }
        editor.putFloat(str, f)
        editor.apply()
    }

    fun saveLong(str: String, j: Long) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Saving " + str + " with value " + j)
        }
        editor.putLong(str, j)
        editor.apply()
    }

    fun saveDouble(str: String, d: Double) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Saving " + str + " with value " + d)
        }
        editor.putLong(str, java.lang.Double.doubleToRawLongBits(d))
        editor.apply()
    }

    fun saveStringSet(str: String, set: Set<String?>) {
        if (this.isLoggingEnabled) {
            Log.d("SharedPreferences", "Saving " + str + " with value " + set.toString())
        }
        editor.putStringSet(str, set)
        editor.apply()
    }

    companion object {
        private var mInstance: SharedPreferences? = null
        fun getInstance(context: Context): SharedPreferences? {
            if (mInstance == null) {
                mInstance = SharedPreferences(context)
            }
            return mInstance
        }

        @JvmStatic
        fun getInstance(context: Context, str: String): SharedPreferences? {
            val sharedPreferences: SharedPreferences? = mInstance
            if ((sharedPreferences != null && !(sharedPreferences.filename == str)) || mInstance == null) {
                mInstance = SharedPreferences(context, str)
            }
            return mInstance
        }
    }
}
