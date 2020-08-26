package com.omi.pharmacy.helper.extensions

import android.widget.Toast
import com.omi.pharmacy.app.App

/**
 * Created by tinhvv on 10/24/18.
 */
fun toast(message: Any, length: Int = Toast.LENGTH_LONG) {
    when (message) {
        is String -> Toast.makeText(App.shared(), message.mapCode(), length).show()
        is Int -> Toast.makeText(App.shared(), message, length).show()
        else -> throw IllegalArgumentException("Argument message type is invalid. The first argument is only accepted on Int or String")
    }
}