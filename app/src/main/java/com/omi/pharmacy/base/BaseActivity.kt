package com.omi.pharmacy.base

import android.app.AlertDialog
import android.arch.lifecycle.LifecycleObserver
import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.WindowManager
import com.omi.pharmacy.R
import com.omi.pharmacy.helper.extensions.bind
import com.omi.pharmacy.helper.extensions.getColor

/**
 * Created by tinhvv on 10/24/18.
 */
abstract class BaseActivity: AppCompatActivity(), LifecycleObserver {

    companion object {
        val TAG = "BaseActivity"
    }

    open fun contentFragment(): BaseFragment? = null
    open fun isUsingBaseContent() = true

    open fun addContentFragmentIfEmpty() {
        Log.d(TAG, "addContentFragmentIfEmpty ")
        var fragment = supportFragmentManager.findFragmentById(R.id.contentLayout)
        if (fragment != null) {
            return
        }
        fragment = contentFragment()
        if (fragment == null) {
            return
        }

        Log.d(TAG, "addContentFragmentIfEmpty begin")
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.contentLayout, fragment)
        transaction.addToBackStack("Root")
        transaction.commit()

        Log.d(TAG, "addContentFragmentIfEmpty done")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        settingTranfStatus()
        if (isUsingBaseContent()) {
            setContentView(R.layout.activity_base)
        }
    }

    public fun settingTranfStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS/*,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS*/)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                w.navigationBarColor = R.color.colorPrimaryDark.getColor()
            }
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        }
    }

    public fun disableTranfStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            val w = window // in Activity's onCreate() for instance
            w.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
            w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        }
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
    }

    open fun openFragment(fragment: BaseFragment, addToBackStack: Boolean = true, name: String? = null) {
        if (bind<View>(R.id.contentLayout) != null) {
            val transaction = supportFragmentManager
                    .beginTransaction()
            transaction.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                    R.anim.enter_from_left, R.anim.exit_to_right)
            transaction.add(R.id.contentLayout, fragment)
            if (addToBackStack) {
                transaction.addToBackStack(name)
            }
            transaction.commitAllowingStateLoss()
        }
    }

    fun popBackStack(name: String, flags: Int) {
        supportFragmentManager.popBackStack(name, flags)
    }

    fun popBackStackImmediate(name: String, flags: Int): Boolean {
        return supportFragmentManager.popBackStackImmediate(name, flags)
    }

    fun showConfirm(title: String? = null, content: String, rightButtonTitle: String = "Đồng ý", handlerRight: ((DialogInterface) -> Unit)? = null, leftButtonTitle: String? = null, handlerLeft: ((DialogInterface) -> Unit)? = null) {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        dialog.setPositiveButton(rightButtonTitle, null)
        title?.let {
            dialog.setTitle(title)
        }
        leftButtonTitle?.let {
            dialog.setNegativeButton(it, null)
        }
        dialog.setMessage(content)
        val diaInterface = dialog.create()
        diaInterface.setOnShowListener { diai ->
            val positive = diaInterface.getButton(AlertDialog.BUTTON_POSITIVE)
            positive?.setOnClickListener {
                handlerRight?.invoke(diai)
            }

            val negative = diaInterface.getButton(AlertDialog.BUTTON_NEGATIVE)
            negative?.setOnClickListener {
                handlerLeft?.invoke(diai)
            }
        }
        diaInterface.show()
    }

    fun showMessage(content: String, leftButtonTitle: String = "OK") {
        val dialog = AlertDialog.Builder(this)
        dialog.setCancelable(false)
        leftButtonTitle?.let {
            dialog.setNegativeButton(it, null)
        }
        dialog.setMessage(content)
        val diaInterface = dialog.create()
        diaInterface.show()
    }
}