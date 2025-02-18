package vibe.weather.utils

import android.util.Log
import vibe.weather.BuildConfig

fun logD(message: String) {
    if (BuildConfig.DEBUG) {
        Log.d("vitDebug", message)
    }
}

fun logE(message: String) {
    if (BuildConfig.DEBUG) {
        Log.e("vitDebug", message)
    }
}
