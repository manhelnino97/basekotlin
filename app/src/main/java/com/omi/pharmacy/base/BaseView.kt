package com.omi.pharmacy.base

/**
 * Created by tinhvv on 10/24/18.
 */
interface BaseView {
    fun showLoading(text: String? = null)
    fun hideLoading()

    fun showError(message: String, isToast: Boolean = true)

    fun showAlert(message: String) {
        showError(message, false)
    }

    fun onRefresh()
    fun onLoadmore()

    fun back()
}