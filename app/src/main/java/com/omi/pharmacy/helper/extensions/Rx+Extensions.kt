package com.omi.pharmacy.helper.extensions

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by tinhvv on 10/24/18.
 */
fun <T> Observable<T>.applyOn(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
}

fun <T> Single<T>.applyOn(): Single<T> {
    return this.subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
}