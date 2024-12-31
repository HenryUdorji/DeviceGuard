package com.ifechukwu.deviceguard

import android.app.Application
import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.ifechukwu.deviceguard.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

/**
 * @Author: ifechukwu.udorji
 * @Date: 12/30/2024
 */
class DeviceGuardApp: Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().isCrashlyticsCollectionEnabled = false
            Timber.plant(Timber.DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@DeviceGuardApp)
            modules(appModule)
        }
    }

    private class CrashReportingTree : Timber.Tree() {
        private val crashlytics: FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

        override fun log(
            priority: Int,
            tag: String?,
            message: String,
            t: Throwable?
        ) {
            if (priority == Log.ERROR || priority == Log.DEBUG) {
                crashlytics.log(message)
                if (t != null) {
                    crashlytics.recordException(
                        t
                    )
                }
            }
        }
    }
}