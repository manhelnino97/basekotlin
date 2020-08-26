package com.omi.pharmacy.base

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout
import com.lcodecore.tkrefreshlayout.footer.LoadingView
import com.lcodecore.tkrefreshlayout.header.progresslayout.ProgressLayout
import com.omi.pharmacy.R
import com.omi.pharmacy.helper.extensions.*
import com.omi.pharmacy.helper.view.PopupFragment
import kotlinx.android.synthetic.main.fragment_base.view.*

/**
 * Created by tinhvv on 10/24/18.
 */
abstract class BaseFragment: Fragment(), BaseView {
    open fun getPresenter(): BasePresenter<BaseView>? = null
    open val isTransfStatus = false
    open val isPaddingBottomBar = true

    open val isUsingViewBase = true
    protected open fun isShowToolbar(): Boolean = false
    protected open fun isShowToolbarIcon(): Boolean = false

    protected open var onMenuSelected: ((MenuItem) -> Unit)? = null

    private lateinit var mActivity: BaseActivity
    private lateinit var rootView: View
    protected var toolbar: Toolbar? = null

    protected var canLoadmore: Boolean = false
    private var refreshView: TwinklingRefreshLayout? = null
    protected open val isRootFragment = false
    private var btnMenu: AppCompatImageView? = null

    fun showToolbar(isShow: Boolean) {
        if (isShow) {
            rootView.toolbar?.visible()
        } else {
            rootView.toolbar?.gone()
        }
    }

    protected open var mTitle: String = ""
    fun setTitle(title: String) {
        mTitle = title
        toolbar?.title = mTitle
    }

    protected abstract fun getLayoutRes(): Int

    protected open fun getMenu(): Int? {
        return null
    }

