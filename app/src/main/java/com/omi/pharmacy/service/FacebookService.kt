package com.omi.pharmacy.service

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.support.v4.app.Fragment
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.Profile
import com.facebook.internal.FragmentWrapper
import com.facebook.internal.LoginAuthorizationType
import com.facebook.internal.Utility
import com.facebook.login.DefaultAudience
import com.facebook.login.LoginBehavior
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.omi.pharmacy.R

/**
 * Created by tinhvv on 10/24/18.
 */
class FacebookService {
    private var properties = FacebookProperties()
    private var parentFragment: FragmentWrapper? = null
    var activity: Activity? = null

    fun getDefaultAudience(): DefaultAudience {
        return properties.defaultAudience
    }

    fun getLoginBehavior(): LoginBehavior {
        return properties.loginBehavior
    }

    fun setFragment(fragment: Fragment) {
        parentFragment = FragmentWrapper(fragment)
    }

    fun setFragment(fragment: android.app.Fragment) {
        parentFragment = FragmentWrapper(fragment)
    }

    fun getFragment(): Fragment? {
        return if (parentFragment != null) parentFragment?.supportFragment else null
    }

    fun getNativeFragment(): android.app.Fragment? {
        return if (parentFragment != null) parentFragment?.nativeFragment else null
    }


    protected fun getLoginManager(): LoginManager {
        val manager = LoginManager.getInstance()
        manager.defaultAudience = getDefaultAudience()
        manager.loginBehavior = getLoginBehavior()
        return manager
    }

    fun registerCallback(
            callbackManager: CallbackManager,
            callback: FacebookCallback<LoginResult>) {
        getLoginManager().registerCallback(callbackManager, callback)
    }

    fun unregisterCallback(callbackManager: CallbackManager) {
        getLoginManager().unregisterCallback(callbackManager)
    }

    fun isLogin(): Boolean {
        val accessToken = AccessToken.getCurrentAccessToken()
        return accessToken != null && !accessToken.isExpired
    }

    fun accessToken(): AccessToken {
        return AccessToken.getCurrentAccessToken()
    }

    fun performLogin() {
        val loginManager = getLoginManager()
        if (LoginAuthorizationType.PUBLISH == properties.authorizationType) {
            if (this.getFragment() != null) {
                loginManager.logInWithPublishPermissions(
                        this.getFragment(),
                        properties.permissions)
            } else if (this.getNativeFragment() != null) {
                loginManager.logInWithPublishPermissions(
                        this.getNativeFragment(),
                        properties.permissions)
            } else {
                loginManager.logInWithPublishPermissions(
                        this.activity,
                        properties.permissions)
            }
        } else {
            if (this.getFragment() != null) {
                loginManager.logInWithReadPermissions(
                        this.getFragment(),
                        properties.permissions)
            } else if (this.getNativeFragment() != null) {
                loginManager.logInWithReadPermissions(
                        this.getNativeFragment(),
                        properties.permissions)
            } else {
                loginManager.logInWithReadPermissions(
                        this.activity,
                        properties.permissions)
            }
        }
    }

    fun performLogout(context: Context, confirmLogout: Boolean = false) {
        val loginManager = getLoginManager()
        if (confirmLogout) {
            // Create a confirmation dialog
            val logout = context.resources.getString(
                    R.string.com_facebook_loginview_log_out_action)
            val cancel = context.resources.getString(
                    R.string.com_facebook_loginview_cancel_action)
            val message: String
            val profile = Profile.getCurrentProfile()
            if (profile != null && profile.name != null) {
                message = String.format(
                        context.resources.getString(
                                R.string.com_facebook_loginview_logged_in_as),
                        profile.name)
            } else {
                message = context.resources.getString(
                        R.string.com_facebook_loginview_logged_in_using_facebook)
            }
            val builder = AlertDialog.Builder(context)
            builder.setMessage(message)
                    .setCancelable(true)
                    .setPositiveButton(logout, { _, _ -> loginManager.logOut() })
                    .setNegativeButton(cancel, null)
            builder.create().show()
        } else {
            loginManager.logOut()
        }
    }

    internal class FacebookProperties {
        var defaultAudience = DefaultAudience.FRIENDS
        var permissions: List<String>? = listOf("public_profile, email")
            private set
        var authorizationType: LoginAuthorizationType? = null
        var loginBehavior = LoginBehavior.NATIVE_WITH_FALLBACK

        fun setReadPermissions(permissions: List<String>) {

            if (LoginAuthorizationType.PUBLISH == authorizationType) {
                throw UnsupportedOperationException("Cannot call setReadPermissions after " + "setPublishPermissions has been called.")
            }
            this.permissions = permissions
            authorizationType = LoginAuthorizationType.READ
        }

        fun setPublishPermissions(permissions: List<String>) {

            if (LoginAuthorizationType.READ == authorizationType) {
                throw UnsupportedOperationException("Cannot call setPublishPermissions after " + "setReadPermissions has been called.")
            }
            if (Utility.isNullOrEmpty(permissions)) {
                throw IllegalArgumentException(
                        "Permissions for publish actions cannot be null or empty.")
            }
            this.permissions = permissions
            authorizationType = LoginAuthorizationType.PUBLISH
        }

        fun clearPermissions() {
            permissions = null
            authorizationType = null
        }
    }
}