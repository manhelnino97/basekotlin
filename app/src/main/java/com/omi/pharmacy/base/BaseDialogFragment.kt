package com.omi.pharmacy.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.View
import android.view.WindowManager

/**
 * Created by tinhvv on 10/24/18.
 */
abstract class BaseDialogFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
    }

    protected open val heightOver: Int = WindowManager.LayoutParams.WRAP_CONTENT

    override fun onResume() {

        super.onResume()
    }
}