package com.omi.pharmacy.view.Authenticate

import android.os.Bundle
import com.omi.pharmacy.base.BaseActivity
import com.omi.pharmacy.base.BaseFragment

/**
 * Created by tinhvv on 10/24/18.
 */
class LoginActivity : BaseActivity() {
    override fun contentFragment(): BaseFragment? {
        return LoginFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        addContentFragmentIfEmpty()
    }
}