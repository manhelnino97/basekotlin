package com.omi.pharmacy.base

/**
 * Created by tinhvv on 10/24/18.
 */
interface BasePresenter<V: BaseView> {
    fun attachView(view: V)
    fun detachView()
}