    open var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (activity !is BaseActivity) {
            Throwable("Activity no override BaseActivity")
        }
        mActivity = activity as BaseActivity
    }

    fun showBackButton() {
//        rootView.toolbar?.setNavigationIcon(R.drawable.bi_ic_back_white)
        rootView.toolbar?.setNavigationOnClickListener {
            back()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (!isUsingViewBase) {
            rootView = inflater.inflate(getLayoutRes(), container, false)
            return rootView
        }
        rootView = inflater.inflate(R.layout.fragment_base, container, false)
        if (!isShowToolbar()) {
            rootView.toolbar?.gone()
        }

        if (!isShowToolbarIcon()) {
            rootView.toolbar?.navigationIcon = null
            rootView.toolbar?.toolbar?.title = ""
        }

        val contentView = inflater.inflate(getLayoutRes(), container, false)
        contentView?.let {
            rootView.contentLayout?.addChild(contentView)
//            refreshView = contentView.bind(R.id.refreshView)
        }
        refreshView?.let {
            it.setHeaderView(ProgressLayout(it.context))
            it.setBottomView(LoadingView(it.context))
            it.setOnRefreshListener(object : RefreshListenerAdapter() {
                override fun onRefresh(refreshLayout: TwinklingRefreshLayout?) {
                    onRefresh()
                }

                override fun onLoadMore(refreshLayout: TwinklingRefreshLayout?) {
                    if (canLoadmore) {
                        onLoadmore()
                    } else {
                        refreshLayout?.finishLoadmore()
                    }
                }
            })
        }
        //Add new Framelayout
        if (isRootFragment) {
            val frameRoot = FrameLayout(context)
            frameRoot.id = R.id.container_popover
            (rootView as? ViewGroup)?.addView(frameRoot, -1, rootView.container.layoutParams)
        }
        toolbar = rootView.bind(R.id.toolbar)
        progressBar = rootView.bind(R.id.progressBar)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            view.setupHiddenKeyboard(it)
        }
        getPresenter()?.attachView(this)

        savedInstanceState?.let {
            mTitle = it.getString("title")
        }


        toolbar?.title = mTitle

//        view.findViewById<View>(R.id.btnBack)?.setOnClickListener {
//            back()
//        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("title", mTitle)
        super.onSaveInstanceState(outState)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)

    }

    override fun showLoading(text: String?) {
        activity?.runOnUiThread {
            rootView.progressLayout?.visible()
        }
    }

    override fun hideLoading() {
        activity?.runOnUiThread {
            rootView.progressLayout?.gone()
        }
    }

    override fun showError(message: String, isToast: Boolean) {
        activity?.runOnUiThread {
            if (isToast) {
                toast(message)
            } else {
                showNotice(message)
            }
        }
    }

    override fun back() {
        if (isShowPopup()) {
            backInPopup()
        } else {
            try {

                activity?.supportFragmentManager?.popBackStack()
            }catch (ex: IllegalStateException) {

            }
        }
        hideKeyboard()
    }

    fun hideKeyboard() {
        val view = activity?.currentFocus
        if (view != null) {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun popBackStack(name: String, flags: Int, isFromActivity: Boolean = true) {
        if (isFromActivity) {
            mActivity.popBackStack(name, flags)
        } else {
            childFragmentManager.popBackStack(name, flags)
        }
    }

    fun popBackStackImmediate(name: String, flags: Int, isFromActivity: Boolean = true): Boolean {
        if (isFromActivity) {
            return mActivity.popBackStackImmediate(name, flags)
        } else {
            return childFragmentManager.popBackStackImmediate(name, flags)
        }
    }

    fun openFragment(fragment: BaseFragment, isAddToActivity: Boolean = true, addToBackStack: Boolean = true, name: String? = null) {
        hideKeyboard()
        if (isAddToActivity) {
            mActivity.openFragment(fragment, addToBackStack, name)
        } else {
            openChildFragment(fragment, addToBackStack, name)
        }
    }

    fun replaceFragment(fragment: BaseFragment, isAddToActivity: Boolean = true) {
        if (isAddToActivity) {
            mActivity.openFragment(fragment, false)
        } else {
            openChildFragment(fragment, false)
        }
    }

    fun openChildFragment(fragment: BaseFragment, addToBackStack: Boolean = true, name: String? = null) {
        if (!isRootFragment && parentFragment is BaseFragment) {
            (parentFragment as? BaseFragment)?.openChildFragment(fragment)
        } else {
            val transaction = childFragmentManager.beginTransaction()
                    .setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_left,
                            R.anim.enter_from_left, R.anim.exit_to_right)
            if (isRootFragment) {
                transaction.replace(R.id.container_popover, fragment)
            } else {
                transaction.replace(R.id.container, fragment)
            }
            if (addToBackStack) {
                transaction.addToBackStack(name)
            }
            transaction.commitAllowingStateLoss()
        }
    }

    override fun onLoadmore() {

    }

    override fun onRefresh() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        getPresenter()?.detachView()
    }

    protected fun isShowPopup(): Boolean {
        if (parentFragment == null) return false
        if (parentFragment is PopupFragment) {
            return true
        }
        return (parentFragment as? BaseFragment)?.isShowPopup() ?: false
    }

    protected fun backInPopup(): Boolean {
        if (parentFragment == null) return false
        if (parentFragment is PopupFragment) {
            (parentFragment as PopupFragment).back()
            return true
        }
        return (parentFragment as? BaseFragment)?.backInPopup() ?: false
    }

    protected fun dismissInPopup(): Boolean {
        if (parentFragment == null) return false
        if (parentFragment is PopupFragment) {
            (parentFragment as PopupFragment).dismissAllowingStateLoss()
            return true
        }
        return (parentFragment as? BaseFragment)?.dismissInPopup() ?: false
    }

    protected fun dismiss() {
        if (isShowPopup()) {
            dismissInPopup()
        } else {
            back()
        }
    }

    protected fun showNotice(message: String, handler: (() -> Unit)? = null) {
        activity?.runOnUiThread {
            context?.let {
                showConfirm(it, null, message.mapCode(), R.string.ok.getString(), rightButtonClickHandler = handler)
            }
        }
    }

    fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun getNavigationBarHeight(): Int {
        val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
        val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)
        if (!hasBackKey || !hasHomeKey) return 0
        if (Build.VERSION.SDK_INT >= 21) {
            var result = 0
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        } else return 0
    }

    fun hasNavBar(): Boolean {
        val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
        return id > 0 && resources.getBoolean(id)
    }
}