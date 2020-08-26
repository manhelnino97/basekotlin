package com.omi.pharmacy.helper.view

import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.*
import com.omi.pharmacy.R
import com.omi.pharmacy.app.App
import com.omi.pharmacy.base.BaseDialogFragment

/**
 * Created by tinhvv on 10/24/18.
 */
class PopupFragment: BaseDialogFragment() {

    companion object {
        fun getInstance (fragment: Fragment, touchOutsideToDismiss: Boolean = true): PopupFragment {
            val frag = PopupFragment()
            frag.fragment = fragment
            frag.touchOutsideToDismiss = touchOutsideToDismiss
            return frag
        }
    }

    var fragment: Fragment? = null
    private var touchOutsideToDismiss = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.layout_popover, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog.window.setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        dialog.setCanceledOnTouchOutside(touchOutsideToDismiss)
        fragment?.let {
            childFragmentManager.beginTransaction().replace(R.id.container_popover, fragment, "PopupFragment").commitAllowingStateLoss()
        }
    }

    fun back() {
        if (fragment?.childFragmentManager?.backStackEntryCount ?: 0 > 0 ) {
            fragment?.childFragmentManager?.popBackStack()
        }else {
            dismissAllowingStateLoss()
        }
    }

    fun showPopup(framentManager: FragmentManager) {
        val ft = framentManager.beginTransaction()
        val prev = framentManager.findFragmentByTag("dialog")
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        show(ft, "dialog")
    }

    override fun onResume() {
        val window = dialog.window
        val size = Point()
        val display = window.windowManager.defaultDisplay
        window.setBackgroundDrawableResource(android.R.color.transparent)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        display.getSize(size)

        val de = App.shared().resources.displayMetrics.density
        val width = if (size.x > (500 * de) * 0.95) (500.0 * de) else (size.x * 0.95)
        val height = if (size.y > (840 * de) * 0.95) (840.0 * de) else (size.y * 0.95)

        window.setLayout(width.toInt(), height.toInt())
        window.setGravity(Gravity.CENTER)

        super.onResume()
    }
}