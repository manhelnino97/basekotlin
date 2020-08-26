package com.omi.pharmacy.view.Authenticate

import com.omi.pharmacy.R
import com.omi.pharmacy.helper.extensions.applyOn
import com.omi.pharmacy.helper.extensions.getString
import com.omi.pharmacy.manager.network.NetworkManager
import com.omi.pharmacy.model.request.Login
import com.omi.pharmacy.model.response.Response
import com.omi.pharmacy.model.response.User
import io.reactivex.disposables.Disposable
import java.lang.ref.WeakReference

/**
 * Created by tinhvv on 10/24/18.
 */
class LoginPresenter: LoginContractor.Presenter {
    private var view: WeakReference<LoginContractor.View?>? = null
    private var disposable: Disposable? = null
    override fun attachView(view: LoginContractor.View) {
        this.view = WeakReference(view)
    }

    override fun detachView() {
        disposable?.dispose()
        this.view?.clear()
        this.view = null
    }

    override fun login(email: String, password: String) {
        val model = Login(email, password)
        this.view?.get()?.showLoading()
        disposable = NetworkManager.shared.accountAPI.login(model)
                .applyOn()
                .subscribe({ response ->

                }, { _ ->
                    this.view?.get()?.showLoginError(R.string.LOGIN_FAILED.getString())
                    this.view?.get()?.hideLoading()
                })

    }

    private fun processResponse(response: Response<User>) {
        if (response.isSuccess) {

        } else {
            this.view?.get()?.showLoginError(response.message ?: R.string.LOGIN_FAILED.getString())
            this.view?.get()?.hideLoading()
        }
    }


    override fun forgotPassword(email: String?) {
        this.view?.get()?.showForgotPassword(email)
    }

    override fun register() {
        this.view?.get()?.showRegister()
    }

    override fun loginFacebook(fbId: String, token: String) {

    }

    override fun registerFCMToken(token: String) {

    }
}