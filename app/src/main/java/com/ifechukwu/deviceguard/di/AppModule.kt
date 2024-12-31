package com.ifechukwu.deviceguard.di

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.SharedPreferences
import com.ifechukwu.deviceguard.DeviceAdminReceiver
import com.ifechukwu.deviceguard.DeviceController
import com.ifechukwu.deviceguard.FCMService
import com.ifechukwu.deviceguard.SessionManager
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module


/**
 * @Author: ifechukwu.udorji
 * @Date: 12/31/2024
 */

val appModule = module {
    single {
        val context = androidContext().applicationContext
        context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
    }

    single {
        val context = androidContext().applicationContext
        ComponentName(context, DeviceAdminReceiver::class.java)
    }

    singleOf(::DeviceController)

    single {
        val context = androidContext().applicationContext
        context.getSharedPreferences("deviceguardpref", Context.MODE_PRIVATE)
    }

    single { SessionManager(get()) }
}