package com.omi.pharmacy.model.response

/**
 * Created by tinhvv on 10/24/18.
 */
class Response<T> {
    var data: T? = null
    var status: Int = 0
    var message: String? = null

    var isSuccess: Boolean = false
        get() = status == 1
}