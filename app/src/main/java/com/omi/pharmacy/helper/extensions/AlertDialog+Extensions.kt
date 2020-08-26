package com.omi.pharmacy.helper.extensions

import android.app.AlertDialog
import android.content.Context

/**
 * Created by tinhvv on 10/24/18.
 */
fun showConfirm(context: Context, title: String? = null, content: String, rightButtonTitle: String = "Đồng ý", leftButtonTitle: String? = null, rightButtonClickHandler: (() -> Unit)? = null, cancelListener: (() -> Unit)? = null) {
    val dialog = AlertDialog.Builder(context)
    dialog.setCancelable(true)
    dialog.setPositiveButton(rightButtonTitle, { dia, _ ->
        dia.dismiss()
        rightButtonClickHandler?.invoke()
    })
    title?.let {
        dialog.setTitle(title)
    }
    leftButtonTitle?.let {
        dialog.setNegativeButton(it, { dia, _ ->
            dia.dismiss()
        })
    }
    dialog.setOnCancelListener {
        cancelListener?.invoke()
    }
    dialog.setOnDismissListener {
        cancelListener?.invoke()
    }
    dialog.setMessage(content)
    dialog.create().show()
}