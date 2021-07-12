package com.example.mvi_noteapp.util

import android.util.Log
import com.example.mvi_noteapp.util.Constants.DEBUG
import com.example.mvi_noteapp.util.Constants.TAG

var isUnitTest = false

fun printLogD(className: String?, message: String ) {
    if (DEBUG && !isUnitTest) {
        Log.d(TAG, "$className: $message")
    }
    else if(DEBUG && isUnitTest){
        println("$className: $message")
    }
}

