package com.ifechukwu.deviceguard

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.os.UserHandle

/**
 * @Author: ifechukwu.udorji
 * @Date: 12/30/2024
 */
class DeviceAdminReceiver: DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence? {
        return super.onDisableRequested(context, intent)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
    }

    override fun onPasswordChanged(context: Context, intent: Intent, user: UserHandle) {
        super.onPasswordChanged(context, intent, user)
    }

    override fun onPasswordExpiring(context: Context, intent: Intent, user: UserHandle) {
        super.onPasswordExpiring(context, intent, user)
    }
}