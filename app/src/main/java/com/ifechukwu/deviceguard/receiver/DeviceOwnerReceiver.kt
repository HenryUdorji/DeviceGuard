package com.ifechukwu.deviceguard.receiver

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import com.ifechukwu.deviceguard.MainActivity
import com.ifechukwu.deviceguard.R


/**
 * @Author: ifechukwu.udorji
 * @Date: 12/31/2024
 */
class DeviceOwnerReceiver: DeviceAdminReceiver() {
    override fun onEnabled(context: Context, intent: Intent) {
        super.onEnabled(context, intent)
    }

    override fun onDisableRequested(context: Context, intent: Intent): CharSequence? {
        return super.onDisableRequested(context, intent)
    }

    override fun onDisabled(context: Context, intent: Intent) {
        super.onDisabled(context, intent)
    }

    override fun onProfileProvisioningComplete(context: Context, intent: Intent) {
//        val dpm = context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
//        val componentName = ComponentName(context.applicationContext, DeviceOwnerReceiver::class.java)
//        dpm.setProfileName(componentName, context.getString(R.string.profile_name))
//        dpm.setProfileEnabled(componentName)
//
//        // Open the main screen
//        val launch = Intent(context, MainActivity::class.java)
//        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(launch)
    }
}