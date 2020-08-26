package com.omi.pharmacy.helper.extensions

import android.util.Log
import com.omi.pharmacy.BuildConfig

/**
 * Created by tinhvv on 10/24/18.
 */

fun logD(message: String, tag: String = "TAG - Pharmacy") {
    if (BuildConfig.DEBUG)
        Log.d(tag,message)
}

fun logE(message: String, tag: String = "TAG - Pharmacy") {
    if (BuildConfig.DEBUG)
        Log.e(tag,message)
}

fun logI(message: String, tag: String = "TAG - Pharmacy") {
    if (BuildConfig.DEBUG)
        Log.i(tag,message)
}