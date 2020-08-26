package com.omi.pharmacy.manager.network

import com.omi.pharmacy.model.request.Login
import com.omi.pharmacy.model.response.Response
import com.omi.pharmacy.model.response.User
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by tinhvv on 10/24/18.
 */
interface AccountAPI {

    @POST("api/v1/accounts/login")
    fun login(@Body param: Login): Observable<Response<User>>

}