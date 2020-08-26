package com.omi.pharmacy.view.ConfirmOrder

import android.os.Bundle
import com.omi.pharmacy.R
import com.omi.pharmacy.base.BaseActivity

class ConfirmOrderActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order)
        supportActionBar?.hide()
    }
}
