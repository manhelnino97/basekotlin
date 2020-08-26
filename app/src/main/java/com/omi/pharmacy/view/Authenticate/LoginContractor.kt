package com.omi.pharmacy.view.Authenticate

import com.omi.pharmacy.base.BasePresenter
import com.omi.pharmacy.base.BaseView

/**
 * Created by tinhvv on 10/24/18.
 */
interface LoginContractor {

    
    interface View: BaseView {
        fun showHome()
        fun showForgotPassword(email: String?)
        fun showRegister()
        fun showLoginError(error: String)
    }

    interface Presenter: BasePresenter<View> {
        fun login(email: String, password: String)
        fun forgotPassword(email: String?)
        fun register()
        fun loginFacebook(fbId: String, token: String)
        fun registerFCMToken(token: String)
    }
}