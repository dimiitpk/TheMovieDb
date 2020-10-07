package com.dimi.moviedatabase.util

import android.util.Log
import com.dimi.moviedatabase.util.Constants.DEBUG
import com.dimi.moviedatabase.util.Constants.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

fun printLogE(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.e(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}