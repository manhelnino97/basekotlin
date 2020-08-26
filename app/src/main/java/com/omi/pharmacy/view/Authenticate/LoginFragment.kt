package com.omi.pharmacy.view.Authenticate

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.facebook.*
import com.facebook.login.LoginResult
import com.omi.pharmacy.R
import com.omi.pharmacy.base.BaseFragment
import com.omi.pharmacy.base.BasePresenter
import com.omi.pharmacy.base.BaseView
import com.omi.pharmacy.helper.extensions.logD
import com.omi.pharmacy.helper.extensions.toast
import com.omi.pharmacy.service.FacebookService
import kotlinx.android.synthetic.main.fragment_login.*
import java.lang.ref.WeakReference

/**
 * Created by tinhvv on 10/24/18.
 */
class LoginFragment: BaseFragment(), LoginContractor.View {
    private val presenter = LoginPresenter()

    override fun getPresenter(): BasePresenter<BaseView>? {
        return presenter as BasePresenter<BaseView>
    }

    override fun getLayoutRes(): Int = R.layout.fragment_login
    override val isTransfStatus = false

    private var callbackManager = CallbackManager.Factory.create()
    private var facebookService = FacebookService()
    private val self = WeakReference(this)
    private val fbCallback = object : FacebookCallback<LoginResult> {
        override fun onSuccess(loginResult: LoginResult) {
            logD("LoginFacebook onSuccess")
            self.get()?.requestFacebookUserData(loginResult.accessToken)
        }

        override fun onCancel() {
            logD("LoginFacebook canceled")
            self.get()?.hideLoading()
        }

        override fun onError(exception: FacebookException) {
            self.get()?.hideLoading()
            logD("Loginfacebook onError $exception")
            exception.printStackTrace()
            toast(exception.localizedMessage ?: (exception.message ?: return))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        btnLoginFacebook?.setOnClickListener {
//            showLoading()
//            if (facebookService.isLogin()) {
//                val accessToken = facebookService.accessToken()
//                requestFacebookUserData(accessToken)
//            } else {
//                facebookService.performLogin()
//            }
//        }
//
//        facebookService.registerCallback(callbackManager, fbCallback)
//        context?.let { facebookService.performLogout(it) }
    }

    private fun requestFacebookUserData(accessToken: AccessToken) {
        val request = GraphRequest.newMeRequest(accessToken) { json, data ->
            if (json.has("email")) {
                self.get()?.presenter?.loginFacebook(accessToken.userId, accessToken.token)
            } else {
                self.get()?.context?.let { self.get()?.facebookService?.performLogout(it) }
                self.get()?.activity?.runOnUiThread {
                    self.get()?.hideLoading()
                    self.get()?.showNotice("Không thể lấy email từ facebook của bạn")
                }
            }
        }
        val parameters = Bundle()
        parameters.putString("fields", "id,email")
        request.parameters = parameters
        request.executeAsync()
    }

    override fun showHome() {
//        if (activity != null && activity is HomeActivity) {
//            (activity as? HomeActivity)?.backToHome()
//        } else {
        back()
//        }
    }

    override fun showForgotPassword(email: String?) {
//        openFragment(ForgotPasswordFragment())
    }

    override fun showRegister() {
//        openFragment(RegisterFragment())
    }

    override fun showLoginError(error: String) {
        activity?.runOnUiThread {
            showNotice(error)
        }
    }



    override fun onResume() {
        super.onResume()
        presenter.attachView(this)
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        self.clear()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        logD("LoginFragment onActivityResult requestCode = $requestCode,resultCode = $resultCode")
        logD("LoginFragment onActivityResult data = $data")
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }
}