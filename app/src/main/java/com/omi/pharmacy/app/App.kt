package com.omi.pharmacy.app

import android.content.Context
import android.support.multidex.MultiDexApplication

/**
 * Created by tinhvv on 10/24/18.
 */
class App: MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
    }

    companion object {

        private lateinit var instance: App

        fun shared(): App {
            return instance
        }
    }
}