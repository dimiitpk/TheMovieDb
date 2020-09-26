package com.dimi.moviedatabase.util

import android.util.Log
import com.dimi.moviedatabase.util.Constants.DEBUG
import com.dimi.moviedatabase.util.Constants.TAG


fun printLogD(className: String?, message: String) {
    if (DEBUG) {
        Log.d(TAG, "$className: $message")
    }
}

fun printLogE(className: String?, message: String) {
    if (DEBUG) {
        Log.e(TAG, "$className: $message")
    }
}